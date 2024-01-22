package org.example;

public interface Memory {
    byte read(int address);
    void write(int address, byte value);
    int readWord(int address);
    byte readZeroPage(int address);
    int readZeroPageWord(int address);
    void writeZeroPage(int address, byte value);
}
