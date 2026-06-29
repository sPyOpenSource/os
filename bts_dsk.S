.intel_syntax noprefix
.code16

.equ INITSTACK, 0x9BFFC

.section .bss
.global sinput
sinput:
    .space 31744

.section .text
.global _start

_start:
    jmp entry_detect

.balign 8
multiboot2_header:
    .long 0xE85250D6
    .long 0
    .long multiboot2_header_end - multiboot2_header
    .long -(0xE85250D6 + 0 + (multiboot2_header_end - multiboot2_header))
    .word 0
    .word 0
    .long 8
multiboot2_header_end:

entry_detect:
    smsw ax
    test al, 1
    jnz mb2_entry

.global PIdest, PICDAddr, PIExAddr
PIdest:     .long 0
PICDAddr:   .long 0
PIExAddr:   .long 0

.align 4, 0x00
mygdt:
    .word 0x0000, 0x0000, 0x0000, 0x0000
    .word 0xFFFF, 0x0000, 0x9A00, 0x00CF
    .word 0xFFFF, 0x0000, 0x9200, 0x00CF
    .word 0xFFFF, 0x0000, 0x9A06, 0x008F
endgdt:

ptrgdt:
    .word endgdt - mygdt
    .long mygdt

PrintChar:
    push si
    push di
    push bx
    mov bl, 0x07
    mov ah, 0x0E
    int 0x10
    pop bx
    pop di
    pop si
    ret

waitKeyboard:
    in al, 0x64
    test al, 0x02
    jnz waitKeyboard
    ret

start:
    cli
    xor ax, ax
    mov ss, ax
    mov sp, 0x7BFC
    mov ds, ax

    mov al, 'A'
    call PrintChar
    call waitKeyboard
    mov al, 0xD1
    out 0x64, al
    call waitKeyboard
    mov al, 0xDF
    out 0x60, al
    call waitKeyboard

    lgdt [ptrgdt]
    mov eax, cr0
    or al, 1
    mov cr0, eax
    mov dx, 0x010
    mov fs, dx
    dec ax
    mov cr0, eax

CallJava:
    mov al, 'P'
    call PrintChar
    lgdt [ptrgdt]
    mov eax, cr0
    or al, 1
    mov cr0, eax

    .byte 0xEA
    .long doit
    .word 0x0008

.code32
doit:
    mov dx, 0x010
    mov ds, dx
    mov es, dx
    mov fs, dx
    mov gs, dx
    mov ss, dx
    mov esp, INITSTACK
    mov edi, [PICDAddr]
    mov eax, [PIExAddr]
    call eax

Ready:
    jmp Ready

.code32
mb2_entry:
    lgdt [ptrgdt]
    mov dx, 0x010
    mov ds, dx
    mov es, dx
    mov fs, dx
    mov gs, dx
    mov ss, dx
    mov esp, INITSTACK
    mov edi, [PICDAddr]
    mov eax, [PIExAddr]
    call eax
    jmp Ready
