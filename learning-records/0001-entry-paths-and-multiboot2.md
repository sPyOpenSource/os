# 0001 — Boot Sector Entry Points and Multiboot2

The PicOS boot sector has **two entry paths**: legacy BIOS (16-bit real mode at 0x7C00) and GRUB Multiboot2 (32-bit protected mode at `_start`). The entry detection uses `smsw` to check CR0.PE — the same byte sequence `0F 01 E0` reads CR0 into AX in 16-bit mode and EAX in 32-bit mode. This means the check works from either boot path without conditional encoding.

The Multiboot2 header must be **8-byte aligned** and within the first 32768 bytes of the image. The checksum field makes the 32-bit sum of magic + architecture + header_length + checksum equal zero.
