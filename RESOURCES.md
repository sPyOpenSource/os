# Resources

## Primary Sources

### JVM Specification
- **The Java Virtual Machine Specification (Java SE 17 Edition)** — Chapters 2 (ClassFile Format) and 4 (Constant Pool)
  - https://docs.oracle.com/javase/specs/jvms/se17/html/

### This Codebase
- `src/jx/classfile/ClassData.java` — Root classfile parser
- `src/jx/classfile/constantpool/ConstantPool.java` — Constant pool parser
- `src/jx/classstore/ClassManager.java` — Class loading entry point
- `src/jx/classstore/ClassStore.java` — Class storage
- `src/jx/classstore/MemoryClassSource.java` — Memory-to-ClassData adapter
- `src/jx/zero/DomainStarter.java` — Kernel bridge for domain creation
- `src/jx/zip/ZipFile.java` — ZIP archive reader

## Secondary Sources

### Books
- *The Java Virtual Machine* by Jon Meyer & Troy Downing (O'Reilly) — classic reference
- *Inside the Java Virtual Machine* by Bill Venners — accessible explanation

### Articles
- "The ClassFile Format" — https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html

## Communities
- `/r/java` on Reddit — general Java discussions
- `/r/JVM` — JVM-specific topics
