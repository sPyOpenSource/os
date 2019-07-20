package jx.zero;

public interface NetEmulation extends Portal {
    int open(String deviceName, byte[] macaddr);
    /**
     * @param buffer 
     * @return size of received data */
    int receive(Memory buffer);
    int send(Memory buffer, int offset, int size);
    int getMTU();
    byte[] getMACAddress();
    int close();
}
