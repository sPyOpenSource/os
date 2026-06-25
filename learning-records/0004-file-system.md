# Learning Record 0004: File System

## Date
2026-06-25

## What I Learned
JCore OS has two file systems:

1. **BootFS** — Simple in-memory filesystem for boot. Stores .jll (compiled), .zip (source), compiler files. No directories, just filenames. Used by DomainStarter to load libraries when creating domains.

2. **Runtime FS** — Unix-like filesystem with three interfaces:
   - `FS` — top-level operations (cd, mkdir, read, write, etc.)
   - `Node` — inode-level operations (permissions, timestamps, data)
   - `FileSystem` — driver-level (init with block device, get root inode)

## Key Insight
All filesystem access crosses domain boundaries via portal interfaces (`extends Portal`). This is the same pattern as InitialNaming — kernel services are accessed through inter-domain calls, not direct function calls.

## Current State
- NFS v2 is the only working implementation (`NFSInode.java`)
- EXT2 is planned but not functional
- BootFS is for loading code at startup; FS is for runtime I/O
- java.io.File delegates to jx.fs.FS via InitialNaming lookup

## Open Questions
- [ ] How does the NFS RPC implementation work?
- [ ] What's the block cache architecture?
- [ ] How does the inode overlay mechanism work for mounting?
