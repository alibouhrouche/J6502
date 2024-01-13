package org.example;

public class Memory {
    private byte[] RAM = new byte[0x7FFF];
    private final byte[] ROM;

    public Memory(byte[] ROM) {
        if (ROM == null) {
            throw new IllegalArgumentException("ROM cannot be null");
        }
        if (ROM.length == 0) {
            throw new IllegalArgumentException("ROM cannot be empty");
        }
        if (ROM.length > 32 * 1024) {
            throw new IllegalArgumentException("ROM must be less than 32KB");
        }
        if (ROM.length == 32 * 1024) {
            this.ROM = ROM;
        } else {
            this.ROM = new byte[32 * 1024];
            System.arraycopy(ROM, 0, this.ROM, 0, ROM.length);
            this.ROM[0x7FFC] = (byte) 0x00;
            this.ROM[0x7FFD] = (byte) 0x80;
        }
    }
    private void checkBounds(int address) {
        if (address > 0xFFFF || address < 0) {
            throw new IllegalArgumentException("Address out of bounds");
        }
    }
    public byte read(int address) {
        checkBounds(address);
        if ((address & 0x8000) == 0x8000) {
            return ROM[address & 0x7FFF];
        } else {
            return RAM[address];
        }
    }
    public void write(int address, byte value) {
        checkBounds(address);
        if ((address & 0x8000) == 0) {
            RAM[address] = value;
        }
    }
    public int readWord(int address) {
        checkBounds(address);
        if ((address & 0x8000) == 0x8000) {
            return Byte.toUnsignedInt(ROM[address & 0x7FFF]) | Byte.toUnsignedInt(ROM[(address + 1) & 0x7FFF]) << 8;
        } else {
            return Byte.toUnsignedInt(RAM[address]) | Byte.toUnsignedInt(RAM[(address + 1) & 0xFFFF]) << 8;
        }
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
