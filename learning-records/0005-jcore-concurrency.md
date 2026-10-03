# Learning Record 0005: JCore Concurrency

## Date
2026-10-03

## What I Learned

**The standard Java concurrency vocabulary is dead in this codebase.**

| Construct | Reality | Evidence |
|---|---|---|
| `synchronized` | no-op — `vm_monitorenter` just prints | `src/core/vmsupport.c:398` |
| `wait()` / `notify()` / `notifyAll()` | `sys_panic("not implemented")` | `src/Interface/zero_object.c:55–79` |
| `Thread.interrupt()` | empty body | `src/java/lang/Thread.java:97` |
| `Thread.isInterrupted()` | hardcoded `return false` | `src/java/lang/Thread.java:109` |
| `Thread.stop/suspend/resume/destroy` | empty bodies | `src/java/lang/Thread.java:91,112,119,122` |
| `volatile` | compiler has no `ACC_VOLATILE` handling | `Compiler/src/jx/compiler/` |
| `Mutex` portal | declared, zero call sites | `Zero/src/jx/zero/Mutex.java` |

Scale of the gap: **283 `synchronized` modifiers across 49 files** in `OS/src`, all inert.

## Key Insights

1. **Blocking lives behind the Java line.** JCore is deliberately non-blocking in Java; blocking is reached only through `jx.zero` portal calls into the kernel. This is a design choice, not a missing feature — the dissertation's §7.4 "Concurrency control" is the rationale.

2. **`disableThreadSwitching()` is the critical-section primitive.** It is what makes a test-and-set indivisible. `BlockingLockImpl` = a mutex built from it plus a `CPUState` queue. Pattern: disable → tiny critical bit → enable.

3. **`AtomicVariable` is a condition variable, not a lock.** `blockIfEqual` / `atomicUpdateUnblock` give you wait/notify semantics with no ownership requirement. `Queue.java` gets lost-wakeup-freedom by making each queue link *be* an `AtomicVariable`, so "set the pointer" and "wake the consumer" are one operation. Credits Massalin & Pu's synthesis kernel.

4. **`DISABLE_IRQ` is the memory barrier.** The native `blockIfEqual` wraps the check *and* the block in interrupts-off. On a single CPU that makes check-then-act atomic — which is why JCore needs no fences and gets no benefit from `volatile`.

5. **Scheduling is two-level round-robin with no priorities at all.** Global level cycles domains (`SCHED_GLOBAL_RR`), each domain cycles its own threads (`SCHED_LOCAL_RR`). 100 Hz timer. `setPriority()` writes a field nothing reads. **There is no occurrence of "prio" anywhere in `src/core/`.**

## Traps Worth Remembering

- **`src/core/runq.c` is dead code.** Under `-DNEW_SCHED` (on, line 51) `reschedule()` calls `Sched_preempted()`, not `Sched_reschedule()`. The live scheduler is `sched_global_rr.c`. Don't read `runq.c` and conclude the design.
- **`CPUState.STATE_BLOCKEDUSER = 9` is wrong.** The kernel has `STATE_PORTAL_WAIT_FOR_RET = 9`, `STATE_BLOCKEDUSER = 10` (`src/Headers/core/thread.h:26–27`). Latent — nothing in `OS/src` calls `CPUState.getState()`.
- **Verify build flags before trusting a name.** `-DSMP` and `-DJAVASCHEDULER` are *commented out* (lines 40–41), which is why it's single-CPU.

## Verification Habit Formed

Every claim in this lesson was checked against source rather than recalled. Three claims from an initial automated survey turned out to be **wrong or inverted** and were corrected by reading the files: SMP flag state, which scheduler is compiled, and `BootFS.java`'s true location. The lesson-writing process is itself the skill.

## Current State

Can read any concurrency code in `OS/src` and tell whether it actually synchronizes. Can explain why `synchronized` provides no exclusion. Know the four portal names that matter: `Scheduler`, `CPUManager`, `AtomicVariable`, `Mutex`(dead).

## Open Questions

- [ ] Where exactly in the AOT compiler is `monitorenter` discarded? (→ next lesson)
- [ ] How does `MultiThreadList` get away with a lock-free CAS loop on top of this?
- [ ] What is `SCHED_LOCAL_JAVA` (line 54, enabled) for, given `JAVASCHEDULER` is off?
- [ ] How does a portal call interact with preemption — where's the `cli` emitted?
- [ ] What does `CPUManager.wait(Object, int, int)` do? Looks like a second wait/notify path.