  .org $8000
NUM = 0x00
reset:
testADCDecimal1:
    SED      ; Decimal mode
    LDA #$05
    CLC
    ADC #$05
testASLDecimal:
    SED      ; Decimal mode (has no effect on this sequence)
    LDA #$05
    ASL A
testADCDecimal2:
    SED      ; Decimal mode
    LDA #$09
    CLC
    ADC #$01
testINCDecimal:
    SED      ; Decimal mode (has no effect on this sequence)
    LDA #$09
    STA NUM
    INC NUM
testADCDecimalCarry1:
    CLD      ; Binary mode (binary addition: 88 + 70 + 1 = 159)
    SEC      ; Note: carry is set, not clear!
    LDA #$58 ; 88
    ADC #$46 ; 70 (after this instruction, C = 0, A = $9F = 159)
testADCDecimalCarry2:
    SED      ; Decimal mode (BCD addition: 58 + 46 + 1 = 105)
    SEC      ; Note: carry is set, not clear!
    LDA #$58
    ADC #$46 ; After this instruction, C = 1, A = $05
testADCDecimalCarry3:
    SED      ; Decimal mode (BCD addition: 12 + 34 = 46)
    CLC
    LDA #$12
    ADC #$34 ; After this instruction, C = 0, A = $46
testADCDecimalCarry4:
    SED      ; Decimal mode (BCD addition: 15 + 26 = 41)
    CLC
    LDA #$15
    ADC #$26 ; After this instruction, C = 0, A = $41
testADCDecimalCarry5:
    SED      ; Decimal mode (BCD addition: 81 + 92 = 173)
    CLC
    LDA #$81
    ADC #$92 ; After this instruction, C = 1, A = $73
testSBCDecimalCarry1:
    SED      ; Decimal mode (BCD subtraction: 46 - 12 = 34)
    SEC
    LDA #$46
    SBC #$12 ; After this instruction, C = 1, A = $34)
testSBCDecimalCarry2:
    SED      ; Decimal mode (BCD subtraction: 40 - 13 = 27)
    SEC
    LDA #$40
    SBC #$13 ; After this instruction, C = 1, A = $27)
testSBCDecimalCarry3:
    SED      ; Decimal mode (BCD subtraction: 32 - 2 - 1 = 29)
    CLC      ; Note: carry is clear, not set!
    LDA #$32
    SBC #$02 ; After this instruction, C = 1, A = $29)
testSBCDecimalCarry4:
    SED      ; Decimal mode (BCD subtraction: 12 - 21)
    SEC
    LDA #$12
    SBC #$21 ; After this instruction, C = 0, A = $91)
testSBCDecimalCarry5:
    SED      ; Decimal mode (BCD subtraction: 21 - 34)
    SEC
    LDA #$21
    SBC #$34 ; After this instruction, C = 0, A = $87)

  .org $f800
  .word testADCDecimal1
  .word testASLDecimal
  .word testADCDecimal2
  .word testINCDecimal
  .word testADCDecimalCarry1
  .word testADCDecimalCarry2
  .word testADCDecimalCarry3
  .word testADCDecimalCarry4
  .word testADCDecimalCarry5
  .word testSBCDecimalCarry1
  .word testSBCDecimalCarry2
  .word testSBCDecimalCarry3
  .word testSBCDecimalCarry4
  .word testSBCDecimalCarry5
  .org $fffc
  .word reset
  .word $0000