# Resources

## Primary Sources

### The JX Design Literature (highest trust — the authors' own account)

- **Peter Druschel, *The Design and Implementation of the JX Operating System*** (dissertation, FAU Erlangen-Nürnberg)
  - https://www4.cs.fau.de/Projects/JX/publications/diss.pdf
  - **Chapter 7 "Processor Scheduling"** (p. 73) — scheduler configurations: §7.2.1 global kernel scheduler, §7.2.2 global + domain-local kernel schedulers, §7.2.3 + domain-local Java schedulers. These map 1:1 onto the `SCHED_*` flags in `settings.makefile`.
  - **§7.4 "Concurrency control" (p. 80)** — the design rationale for why blocking lives behind portals. Two pages. Read this for lesson 5's content.
  - Chapter 4 Domains (p. 27), Chapter 5 Communication/portals.

- **Golm, Felser, Wawersich, Kleinöder — "The JX Operating System", USENIX ATC 2002**
  - https://www4.cs.fau.de/Projects/JX/publications/jx-usenix.pdf
  - Shorter than the dissertation and the better entry point. §2.1 Domains, §2.2 Portals, §2.3 Services. Describes type-safety-based protection instead of MMU.

- **"Flexibility, Performance, Protection, and Resource Management in the Type-Safe JX Operating System"**
  - https://os.itec.kit.edu/913.php
  - Best short prose on the two-level scheduler design (§2.9 Scheduling) and domain-local type spaces.

### Background Reading

- **"Threads and Input/Output in the Synthesis Kernel"** — Massalin & Pu, ACM SOSP 1989, pp. 191–201
  - The wait-free synchronization JCore's `OS/src/jx/synch/blocking/Queue.java` credits in its file header. JX inherited this deliberately.
  - PDF: https://www.cs.unibo.it/~renzo/so/old/articoli2/MP89.pdf · DOI: https://doi.org/10.1145/74850.74869
  - Worth reading §2.3 ("Optimistic Queues") and §3.1 ("Optimistic Synchronization") — that is the direct ancestor of `AtomicVariable`.

### JVM Specification
- **The Java Virtual Machine Specification (Java SE 17 Edition)** — Chapters 2 (ClassFile Format) and 4 (Constant Pool)
  - https://docs.oracle.com/javase/specs/jvms/se17/html/

## Codebase Sources (paths relative to `jcore/` root)

### Concurrency
- `Zero/src/jx/zero/AtomicVariable.java` — condition variable portal (the `wait`/`notify` replacement)
- `Zero/src/jx/zero/Scheduler.java` — preemption inhibit (the critical-section primitive)
- `Zero/src/jx/zero/CPUManager.java` — thread lifecycle: block/unblock/yield/sleep
- `Zero/src/jx/zero/CPUState.java` — thread handle
- `Zero/src/jx/zero/Mutex.java` — **dead**, declared but never called
- `src/core/vmsupport.c:398` — `vm_monitorenter`, the `synchronized` no-op
- `src/Interface/zero_object.c:55-79` — `wait`/`notify` → `sys_panic`
- `src/Interface/zero_AtomicVariable.c` — `DISABLE_IRQ`-guarded condition variable
- `src/core/sched_global_rr.c` — **the live scheduler**
- `settings.makefile` — ground truth for which code is actually compiled
- `OS/src/jx/synch/blocking/Queue.java` — condition-variable queue pattern
- `OS/src/jx/synch/mutex/BlockingLockImpl.java` — mutex-from-scratch pattern
- `OS/src/jx/buffer/multithread/MultiThreadList.java` — lock-free CAS list

### Class Loading Pipeline
- `OS/src/jx/classfile/ClassData.java` — Root classfile parser
- `OS/src/jx/classfile/constantpool/ConstantPool.java` — Constant pool parser
- `OS/src/jx/classstore/ClassManager.java` — Class loading entry point
- `OS/src/jx/classstore/ClassStore.java` — Class storage
- `OS/src/jx/classstore/MemoryClassSource.java` — Memory-to-ClassData adapter
- `OS/src/jx/zero/DomainStarter.java` — Kernel bridge for domain creation
- `OS/src/jx/zip/ZipFile.java` — ZIP archive reader

### File System
- `OS/src/jx/fs/` — `FS`, `Node`, `FileSystem` interfaces
- `OS/src/java/io/File.java` — delegates to `jx.fs.FS`
- `Zero/src/jx/zero/BootFS.java` — boot-time in-memory filesystem

## Secondary Sources

### Books
- *The Java Virtual Machine* by Jon Meyer & Troy Downing (O'Reilly) — classic reference
- *Inside the Java Virtual Machine* by Bill Venners — accessible explanation

### Articles
- "The ClassFile Format" — https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html
- "JX (operating system)" — https://en.wikipedia.org/wiki/JX_(operating_system) — good orientation, weak on internals

## Communities

- `/r/java` on Reddit — general Java discussions
- `/r/JVM` — JVM-specific topics
- **JX mailing list / archives** — the most likely place to find maintainers discussing why `synchronized` was dropped. Search the JX project pages at https://www4.cs.fau.de/Projects/JX/ for the archive; the USENIX paper's contact address is the historical route.
