package jx.classstore; 

import java.io.*; 
import jx.zero.Memory; 
import jx.classfile.ClassData; 

import jx.zero.memory.MemoryInputStream;

public class MemoryClassSource extends ClassData {

  public MemoryClassSource(Memory input) throws IOException {
      super(new DataInputStream(new MemoryInputStream(input))); 
  }

  public MemoryClassSource(Memory input, boolean allowNative) throws IOException {
      super(new DataInputStream(new MemoryInputStream(input)), allowNative); 
  }
}


