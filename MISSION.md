# Mission: PicOS Boot Sector

## Why
I need to understand, modify, and extend `bts_dsk.S` — the boot sector that loads PicOS from a floppy disk into memory and hands control to the Java runtime. The boot sector is the first code that runs when PicOS boots, and touching it is required for any low-level OS contribution.

## Success looks like
- I can explain every line in `bts_dsk.S` and what it does at boot time
- I can make changes to the boot sector (e.g., add a new boot path, support a new bootloader protocol) without breaking the boot
- I can assemble the boot sector myself and verify it fits in 512 bytes
- I understand how the boot sector hands off to Java code

## Constraints
- Learning happens in this workspace alongside the real PicOS codebase
- The assembler is GNU GAS (AT&T/Intel mixed), targeting i386 raw binary
- No emulator or test hardware may be available on this Mac

## Out of scope
- Writing a full OS from scratch
- The Java runtime or kernel code inside PicOS (we focus on the boot boundary)
- x86-64 (long mode) boot — PicOS is 32-bit at this stage
