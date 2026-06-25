# Learning Record 0002: How JCore Reimplements the JDK

## Date
2026-06-25

## What I Learned
JCore's JDK replaces three standard JDK mechanisms with its own architecture:

1. **No JNI** — All kernel interaction uses `InitialNaming.getInitialNaming().lookup("ServiceName")` instead of `native` method declarations. Services like `CPUManager`, `Clock`, `DomainManager`, `FS` are looked up by name.

2. **`java.lang.Object` is a skeleton** — All 11 methods throw `Error("Object method not implemented")`. The real implementations are injected by the AOT compiler during `.class → .jll` compilation.

3. **`Class.forName()` is a kernel bridge** — Calls `cpuManager.getClass(className)` instead of using a ClassLoader hierarchy. Each `Class` object wraps a kernel `VMClass` handle.

## Key Insight
The JDK code is designed for AOT compilation, not runtime interpretation. Reflection is mostly non-functional because the compiler resolves everything statically. This is the opposite of HotSpot's "maximally dynamic" approach.

## Connections to Lesson 1
- `ClassManager` from Lesson 1 is used at domain-creation time (by `DomainStarter`)
- `Class.forName()` from this lesson is used at runtime (bridges to kernel `VMClass`)
- These are two different paths — one for loading, one for runtime

## Open Questions
- [ ] How does the AOT compiler actually replace the skeleton methods in Object?
- [ ] What does VMClass look like on the kernel side?
- [ ] How does InitialNaming work internally?
