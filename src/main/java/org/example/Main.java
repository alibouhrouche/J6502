package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Main {
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("roms/vga.bin", "r")) {
            FileChannel fileChannel = file.getChannel();
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            MemoryFile mem = new MemoryFile(buffer);
            Screen screen = new Screen();
            mem.attachDMA(screen);
            JFrame f = new JFrame();
            f.setLayout(new BorderLayout());
            f.add(screen, BorderLayout.CENTER);
            f.setSize(128*5, 96*5);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            f.setVisible(true);
            CPU cpu = new CPU(mem);
            cpu.reset();
            Thread t = new Thread(cpu);
            t.start();
//            while (cpu.PC != 0) {
//                cpu.cycle();
//                screen.repaint();
//                System.in.read();
//                System.out.printf("A: 0x%02X, X: 0x%02X, Y: 0x%02X, P: 0x%02X, SP: 0x%02X, PC: 0x%04X\n",
//                    cpu.accumulator, cpu.X, cpu.Y, cpu.status, cpu.stack, cpu.PC);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        InputStreamReader inStream = new InputStreamReader(System.in);
//        while (inStream.ready()) {
//            System.out.println((char)inStream.read());
//        }
    }
}