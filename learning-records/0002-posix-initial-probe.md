# 0002 — POSIX Standard: Initial Probe

The user has **surface-level familiarity** with POSIX (acronym, "run C programs") but no depth. Key gaps exposed by grilling:

- Could not name POSIX categories beyond system calls, networking, filesystem — missing: shell/utilities, threads (pthreads), terminal I/O, real-time extensions
- Did not know POSIX has been revised and things removed (gets(), bcopy/bzero/bcmp, re_comp)
- Did not know the process model POSIX guarantees at program start (PID, fd 0/1/2, environ, argc/argv)
- Did not know the "everything is a file descriptor" principle or what kinds of things fds can represent
- Did not know that FILE* and mmap() are separate from the fd abstraction
- Did not know how fork() works (returns twice, child gets 0, parent gets child PID)
- Asked to stop before copy-on-write — this is the current frontier

**Zone of proximal development:** The user can absorb POSIX concepts when explained, but has no prior mental model. Next grilling session should resume at fork()'s implementation (copy-on-write), then move to execve() and the fork-exec pattern, then signals. All of this is directly relevant to PicOS — these are the syscalls PicOS would need to implement.
