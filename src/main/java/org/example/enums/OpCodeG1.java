package org.example.enums;

public enum OpCodeG1 {
    /**
     * <h3>ORA - "OR" Memory with Accumulator</h3>
     * The ORA instruction transfers the memory and the accumulator
     * to the adder which performs a binary "OR"
     * on a bit-by-bit basis and stores the result in the accumulator.
     * <br/>
     * This instruction affects the accumulator;
     * sets the zero flag if the result in the accumulator is 0,
     * otherwise resets the zero flag;
     * sets the negative flag if the result in the accumulator has bit 7 on,
     * otherwise resets the negative flag.
     */
    ORA,
    /**
     * <h3>AND - "AND" Memory with Accumulator</h3>
     * The AND instruction transfer the accumulator and memory
     * to the adder which performs a bit-by-bit AND operation
     * and stores the result back in the accumulator.
     * <br/>
     * This instruction affects the accumulator;
     * sets the zero flag if the result in the accumulator is 0, otherwise resets the zero flag;
     * sets the negative flag if the result in the accumulator has bit 7 on,
     * otherwise resets the negative flag.
     */
    AND,
    /**
     * <h3>EOR - "Exclusive OR" Memory with Accumulator</h3>
     * The EOR instruction transfers the memory and the accumulator
     * to the adder which performs a binary "EXCLUSIVE OR"
     * on a bit-by-bit basis and stores the result in the accumulator.
     * <br/>
     * This instruction affects the accumulator;
     * sets the zero flag if the result in the accumulator is 0,
     * otherwise resets the zero flag sets the negative flag
     * if the result in the accumulator has bit 7 on,
     * otherwise resets the negative flag.
     */
    EOR,
    /**
     * <h3>ADC - Add Memory to Accumulator with Carry</h3>
     * This instruction adds the value of memory and carry
     * from the previous operation to the value of
     * the accumulator and stores the result in the accumulator.
     * <br/>
     * This instruction affects the accumulator;
     * sets the carry flag when the sum of a binary add
     * exceeds 255 or when the sum of a decimal add exceeds 99,
     * otherwise carry is reset.
     * The overflow flag is set when the sign or bit 7
     * is changed due to the result exceeding +127 or -128,
     * otherwise overflow is reset.
     * The negative flag is set if the accumulator result contains bit 7 on,
     * otherwise the negative flag is reset.
     * The zero flag is set if the accumulator result is 0,
     * otherwise the zero flag is reset.
     */
    ADC,
    /**
     * <h3>STA - Store Accumulator in Memory</h3>
     * This instruction transfers the contents of the accumulator to memory.
     * <br/>
     * This instruction affects none of the flags in the processor status register
     * and does not affect the accumulator.
     */
    STA,
    /**
     * <h3>LDA - Load Accumulator with Memory</h3>
     * When instruction LDA is executed by the microprocessor,
     * data is transferred from memory to the accumulator
     * and stored in the accumulator.
     * <br/>
     * LDA affects the contents of the accumulator,
     * does not affect the carry or overflow flags;
     * sets the zero flag if the accumulator is zero as a result of the LDA,
     * otherwise resets the zero flag;
     * sets the negative flag if bit 7 of the accumulator is a 1,
     * otherwise resets the negative flag.
     */
    LDA,
    /**
     * <h3>CMP - Compare Memory and Accumulator</h3>
     * This instruction subtracts the contents of memory from the contents of the accumulator.
     * <br/>
     * The use of the CMP affects the following flags:
     * Z flag is set on an equal comparison, reset otherwise;
     * the N flag is set or reset by the result bit 7,
     * the carry flag is set when the value in memory is less than or equal to the accumulator,
     * reset when it is greater than the accumulator.
     * The accumulator is not affected.
     */
    CMP,
    /**
     * <h3>SBC - Subtract Memory from Accumulator with Borrow</h3>
     * This instruction subtracts the value of memory and borrow from the value of the accumulator,
     * using two's complement arithmetic, and stores the result in the accumulator.
     * Borrow is defined as the carry flag complemented; therefore,
     * a resultant carry flag indicates that a borrow has not occurred.
     * <br/>
     * This instruction affects the accumulator.
     * The carry flag is set if the result is greater than or equal to 0.
     * The carry flag is reset when the result is less than 0, indicating a borrow.
     * The overflow flag is set when the result exceeds +127 or -127, otherwise it is reset.
     * The negative flag is set if the result in the accumulator has bit 7 on, otherwise it is reset.
     * The Z flag is set if the result in the accumulator is 0, otherwise it is reset.
     */
    SBC,
}
