package org.example;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Optional;

public class MemoryFile implements Memory {
    private final byte[] RAM = new byte[0x7FFF];
    private final MappedByteBuffer ROM;
    private ArrayList<IOMemory> ioMemoryList;
    public MemoryFile(MappedByteBuffer ROM) {
        if (ROM == null) {
            throw new IllegalArgumentException("ROM cannot be null");
        }
        this.ROM = ROM;
    }
    private void checkBounds(int address) {
        if (address > 0xFFFF || address < 0) {
            throw new IllegalArgumentException("Address out of bounds");
        }
    }
    public void attachDMA(IOMemory handler) {
        if (ioMemoryList == null) {
            ioMemoryList = new ArrayList<>();
        }
        ioMemoryList.add(handler);
    }
    private byte readRAM(int address) {
        return ioMemoryList.stream()
                .filter(x -> x.inRange(address))
                .findFirst()
                .map(ioMemory -> ioMemory.read(address))
                .orElseGet(() -> RAM[address]);
    }
    public byte read(int address) {
        checkBounds(address);
        if ((address & 0x8000) == 0x8000) {
            try {
                return ROM.get(address & 0x7FFF);
            } catch (IndexOutOfBoundsException e) {
                return 0x00;
            }
        }
        return readRAM(address);
    }
    public void write(int address, byte value) {
        checkBounds(address);
        if ((address & 0x8000) == 0) ioMemoryList.stream()
                .filter(x -> x.inRange(address))
                .findFirst()
                .ifPresentOrElse(x -> x.write(address, value), () -> RAM[address] = value);
    }
    public int readWord(int address) {
        checkBounds(address);
        if ((address & 0x8000) == 0x8000) {
            try {
                return Byte.toUnsignedInt(ROM.get(address & 0x7FFF)) | Byte.toUnsignedInt(ROM.get((address + 1) & 0x7FFF)) << 8;
//                return Short.toUnsignedInt(ROM.getShort(address & 0x7FFF));
            } catch (IndexOutOfBoundsException e) {
                return 0x0000;
            }
//            return Byte.toUnsignedInt(ROM[address & 0x7FFF]) | Byte.toUnsignedInt(ROM[(address + 1) & 0x7FFF]) << 8;
        }
        return Byte.toUnsignedInt(readRAM(address)) |
                (Byte.toUnsignedInt(readRAM((address + 1) & 0x7FFF)) << 8);
//        return Byte.toUnsignedInt(RAM[address]) | Byte.toUnsignedInt(RAM[(address + 1) & 0xFFFF]) << 8;
    }
    public byte readZeroPage(int address) {
        if (address > 0xFF || address < 0) {
            throw new IllegalArgumentException("Address out of bounds");
        }
        return RAM[address];
    }
    public int readZeroPageWord(int address) {
        if (address > 0xFE || address < 0) {
            throw new IllegalArgumentException("Address out of bounds");
        }
        return RAM[address] | RAM[(address + 1) & 0xFF] << 8;
    }

    public void writeZeroPage(int address, byte data) {
        if (address > 0xFE || address < 0) {
            throw new IllegalArgumentException("Address out of bounds");
        }
        RAM[address] = data;
    }
}
