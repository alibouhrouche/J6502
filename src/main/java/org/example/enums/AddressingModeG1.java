package org.example.enums;

public enum AddressingModeG1 {
    /**
     * <h3>X-Indexed Zero Page Indirect</h3>
     * In indexed indirect addressing,
     * the second byte of the instruction is added to the contents of the X index register,
     * discarding the carry. The result of this addition points to a memory location
     * on page zero whose contents is the low order eight bits of the effective address.
     * The next memory location in page zero contains the high order eight bits of the effective address.
     * Both memory locations specifying the high and low
     * order bytes of the effective address must be in page zero.
     */
    XIndexedZeroPageIndirect,
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
     * <h3>Immediate</h3>
     * In immediate addressing,
     * the operand is contained in the second byte of the instruction,
     * with no further memory addressing required.
     */
    Immediate,
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
     * <h3>Zero Page Indirect Y-Indexed</h3>
     * In indirect indexed addressing,
     * the second byte of the instruction points to a memory location in page zero.
     * The contents of this memory location is added to the contents of the Y index register,
     * the result being the low order eight bits of the effective address.
     * The carry from this addition is added to the contents of the next page zero memory location,
     * the result being the high order eight bits of the effective address.
     */
    ZeroPageIndirectYIndexed,
    /**
     * <h3>X-Indexed Zero Page</h3>
     * This form of addressing is used in conjunction with the X index register.
     * The effective address is calculated by adding the second byte to the contents of the index register.
     * Since this is a form of "Zero Page" addressing,
     * the content of the second byte references a location in page zero.
     * Additionally, due to the “Zero Page" addressing nature of this mode,
     * no carry is added to the high order 8 bits of memory and crossing of page boundaries does not occur.
     */
    XIndexedZeroPage,
    /**
     * <h3>Y-Indexed Absolute</h3>
     * This form of addressing is used in conjunction with the Y index register.
     * The effective address is formed by adding the contents of Y to the address contained
     * in the second and third bytes of the instruction.
     * This mode allows the index register to contain the index or count value and the instruction
     * to contain the base address.
     * This type of indexing allows any location referencing and the index
     * to modify multiple fields resulting in reduced coding and execution time.
     */
    YIndexedAbsolute,
    /**
     * <h3>X-Indexed Absolute</h3>
     * This form of addressing is used in conjunction with the X index register.
     * The effective address is formed by adding the contents of
     * X to the address contained in the second and third bytes of the instruction.
     * This mode allows the index register to contain the index or count value and
     * the instruction to contain the base address.
     * This type of indexing allows any location referencing and the index
     * to modify multiple fields resulting in reduced coding and execution time.
     */
    XIndexedAbsolute,
}
