# 0003 — GDT Structure in bts_dsk.S

The four-entry GDT in bts_dsk.S was decoded byte-by-byte. Key takeaways:
- Entry 0: null descriptor (mandatory)
- Entry 1 (sel 0x08): 32-bit code, base 0, 4GB, ring 0 — `0x9A` access byte, `0xCF` flags
- Entry 2 (sel 0x10): 32-bit data, base 0, 4GB, ring 0 — `0x92` access byte
- Entry 3 (sel 0x18): 16-bit code, base 0x60000, 4GB — `0x9A` access byte, `0x8F` flags, D/B=0
- The 16-bit segment exists for BIOS callbacks from protected mode
- GAS `.word` little-endian encoding can be confusing when reading against the Intel descriptor byte layout
- The `ptrgdt` limit field may be off-by-one (`endgdt - mygdt` = size, not size-1)
