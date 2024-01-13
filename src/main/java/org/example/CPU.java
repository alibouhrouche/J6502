package org.example;

import org.example.enums.*;

import java.util.function.Function;

public class CPU implements Runnable {
    public Memory mem;
    public int PC;
    public byte stack;
    public byte accumulator;
    public byte X;
    public byte Y;
    public byte status;
    public CPU(byte[] ROM) {
        mem = new Memory(ROM);
        reset();
    }

    public void reset() {
        PC = mem.readWord(0xFFFC);
        stack = (byte) 0xFF;
        accumulator = 0x00;
        X = 0x00;
        Y = 0x00;
        status = 0x00;
    }

    private byte next() {
        byte val = mem.read(PC);
        PC = (PC + 1) & 0xFFFF;
        return val;
    }

    private int nextWord() {
        int val = mem.readWord(PC);
        PC = (PC + 2) & 0xFFFF;
        return val;
    }

    private void setFlag(Flags flag, boolean value) {
        switch (flag) {
            case C:
                status = (byte) (value ? status | 0x01 : status & 0xFE);
                break;
            case Z:
                status = (byte) (value ? status | 0x02 : status & 0xFD);
                break;
            case I:
                status = (byte) (value ? status | 0x04 : status & 0xFB);
                break;
            case D:
                status = (byte) (value ? status | 0x08 : status & 0xF7);
                break;
            case B:
                status = (byte) (value ? status | 0x10 : status & 0xEF);
                break;
            case U:
                status = (byte) (value ? status | 0x20 : status & 0xDF);
                break;
            case V:
                status = (byte) (value ? status | 0x40 : status & 0xBF);
                break;
            case N:
                status = (byte) (value ? status | 0x80 : status & 0x7F);
                break;
        }
    }

    private boolean getFlag(Flags flag) {
        return switch (flag) {
            case C -> (status & 0x01) == 0x01;
            case Z -> (status & 0x02) == 0x02;
            case I -> (status & 0x04) == 0x04;
            case D -> (status & 0x08) == 0x08;
            case B -> (status & 0x10) == 0x10;
            case U -> (status & 0x20) == 0x20;
            case V -> (status & 0x40) == 0x40;
            case N -> (status & 0x80) == 0x80;
        };
    }

    private void pushStack(byte val) {
        mem.write(0x100 + Byte.toUnsignedInt(stack), val);
        stack = (byte) ((Byte.toUnsignedInt(stack) - 1) & 0xFF);
    }

    private byte popStack() {
        stack = (byte) ((Byte.toUnsignedInt(stack) + 1) & 0xFF);
        return mem.read(0x100 + Byte.toUnsignedInt(stack));
    }

    private byte readG1(AddressingModeG1 b) {
        return switch (b) {
            case XIndexedZeroPageIndirect -> mem.read(mem.readZeroPageWord((next() + X) & 0xFF));
            case ZeroPage -> mem.readZeroPage(next());
            case Immediate -> next();
            case Absolute -> mem.read(nextWord());
            case ZeroPageIndirectYIndexed -> mem.read(mem.readWord(next()) + Y);
            case XIndexedZeroPage -> mem.read((next() + X) & 0xFF);
            case YIndexedAbsolute -> mem.read(nextWord() + Y);
            case XIndexedAbsolute -> mem.read(nextWord() + X);
        };
    }

    private void writeG1(AddressingModeG1 b, byte data) {
        switch (b) {
            case XIndexedZeroPageIndirect -> mem.write(mem.readZeroPageWord((next() + X) & 0xFF), data);
            case ZeroPage -> mem.writeZeroPage(next(), data);
            case Absolute -> mem.write(nextWord(), data);
            case ZeroPageIndirectYIndexed -> mem.write(mem.readWord(next()) + Y, data);
            case XIndexedZeroPage -> mem.write((next() + X) & 0xFF, data);
            case YIndexedAbsolute -> mem.write(nextWord() + Y, data);
            case XIndexedAbsolute -> mem.write(nextWord() + X, data);
        }
    }

    private byte readG2(AddressingModeG2 b, boolean isX) {
        byte index = isX ? X : Y;
        return switch (b) {
            case Immediate -> next();
            case ZeroPage -> mem.readZeroPage(next());
            case Accumulator -> accumulator;
            case Absolute -> mem.read(nextWord());
            case XYIndexedZeroPage -> mem.read((next() + index) & 0xFF);
            case XYIndexedAbsolute -> mem.read(nextWord() + index);
            default -> (byte) 0;
        };
    }

    private void editG2(AddressingModeG2 b, Function<Byte, Byte> f) {
        switch (b) {
            case Immediate -> {
                byte input = next();
                f.apply(input);
                throw new Error("JAM");
            }
            case ZeroPage -> {
                byte address = next();
                byte input = mem.readZeroPage(address);
                byte output = f.apply(input);
                mem.writeZeroPage(address, output);
            }
            case Accumulator -> {
                byte input = accumulator;
                accumulator = f.apply(input);
            }
            case Absolute -> {
                int address = nextWord();
                byte input = mem.read(address);
                byte output = f.apply(input);
                mem.write(address, output);
            }
            case XYIndexedZeroPage -> {
                int address = (next() + X) & 0xFF;
                byte input = mem.read(address);
                byte output = f.apply(input);
                mem.write(address, output);
            }
            case XYIndexedAbsolute -> {
                int address = nextWord() + X;
                byte input = mem.read(address);
                byte output = f.apply(input);
                mem.write(address, output);
            }
            case Jam -> throw new Error("JAM");
        };
    }
    private void writeG2(AddressingModeG2 b, byte data, boolean isY) {
        switch (b) {
            case ZeroPage -> {
                byte address = next();
                mem.writeZeroPage(address, data);
            }
            case Accumulator -> { // Implied

            }
        }
    }

    private float toDecimal(byte x) {
        int u = x >> 4;
        int l = x & 0x0F;
        return (u * 10) + l;
    }

    private byte fromDecimal(float x) {
        int u = (int) x / 10;
        int l = (int) x % 10;
        return (byte) ((u << 4) + l);
    }

    public void cycle() {
        int opcode = Byte.toUnsignedInt(next());
        int a = opcode >> 5;
        int b = (opcode >> 2) & 0x07;
        int c = opcode & 0x03;
        switch (c) {
            case 1 -> handleG1(a, AddressingModeG1.values()[b]);
            case 2 -> handleG2(a, AddressingModeG2.values()[b]);
            case 0 -> handleG3(a, AddressingModeG3.values()[b]);
        }
    }
    private void handleG3(int a, AddressingModeG3 b) {
        switch (b) {
            // Conditional Branches
            case Relative -> {
                int x = a >> 1;
                boolean y = (a & 1) == 1;
                byte offset = next();
                boolean jump = switch (x) {
                    case 0 -> getFlag(Flags.N) == y;
                    case 1 -> getFlag(Flags.V) == y;
                    case 2 -> getFlag(Flags.C) == y;
                    case 3 -> getFlag(Flags.Z) == y;
                    default -> false;
                };
                if (jump) PC = (PC + offset) & 0xFFFF;
            }
            case AlterStack -> {
                switch (a) {
                    case 0 -> pushStack(stack); // PHP - Push Processor Status On Stack
                    case 1 -> {
                        // PLP - Pull Processor Status From Stack
                        byte ret = popStack();
                        setFlag(Flags.N, (ret & 0x80) == 0x80);
                        setFlag(Flags.V, (ret & 0x40) == 0x40);
                        setFlag(Flags.D, (ret & 0x08) == 0x08);
                        setFlag(Flags.I, (ret & 0x04) == 0x04);
                        setFlag(Flags.Z, (ret & 0x02) == 0x02);
                        setFlag(Flags.C, (ret & 0x01) == 0x01);
                    }
                    case 2 -> pushStack(accumulator); // PHA - Push Accumulator On Stack
                    case 3 -> accumulator = popStack(); // PLA - Pull Accumulator From Stack
                    case 4 -> {
                        // DEY - Decrement Index Y
                        Y--;
                        setFlag(Flags.Z, Y == 0);
                        setFlag(Flags.N, (Y & 0x80) == 0x80);
                    }
                    case 5 -> {
                        // TAY - Transfer Accumulator To Index Y
                        Y = accumulator;
                        setFlag(Flags.Z, Y == 0);
                        setFlag(Flags.N, (Y & 0x80) == 0x80);
                    }
                    case 6 -> {
                        // INY - Increment Index Y
                        Y++;
                        setFlag(Flags.Z, Y == 0);
                        setFlag(Flags.N, (Y & 0x80) == 0x80);
                    }
                    case 7 -> {
                        // INX - Increment Index X
                        X++;
                        setFlag(Flags.Z, X == 0);
                        setFlag(Flags.N, (X & 0x80) == 0x80);
                    }
                }
            }
            case AlterFlags -> {
                switch (a) {
                    case 0 -> setFlag(Flags.C, false); // CLC - Clear Carry Flag
                    case 1 -> setFlag(Flags.C, true); // SEC - Set Carry Flag
                    case 2 -> setFlag(Flags.I, false); // CLI - Clear Interrupt Disable
                    case 3 -> setFlag(Flags.I, true); // SEI - Set Interrupt Disable
                    case 4 -> {
                        // TYA - Transfer Index Y To Accumulator
                        accumulator = Y;
                        setFlag(Flags.Z, accumulator == 0);
                        setFlag(Flags.N, (accumulator & 0x80) == 0x80);
                    }
                    case 5 -> setFlag(Flags.V, false); // CLV - Clear Overflow Flag
                    case 6 -> setFlag(Flags.D, false); // CLD - Clear Decimal Mode
                    case 7 -> setFlag(Flags.D, true); // SED - Set Decimal Mode
                }
            }
            case Absolute -> {
                int address = nextWord();
                switch (a) {
                    case 1 -> { // BIT - Test Bits In Memory With Accumulator
                        byte ret = (byte) (accumulator & mem.read(address));
                        setFlag(Flags.Z, ret == 0);
                        setFlag(Flags.N, (ret & 0x80) == 0x80);
                        setFlag(Flags.V, (ret & 0x40) == 0x40);
                    }
                    case 2 -> PC = address; // JMP - Jump
                    case 3 -> PC = mem.readWord(address); // JMP - Jump Indirect
                    case 4 -> mem.write(address, Y); // STY - Store Index Register Y In Memory
                    case 5 -> Y = mem.read(address); // LDY - Load Index Register Y From Memory
                    case 6 -> {
                        // CPY - Compare Index Register Y With Memory
                        byte ret = (byte) (Y - mem.read(address));
                        setFlag(Flags.Z, ret == 0);
                        setFlag(Flags.N, (ret & 0x80) == 0x80);
                        setFlag(Flags.C, Y >= mem.read(address));
                    }
                    case 7 -> {
                        // CPX - Compare Index Register X With Memory
                        byte ret = (byte) (X - mem.read(address));
                        setFlag(Flags.Z, ret == 0);
                        setFlag(Flags.N, (ret & 0x80) == 0x80);
                        setFlag(Flags.C, X >= mem.read(address));
                    }
                }
            }
            case Immediate -> {
                switch (a) {
                    case 0 -> { // BRK - Break Command
                        pushStack((byte) (PC >> 8));
                        pushStack((byte) PC);
                        pushStack((byte) (status | 0x10));
                        PC = mem.readWord(0xFFFE);
                    }
                    case 1 -> { // JSR - Jump To Subroutine
                        pushStack((byte) (PC >> 8));
                        pushStack((byte) PC);
                        PC = nextWord();
                    }
                    case 2 -> {
                        // RTI - Return from Interrupt
                        byte ret = popStack();
                        setFlag(Flags.I, (ret & 0x04) == 0x04);
                        setFlag(Flags.D, (ret & 0x08) == 0x08);
                        setFlag(Flags.B, (ret & 0x10) == 0x10);
                        setFlag(Flags.U, (ret & 0x20) == 0x20);
                        setFlag(Flags.V, (ret & 0x40) == 0x40);
                        setFlag(Flags.N, (ret & 0x80) == 0x80);
                        PC = popStack() | (popStack() << 8);
                    }
                    case 3 -> {
                        // RTS - Return from Subroutine
                        PC = popStack() | (popStack() << 8);
                        PC++;
                    }
                    case 4 -> next(); // NOP - No Operation
                    case 5 -> {
                        // LDY - Load Index Register Y From Memory
                        Y = next();
                        setFlag(Flags.Z, Y == 0);
                        setFlag(Flags.N, (Y & 0x80) == 0x80);
                    }
                    case 6 -> {
                        // CPY - Compare Index Register Y With Memory
                        byte val = next();
                        byte ret = (byte) (Y - val);
                        setFlag(Flags.Z, ret == 0);
                        setFlag(Flags.N, (ret & 0x80) == 0x80);
                        setFlag(Flags.C, Y >= val);
                    }
                    case 7 -> {
                        // CPX - Compare Index Register X With Memory
                        byte val = next();
                        byte ret = (byte) (X - val);
                        setFlag(Flags.Z, ret == 0);
                        setFlag(Flags.N, (ret & 0x80) == 0x80);
                        setFlag(Flags.C, X >= val);
                    }
                }
            }
            case ZeroPage -> {
                int address = Byte.toUnsignedInt(next());
                byte val = mem.readZeroPage(address);
                switch (a) {
                    case 1 -> {
                        // BIT - Test Bits In Memory With Accumulator
                        byte ret = (byte) (accumulator & val);
                        setFlag(Flags.Z, ret == 0);
                        setFlag(Flags.N, (val & 0x80) == 0x80);
                        setFlag(Flags.V, (val & 0x40) == 0x40);
                    }
                    case 4 -> mem.writeZeroPage(address, Y); // STY - Store Index Register Y In Memory
                    case 5 -> {
                        // LDY - Load Index Register Y From Memory
                        Y = val;
                        setFlag(Flags.Z, Y == 0);
                        setFlag(Flags.N, (Y & 0x80) == 0x80);
                    }
                    case 6 -> {
                        // CPY - Compare Index Register Y With Memory
                        byte ret = (byte) (Y - val);
                        setFlag(Flags.Z, ret == 0);
                        setFlag(Flags.N, (ret & 0x80) == 0x80);
                        setFlag(Flags.C, Y >= val);
                    }
                    case 7 -> {
                        // CPX - Compare Index Register X With Memory
                        byte ret = (byte) (X - val);
                        setFlag(Flags.Z, ret == 0);
                        setFlag(Flags.N, (ret & 0x80) == 0x80);
                        setFlag(Flags.C, X >= val);
                    }
                }
            }
            case XIndexedZeroPage -> {
                byte val = mem.readZeroPage(X + Byte.toUnsignedInt(next()));
                switch (a) {
                    case 4 -> mem.writeZeroPage(X + Byte.toUnsignedInt(next()), Y); // STY - Store Index Register Y In Memory
                    case 5 -> {
                        // LDY - Load Index Register Y From Memory
                        Y = val;
                        setFlag(Flags.Z, Y == 0);
                        setFlag(Flags.N, (Y & 0x80) == 0x80);
                    }
                }
            }
            case XIndexedAbsolute -> {
                int address = nextWord();
                byte val = mem.read(address + X);
                switch (a) {
                    case 4 -> mem.write(address + X, (byte) (val & Y)); // SHY - Store Index Register Y "AND" Value
                    case 5 -> {
                        // LDY - Load Index Register Y From Memory
                        Y = val;
                        setFlag(Flags.Z, Y == 0);
                        setFlag(Flags.N, (Y & 0x80) == 0x80);
                    }
                }
            }
        }
    }
    private void handleG2(int a, AddressingModeG2 b) {
        OpCodeG2 op = OpCodeG2.values()[a];
        switch (op) {
            case ASL -> editG2(b, data -> {
                setFlag(Flags.C, (data & 0x80) == 0x80);
                data = (byte) (data << 1);
                setFlag(Flags.Z, data == 0);
                setFlag(Flags.N, (data & 0x80) == 0x80);
                return data;
            });
            case ROL -> editG2(b, data -> {
                boolean carry = getFlag(Flags.C);
                setFlag(Flags.C, (data & 0x80) == 0x80);
                data = (byte) ((data << 1) + (carry ? 1 : 0));
                setFlag(Flags.Z, data == 0);
                setFlag(Flags.N, (data & 0x80) == 0x80);
                return data;
            });
            case LSR -> editG2(b, data -> {
                setFlag(Flags.C, (data & 0x01) == 0x01);
                data = (byte) (data >> 1);
                setFlag(Flags.Z, data == 0);
                setFlag(Flags.N, (data & 0x80) == 0x80);
                return data;
            });
            case ROR -> editG2(b, data -> {
                boolean carry = getFlag(Flags.C);
                setFlag(Flags.C, (data & 0x01) == 0x01);
                data = (byte) ((data >> 1) + (carry ? 0x80 : 0));
                setFlag(Flags.Z, data == 0);
                setFlag(Flags.N, (data & 0x80) == 0x80);
                return data;
            });
            case STX -> {
                switch (b) {
                    case Immediate -> next();
                    case Absolute -> mem.write(nextWord(), X);
                    case ZeroPage -> mem.writeZeroPage(next(), X);
                    case XYIndexedZeroPage -> mem.writeZeroPage((next() + Y) & 0xFF, X);
                    // TXA - Transfer Index X To Accumulator
                    case Accumulator -> {
                        accumulator = X;
                        setFlag(Flags.N, (accumulator & 0x80) == 0x80);
                        setFlag(Flags.Z, accumulator == 0);
                    }
                    // TXS - Transfer Index X To Stack Register
                    case Implied -> pushStack(X);
                    // SHX - Store Index Register X "AND" Value (Undocumented)
                    case XYIndexedAbsolute -> {
                        int address = nextWord();
                        byte input = mem.read(address);
                        mem.write(address, (byte) (X & (input + 1)));
                    }
                }
            }
            case LDX -> {
                // TSX - Transfer Stack Pointer To Index X
                if (b == AddressingModeG2.Implied) {
                    X = popStack();
                    setFlag(Flags.N, (X & 0x80) == 0x80);
                    setFlag(Flags.Z, X == 0);
                } else {
                    X = readG2(b, false);
                }
            }
            case DEC -> {
                if (b == AddressingModeG2.Accumulator) {
                    X -= 1;
                    setFlag(Flags.N, (X & 0x80) == 0x80);
                    setFlag(Flags.Z, X == 0);
                } else {
                    editG2(b, data -> {
                        data = (byte) (data - 1);
                        setFlag(Flags.N, (data & 0x80) == 0x80);
                        setFlag(Flags.Z, data == 0);
                        return data;
                    });
                }
            }
            case INC -> {
                if (b != AddressingModeG2.Accumulator) {
                    editG2(b, data -> {
                        data = (byte) (data + 1);
                        setFlag(Flags.N, (data & 0x80) == 0x80);
                        setFlag(Flags.Z, data == 0);
                        return data;
                    });
                }
            }
        }
    }

    private void handleG1(int a, AddressingModeG1 b) {
        OpCodeG1 op = OpCodeG1.values()[a];
        switch (op) {
            case ORA -> {
                byte data = readG1(b);
                accumulator |= data;
                setFlag(Flags.Z, accumulator == 0);
                setFlag(Flags.N, (accumulator & 0x80) == 0x80);
            }
            case AND -> {
                byte data = readG1(b);
                accumulator &= data;
                setFlag(Flags.Z, accumulator == 0);
                setFlag(Flags.N, (accumulator & 0x80) == 0x80);
            }
            case EOR -> {
                byte data = readG1(b);
                accumulator ^= data;
                setFlag(Flags.Z, accumulator == 0);
                setFlag(Flags.N, (accumulator & 0x80) == 0x80);
            }
            case ADC -> {
                byte data = readG1(b);
                if (getFlag(Flags.D)) {
                    int al = (accumulator & 0x0F) + (data & 0x0F) + (getFlag(Flags.C) ? 1 : 0);
                    int ah = (accumulator >> 4) + (data >> 4) + (al > 15 ? 1 : 0);
                    setFlag(Flags.Z, ((accumulator + data + (getFlag(Flags.C) ? 1 : 0)) & 0xFF) == 0);
                    if (al > 9) al += 6;
                    setFlag(Flags.N, (ah & 0x08) != 0);
                    setFlag(Flags.V, (
                            ((ah << 4) ^ accumulator) & 128) != 0 &&
                            ((accumulator ^ data) & 0x80) == 0
                    );
                    if (ah > 9) ah += 6;
                    setFlag(Flags.C, ah > 0x0F);
                    accumulator = (byte) (((ah << 4) | (al & 0x0F)) & 0xFF);
                } else {
                    int tmp = (Byte.toUnsignedInt(accumulator) + Byte.toUnsignedInt(data) + (getFlag(Flags.C) ? 1 : 0));
                    setFlag(Flags.Z, tmp == 0);
                    setFlag(Flags.N, (tmp & 0x80) != 0);
                    setFlag(Flags.V, (
                            (accumulator & 0x80) != 0 &&
                                    ((accumulator ^ data) & 0x80) == 0)
                    );
                    setFlag(Flags.C, tmp > 0xFF);
                    accumulator = (byte) tmp;
                }
            }
            case STA -> writeG1(b, accumulator);
            case LDA -> {
                accumulator = readG1(b);
                setFlag(Flags.Z, accumulator == 0);
                setFlag(Flags.N, (accumulator & 0x80) == 0x80);
            }
            case CMP -> {
                byte data = (byte) (accumulator - readG1(b));
                setFlag(Flags.Z, data == 0);
                setFlag(Flags.N, (data & 0x80) == 0x80);
                setFlag(Flags.C, data <= 0);
            }
            case SBC -> {
                byte data = readG1(b);
                byte A = calcSBC(data);
                setFlag(Flags.C, ((accumulator - data - (getFlag(Flags.C) ? 0 : 1)) & 256) != 0);
                setFlag(Flags.Z, ((accumulator - data - (getFlag(Flags.C) ? 0 : 1)) & 255) != 0);
                setFlag(Flags.V, (
                        (accumulator - data - (getFlag(Flags.C) ? 0 : 1)) & 128) != 0 &&
                        ((accumulator ^ data) & 128) != 0
                );
                setFlag(Flags.N, (accumulator - data - (getFlag(Flags.C) ? 0 : 1) & 128) != 0);
                accumulator = A;
            }
        }
    }

    private byte calcSBC(byte data) {
        byte A;
        if (getFlag(Flags.D)) {
            int al = (accumulator & 0x0F) - (data & 0x0F) - (getFlag(Flags.C) ? 0 : 1);
            if ((al & 16) != 0) al -= 6;
            int ah = (accumulator >> 4) - (data >> 4) - ((al & 16) != 0 ? 1 : 0);
            if ((ah & 16) != 0) ah -= 6;
            A = (byte) (((ah << 4) | (al & 0x0F)) & 0xFF);
        } else {
            A = (byte) (accumulator - data - (getFlag(Flags.C) ? 0 : 1));
        }
        return A;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            cycle();
        }
    }
}
