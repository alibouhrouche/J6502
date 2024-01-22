enum Block {
    /**
     *  <pre><code>
     *  SED      ; Decimal mode
     *  LDA #$05
     *  CLC
     *  ADC #$05  </code></pre>
     *  the accumulator is $10
     **/
    testADCDecimal1(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (has no effect on this sequence)
     * LDA #$05
     * ASL A	</code></pre>
     * the accumulator is $0A
     */
    testASLDecimal(3),
    /**
     * <pre><code>
     * SED      ; Decimal mode
     * LDA #$09
     * CLC
     * ADC #$01	</code></pre>
     * the accumulator is $10
     */
    testADCDecimal2(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (has no effect on this sequence)
     * LDA #$09
     * STA NUM
     * INC NUM		</code></pre>
     * NUM (assuming it is an ordinary RAM location) will contain $0A
     */
    testINCDecimal(4),
    /**
     * <pre><code>
     * CLD      ; Binary mode (binary addition: 88 + 70 + 1 = 159)
     * SEC      ; Note: carry is set, not clear!
     * LDA #$58 ; 88
     * ADC #$46 ; 70 (after this instruction, C = 0, A = $9F = 159)
     * </code></pre>
     */
    testADCDecimalCarry1(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (BCD addition: 58 + 46 + 1 = 105)
     * SEC      ; Note: carry is set, not clear!
     * LDA #$58
     * ADC #$46 ; After this instruction, C = 1, A = $05
     * </code></pre>
     */
    testADCDecimalCarry2(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (BCD addition: 12 + 34 = 46)
     * CLC
     * LDA #$12
     * ADC #$34 ; After this instruction, C = 0, A = $46
     * </code></pre>
     */
    testADCDecimalCarry3(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (BCD addition: 15 + 26 = 41)
     * CLC
     * LDA #$15
     * ADC #$26 ; After this instruction, C = 0, A = $41
     * </code></pre>
     */
    testADCDecimalCarry4(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (BCD addition: 81 + 92 = 173)
     * CLC
     * LDA #$81
     * ADC #$92 ; After this instruction, C = 1, A = $73
     * </code></pre>
     */
    testADCDecimalCarry5(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (BCD subtraction: 46 - 12 = 34)
     * SEC
     * LDA #$46
     * SBC #$12 ; After this instruction, C = 1, A = $34)
     * </code></pre>
     */
    testSBCDecimalCarry1(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (BCD subtraction: 40 - 13 = 27)
     * SEC
     * LDA #$40
     * SBC #$13 ; After this instruction, C = 1, A = $27)
     * </code></pre>
     */
    testSBCDecimalCarry2(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (BCD subtraction: 32 - 2 - 1 = 29)
     * CLC      ; Note: carry is clear, not set!
     * LDA #$32
     * SBC #$02 ; After this instruction, C = 1, A = $29)
     * </code></pre>
     */
    testSBCDecimalCarry3(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (BCD subtraction: 12 - 21)
     * SEC
     * LDA #$12
     * SBC #$21 ; After this instruction, C = 0, A = $91)
     * </code></pre>
     */
    testSBCDecimalCarry4(4),
    /**
     * <pre><code>
     * SED      ; Decimal mode (BCD subtraction: 21 - 34)
     * SEC
     * LDA #$21
     * SBC #$34 ; After this instruction, C = 0, A = $87)
     * </code></pre>
     */
    testSBCDecimalCarry5(4);
    public final int lines;
    Block(int lines){
        this.lines = lines;
    }
}
