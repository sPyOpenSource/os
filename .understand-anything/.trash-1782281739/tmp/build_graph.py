#!/usr/bin/env python3
"""Build knowledge graph nodes/edges from extract-structure results for batches 6-10."""
import json, os, sys, hashlib

PROJECT_ROOT = "/Users/xuyi/Source/OS/armOS/lib/jcore/OS"
OUTPUT_DIR = os.path.join(PROJECT_ROOT, ".understand-anything", "intermediate")

def node_id(prefix, name):
    raw = f"{prefix}:{name}"
    return hashlib.sha256(raw.encode()).hexdigest()[:16]

def process_batch(batch_index):
    # Read raw batch data (imports + neighbors)
    raw_path = os.path.join(OUTPUT_DIR, f"batch-{batch_index}-raw.json")
    with open(raw_path) as f:
        raw = json.load(f)
    
    batch_import_data = raw.get("batchImportData", {})
    batch_neighbor_map = raw.get("neighborMap", {})
    
    # Read extract results
    extract_path = os.path.join(PROJECT_ROOT, ".understand-anything", "tmp", f"ua-file-extract-results-{batch_index}.json")
    with open(extract_path) as f:
        extract = json.load(f)
    
    nodes = []
    edges = []
    file_id_map = {}  # path -> nodeId
    
    # Create file nodes
    for result in extract["results"]:
        fpath = result["path"]
        fid = node_id("file", fpath)
        file_id_map[fpath] = fid
        
        entry_point = False
        if fpath.endswith("/Main.java"):
            entry_point = True
        
        node = {
            "id": fid,
            "type": "file",
            "name": os.path.basename(fpath),
            "filePath": fpath,
            "language": result.get("language", "java"),
            "totalLines": result.get("totalLines", 0),
            "nonEmptyLines": result.get("nonEmptyLines", 0),
            "entryPoint": entry_point
        }
        
        # Add metrics
        metrics = result.get("metrics", {})
        if metrics:
            node["metrics"] = metrics
        
        nodes.append(node)
    
    # Create class and function nodes + contains/exports edges
    for result in extract["results"]:
        fpath = result["path"]
        fid = file_id_map[fpath]
        
        # Classes
        for cls in result.get("classes", []):
            cname = cls["name"]
            cid = node_id("class", f"{fpath}::{cname}")
            nodes.append({
                "id": cid,
                "type": "class",
                "name": cname,
                "filePath": fpath,
                "startLine": cls.get("startLine"),
                "endLine": cls.get("endLine"),
                "methods": cls.get("methods", []),
                "properties": cls.get("properties", [])
            })
            edges.append({
                "source": fid,
                "target": cid,
                "type": "contains"
            })
            
            # Add exports edge if class name in exports
            exports = result.get("exports", [])
            if any(e["name"] == cname for e in exports):
                edges.append({
                    "source": cid,
                    "target": fid,
                    "type": "exports"
                })
        
        # Functions (top-level, not in classes likely for interface methods)
        for fn in result.get("functions", []):
            fname = fn["name"]
            # Skip if this function belongs to a class method list
            class_methods = []
            for cls in result.get("classes", []):
                class_methods.extend(cls.get("methods", []))
            if fname in class_methods:
                continue
            
            fnid = node_id("function", f"{fpath}::{fname}")
            nodes.append({
                "id": fnid,
                "type": "function",
                "name": fname,
                "filePath": fpath,
                "startLine": fn.get("startLine"),
                "endLine": fn.get("endLine"),
                "params": fn.get("params", [])
            })
            edges.append({
                "source": fid,
                "target": fnid,
                "type": "contains"
            })
            
            # Add exports edge
            exports = result.get("exports", [])
            if any(e["name"] == fname for e in exports):
                edges.append({
                    "source": fnid,
                    "target": fid,
                    "type": "exports"
                })
    
    # Create import edges from batchImportData
    for fpath, imports in batch_import_data.items():
        if fpath not in file_id_map:
            continue
        fid = file_id_map[fpath]
        for imp in imports:
            if imp in file_id_map:
                edges.append({
                    "source": fid,
                    "target": file_id_map[imp],
                    "type": "imports"
                })
            else:
                # External import - create dependency node
                imp_id = node_id("dep", imp)
                # Check if we already have this dep node
                if not any(n["id"] == imp_id for n in nodes):
                    nodes.append({
                        "id": imp_id,
                        "type": "dependency",
                        "name": os.path.basename(imp),
                        "filePath": imp
                    })
                edges.append({
                    "source": fid,
                    "target": imp_id,
                    "type": "imports"
                })
    
    # Create depends_on edges from neighborMap
    for fpath, neighbors in batch_neighbor_map.items():
        if fpath not in file_id_map:
            continue
        fid = file_id_map[fpath]
        for nb in neighbors:
            npath = nb["path"]
            if npath in file_id_map:
                edges.append({
                    "source": fid,
                    "target": file_id_map[npath],
                    "type": "depends_on"
                })
            else:
                ndep_id = node_id("dep", npath)
                if not any(n["id"] == ndep_id for n in nodes):
                    nodes.append({
                        "id": ndep_id,
                        "type": "dependency",
                        "name": os.path.basename(npath),
                        "filePath": npath
                    })
                edges.append({
                    "source": fid,
                    "target": ndep_id,
                    "type": "depends_on"
                })
    
    # Call graph edges from extract
    for result in extract["results"]:
        fpath = result["path"]
        if fpath not in file_id_map:
            continue
        for cg in result.get("callGraph", []):
            caller_name = cg.get("caller", "")
            callee_name = cg.get("callee", "")
            # Find function nodes
            caller_id = None
            callee_id = None
            for n in nodes:
                if n["type"] == "function" and n["filePath"] == fpath and n["name"] == caller_name:
                    caller_id = n["id"]
                # callee might be in same file or cross-file - try matching name
                if n["type"] == "function" and n["name"] == callee_name:
                    callee_id = n["id"]
            if caller_id and callee_id:
                edges.append({
                    "source": caller_id,
                    "target": callee_id,
                    "type": "calls"
                })
    
    # Check if we need to split (max 60 nodes or 120 edges)
    num_nodes = len(nodes)
    num_edges = len(edges)
    needs_split = num_nodes > 60 or num_edges > 120
    
    if needs_split:
        # Split into parts of roughly 40 nodes each
        part = 1
        idx = 0
        while idx < num_nodes:
            part_nodes = nodes[idx:idx+40]
            part_fids = {n["id"] for n in part_nodes}
            part_edges = [e for e in edges if e["source"] in part_fids or e["target"] in part_fids]
            
            part_output = {
                "batchIndex": batch_index,
                "part": part,
                "schemaVersion": 1,
                "nodes": part_nodes,
                "edges": part_edges
            }
            
            part_path = os.path.join(OUTPUT_DIR, f"batch-{batch_index}-part-{part}.json")
            with open(part_path, "w") as f:
                json.dump(part_output, f, indent=2)
            print(f"Wrote {part_path}: {len(part_nodes)} nodes, {len(part_edges)} edges")
            
            part += 1
            idx += 40
    else:
        output = {
            "batchIndex": batch_index,
            "schemaVersion": 1,
            "nodes": nodes,
            "edges": edges
        }
        out_path = os.path.join(OUTPUT_DIR, f"batch-{batch_index}.json")
        with open(out_path, "w") as f:
            json.dump(output, f, indent=2)
        print(f"Wrote {out_path}: {num_nodes} nodes, {num_edges} edges")
    
    return num_nodes, num_edges

# Process all batches
totals = {}
for bi in [6, 7, 8, 9, 10]:
    print(f"\n=== Processing batch {bi} ===")
    n, e = process_batch(bi)
    totals[bi] = (n, e)

print("\n=== SUMMARY ===")
for bi, (n, e) in sorted(totals.items()):
    print(f"Batch {bi}: {n} nodes, {e} edges")
print(f"Total: {sum(v[0] for v in totals.values())} nodes, {sum(v[1] for v in totals.values())} edges")
