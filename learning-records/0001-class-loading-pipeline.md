# Learning Record 0001: Class Loading Pipeline

## Date
2026-06-24

## What I Learned
The JCore OS loads Java classes from ZIP archives through a two-store architecture:

1. **ClassManager** splits classes into two stores: library classes (`libClassStore`) and domain (application) classes (`domClassStore`)
2. **ClassStore** is a hashtable-backed storage keyed by class name
3. **MemoryClassSource** bridges the kernel `Memory` object to `ClassData` via `DataInputStream(MemoryInputStream)`
4. **ClassFinder** is the lookup interface — `findClass(className)` normalizes dots to slashes then queries domain store first, then lib store
5. **ClassManager.addFromZip()** iterates ZIP entries, filters for `.class` files, parses each with `MemoryClassSource`, and stores the resulting `ClassData`

## Key Insight
This is not a traditional JVM classloader (no delegation model, no parent-first loading). It's a flat two-store design optimized for AOT compilation: all classes are loaded eagerly at domain-creation time, not lazily at runtime.

## Connections to the Kernel
- `Memory` objects come from the kernel's memory manager — this is how ZIP data flows from BootFS to the class parser
- The parsed `ClassData` feeds into `ByteCodeTranslater` for AOT compilation to native `.jll` files
- At runtime, `java.lang.Class` wraps `VMClass` (a kernel handle), not `ClassData`

## Open Questions
- [ ] How does `MemoryInputStream` work? What format does the `Memory` object use?
- [ ] What happens if a class references another class that hasn't been loaded yet?
- [ ] How does the assignability check work without a full class hierarchy?
