package org.example.enums;

public enum OpCodeG2 {
    /**
     * <h3>ASL - Arithmetic Shift Left</h3>
     * The shift left instruction shifts either the accumulator or
     * the address memory location 1 bit to the left,
     * with the bit 0 always being set to 0
     * and the the input bit 7 being stored in the carry flag.
     * ASL either shifts the accumulator left 1 bit or is a
     * read/modify/write instruction that affects only memory.
     * <br/>
     * The instruction does not affect the overflow bit,
     * sets N equal to the result bit 7 (bit 6 in the input),
     * sets Z flag if the result is equal to 0,
     * otherwise resets Z and stores the input bit 7 in the carry flag.
     */
    ASL,
    /**
     * <h3>ROL - Rotate Left</h3>
     * The rotate left instruction shifts either
     * the accumulator or addressed memory left 1 bit,
     * with the input carry being stored in bit 0 and
     * with the input bit 7 being stored in the carry flags.
     * <br/>
     * The ROL instruction either shifts the accumulator
     * left 1 bit and stores the carry in accumulator bit 0
     * or does not affect the internal registers at all.
     * The ROL instruction sets carry equal to the input bit 7,
     * sets N equal to the input bit 6 ,
     * sets the Z flag if the result of the rotate is 0,
     * otherwise it resets Z and does not affect the overflow flag at all.
     */
    ROL,
    /**
     * <h3>LSR - Logical Shift Right</h3>
     * This instruction shifts either the accumulator
     * or a specified memory location 1 bit to the right,
     * with the higher bit of the result always being set to 0,
     * and the low bit which is shifted out of the field being stored in the carry flag.
     * <br/>
     * The shift right instruction either affects the accumulator by shifting it
     * right 1 or is a read/modify/write instruction which changes a specified memory location
     * but does not affect any internal registers. The shift right does not affect the overflow flag.
     * The N flag is always reset.
     * The Z flag is set if the result of the shift is 0 and reset otherwise.
     * The carry is set equal to bit 0 of the input.
     */
    LSR,
    /**
     * <h3>ROR - Rotate Right</h3>
     * The rotate right instruction shifts either the accumulator or addressed memory right 1 bit with bit 0 shifted into the carry and carry shifted into bit 7.
     * <br/>
     * The ROR instruction either shifts the accumulator right 1 bit and stores the carry in accumulator bit 7 or does not affect the internal registers at all. The ROR instruction sets carry equal to input bit 0, sets N equal to the input carry and sets the Z flag if the result of the rotate is 0; otherwise it resets Z and does not affect the overflow flag at all.
     * <br/>
     * (Available on Microprocessors after June, 1976)
     */
    ROR,
    /**
     * <h3>STX - Store Index Register X In Memory</h3>
     * Transfers value of X register to addressed memory location.
     * <br/>
     * No flags or registers in the microprocessor are affected by the store operation.
     */
    STX,
    /**
     * <h3>LDX - Load Index Register X From Memory</h3>
     * Load the index register X from memory.
     * <br/>
     * LDX does not affect the C or V flags; sets Z if the value loaded was zero,
     * otherwise resets it; sets N if the value loaded in bit 7 is a 1;
     * otherwise N is reset, and affects only the X register.
     */
    LDX,
    /**
     * <h3>DEC - Decrement Memory By One</h3>
     * This instruction subtracts 1, in two's complement, from the contents of the addressed memory location.
     * <br/>
     * The decrement instruction does not affect any internal register in the microprocessor.
     * It does not affect the carry or overflow flags.
     * If bit 7 is on as a result of the decrement, then the N flag is set,
     * otherwise it is reset.
     * If the result of the decrement is 0, the Z flag is set, otherwise it is reset.
     */
    DEC,
    /**
     * <h3>INC - Increment Memory By One</h3>
     * This instruction adds 1 to the contents of the addressed memory location.
     * <br/>
     * The increment memory instruction does not affect any internal registers
     * and does not affect the carry or overflow flags.
     * If bit 7 is on as the result of the increment,N is set,
     * otherwise it is reset;
     * if the increment causes the result to become 0,
     * the Z flag is set on, otherwise it is reset.
     */
    INC,
}
