# Teaching Notes

## User Preferences
- Wants to learn Java internals grounded in this OS codebase
- Prefers to start with class loading pipeline
- Active learner — interactive lessons > passive reading
- Prior Java concurrency experience: comfortable with `synchronized` + threads, fuzzy on memory visibility
- Chose to learn **JCore's own concurrency model** over generic Java concurrency — prefers OS-grounded over language-generic framing

## Completed Lessons
1. ✅ Class loading pipeline
2. ✅ How JCore reimplements the JDK
3. ✅ 32-bit vs 64-bit
4. ✅ File system (BootFS + runtime FS)
5. ✅ Concurrency in JCore — `synchronized` is a no-op

## Future Topics (Zone of Proximal Development)
6. Where the AOT compiler discards `monitorenter` (direct follow-on from lesson 5)
7. The classfile binary format (constant pool, access flags, etc.)
8. MemoryClassSource — connecting Memory to DataInputStream
9. DomainStarter — how class loading connects to kernel domain creation
10. ByteCodeTranslater — AOT compilation pipeline
11. How VMClass works on the kernel side
12. InitialNaming — kernel service lookup internals
13. The C kernel's type system (ObjectDesc, ClassDesc, MethodDesc)
14. NFS v2 implementation — how remote filesystem access works
15. Lock-free structures — `MultiThreadList` and the CAS portal

## Working Practices
- **Verify every claim against source.** Automated codebase surveys produced three wrong/inverted claims this session (SMP flag state, which scheduler compiles, `BootFS.java` location). Always `grep -n` and read the actual file before teaching it.
- **Check build flags before trusting a name.** `settings.makefile` is the ground truth for what is actually compiled — several scheduler files are dead code.
