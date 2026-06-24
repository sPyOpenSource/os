#!/usr/bin/env node

const fs = require('fs');

const inputPath = process.argv[2] || '/Users/xuyi/Source/OS/armOS/lib/jcore/OS/.understand-anything/tmp/ua-arch-input.json';
const outputPath = process.argv[3] || '/Users/xuyi/Source/OS/armOS/lib/jcore/OS/.understand-anything/tmp/ua-arch-results.json';

const data = JSON.parse(fs.readFileSync(inputPath, 'utf-8'));
const { fileNodes, importEdges, allEdges } = data;

// ─── 1. Classify nodes ──────────────────────────────────────────
const readableNodes = [];   // nodes with file:src/... paths
const hashNodes = [];       // nodes with file:<hash> IDs
const otherNodes = [];      // anything else

for (const n of fileNodes) {
  if (n.id.startsWith('file:src/')) {
    readableNodes.push(n);
  } else if (n.id.startsWith('file:')) {
    hashNodes.push(n);
  } else {
    otherNodes.push(n);
  }
}

// ─── 2. Extract package prefix from file:src/ path ──────────────
function getPackagePrefix(fileId) {
  const path = fileId.replace(/^file:src\//, '');
  const parts = path.split('/');
  if (parts.length >= 3) {
    return parts.slice(0, 2).join('/');  // e.g. jx/classfile
  }
  if (parts.length === 2) {
    return parts[0] + '/' + parts[1];
  }
  return parts[0] || 'root';
}

// For top-level grouping
function getTopLevelPrefix(fileId) {
  const path = fileId.replace(/^file:src\//, '');
  const parts = path.split('/');
  return parts[0] || 'root';
}

// ─── 3. Group readable nodes by package prefix ──────────────────
const groups = {};
const topLevelGroups = {};
const nodeToGroup = {};

for (const n of readableNodes) {
  const grp = getPackagePrefix(n.id);
  const topGrp = getTopLevelPrefix(n.id);
  if (!groups[grp]) groups[grp] = [];
  if (!topLevelGroups[topGrp]) topLevelGroups[topGrp] = [];
  groups[grp].push(n.id);
  topLevelGroups[topGrp].push(n.id);
  nodeToGroup[n.id] = grp;
}

// ─── 4. Build adjacency: readable import edges ──────────────────
// Only 2 file:src/ -> file:src/ import edges, so we build per-group matrix

const groupList = Object.keys(groups).sort();
const groupIndex = {};
groupList.forEach((g, i) => { groupIndex[g] = i; });

const adjMatrix = groupList.map(() => groupList.map(() => 0));

// Count imports between groups (using file:src -> file:src edges only)
for (const e of importEdges) {
  const s = e.source;
  const t = e.target;
  if (s.startsWith('file:src/') && t.startsWith('file:src/')) {
    const sg = nodeToGroup[s];
    const tg = nodeToGroup[t];
    if (sg && tg) {
      adjMatrix[groupIndex[sg]][groupIndex[tg]] += 1;
    }
  }
}

// ─── 5. Intra-group density ─────────────────────────────────────
const intraDensity = {};
for (const grp of groupList) {
  const members = groups[grp];
  const n = members.length;
  if (n <= 1) {
    intraDensity[grp] = 0;
    continue;
  }
  // Count internal import edges
  let internalEdges = 0;
  for (const e of importEdges) {
    if (e.source.startsWith('file:src/') && e.target.startsWith('file:src/')) {
      if (nodeToGroup[e.source] === grp && nodeToGroup[e.target] === grp) {
        internalEdges += 1;
      }
    }
  }
  const maxPossible = n * (n - 1);
  intraDensity[grp] = maxPossible > 0 ? internalEdges / maxPossible : 0;
}

// ─── 6. Build CONTAINS hierarchy for hash-ID resolution ─────────
// Walk CONTAINS edges to map IDs up to file:src/ paths
const containsParent = {};
for (const e of allEdges) {
  if (e.type === 'CONTAINS') {
    containsParent[e.target] = e.source;
  }
}

function resolveToFilePath(nodeId, visited = new Set()) {
  if (nodeId.startsWith('file:src/')) return nodeId;
  if (visited.has(nodeId)) return null;
  visited.add(nodeId);
  if (containsParent[nodeId]) {
    return resolveToFilePath(containsParent[nodeId], visited);
  }
  return null;
}

// Try to resolve hash nodes in import edges
const resolvedHashCount = {};
let totalHashEdges = 0;
let resolvedHashEdges = 0;

for (const e of importEdges) {
  if (e.source.startsWith('file:src/') || e.target.startsWith('file:src/')) {
    continue;
  }
  totalHashEdges++;
  const sResolved = resolveToFilePath(e.source);
  const tResolved = resolveToFilePath(e.target);
  if (sResolved && tResolved && sResolved.startsWith('file:src/') && tResolved.startsWith('file:src/')) {
    resolvedHashEdges++;
    const sg = nodeToGroup[sResolved];
    const tg = nodeToGroup[tResolved];
    if (sg && tg) {
      const key = sg + ' -> ' + tg;
      resolvedHashCount[key] = (resolvedHashCount[key] || 0) + 1;
    }
  }
}

// ─── 7. All-resolved adjacency matrix (including resolved hash edges) ──
const resolvedAdjMatrix = groupList.map(() => groupList.map(() => 0));

// Add readable edges
for (const e of importEdges) {
  if (e.source.startsWith('file:src/') && e.target.startsWith('file:src/')) {
    const sg = nodeToGroup[e.source];
    const tg = nodeToGroup[e.target];
    if (sg && tg) {
      resolvedAdjMatrix[groupIndex[sg]][groupIndex[tg]] += 1;
    }
  }
}

// Add resolved hash edges
for (const e of importEdges) {
  if (e.source.startsWith('file:src/') || e.target.startsWith('file:src/')) continue;
  const sResolved = resolveToFilePath(e.source);
  const tResolved = resolveToFilePath(e.target);
  if (sResolved && tResolved) {
    const sg = nodeToGroup[sResolved];
    const tg = nodeToGroup[tResolved];
    if (sg && tg) {
      resolvedAdjMatrix[groupIndex[sg]][groupIndex[tg]] += 1;
    }
  }
}

// ─── 8. Node type summary ────────────────────────────────────────
const typeCounts = {};
for (const n of fileNodes) {
  const t = n.type || 'file';
  typeCounts[t] = (typeCounts[t] || 0) + 1;
}

// ─── 9. Pattern matching: map groups to known architectural layers
const layerPatterns = {
  'kernel-core': ['jx/buffer', 'jx/zero'],
  'classfile': ['jx/classfile'],
  'concurrency': ['jx/concurrent'],
  'filesystem': ['jx/fs', 'jx/bio'],
  'kernel-util': ['jx/timer', 'jx/timerpc', 'jx/zip'],
  'rpc': ['jx/xdr'],
  'java-stdlib': ['java/io', 'java/lang', 'java/net', 'java/util', 'java/applet', 'gnu/java'],
  'security': ['java/security', 'javax/crypto', 'sun/security'],
  'device-drivers': ['org/jnode', 'metaxa/os'],
  'other': ['sun/tools', 'metaxa', 'package.bluej', 'sun']
};

const layerAssignment = {};
for (const [layer, patterns] of Object.entries(layerPatterns)) {
  for (const p of patterns) {
    for (const grp of groupList) {
      if (grp === p || grp.startsWith(p + '/')) {
        if (!layerAssignment[layer]) layerAssignment[layer] = [];
        layerAssignment[layer].push(...groups[grp]);
      }
    }
  }
}

// Deduplicate
for (const layer of Object.keys(layerAssignment)) {
  layerAssignment[layer] = [...new Set(layerAssignment[layer])];
}

// ─── 10. Build output ────────────────────────────────────────────
const result = {
  summary: {
    totalNodes: fileNodes.length,
    readableNodes: readableNodes.length,
    hashNodes: hashNodes.length,
    totalImportEdges: importEdges.length,
    readableImportEdges: importEdges.filter(e => e.source.startsWith('file:src/') && e.target.startsWith('file:src/')).length,
    hashImportEdgesTotal: totalHashEdges,
    resolvedHashImportEdges: resolvedHashEdges,
    groupsFound: groupList.length,
    topLevelPrefixes: Object.keys(topLevelGroups).sort()
  },
  nodeTypeDistribution: typeCounts,
  nodeGroupDistribution: groupList.map(g => ({ group: g, fileCount: groups[g].length })),
  topLevelDistribution: Object.entries(topLevelGroups)
    .sort((a, b) => b[1].length - a[1].length)
    .map(([prefix, files]) => ({ prefix, fileCount: files.length })),
  importAdjacencyMatrix: {
    groups: groupList,
    matrix: adjMatrix
  },
  resolvedImportAdjacencyMatrix: {
    groups: groupList,
    matrix: resolvedAdjMatrix
  },
  intraGroupDensity: groupList.map(g => ({
    group: g,
    fileCount: groups[g].length,
    density: intraDensity[g]
  })),
  crossGroupImportSummary: (() => {
    const summary = [];
    for (let i = 0; i < groupList.length; i++) {
      for (let j = 0; j < groupList.length; j++) {
        if (resolvedAdjMatrix[i][j] > 0) {
          summary.push({
            from: groupList[i],
            to: groupList[j],
            count: resolvedAdjMatrix[i][j]
          });
        }
      }
    }
    return summary.sort((a, b) => b.count - a.count);
  })(),
  layerPatterns: Object.entries(layerAssignment).map(([layer, files]) => ({
    layer,
    fileCount: files.length,
    groups: [...new Set(files.map(f => getPackagePrefix(f)))].sort()
  })),
  metadata: {
    script: 'ua-arch-analyze.js',
    version: '1.0.0',
    inputFile: inputPath,
    generatedAt: new Date().toISOString()
  }
};

fs.writeFileSync(outputPath, JSON.stringify(result, null, 2), 'utf-8');
console.log(`Wrote ${outputPath}`);
console.log(`  ${fileNodes.length} nodes total (${readableNodes.length} readable, ${hashNodes.length} hashed)`);
console.log(`  ${importEdges.length} import edges (${result.summary.resolvedHashImportEdges} resolved from hash IDs)`);
console.log(`  ${groupList.length} package groups identified`);
console.log(`  ${Object.keys(layerAssignment).length} architectural layers matched by pattern`);
