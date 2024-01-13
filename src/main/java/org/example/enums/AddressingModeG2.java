package org.example.enums;

public enum AddressingModeG2 {
    /**
     * <h3>Immediate</h3>
     * In immediate addressing,
     * the operand is contained in the second byte of the instruction,
     * with no further memory addressing required.
     */
    Immediate,
    /**
     * <h3>Zero Page</h3>
     * The zero-page instructions allow for shorter code
     * and execution times by only fetching the second byte
     * of the instruction and assuming a zero high address byte.
     * Careful use of the zero page can
     * result in significant increase in code efficiency.
     */
    ZeroPage,
    /**
     * <h3>Accumulator</h3>
     * This form of addressing is represented with a one byte instruction,
     * implying an operation on the accumulator.
     */
    Accumulator,
    /**
     * <h3>Absolute</h3>
     * In absolute addressing,
     * the second byte of the instruction specifies
     * the eight low order bits of the effective address
     * while the third byte specifies the eight high order bits.
     * Thus, the absolute addressing mode allows access
     * to the entire 65 K bytes of addressable memory.
     */
    Absolute,
    /**
     * <h3>JAM</h3>
     * Jam is an illegal instruction.
     */
    Jam,
    /**
     * <h3>X-Indexed Zero Page (Y For STX/LDX)</h3>
     * This form of addressing is used in conjunction with the (X/Y) index register.
     * The effective address is calculated by adding the second byte to the contents of the index register.
     * Since this is a form of "Zero Page" addressing,
     * the content of the second byte references a location in page zero.
     * Additionally, due to the “Zero Page" addressing nature of this mode,
     * no carry is added to the high order 8 bits of memory and crossing of page boundaries does not occur.
     */
    XYIndexedZeroPage,
    /**
     * <h3>Implied</h3>
     * In the implied addressing mode,
     * the address containing the operand is implicitly stated in the operation code of the instruction.
     */
    Implied,
    /**
     * <h3>X-Indexed Absolute (Y for SHX/LDY)</h3>
     * This form of addressing is used in conjunction with the (X/Y) index register.
     * The effective address is formed by adding the contents of
     * X to the address contained in the second and third bytes of the instruction.
     * This mode allows the index register to contain the index or count value and
     * the instruction to contain the base address.
     * This type of indexing allows any location referencing and the index
     * to modify multiple fields resulting in reduced coding and execution time.
     */
    XYIndexedAbsolute,
}
