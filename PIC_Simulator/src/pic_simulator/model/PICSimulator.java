/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Alex
 */
public class PICSimulator {
    
    public final Notifier _notifier;
    
    public Map<Integer, Integer> _programMemory;
    public Map<Integer, Integer> _registers;
    public Stack<Integer> _stack;
    public int _pcRegister;
    public int _instructionRegsiter;
    public int _wRegister;

    public final int DEFAULT_INSTRUCTION_VALUE = 0;
    public final int DEFAULT_REGSITER_VALUE = 0;
    public final int STATUS_REGISTER_ADDRESS_BANK0 = 0x3;
    public final int STATUS_REGISTER_ADDRESS_BANK1 = 0x83;

    
    public PICSimulator(Notifier notifier) {
        _notifier = notifier;
    }
    
    public void initialize() {
        _registers = new HashMap<>();
        _programMemory = new HashMap<>();
        _stack = new Stack<>();
        _pcRegister = 0;
        _instructionRegsiter = 0;
        _wRegister = 0;
    }
    
//    public int shrinkInt(int value, int bit) {
//    }
    
    public int getPCRegister() {
        return _pcRegister;
    };
    
    public void setPCRegister(int value) {
        _notifier.changedPCRegister(_pcRegister, value);
        _pcRegister = value;
    }
    
    public int getInstructionRegsiter() {
        return _instructionRegsiter;
    }
    
    public void setInstructionRegsiter(int value) {
        _notifier.changedInstructionRegister(_instructionRegsiter, value);
        _instructionRegsiter = value;
    }
    
    public int getWRegister() {
        return _wRegister;
    }
    
    public void setWRegister(int value) {
        _notifier.changedWRegister(_wRegister, value);
        _wRegister = value;
    }
    
    public int getRegister(int address) {
        //shrink to an 7 bit address
        address = address & 0b1111111;
        //use the RP0 bit in the STATUS register to form an 8 bit address
        if (getSTATUSbitRP0() != 0) address = address | 0b10000000;
        int value = _registers.get(address);
        return value;
    }
    
    public void setRegister(int address, int value) {
        //shrink to an 7 bit address
        address = address & 0b1111111;
        //use the RP0 bit in the STATUS register to form an 8 bit address
        if (getSTATUSbitRP0() != 0) address = address | 0b10000000;
        _registers.put(address, value);
        _notifier.changedRegister(address, getRegister(address), value);
        //also set the mapped registers if address belongs to an mapped register
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int getInstructionFromProgramMemory(int address) {
        Integer value = _programMemory.get(address);
        if (value == null) {
            value = DEFAULT_INSTRUCTION_VALUE;
        }
        return value;
    }
    
    public void setInstructionToProgramMemory(int address, int instruction) {
        _programMemory.put(address, instruction);
    }
    
    public int popStack() {
        int value = _stack.pop();
        _notifier.popStack(value);
        return value;
    }
    
    public void pushStack(int value) {
        _stack.push(value);
        _notifier.pushStack(value);
    }
    
    public void makeStep() {
        fetchNextInstruction();
        decodeAndExecuteInstruction(getInstructionRegsiter());
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void fetchNextInstruction() {
        int value = getPCRegister();
        value = getInstructionFromProgramMemory(value);
        setInstructionRegsiter(value);
    }
    
    public void decodeAndExecuteInstruction(int instruction) {
        switch (InstructionDecoder.decode(instruction)) {
            //BYTE-ORIENTED FILE REGISTER OPERATIONS
            case ADDWF:
                ADDWF(instruction);
                break;
            case ANDWF:
                ANDWF(instruction);
                break;
            case CLRF:
                CLRF(instruction);
                break;
            case CLRW:
                CLRW();
                break;
            case COMF:
                COMF(instruction);
                break;
            case DECF:
                DECF(instruction);
                break;
            case DECFSZ:
                DECFSZ(instruction);
                break;
            case INCF:
                INCF(instruction);
                break;
            case INCFSZ:
                INCFSZ(instruction);
                break;
            case IORWF:
                IORWF(instruction);
                break;
            case MOVF:
                MOVF(instruction);
                break;
            case MOVWF:
                MOVWF(instruction);
                break;
            case NOP:
                NOP();
                break;
            case RLF:
                RLF(instruction);
                break;
            case RRF:
                RRF(instruction);
                break;
            case SUBWF:
                SUBWF(instruction);
                break;
            case SWAPF:
                SWAPF(instruction);
                break;
            case XORWF:
                XORWF(instruction);
                break;
            //BIT-ORIENTED FILE REGISTER OPERATIONS
            case BCF:
                BCF(instruction);
                break;
            case BSF:
                BSF(instruction);
                break;
            case BTFSC:
                BTFSC(instruction);
                break;
            case BTFSS:
                BTFSS(instruction);
            //LITERAL AND CONTROL OPERATIONS
            case ADDLW:
                ADDLW(instruction);
                break;
            case ANDLW:
                ANDLW(instruction);
                break;
            case CALL:
                CALL(instruction);
                break;
            case CLRWDT:
                CLRWDT();
                break;
            case GOTO:
                GOTO(instruction);
                break;
            case IORLW:
                IORLW(instruction);
                break;
            case MOVLW:
                MOVLW(instruction);
                break;
            case RETFIE:
                RETFIE();
                break;
            case RETLW:
                RETLW(instruction);
                break;
            case RETURN:
                RETURN();
                break;
            case SLEEP:
                SLEEP();
                break;
            case SUBLW:
                SUBLW(instruction);
                break;
            case XORLW:
                XORLW(instruction);
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public int extractBits(int value, int from, int to) {
        int result = 0;
        int diff = to-from;
        for (int i = 0; i <= diff; i++) {
            int bit = value & (1 << (from + i) );
            if (bit != 0) result = result | (1 << i);
        }
        return result;
    }
    
    public void nextCycle() {
        _notifier.nextCycle();
    }
    
    /*STATUS REGSITER*/
    public int getSTATUSRegister() {
        Integer value = _registers.get(STATUS_REGISTER_ADDRESS_BANK0);
        if (value == null) {
            value = DEFAULT_REGSITER_VALUE;
        }
        return value;
    }
    
    public void setSTATUSRegister(int value) {
        //short int to 8 bit
        value = value & 0b1111111;
        _registers.put(STATUS_REGISTER_ADDRESS_BANK0, value);
        _registers.put(STATUS_REGISTER_ADDRESS_BANK1, value);
    }
    
    public int getSTATUSbitC() {
        return getSTATUSRegister() & 1;
    }
    
    public void setSTATUSbitC(int b) {
        int bitmask = 1;
        int value = getSTATUSRegister() & ~bitmask;
        if (b != 0) value = value | bitmask;
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitDC() {
        return getSTATUSRegister() & 2;
    }
    
    public void setSTATUSbitDC(int b) {
        int bitmask = 2;
        int value = getSTATUSRegister() & ~bitmask;
        if (b != 0) value = value | bitmask;
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitZ() {
        return getSTATUSRegister() & 4;
    }
    
    public void setSTATUSbitZ(int b) {
        int bitmask = 4;
        int value = getSTATUSRegister() & ~bitmask;
        if (b != 0) value = value | bitmask;
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitPD() {
        return getSTATUSRegister() & 8;
    }
    
    public void setSTATUSbitPD(int b) {
        int bitmask = 8;
        int value = getSTATUSRegister() & ~bitmask;
        if (b != 0) value = value | bitmask;
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitTO() {
        return getSTATUSRegister() & 16;
    }
    
    public void setSTATUSbitTO(int b) {
        int bitmask = 16;
        int value = getSTATUSRegister() & ~bitmask;
        if (b != 0) value = value | bitmask;
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitRP0() {
        return getSTATUSRegister() & 32;
    }
    
    public void setSTATUSbitRP0(int b) {
        int bitmask = 32;
        int value = getSTATUSRegister() & ~bitmask;
        if (b != 0) value = value | bitmask;
        setSTATUSRegister(value);
    }
    
    
    
    
    /*BYTE-ORIENTED FILE REGISTER OPERATIONS*/
    public void ADDWF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void ANDWF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CLRF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CLRW() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void COMF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void DECF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void DECFSZ(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void INCF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void INCFSZ(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void IORWF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void MOVF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void MOVWF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void NOP() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RLF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RRF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SUBWF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SWAPF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void XORWF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*BIT-ORIENTED FILE REGISTER OPERATIONS*/
    public void BCF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void BSF(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void BTFSC(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void BTFSS(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*LITERAL AND CONTROL OPERATIONS*/
    public void ADDLW(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void ANDLW(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CALL(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CLRWDT() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void GOTO(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void IORLW(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void MOVLW(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RETFIE() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RETLW(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RETURN() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SLEEP() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SUBLW(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void XORLW(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
