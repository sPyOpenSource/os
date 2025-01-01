package rte;

public class DynamicAri {
  public static byte binByte(byte a, byte b, byte op) {
    switch (op) {
      case (byte)'/': case (byte)'%': //almost the same code for / and %
        //adopted from http://www.mikrocontroller.net/articles/AVR-Tutorial:_Arithmetik8
        MAGIC.inline16(0x813f); //ldd  r19,y+7   ; dividend
        MAGIC.inline16(0x811e); //ldd  r17,y+6   ; divisor
        MAGIC.inline16(0x2e33); //mov  r3,r19    ; remember for sign adjustment
        MAGIC.inline16(0x2e41); //mov  r4,r17    ; remember for sign adjustment
        MAGIC.inline16(0xfc37); //sbrc r3,7      ; test sign bit, skip next negate if clear
        MAGIC.inline16(0x9531); //neg  r19       ; negate
        MAGIC.inline16(0xfc47); //sbrc r4,7      ; test sign bit, skip next negate if clear
        MAGIC.inline16(0x9511); //neg  r17       ; negate
        MAGIC.inline16(0xe028); //ldi  r18,8     ; divide 8 bit
        MAGIC.inline16(0x2700); //clr  r16       ; remainder
        MAGIC.inline16(0x2744); //clr  r20       ; quotient
          //divloop:
        MAGIC.inline16(0x0f33); //lsl  r19       ; multiply by 2
        MAGIC.inline16(0x1f00); //rol  r16       ; append next bit of dividend
        MAGIC.inline16(0x0f44); //lsl  r20       ; multiply quotient
        MAGIC.inline16(0x1701); //cp   r16,r17   ; check if divisor is greater
        MAGIC.inline16(0xf010); //brlo div_zero  ; if not, keep zero
        MAGIC.inline16(0x6041); //sbr  r20,1     ; if so, set bit
        MAGIC.inline16(0x1b01); //sub  r16,r17   ; and subtract divisor
          //div_zero:
        MAGIC.inline16(0x952a); //dec  r18       ; decrease loop counter
        MAGIC.inline16(0xf7b9); //brne divloop   ; repeat
         //quotient in r20, remainder in r16
        MAGIC.inline16(0x813d); //ldd  r19,y+5   ; get op
        MAGIC.inline16(0x3235); //cpi  r19,'%'   ; test if modulo
        MAGIC.inline16(0xf011); //breq modop     ; if so, skip div-part
        MAGIC.inline16(0x2f04); //mov  r16,r20   ; copy result
        MAGIC.inline16(0x2434); //eor  r3,r4     ; xor-sign
          //modop:
        MAGIC.inline16(0xfc37); //sbrc r3,7      ; test sign bit, skip next negate if clear
        MAGIC.inline16(0x9501); //neg  r16       ; negate
//        MAGIC.inline16(0x830f); //std  y+7,r16   ; store result
        break;
    }
    MAGIC.stopBlockCoding();
    return a;
  }

  public static short binShort(short a, short b, byte op) {
    return (short)binInt((int)a, (int)b, op); //use implementation for int
  }

  public static int binInt(int a, int b, byte op) {
    switch (op) {
      case (byte)'*':
        //selfmade
        MAGIC.inline16(0x845a); //ldd  r5,y+10
        MAGIC.inline16(0x846b); //ldd  r6,y+11
        MAGIC.inline16(0x847c); //ldd  r7,y+12
        MAGIC.inline16(0x848d); //ldd  r8,y+13
        MAGIC.inline16(0x814e); //ldd  r20,y+6
        MAGIC.inline16(0x815f); //ldd  r21,y+7
        MAGIC.inline16(0x8568); //ldd  r22,y+8
        MAGIC.inline16(0x8579); //ldd  r23,y+9
        //multiply a1..a4==r5..r8; b1..b4==r20..r23
        MAGIC.inline16(0x9e57); //mul r5,r23 ;a1*b4
        MAGIC.inline16(0x2d30); //mov r19,r0
        MAGIC.inline16(0x9e56); //mul r5,r22 ;a1*b3
        MAGIC.inline16(0x2d20); //mov r18,r0
        MAGIC.inline16(0x0d31); //add r19,r1
        MAGIC.inline16(0x9e54); //mul r5,r20 ;a1*b1
        MAGIC.inline16(0x2d00); //mov r16,r0
        MAGIC.inline16(0x2d11); //mov r17,r1
        MAGIC.inline16(0x9e55); //mul r5,r21 ;a1*b2
        MAGIC.inline16(0x0d10); //add r17,r0
        MAGIC.inline16(0x1d21); //adc r18,r1
        MAGIC.inline16(0x1d3b); //adc r19,r11
        MAGIC.inline16(0x9e64); //mul r6,r20 ;a2*b1
        MAGIC.inline16(0x0d10); //add r17,r0
        MAGIC.inline16(0x1d21); //adc r18,r1
        MAGIC.inline16(0x1d3b); //adc r19,r11
        MAGIC.inline16(0x9e65); //mul r6,r21 ;a2*b2
        MAGIC.inline16(0x0d20); //add r18,r0
        MAGIC.inline16(0x1d31); //adc r19,r1
        MAGIC.inline16(0x9e66); //mul r6,r22 ;a2*b3
        MAGIC.inline16(0x0d30); //add r19,r0
        MAGIC.inline16(0x9e74); //mul r7,r20 ;a3*b1
        MAGIC.inline16(0x0d20); //add r18,r0
        MAGIC.inline16(0x1d31); //adc r19,r1
        MAGIC.inline16(0x9e75); //mul r7,r21 ;a3*b2
        MAGIC.inline16(0x0d30); //add r19,r0
        MAGIC.inline16(0x9e84); //mul r8,r20 ;a4*b1
        MAGIC.inline16(0x0d30); //add r19,r0
        //allDone
//        MAGIC.inline16(0x870a); //std  y+10,r16
//        MAGIC.inline16(0x871b); //std  y+11,r17
//        MAGIC.inline16(0x872c); //std  y+12,r18
//        MAGIC.inline16(0x873d); //std  y+13,r19
        break;
      case (byte)'/': case (byte)'%': //allmost the same code for / and %
        //adopted from http://www.mikrocontroller.net/topic/48511
        MAGIC.inline16(0x845a); //ldd  r5,y+10
        MAGIC.inline16(0x846b); //ldd  r6,y+11
        MAGIC.inline16(0x847c); //ldd  r7,y+12
        MAGIC.inline16(0x848d); //ldd  r8,y+13
        MAGIC.inline16(0x814e); //ldd  r20,y+6
        MAGIC.inline16(0x815f); //ldd  r21,y+7
        MAGIC.inline16(0x8568); //ldd  r22,y+8
        MAGIC.inline16(0x8579); //ldd  r23,y+9
          //remember and remove signs
        MAGIC.inline16(0x2c38); //mov  r3,r8
        MAGIC.inline16(0x2e47); //mov  r4,r23
        MAGIC.inline16(0x2033); //tst  r3
        MAGIC.inline16(0xf442); //brpl checkNext1
        MAGIC.inline16(0x9450); //com  r5
        MAGIC.inline16(0x9460); //com  r6
        MAGIC.inline16(0x9470); //com  r7
        MAGIC.inline16(0x9480); //com  r8
        MAGIC.inline16(0x1c5b); //adc  r5,r11
        MAGIC.inline16(0x1c6b); //adc  r6,r11
        MAGIC.inline16(0x1c7b); //adc  r7,r11
        MAGIC.inline16(0x1c8b); //adc  r8,r11
          //checkNext1:
        MAGIC.inline16(0x2044); //tst  r4
        MAGIC.inline16(0xf442); //brpl checkNext2
        MAGIC.inline16(0x9540); //com  r20
        MAGIC.inline16(0x9550); //com  r21
        MAGIC.inline16(0x9560); //com  r22
        MAGIC.inline16(0x9570); //com  r23
        MAGIC.inline16(0x1d4b); //adc  r20,r11
        MAGIC.inline16(0x1d5b); //adc  r21,r11
        MAGIC.inline16(0x1d6b); //adc  r22,r11
        MAGIC.inline16(0x1d7b); //adc  r23,r11
          //checkNext2:
          //divide
        MAGIC.inline16(0x2700); //clr  r16
        MAGIC.inline16(0x2711); //clr  r17
        MAGIC.inline16(0x2722); //clr  r18
        MAGIC.inline16(0x2733); //clr  r19
        MAGIC.inline16(0xe280); //ldi  r24, 32
          //udi1:
        MAGIC.inline16(0x0c55); //lsl  r5
        MAGIC.inline16(0x1c66); //rol  r6
        MAGIC.inline16(0x1c77); //rol  r7
        MAGIC.inline16(0x1c88); //rol  r8
        MAGIC.inline16(0x1f00); //rol  r16
        MAGIC.inline16(0x1f11); //rol  r17
        MAGIC.inline16(0x1f22); //rol  r18
        MAGIC.inline16(0x1f33); //rol  r19
        MAGIC.inline16(0x1704); //cp   r16, r20
        MAGIC.inline16(0x0715); //cpc  r17, r21
        MAGIC.inline16(0x0726); //cpc  r18, r22
        MAGIC.inline16(0x0737); //cpc  r19, r23
        MAGIC.inline16(0xf028); //brcs udi2
        MAGIC.inline16(0x1b04); //sub  r16, r20
        MAGIC.inline16(0x0b15); //sbc  r17, r21
        MAGIC.inline16(0x0b26); //sbc  r18, r22
        MAGIC.inline16(0x0b37); //sbc  r19, r23
        MAGIC.inline16(0x9453); //inc  r5
          //udi2:
        MAGIC.inline16(0x958a); //dec  r24
        MAGIC.inline16(0xf761); //brne udi1
          //r5..8 = r5..8 / r20..23, r5..8 = remainder
        MAGIC.inline16(0x818d); //ldd  r24,y+5
        MAGIC.inline16(0x3285); //cpi  r24,'%'
        MAGIC.inline16(0xf029); //breq modop
        MAGIC.inline16(0x2d05); //mov  r16,r5
        MAGIC.inline16(0x2d16); //mov  r17,r6
        MAGIC.inline16(0x2d27); //mov  r18,r7
        MAGIC.inline16(0x2d38); //mov  r19,r8
        MAGIC.inline16(0x2434); //eor  r3,r4
          //modop:
          //divide done, adjust sign
        MAGIC.inline16(0x2033); //tst  r3
        MAGIC.inline16(0xf442); //brpl allDone
        MAGIC.inline16(0x9500); //com  r16
        MAGIC.inline16(0x9510); //com  r17
        MAGIC.inline16(0x9520); //com  r18
        MAGIC.inline16(0x9530); //com  r19
        MAGIC.inline16(0x1d0b); //adc  r16,r11
        MAGIC.inline16(0x1d1b); //adc  r17,r11
        MAGIC.inline16(0x1d2b); //adc  r18,r11
        MAGIC.inline16(0x1d3b); //adc  r19,r11
          //allDone:
//        MAGIC.inline16(0x870a); //std  y+10,r16
//        MAGIC.inline16(0x871b); //std  y+11,r17
//        MAGIC.inline16(0x872c); //std  y+12,r18
//        MAGIC.inline16(0x873d); //std  y+13,r19
      break;
    }
    MAGIC.stopBlockCoding();
    return a;
  }
  
  public long binLong(long a, long b, byte op) {
    switch (op) {
      case (byte)'*':
        //selfmade
        MAGIC.inline16(0x848e); //ldd  r8,y+14
        MAGIC.inline16(0x849f); //ldd  r9,y+15
        MAGIC.inline16(0x88a8); //ldd  r10,y+16
        MAGIC.inline16(0x8989); //ldd  r24,y+17
        MAGIC.inline16(0x899a); //ldd  r25,y+18
        MAGIC.inline16(0x89ab); //ldd  r26,y+19
        MAGIC.inline16(0x89bc); //ldd  r27,y+20
        MAGIC.inline16(0x89ed); //ldd  r30,y+21
        MAGIC.inline16(0x800e); //ldd  r0,y+6
        MAGIC.inline16(0x801f); //ldd  r1,y+7
        MAGIC.inline16(0x8428); //ldd  r2,y+8
        MAGIC.inline16(0x8439); //ldd  r3,y+9
        MAGIC.inline16(0x844a); //ldd  r4,y+10
        MAGIC.inline16(0x845b); //ldd  r5,y+11
        MAGIC.inline16(0x846c); //ldd  r6,y+12
        MAGIC.inline16(0x847d); //ldd  r7,y+13
           //;multiply a1..8==r0..8; b1..8==r8..10+24..27+30; res1..8==r16..23, cnt==r31
        MAGIC.inline16(0xe4f0); //ldi  r31,64
        MAGIC.inline16(0x2700); //clr  r16
        MAGIC.inline16(0x2711); //clr  r17
        MAGIC.inline16(0x2722); //clr  r18
        MAGIC.inline16(0x2733); //clr  r19
        MAGIC.inline16(0x2744); //clr  r20
        MAGIC.inline16(0x2755); //clr  r21
        MAGIC.inline16(0x2766); //clr  r22
        MAGIC.inline16(0x2777); //clr  r23
          //nextBit:
        MAGIC.inline16(0x95e6); //lsr  r30
        MAGIC.inline16(0x95b7); //ror  r27
        MAGIC.inline16(0x95a7); //ror  r26
        MAGIC.inline16(0x9597); //ror  r25
        MAGIC.inline16(0x9587); //ror  r24
        MAGIC.inline16(0x94a7); //ror  r10
        MAGIC.inline16(0x9497); //ror  r9
        MAGIC.inline16(0x9487); //ror  r8
        MAGIC.inline16(0xf440); //brcc noAdd
        MAGIC.inline16(0x0d00); //add  r16,r0
        MAGIC.inline16(0x1d11); //adc  r17,r1
        MAGIC.inline16(0x1d22); //adc  r18,r2
        MAGIC.inline16(0x1d33); //adc  r19,r3
        MAGIC.inline16(0x1d44); //adc  r20,r4
        MAGIC.inline16(0x1d55); //adc  r21,r5
        MAGIC.inline16(0x1d66); //adc  r22,r6
        MAGIC.inline16(0x1d77); //adc  r23,r7
          //noAdd:
        MAGIC.inline16(0x0c00); //lsl  r0
        MAGIC.inline16(0x1c11); //rol  r1
        MAGIC.inline16(0x1c22); //rol  r2
        MAGIC.inline16(0x1c33); //rol  r3
        MAGIC.inline16(0x1c44); //rol  r4
        MAGIC.inline16(0x1c55); //rol  r5
        MAGIC.inline16(0x1c66); //rol  r6
        MAGIC.inline16(0x1c77); //rol  r7
        MAGIC.inline16(0x95fa); //dec  r31
        MAGIC.inline16(0xf729); //brne nextBit
           //;allDone
//        MAGIC.inline16(0x870e); //std  y+14,r16
//        MAGIC.inline16(0x871f); //std  y+15,r17
//        MAGIC.inline16(0x8b28); //std  y+16,r18
//        MAGIC.inline16(0x8b39); //std  y+17,r19
//        MAGIC.inline16(0x8b4a); //std  y+18,r20
//        MAGIC.inline16(0x8b5b); //std  y+19,r21
//        MAGIC.inline16(0x8b6c); //std  y+20,r22
//        MAGIC.inline16(0x8b7d); //std  y+21,r23
        break;
      case (byte)'/': case (byte)'%': //almost the same code for / and %
        //adopted from http://www.mikrocontroller.net/topic/48511
        MAGIC.inline16(0x848e); //ldd  r8,y+14
        MAGIC.inline16(0x849f); //ldd  r9,y+15
        MAGIC.inline16(0x88a8); //ldd  r10,y+16
        MAGIC.inline16(0x8989); //ldd  r24,y+17
        MAGIC.inline16(0x899a); //ldd  r25,y+18
        MAGIC.inline16(0x89ab); //ldd  r26,y+19
        MAGIC.inline16(0x89bc); //ldd  r27,y+20
        MAGIC.inline16(0x89ed); //ldd  r30,y+21
        MAGIC.inline16(0x800e); //ldd  r0,y+6
        MAGIC.inline16(0x801f); //ldd  r1,y+7
        MAGIC.inline16(0x8428); //ldd  r2,y+8
        MAGIC.inline16(0x8439); //ldd  r3,y+9
        MAGIC.inline16(0x844a); //ldd  r4,y+10
        MAGIC.inline16(0x845b); //ldd  r5,y+11
        MAGIC.inline16(0x846c); //ldd  r6,y+12
        MAGIC.inline16(0x847d); //ldd  r7,y+13
         //remember and remove signs
        MAGIC.inline16(0x23ee); //tst  r30
        MAGIC.inline16(0xf482); //brpl checkNext1
        MAGIC.inline16(0x9480); //com  r8
        MAGIC.inline16(0x9490); //com  r9
        MAGIC.inline16(0x94a0); //com  r10
        MAGIC.inline16(0x9580); //com  r24
        MAGIC.inline16(0x9590); //com  r25
        MAGIC.inline16(0x95a0); //com  r26
        MAGIC.inline16(0x95b0); //com  r27
        MAGIC.inline16(0x95e0); //com  r30
        MAGIC.inline16(0x1c8b); //adc  r8,r11
        MAGIC.inline16(0x1c9b); //adc  r9,r11
        MAGIC.inline16(0x1cab); //adc  r10,r11
        MAGIC.inline16(0x1d8b); //adc  r24,r11
        MAGIC.inline16(0x1d9b); //adc  r25,r11
        MAGIC.inline16(0x1dab); //adc  r26,r11
        MAGIC.inline16(0x1dbb); //adc  r27,r11
        MAGIC.inline16(0x1deb); //adc  r30,r11
          //checkNext1:
        MAGIC.inline16(0x2077); //tst  r7
        MAGIC.inline16(0xf482); //brpl checkNext2
        MAGIC.inline16(0x9400); //com  r0
        MAGIC.inline16(0x9410); //com  r1
        MAGIC.inline16(0x9420); //com  r2
        MAGIC.inline16(0x9430); //com  r3
        MAGIC.inline16(0x9440); //com  r4
        MAGIC.inline16(0x9450); //com  r5
        MAGIC.inline16(0x9460); //com  r6
        MAGIC.inline16(0x9470); //com  r7
        MAGIC.inline16(0x1c0b); //adc  r0,r11
        MAGIC.inline16(0x1c1b); //adc  r1,r11
        MAGIC.inline16(0x1c2b); //adc  r2,r11
        MAGIC.inline16(0x1c3b); //adc  r3,r11
        MAGIC.inline16(0x1c4b); //adc  r4,r11
        MAGIC.inline16(0x1c5b); //adc  r5,r11
        MAGIC.inline16(0x1c6b); //adc  r6,r11
        MAGIC.inline16(0x1c7b); //adc  r7,r11
          //checkNext2:
          //divide
        MAGIC.inline16(0x2700); //clr  r16
        MAGIC.inline16(0x2711); //clr  r17
        MAGIC.inline16(0x2722); //clr  r18
        MAGIC.inline16(0x2733); //clr  r19
        MAGIC.inline16(0x2744); //clr  r20
        MAGIC.inline16(0x2755); //clr  r21
        MAGIC.inline16(0x2766); //clr  r22
        MAGIC.inline16(0x2777); //clr  r23
        MAGIC.inline16(0xe4f0); //ldi  r31,64
          //udi1:
        MAGIC.inline16(0x0c88); //lsl  r8
        MAGIC.inline16(0x1c99); //rol  r9
        MAGIC.inline16(0x1caa); //rol  r10
        MAGIC.inline16(0x1f88); //rol  r24
        MAGIC.inline16(0x1f99); //rol  r25
        MAGIC.inline16(0x1faa); //rol  r26
        MAGIC.inline16(0x1fbb); //rol  r27
        MAGIC.inline16(0x1fee); //rol  r30
        MAGIC.inline16(0x1f00); //rol  r16
        MAGIC.inline16(0x1f11); //rol  r17
        MAGIC.inline16(0x1f22); //rol  r18
        MAGIC.inline16(0x1f33); //rol  r19
        MAGIC.inline16(0x1f44); //rol  r20
        MAGIC.inline16(0x1f55); //rol  r21
        MAGIC.inline16(0x1f66); //rol  r22
        MAGIC.inline16(0x1f77); //rol  r23
        MAGIC.inline16(0x1500); //cp   r16, r0
        MAGIC.inline16(0x0511); //cpc  r17, r1
        MAGIC.inline16(0x0522); //cpc  r18, r2
        MAGIC.inline16(0x0533); //cpc  r19, r3
        MAGIC.inline16(0x0544); //cpc  r20, r4
        MAGIC.inline16(0x0555); //cpc  r21, r5
        MAGIC.inline16(0x0566); //cpc  r22, r6
        MAGIC.inline16(0x0577); //cpc  r23, r7
        MAGIC.inline16(0xf048); //brcs udi2
        MAGIC.inline16(0x1900); //sub  r16, r0
        MAGIC.inline16(0x0911); //sbc  r17, r1
        MAGIC.inline16(0x0922); //sbc  r18, r2
        MAGIC.inline16(0x0933); //sbc  r19, r3
        MAGIC.inline16(0x0944); //sbc  r20, r4
        MAGIC.inline16(0x0955); //sbc  r21, r5
        MAGIC.inline16(0x0966); //sbc  r22, r6
        MAGIC.inline16(0x0977); //sbc  r23, r7
        MAGIC.inline16(0x9483); //inc  r8
          //udi2:
        MAGIC.inline16(0x95fa); //dec  r31
        MAGIC.inline16(0xf6e1); //brne udi1
        MAGIC.inline16(0x880d); //ldd  r0,y+21
        MAGIC.inline16(0x841d); //ldd  r1,y+13
         //r5..8 = r5..8 / r20..23, r5..8 = remainder
        MAGIC.inline16(0x81fd); //ldd  r31,y+5
        MAGIC.inline16(0x32f5); //cpi  r31,'%'
        MAGIC.inline16(0xf049); //breq modop
        MAGIC.inline16(0x2d08); //mov  r16,r8
        MAGIC.inline16(0x2d19); //mov  r17,r9
        MAGIC.inline16(0x2d2a); //mov  r18,r10
        MAGIC.inline16(0x2f38); //mov  r19,r24
        MAGIC.inline16(0x2f49); //mov  r20,r25
        MAGIC.inline16(0x2f5a); //mov  r21,r26
        MAGIC.inline16(0x2f6b); //mov  r22,r27
        MAGIC.inline16(0x2f7e); //mov  r23,r30
        MAGIC.inline16(0x2401); //eor  r0,r1
          //modop:
          //divide done, adjust sign
        MAGIC.inline16(0x2000); //tst  r0
        MAGIC.inline16(0xf482); //brpl allDone
        MAGIC.inline16(0x9500); //com  r16
        MAGIC.inline16(0x9510); //com  r17
        MAGIC.inline16(0x9520); //com  r18
        MAGIC.inline16(0x9530); //com  r19
        MAGIC.inline16(0x9540); //com  r20
        MAGIC.inline16(0x9550); //com  r21
        MAGIC.inline16(0x9560); //com  r22
        MAGIC.inline16(0x9570); //com  r23
        MAGIC.inline16(0x1d0b); //adc  r16,r11
        MAGIC.inline16(0x1d1b); //adc  r17,r11
        MAGIC.inline16(0x1d2b); //adc  r18,r11
        MAGIC.inline16(0x1d3b); //adc  r19,r11
        MAGIC.inline16(0x1d4b); //adc  r20,r11
        MAGIC.inline16(0x1d5b); //adc  r21,r11
        MAGIC.inline16(0x1d6b); //adc  r22,r11
        MAGIC.inline16(0x1d7b); //adc  r23,r11
          //allDone:
//        MAGIC.inline16(0x870e); //std  y+14,r16
//        MAGIC.inline16(0x871f); //std  y+15,r17
//        MAGIC.inline16(0x8b28); //std  y+16,r18
//        MAGIC.inline16(0x8b39); //std  y+17,r19
//        MAGIC.inline16(0x8b4a); //std  y+18,r20
//        MAGIC.inline16(0x8b5b); //std  y+19,r21
//        MAGIC.inline16(0x8b6c); //std  y+20,r22
//        MAGIC.inline16(0x8b7d); //std  y+21,r23
        break;
    }
    MAGIC.stopBlockCoding();
    return a;
  }
}
