/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import pic_simulator.utils.BinaryNumberHelper;

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
        //decode arguments first
        int f = BinaryNumberHelper.extractBits(instruction, 0, 6);
        int d = BinaryNumberHelper.extractBits(instruction, 7, 7);    
        int b = BinaryNumberHelper.extractBits(instruction, 7, 9);
        int k = BinaryNumberHelper.extractBits(instruction, 0, 7);
        int addressk = BinaryNumberHelper.extractBits(instruction, 0, 9);
        
        //decode and execute instruction
        switch (InstructionDecoder.decode(instruction)) {
            //BYTE-ORIENTED FILE REGISTER OPERATIONS
            case ADDWF:
                ADDWF(f, d);
                break;
            case ANDWF:
                ANDWF(f, d);
                break;
            case CLRF:
                CLRF(instruction);
                break;
            case CLRW:
                CLRW();
                break;
            case COMF:
                COMF(f, d);
                break;
            case DECF:
                DECF(f, d);
                break;
            case DECFSZ:
                DECFSZ(f, d);
                break;
            case INCF:
                INCF(f, d);
                break;
            case INCFSZ:
                INCFSZ(f, d);
                break;
            case IORWF:
                IORWF(f, d);
                break;
            case MOVF:
                MOVF(f, d);
                break;
            case MOVWF:
                MOVWF(instruction);
                break;
            case NOP:
                NOP();
                break;
            case RLF:
                RLF(f, d);
                break;
            case RRF:
                RRF(f, d);
                break;
            case SUBWF:
                SUBWF(f, d);
                break;
            case SWAPF:
                SWAPF(f, d);
                break;
            case XORWF:
                XORWF(f, d);
                break;
            //BIT-ORIENTED FILE REGISTER OPERATIONS
            case BCF:
                BCF(f, b);
                break;
            case BSF:
                BSF(f, b);
                break;
            case BTFSC:
                BTFSC(f, b);
                break;
            case BTFSS:
                BTFSS(f, b);
            //LITERAL AND CONTROL OPERATIONS
            case ADDLW:
                ADDLW(k);
                break;
            case ANDLW:
                ANDLW(k);
                break;
            case CALL:
                CALL(addressk);
                break;
            case CLRWDT:
                CLRWDT();
                break;
            case GOTO:
                GOTO(addressk);
                break;
            case IORLW:
                IORLW(k);
                break;
            case MOVLW:
                MOVLW(k);
                break;
            case RETFIE:
                RETFIE();
                break;
            case RETLW:
                RETLW(k);
                break;
            case RETURN:
                RETURN();
                break;
            case SLEEP:
                SLEEP();
                break;
            case SUBLW:
                SUBLW(k);
                break;
            case XORLW:
                XORLW(k);
                break;
            case INVALID_INSTRUCTION:
                _notifier.invalidInstruction(instruction);
                //make a nop as default operation on invalid instruction
                NOP();
                break;
            default:
                throw new AssertionError();
        }
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
        value = BinaryNumberHelper.normalizeToNBitNumber(value, 8);
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
    public void ADDWF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void ANDWF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CLRF(int f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CLRW() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void COMF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void DECF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void DECFSZ(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void INCF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void INCFSZ(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void IORWF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void MOVF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void MOVWF(int f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void NOP() {
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RLF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RRF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SUBWF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SWAPF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void XORWF(int f, int d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*BIT-ORIENTED FILE REGISTER OPERATIONS*/
    public void BCF(int f, int b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void BSF(int f, int b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void BTFSC(int f, int b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void BTFSS(int f, int b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*LITERAL AND CONTROL OPERATIONS*/
    public void ADDLW(int k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void ANDLW(int k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CALL(int addressk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CLRWDT() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void GOTO(int addressk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void IORLW(int k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void MOVLW(int k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RETFIE() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RETLW(int k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RETURN() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SLEEP() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SUBLW(int k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void XORLW(int k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
