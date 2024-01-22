package org.example;

import java.io.InputStreamReader;
import java.util.Optional;

public class IOHandler implements IOMemory {
    InputStreamReader inStream;
    IOHandler() {
        inStream = new InputStreamReader(System.in);
    }

    public boolean inRange(int address) {
        return false;
    }

    public byte read(int address) {
        return 0x00;
    }

    public void write(int address, byte value) {}

}
