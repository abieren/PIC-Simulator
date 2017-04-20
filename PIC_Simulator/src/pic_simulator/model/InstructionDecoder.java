/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import pic_simulator.utils.BinaryNumberHelper;

/**
 *
 * @author Alex
 */
public class InstructionDecoder {
    public static Instruction decode(int instruction) {
        //BYTE-ORIENTED FILE REGISTER OPERATIONS
        //ADDLW
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_000111xxxxxxxx")) return Instruction.ADDLW;
        //ANDWF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_000101xxxxxxxx")) return Instruction.ANDWF;
        //CLRF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_0000011xxxxxxx")) return Instruction.CLRF;
        //CLRW
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_0000010xxxxxxx")) return Instruction.CLRW;
        //COMF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_001001xxxxxxxx")) return Instruction.COMF;
        //DECF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_000011xxxxxxxx")) return Instruction.DECF;
        //DECFSZ
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_001011xxxxxxxx")) return Instruction.DECFSZ;
        //INCF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_001010xxxxxxxx")) return Instruction.INCF;
        //INCFSZ
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_001111xxxxxxxx")) return Instruction.INCFSZ;
        //IORWF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_000100xxxxxxxx")) return Instruction.IORWF;
        //MOVF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_001000xxxxxxxx")) return Instruction.MOVF;
        //MOVWF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_0000001xxxxxxx")) return Instruction.MOVWF;
        //NOP
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_0000000xx00000")) return Instruction.NOP;
        //RLF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_001101xxxxxxxx")) return Instruction.RLF;
        //RRF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_001100xxxxxxxx")) return Instruction.RRF;
        //SUBWF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_000010xxxxxxxx")) return Instruction.SUBWF;
        //SWAPF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_001110xxxxxxxx")) return Instruction.SWAPF;
        //XORWF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_000110xxxxxxxx")) return Instruction.XORWF;
        //BIT-ORIENTED FILE REGISTER OPERATIONS
        //BCF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_0100xxxxxxxxxx")) return Instruction.BCF;
        //BSF
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_0101xxxxxxxxxx")) return Instruction.BSF;
        //BTFSC
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_0110xxxxxxxxxx")) return Instruction.BTFSC;
        //BTFSS
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_0111xxxxxxxxxx")) return Instruction.BTFSS;
        //LITERAL AND CONTROL OPERATIONS
        //ANDLW
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_111001xxxxxxxx")) return Instruction.ANDLW;
        //CALL
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_100xxxxxxxxxxx")) return Instruction.CALL;
        //CLRWDT
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_00000001100100")) return Instruction.CLRWDT;
        //GOTO
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_101xxxxxxxxxxx")) return Instruction.GOTO;
        //IORLW
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_111000xxxxxxxx")) return Instruction.IORLW;
        //MOVLW
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_1100xxxxxxxxxx")) return Instruction.MOVLW;
        //RETFIE
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_00000000001001")) return Instruction.RETFIE;
        //RETLW
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_1101xxxxxxxxxx")) return Instruction.RETLW;
        //RETURN
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_00000000001000")) return Instruction.RETURN;
        //SLEEP
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_00000001100011")) return Instruction.SLEEP;
        //SUBLW
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_111100xxxxxxxx")) return Instruction.SUBLW;
        //XORLW
        if (BinaryNumberHelper.matchPattern(instruction, "13:d_111010xxxxxxxx")) return Instruction.XORLW;
        //if no instruction has been recognized yet, it is a invalid instruction
        return Instruction.INVALID_INSTRUCTION;
    }
}