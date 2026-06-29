# PicOS Boot Sector Resources

## Knowledge

- [OSDev Wiki: Bootloader](https://wiki.osdev.org/Bootloader)
  The canonical reference for OS development boot sequences. Use for: understanding the 512-byte boot sector layout, real-mode constraints, and A20 gate.
- [OSDev Wiki: Multiboot2](https://wiki.osdev.org/Multiboot2)
  The Multiboot2 specification for GRUB-compatible kernels. Use for: understanding the header format and entry protocol.
- [Intel® 64 and IA-32 Architectures Software Developer Manuals](https://www.intel.com/content/www/us/en/developer/articles/technical/intel-sdm.html)
  The definitive reference for x86 instruction encoding, register behavior, and protected-mode transitions. Use for: verifying instruction encodings, GDT format, CR0 bits.
- [GNU Assembler (GAS) Documentation](https://sourceware.org/binutils/docs/as/)
  The assembler used by this project. Use for: `.code16`/`.code32` directives, `.balign`, `.long`/`.word`, and Intel vs AT&T syntax.
- [GNU ld (LD) Documentation](https://sourceware.org/binutils/docs/ld/)
  Linker used to produce the raw binary. Use for: `-Ttext` to set origin, `--oformat binary`.

## Gaps

- The `BootOut` / `compile` tool that walks `bootconf.lin` and patches offsets into the boot sector is a custom PicOS tool. No public docs exist. Understanding it will require reading the source or experimenting with output.
