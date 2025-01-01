package kernel;

public class BIOS {
  private final static int BIOS_STKBSE = KernelConst.BIOS_STKEND-0x28;
  public final static int DS    = BIOS_STKBSE+0x00;
  public final static int ES    = BIOS_STKBSE+0x02;
  public final static int FS    = BIOS_STKBSE+0x04;
  public final static int FLAGS = BIOS_STKBSE+0x06;
  public final static int EDI   = BIOS_STKBSE+0x08;
  public final static int ESI   = BIOS_STKBSE+0x0C;
  public final static int EBP   = BIOS_STKBSE+0x10;
  public final static int ESP   = BIOS_STKBSE+0x14;
  public final static int EBX   = BIOS_STKBSE+0x18;
  public final static int EDX   = BIOS_STKBSE+0x1C;
  public final static int ECX   = BIOS_STKBSE+0x20;
  public final static int EAX   = BIOS_STKBSE+0x24;
  
  public final static int F_CARRY  = 0x0001;
  public final static int F_PARITY = 0x0004;
  public final static int F_AUX    = 0x0010;
  public final static int F_ZERO   = 0x0040;
  public final static int F_SIGN   = 0x0080;
  public final static int F_TRAP   = 0x0100;
  public final static int F_INT    = 0x0200;
  public final static int F_DIR    = 0x0400;
  public final static int F_OVER   = 0x0800;
  
  private static boolean initDone;
  
  //-------------------------------------------------------- call BIOS-IRQ ------------------------------------
  public static void rint(int inter) {
    int addr=KernelConst.BIOS_MEMORY+8;
    
    if (!initDone) { //initialize 16 bit code
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0xBB);
      MAGIC.wMem32(addr, KernelConst.BIOS_MEMORY); //mov ebx,0x60000(BIOS_MEMORY)
      addr+=4;
      
      MAGIC.wMem8(addr++, (byte)0x67);
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0x89);
      MAGIC.wMem8(addr++, (byte)0x23); //mov [ebx],esp
      
      MAGIC.wMem8(addr++, (byte)0x0F);
      MAGIC.wMem8(addr++, (byte)0x20);
      MAGIC.wMem8(addr++, (byte)0xC0); //mov eax,cr0
      
      MAGIC.wMem8(addr++, (byte)0x67);
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0x89);
      MAGIC.wMem8(addr++, (byte)0x43);
      MAGIC.wMem8(addr++, (byte)0x04); //mov [ebx+4],eax
       
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0x25);
      MAGIC.wMem8(addr++, (byte)0xFE);
      MAGIC.wMem8(addr++, (byte)0xFF);
      MAGIC.wMem8(addr++, (byte)0xFE);
      MAGIC.wMem8(addr++, (byte)0x7F); //and eax, 0x7FFEFFFE
      
      MAGIC.wMem8(addr++, (byte)0x0F);
      MAGIC.wMem8(addr++, (byte)0x22);
      MAGIC.wMem8(addr++, (byte)0xC0); //mov cr0, eax
      
      MAGIC.wMem8(addr++, (byte)0xEA);
      MAGIC.wMem8(addr++, (byte)0x28);
      MAGIC.wMem8(addr++, (byte)0x00);
      MAGIC.wMem16(addr, (short)(KernelConst.BIOS_MEMORY>>>4)); //jmp 0x6000(BIOS_MEMORY>>>4):0028
      addr+=2;
          
      MAGIC.wMem8(addr++, (byte)0xBA);
      MAGIC.wMem16(addr, (short)(KernelConst.BIOS_MEMORY>>>4)); //mov dx,0x6000(BIOS_MEMORY>>>4)
      addr+=2;
      
      MAGIC.wMem8(addr++, (byte)0x8E);
      MAGIC.wMem8(addr++, (byte)0xD2); //mov ss,dx
          
      MAGIC.wMem8(addr++, (byte)0x8E);
      MAGIC.wMem8(addr++, (byte)0xEA); //mov gs,dx
      
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0xBC);
      MAGIC.wMem32(addr, BIOS_STKBSE-KernelConst.BIOS_MEMORY); //mov esp,0x2000(BIOS_MEMORY-BIOS_STKBSE)
      addr+=4;
      
      MAGIC.wMem8(addr++, (byte)0x1F); //pop ds
      MAGIC.wMem8(addr++, (byte)0x07); //pop es
      
      MAGIC.wMem8(addr++, (byte)0x0f);
      MAGIC.wMem8(addr++, (byte)0xa1); //pop fs
          
      MAGIC.wMem8(addr++, (byte)0x58); //pop ax -> we have to pop something for symmetry
      
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0x61); //popad
      
      MAGIC.wMem8(addr++, (byte)0xCD);
      MAGIC.wMem8(addr++, (byte)0); //cd inter
      
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0x60); //pushad
      
      MAGIC.wMem8(addr++, (byte)0x9C); //pushf
      
      MAGIC.wMem8(addr++, (byte)0x0f);
      MAGIC.wMem8(addr++, (byte)0xa0); //push fs
          
      MAGIC.wMem8(addr++, (byte)0x06); //push es
      MAGIC.wMem8(addr++, (byte)0x1E); //push ds
      
      MAGIC.wMem8(addr++, (byte)0x2E);
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0xA1);
      MAGIC.wMem8(addr++, (byte)0x04);
      MAGIC.wMem8(addr++, (byte)0x00); //mov eax,[cs:0x0004]
          
      MAGIC.wMem8(addr++, (byte)0x0F);
      MAGIC.wMem8(addr++, (byte)0x22);
      MAGIC.wMem8(addr++, (byte)0xC0); //mov cr0, eax
      
      MAGIC.wMem8(addr++, (byte)0xEA);
      MAGIC.wMem8(addr++, (byte)0x52);
      MAGIC.wMem8(addr++, (byte)0x00);
      MAGIC.wMem8(addr++, (byte)0x18);
      MAGIC.wMem8(addr++, (byte)0x00); //jmp 0x0018:0052
      
      MAGIC.wMem8(addr++, (byte)0xBA);
      MAGIC.wMem8(addr++, (byte)0x10);
      MAGIC.wMem8(addr++, (byte)0x00); //mov dx,0x0010
      
      MAGIC.wMem8(addr++, (byte)0x8E);
      MAGIC.wMem8(addr++, (byte)0xDA); //mov ds,dx
      
      MAGIC.wMem8(addr++, (byte)0x8E);
      MAGIC.wMem8(addr++, (byte)0xC2); //mov es,dx
      
      MAGIC.wMem8(addr++, (byte)0x8E);
      MAGIC.wMem8(addr++, (byte)0xE2); //mov fs,dx
      
      MAGIC.wMem8(addr++, (byte)0x8E);
      MAGIC.wMem8(addr++, (byte)0xEA); //mov gs,dx
      
      MAGIC.wMem8(addr++, (byte)0x8E);
      MAGIC.wMem8(addr++, (byte)0xD2); //mov ss,dx
          
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0xB8);
      MAGIC.wMem32(addr, KernelConst.BIOS_MEMORY); //mov eax, BIOS_MEMORY
      addr+=4;
          
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr++, (byte)0x67);
      MAGIC.wMem8(addr++, (byte)0x8B);
      MAGIC.wMem8(addr++, (byte)0x20); //mov esp, [eax]
      
      MAGIC.wMem8(addr++, (byte)0x66);
      MAGIC.wMem8(addr, (byte)0xCB); //far ret
      
      initDone=true;
    }
    
    //real function after initialization
    MAGIC.wMem8(KernelConst.BIOS_MEMORY+61, (byte)inter); //set interrupt number
    MAGIC.inline(0x9C); //pushf
    MAGIC.inline(0xFA); //cli
    SegmentTables.lidtRM(); //load idt with real mode interrupt table
    //call 16 bit code
    MAGIC.inline(0x56); //push e/rsi
    MAGIC.inline(0x57); //push e/rdi
    if (MAGIC.ptrSize==4) {
      MAGIC.inline(0x9A); MAGIC.inline32(0x00000008); MAGIC.inline(0x18, 0x00); //call far 18:00000008
    }
    else {
      //manually code return address (code after "retf")
      MAGIC.inline(0xE8, 0x00, 0x00, 0x00, 0x00); //call rel 0
      MAGIC.inline(0x83, 0x04, 0x24, 0x0F);       //add dword [rsp],byte 0x0F
      MAGIC.inline(0xC6, 0x44, 0x24, 0x04, 0x08); //mov byte [rsp+4],0x08
      //manually code destination address 18:00000008
      MAGIC.inline(0x6A, 0x18);                   //push byte 0x18
      MAGIC.inline(0x6A, 0x08);                   //push byte 0x08
      //use return far as call
      MAGIC.inline(0x48, 0xCB);                   //retf
    }
    MAGIC.inline(0x5F); //pop e/rdi
    MAGIC.inline(0x5E); //pop e/rsi
    SegmentTables.lidt(); //load idt with protected/long mode interrupt table
    MAGIC.inline(0x9D); //popf
  }
}
