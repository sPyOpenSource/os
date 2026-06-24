# Mission: Understand JCore OS Java Internals

## Why
Understand how the JCore (JX) ARM OS loads, parses, and executes Java programs — to navigate and contribute to the codebase effectively.

## Scope
The Java internals pipeline flows through four layers:

1. **Classfile Format** — the binary `.class` file structure (magic number, constant pool, access flags, fields, methods, attributes)
2. **Parsing Pipeline** — how `jx.classfile.*` reads raw bytes into structured `ClassData` objects
3. **Class Loading** — how `jx.classstore.*` manages classes from ZIP archives and resolves dependencies
4. **AOT Compilation** — how `ByteCodeTranslater` converts bytecode to native `.jll` files

## Goal
Be able to trace the full path a `.class` file takes from disk to execution inside the JCore OS.
