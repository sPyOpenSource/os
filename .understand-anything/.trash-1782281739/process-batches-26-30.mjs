#!/usr/bin/env node
import { readFileSync, writeFileSync, existsSync, mkdirSync } from 'node:fs';
import { resolve, dirname } from 'node:path';
import { fileURLToPath, pathToFileURL } from 'node:url';
import { createRequire } from 'node:module';
import { createHash } from 'node:crypto';

const __dirname = dirname(fileURLToPath(import.meta.url));
const projectRoot = resolve(__dirname, '../..');
const pluginRoot = '/Users/xuyi/.understand-anything-plugin';
const intermediateDir = __dirname;

// Read batches.json
const batchesPath = resolve(intermediateDir, 'batches.json');
const batchesData = JSON.parse(readFileSync(batchesPath, 'utf-8'));
const batches = batchesData.batches;

// Target batch indices
const targetIndices = [26, 27, 28, 29, 30];
const targetBatches = batches.filter(b => targetIndices.includes(b.batchIndex));

console.log(`Found ${targetBatches.length} target batches`);

// Prepare raw input files and run extraction for each
for (const batch of targetBatches) {
  const bi = batch.batchIndex;
  console.log(`\n=== Processing batch ${bi} ===`);

  // Build raw input - script expects batchFiles, not files
  const rawInput = {
    projectRoot,
    batchIndex: bi,
    batchFiles: batch.files,
    batchImportData: batch.batchImportData,
    neighborMap: batch.neighborMap || {}
  };

  // Write raw input
  const rawPath = resolve(intermediateDir, `batch-${bi}-raw.json`);
  writeFileSync(rawPath, JSON.stringify(rawInput, null, 2), 'utf-8');
  console.log(`  Wrote raw input: batch-${bi}-raw.json`);

  // Run extract-structure.mjs
  const extractScript = resolve(pluginRoot, 'skills/understand/extract-structure.mjs');
  const resultPath = resolve(intermediateDir, `batch-${bi}-result.json`);
  
  try {
    const { execSync } = await import('child_process');
    const cmd = `node "${extractScript}" "${rawPath}" "${resultPath}" 2>&1`;
    console.log(`  Running: ${cmd}`);
    const stdout = execSync(cmd, { timeout: 120000 });
    console.log(`  Output: ${stdout.toString().trim()}`);
  } catch (err) {
    console.error(`  Extraction error: ${err.message}`);
    if (err.stdout) console.log(`  stdout: ${err.stdout.toString()}`);
    if (err.stderr) console.log(`  stderr: ${err.stderr.toString()}`);
  }

  // Read result
  if (!existsSync(resultPath)) {
    console.error(`  Result file not found: ${resultPath}`);
    continue;
  }

  const result = JSON.parse(readFileSync(resultPath, 'utf-8'));
  console.log(`  Files analyzed: ${result.filesAnalyzed}, skipped: ${result.filesSkipped.length}`);

  // Build nodes + edges
  const nodes = [];
  const edges = [];
  const idMap = {};

  for (const fileResult of result.results) {
    // Generate stable ID from file path
    const hash = createHash('md5').update(fileResult.path).digest('hex').slice(0, 16);
    idMap[fileResult.path] = hash;

    // Build metrics
    const metrics = fileResult.metrics || {};

    // Determine if entry point (has main method or is boot entry)
    const hasMain = fileResult.functions?.some(f => f.name === 'main') ||
                    fileResult.classes?.some(c => c.name === fileResult.path.split('/').pop().replace('.java', ''));
    const isEntry = fileResult.path.includes('Main') || fileResult.path.includes('Start');

    const node = {
      id: hash,
      type: 'file',
      name: fileResult.path.split('/').pop(),
      filePath: fileResult.path,
      language: fileResult.language,
      totalLines: fileResult.totalLines,
      nonEmptyLines: fileResult.nonEmptyLines,
      entryPoint: isEntry,
      metrics: {
        importCount: metrics.importCount || 0,
        exportCount: metrics.exportCount || 0,
        functionCount: metrics.functionCount || 0,
        classCount: metrics.classCount || 0,
      }
    };
    nodes.push(node);

    // Add class nodes and contains/exports edges
    if (fileResult.classes) {
      for (const cls of fileResult.classes) {
        const classHash = createHash('md5').update(`${fileResult.path}#${cls.name}`).digest('hex').slice(0, 16);
        nodes.push({
          id: classHash,
          type: 'class',
          name: cls.name,
          filePath: fileResult.path,
          language: fileResult.language,
          startLine: cls.startLine,
          endLine: cls.endLine,
          totalLines: cls.endLine - cls.startLine + 1,
          entryPoint: false,
          metrics: {
            methodCount: cls.methods?.length || 0,
            propertyCount: cls.properties?.length || 0,
          }
        });
        edges.push({ source: hash, target: classHash, type: 'contains' });
        edges.push({ source: classHash, target: hash, type: 'exports' });
      }
    }

    // Add function nodes and edges
    if (fileResult.functions) {
      for (const fn of fileResult.functions) {
        // Skip if this function is already covered by a class
        if (fileResult.classes?.some(c => c.methods?.includes(fn.name))) continue;
        const fnHash = createHash('md5').update(`${fileResult.path}#${fn.name}`).digest('hex').slice(0, 16);
        nodes.push({
          id: fnHash,
          type: 'function',
          name: fn.name,
          filePath: fileResult.path,
          language: fileResult.language,
          startLine: fn.startLine,
          endLine: fn.endLine,
          totalLines: fn.endLine - fn.startLine + 1,
          entryPoint: false,
          metrics: { paramCount: fn.params?.length || 0 }
        });
        edges.push({ source: hash, target: fnHash, type: 'contains' });
        edges.push({ source: fnHash, target: hash, type: 'exports' });
      }
    }

    // Import edges from batchImportData
    const imports = batch.batchImportData?.[fileResult.path];
    if (imports && imports.length > 0) {
      for (const imp of imports) {
        const importHash = idMap[imp];
        if (importHash) {
          edges.push({ source: hash, target: importHash, type: 'imports' });
        }
      }
    }
  }

  // Write output
  const output = {
    batchIndex: bi,
    schemaVersion: 1,
    nodes,
    edges
  };

  // Split into parts if too large (e.g., > 500 nodes)
  const MAX_NODES_PER_PART = 500;
  if (nodes.length <= MAX_NODES_PER_PART) {
    const outPath = resolve(intermediateDir, `batch-${bi}.json`);
    writeFileSync(outPath, JSON.stringify(output, null, 2), 'utf-8');
    console.log(`  Written: batch-${bi}.json (${nodes.length} nodes, ${edges.length} edges)`);
  } else {
    const parts = Math.ceil(nodes.length / MAX_NODES_PER_PART);
    for (let p = 0; p < parts; p++) {
      const start = p * MAX_NODES_PER_PART;
      const end = Math.min((p + 1) * MAX_NODES_PER_PART, nodes.length);
      const partNodes = nodes.slice(start, end);
      const partNodeIds = new Set(partNodes.map(n => n.id));
      const partEdges = edges.filter(e => partNodeIds.has(e.source) && partNodeIds.has(e.target));
      const partOutput = {
        batchIndex: bi,
        partIndex: p + 1,
        totalParts: parts,
        schemaVersion: 1,
        nodes: partNodes,
        edges: partEdges
      };
      const outPath = resolve(intermediateDir, `batch-${bi}-part-${p + 1}.json`);
      writeFileSync(outPath, JSON.stringify(partOutput, null, 2), 'utf-8');
      console.log(`  Written: batch-${bi}-part-${p + 1}.json (${partNodes.length} nodes, ${partEdges.length} edges)`);
    }
  }

  console.log(`  Batch ${bi} complete: ${nodes.length} total nodes, ${edges.length} total edges`);
}

console.log('\n=== All batches processed ===');
