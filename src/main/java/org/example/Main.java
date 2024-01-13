package org.example;

public class Main {
    public static void main(String[] args) {
        final byte[] ROM = {
                (byte) 0xa9, 0x01, (byte) 0x8d, 0x00, 0x02, (byte) 0xa9, 0x05, (byte) 0x8d,
                0x01, 0x02, (byte) 0xa9, 0x08, (byte) 0x8d, 0x02, 0x02, (byte) 0x69, (byte) 0x05,
                (byte) 0xe9, 0x03
        };
        CPU cpu = new CPU(ROM);
        cpu.reset();
        for (int i = 0; i < 8; i++) {
            cpu.cycle();
            System.out.printf("A: 0x%02X, X: 0x%02X, Y: 0x%02X, P: 0x%02X, SP: 0x%02X, PC: 0x%04X\n",
                    cpu.accumulator, cpu.X, cpu.Y, cpu.status, cpu.stack, cpu.PC);
        }
    }
}