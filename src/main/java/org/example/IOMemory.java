package org.example;

public interface IOMemory {
    boolean inRange(int address);
    byte read(int address);
    void write(int address, byte value);
}
