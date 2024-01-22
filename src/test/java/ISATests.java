import org.example.CPU;
import org.example.Flags;
import org.example.MemoryFile;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static org.junit.jupiter.api.Assertions.*;

public class ISATests {
    private final CPU cpu;
    public ISATests() {
        try (RandomAccessFile file = new RandomAccessFile("roms/testsuite.bin", "r")) {
            FileChannel channel = file.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            cpu = new CPU(new MemoryFile(buffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void run(Block block) {
        cpu.stack = (byte) 0xFF;
        cpu.accumulator = 0x00;
        cpu.X = 0x00;
        cpu.Y = 0x00;
        cpu.status = 0b00100000;
        cpu.PC = cpu.mem.readWord(0xF800 + block.ordinal() * 2);
        for (int i = 0; i < block.lines; i++) {
            cpu.cycle();
        }
    }
//    void assertFlags(String flags) {
//        String actual = cpu.flagsString();
//        for (char c : flags.toCharArray()) {
//            if (c != '-') {
//                assertTrue(actual.contains(String.valueOf(c)), "Expected flag " + c + " not found in " + actual);
//            }
//        }
//    }
    @Test
    void TestADCDecimal1() {
        run(Block.testADCDecimal1);
        assertEquals(0x10, cpu.accumulator);
    }
    @Test
    void TestASLDecimal() {
        run(Block.testASLDecimal);
        assertEquals(0x0A, cpu.accumulator);
    }
    @Test
    void TestADCDecimal2() {
        run(Block.testADCDecimal2);
        assertEquals(0x10, cpu.accumulator);
    }
    @Test
    void TestINCDecimal() {
        run(Block.testINCDecimal);
        assertEquals((byte)0x0A, cpu.mem.read(0x00));
    }
    @Test
    void TestADCDecimalCarry1() {
        run(Block.testADCDecimalCarry1);
        assertAll(
                () -> assertFalse(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte) 0x9F, cpu.accumulator)
        );
    }
    @Test
    void TestADCDecimalCarry2() {
        run(Block.testADCDecimalCarry2);
        assertAll(
                () -> assertTrue(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte) 0x05, cpu.accumulator)
        );
    }
    @Test
    void TestADCDecimalCarry3() {
        run(Block.testADCDecimalCarry3);
        assertAll(
                () -> assertFalse(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte)0x46, cpu.accumulator)
        );
    }
    @Test
    void TestADCDecimalCarry4() {
        run(Block.testADCDecimalCarry4);
        assertAll(
                () -> assertFalse(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte)0x41, cpu.accumulator)
        );
    }
    @Test
    void TestADCDecimalCarry5() {
        run(Block.testADCDecimalCarry5);
        assertAll(
                () -> assertTrue(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte)0x73, cpu.accumulator)
        );
    }
    @Test
    void TestSBCDecimalCarry1() {
        run(Block.testSBCDecimalCarry1);
        assertAll(
                () -> assertTrue(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte)0x34, cpu.accumulator)
        );
    }
    @Test
    void TestSBCDecimalCarry2() {
        run(Block.testSBCDecimalCarry2);
        assertAll(
                () -> assertTrue(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte)0x27, cpu.accumulator)
        );
    }
    @Test
    void TestSBCDecimalCarry3() {
        run(Block.testSBCDecimalCarry3);
        assertAll(
                () -> assertTrue(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte)0x29, cpu.accumulator)
        );
    }
    @Test
    void TestSBCDecimalCarry4() {
        run(Block.testSBCDecimalCarry4);
        assertAll(
                () -> assertFalse(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte)0x91, cpu.accumulator)
        );
    }
    @Test
    void TestSBCDecimalCarry5() {
        run(Block.testSBCDecimalCarry5);
        assertAll(
                () -> assertFalse(cpu.getFlag(Flags.C)),
                () -> assertEquals((byte)0x87, cpu.accumulator)
        );
    }
    
}
