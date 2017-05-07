/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import java.util.HashMap;
import java.util.List;
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

    public final int MAX_STACK_SIZE = 8;
    public final int DEFAULT_INSTRUCTION_VALUE = 0;
    public final int DEFAULT_REGSITER_VALUE = 0;
    public final int STATUS_REGISTER_ADDRESS_BANK0 = 0x03;
    public final int PCLATH_REGISTER_ADDRESS_BANK0 = 0x0A;
    public final int FSR_ADDRESS_BANK0 = 0x4;
    public final int INTCON_REGISTER_ADDRESS_BANK0 = 0x0B;

    
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
        value = BinaryNumberHelper.truncateToNBit(value, 13);
        _notifier.changedPCRegister(_pcRegister, value);
        _pcRegister = value;
    }
    
    public int getInstructionRegsiter() {
        return _instructionRegsiter;
    }
    
    public void setInstructionRegsiter(int value) {
        value = BinaryNumberHelper.truncateToNBit(value, 14);
        _notifier.changedInstructionRegister(_instructionRegsiter, value);
        _instructionRegsiter = value;
    }
    
    public int getWRegister() {
        return _wRegister;
    }
    
    public void setWRegister(int value) {
        value = BinaryNumberHelper.truncateToNBit(value, 8);
        _notifier.changedWRegister(_wRegister, value);
        _wRegister = value;
    }
    
    public int getRegister(int address) {
        //shrink to an 7 bit address
        address = BinaryNumberHelper.truncateToNBit(address, 7);
        Integer value;
        if (address == 0x0) {
            int fsrValue = getRegister(FSR_ADDRESS_BANK0);
            value = _registers.get(fsrValue);
        } else {
            //use the RP0 bit in the STATUS register to form an 8 bit address
            BinaryNumberHelper.setBit(address, 7, getSTATUSbitRP0());
            value = _registers.get(address);
        }
        //make sure a default value is returned if value of register in
        //hash map hasn't been set yet
        if (value == null) {
            value = DEFAULT_REGSITER_VALUE;
        }
        return value;
    }
    
    public void setRegister(int address, int value) {
        value = BinaryNumberHelper.truncateToNBit(value, 8);
        address = BinaryNumberHelper.truncateToNBit(address, 7);
        //use pointer in fsr when address is 0x0
        if (address == 0x0) {
            int fsrValue = getRegister(FSR_ADDRESS_BANK0);
            address = fsrValue;
        }
        else if (address == FSR_ADDRESS_BANK0) {
            _notifier.changedFSRRegister(getRegister(FSR_ADDRESS_BANK0), value);
        }
        //use the RP0 bit in the STATUS register to form an 8 bit address
        address = BinaryNumberHelper.setBit(address, 7, getSTATUSbitRP0());
        //get all mapped registers for address
        List<Integer> registers = RegisterAddressDecoder.getAllRegistersForAddress(address);
        //modify hash map for every mapped register for given address
        for (Integer register : registers) {
            _notifier.changedRegister(register, getRegister(register), value);
            _registers.put(register, value);
        }
    }
    
    public int getInstructionFromProgramMemory(int address) {
        address = BinaryNumberHelper.truncateToNBit(address, 13);
        Integer value = _programMemory.get(address);
        if (value == null) {
            value = DEFAULT_INSTRUCTION_VALUE;
        }
        return value;
    }
    
    public void setInstructionToProgramMemory(int address, int instruction) {
        address = BinaryNumberHelper.truncateToNBit(address, 13);
        instruction = BinaryNumberHelper.truncateToNBit(instruction, 14);
        _programMemory.put(address, instruction);
    }
    
    public int popStack() {
        boolean isUnderflow = false;
        if (_stack.isEmpty()) {
            _stack.push(0);
            isUnderflow = true;
        }
        int value = _stack.pop();
        _notifier.popStack(value, isUnderflow);
        return value;
    }
    
    public void pushStack(int value) {
        //simulate circular stack on PIC:
        //if stack has maximum size, override the oldest value in stack
        value = BinaryNumberHelper.truncateToNBit(value, 13);
        boolean isOverflow = false;
        if (_stack.size() == MAX_STACK_SIZE) {
            _stack.remove(0);
            isOverflow = true;
        }
        _stack.push(value);
        _notifier.pushStack(value, isOverflow);
    }
    
    public void makeStep() {
        handleInterrupts();
        fetchNextInstruction();
        decodeAndExecuteInstruction(getInstructionRegsiter());
    }
    
    public void fetchNextInstruction() {
        int currentPC = getPCRegister();
        int currentInstr = getInstructionFromProgramMemory(currentPC);
        setInstructionRegsiter(currentInstr);
        //inrement PC register after fetch
        setPCRegister(currentPC+1);
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
    
    public void skipNextInstructionWithNOP() {
        setPCRegister(getPCRegister()+1);
        nextCycle();
    }
    
    public void nextCycle() {
        _notifier.nextCycle();
    }
    
    
    /* Carry and Digit Carry Helpers*/
    
    public boolean isCarry(int a, int b) {
        int tmpa = a & 0x000000FF;
        int tmpb = b & 0x000000FF;
        int resultint = (tmpa+tmpb) >> 8; // is only 0 or 1
        if (resultint == 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public boolean isDigitCarry(int a, int b) {
        int tmpa = a & 0x0000000F;
        int tmpb = b & 0x0000000F;
        int resultint =  (tmpa+tmpb) >> 4; // is only 0 or 1
        if (resultint == 0) {
            return false;
        } else {
            return true;
        }
    }
    
    // maybe change to is not borrow
    public boolean isBorrow(int a, int b) {
        if (a >= b) {
            return true;
        } else {
            return false;
        } 
    }
    
    public boolean isDigitBorrow(int a, int b) {
        return isBorrow(a & 0x0000000F, b & 0x0000000F);
    }
    
    /*STATUS REGSITER*/
    public int getSTATUSRegister() {
        Integer value = _registers.get(STATUS_REGISTER_ADDRESS_BANK0);
        //make sure a default value is returned if value of register in
        //hash map hasn't been set yet
        if (value == null) {
            value = DEFAULT_REGSITER_VALUE;
        }
        return value;
    }
    
    public void setSTATUSRegister(int value) {
        _notifier.changedSTATUSRegister(getSTATUSRegister(), value);
        setRegister(STATUS_REGISTER_ADDRESS_BANK0, value);
    }
    
    public int getSTATUSbitC() {
        return BinaryNumberHelper.getBit(getSTATUSRegister(), 0);
    }
    
    public void setSTATUSbitC(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 0, b);
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitDC() {
        return BinaryNumberHelper.getBit(getSTATUSRegister(), 1);
    }
    
    public void setSTATUSbitDC(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 1, b);
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitZ() {
        return BinaryNumberHelper.getBit(getSTATUSRegister(), 2);
    }
    
    public void setSTATUSbitZ(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 2, b);
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitPD() {
        return BinaryNumberHelper.getBit(getSTATUSRegister(), 3);
    }
    
    public void setSTATUSbitPD(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 3, b);
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitTO() {
        return BinaryNumberHelper.getBit(getSTATUSRegister(), 4);
    }
    
    public void setSTATUSbitTO(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 4, b);
        setSTATUSRegister(value);
    }
    
    public int getSTATUSbitRP0() {
        return BinaryNumberHelper.getBit(getSTATUSRegister(), 5);
    }
    
    public void setSTATUSbitRP0(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 5, b);
        setSTATUSRegister(value);
    }
    
    /*INTCON REGISTER*/
    public int getINTCONRegister() {
        return getRegister(INTCON_REGISTER_ADDRESS_BANK0);
    }
    
    public void setINTCONRegister(int value) {
        _notifier.changedSTATUSRegister(getSTATUSRegister(), value);
        setRegister(INTCON_REGISTER_ADDRESS_BANK0, value);
    }
    
    public void setINTCONbitGIE(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 7, b);
        setSTATUSRegister(value);
    }
    
    public int getINTCONbitGIE() {
        return BinaryNumberHelper.getBit(getINTCONRegister(), 7);
    }
    
    public void setINTCONbitEEIE(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 6, b);
        setSTATUSRegister(value);
    }
    
    public int getINTCONbitEEIE() {
        return BinaryNumberHelper.getBit(getINTCONRegister(), 6);
    }
    
    public void setINTCONbitT0IE(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 5, b);
        setSTATUSRegister(value);
    }
    
    public int getINTCONbitT0IE() {
        return BinaryNumberHelper.getBit(getINTCONRegister(), 5);
    }
    
    public void setINTCONbitINTE(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 4, b);
        setSTATUSRegister(value);
    }
    
    public int getINTCONbitINTE() {
        return BinaryNumberHelper.getBit(getINTCONRegister(), 4);
    }
    
    public void setINTCONbitRBIE(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 3, b);
        setSTATUSRegister(value);
    }
    
    public int getINTCONbitRBIE() {
        return BinaryNumberHelper.getBit(getINTCONRegister(), 3);
    }
    
    public void setINTCONbitT0IF(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 2, b);
        setSTATUSRegister(value);
    }
    
    public int getINTCONbitT0IF() {
        return BinaryNumberHelper.getBit(getINTCONRegister(), 2);
    }
    
    public void setINTCONbitINTF(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 1, b);
        setSTATUSRegister(value);
    }
    
    public int getINTCONbitINTF() {
        return BinaryNumberHelper.getBit(getINTCONRegister(), 1);
    }
    
    public void setINTCONbitRBIF(int b) {
        int value = getSTATUSRegister();
        value = BinaryNumberHelper.setBit(value, 0, b);
        setSTATUSRegister(value);
    }
    
    public int getINTCONbitRBIF() {
        return BinaryNumberHelper.getBit(getINTCONRegister(), 0);
    }
    
    private void handleInterrupts() {
        //chek GIE
        if (getINTCONbitGIE() == 0) return;
        //check Timer Overflow Interrupt
        if (getINTCONbitT0IE() != 0 && getINTCONbitT0IF() != 0) {
            //Interrupt
            CALL(0x4);
        }
    }
    
    
    
    
    
    /*BYTE-ORIENTED FILE REGISTER OPERATIONS*/
    public void ADDWF(int f, int d) {
        int result = getWRegister() + getRegister(f);
        if (result == 0) {
            setSTATUSbitZ(1);
        }
        else {
            setSTATUSbitZ(0);
        }
        if (isDigitCarry(getWRegister(), getRegister(f))) {
            setSTATUSbitDC(1);
        }
        else {
            setSTATUSbitDC(0);
        }
        if (isCarry(getWRegister(), getRegister(f))) {
            setSTATUSbitC(1);
        }
        else {
            setSTATUSbitC(0);
        }
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void ANDWF(int f, int d) {
        int result = getWRegister() & getRegister(f);
        if (result == 0) {
            setSTATUSbitZ(1);
        }
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CLRF(int f) {
        setRegister(f, 0);
        setSTATUSbitZ(1);
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void CLRW() {
        setWRegister(0);
        setSTATUSbitZ(1);
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void COMF(int f, int d) {
        int result = ~getRegister(f);
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void DECF(int f, int d) {
        int result = getRegister(f) - 1;
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void DECFSZ(int f, int d) {
        int result = getRegister(f) - 1;
        //no status affected
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        if (result == 0) {
            skipNextInstructionWithNOP();
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void INCF(int f, int d) {
        int result = getRegister(f) + 1;
        result = BinaryNumberHelper.truncateToNBit(result, 8);
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void INCFSZ(int f, int d) {
        int result = getRegister(f) + 1;
        result = BinaryNumberHelper.truncateToNBit(result, 8);
        //no status affected
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        if (result == 0) {
            skipNextInstructionWithNOP();
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void IORWF(int f, int d) {
        int result = getWRegister() ^ getRegister(f);
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void MOVF(int f, int d) {
        int result = getRegister(f);
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void MOVWF(int f) {
        int result = getWRegister();
        //no status affectred
        setRegister(f, result);
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void NOP() {
        nextCycle();
    }
    
    public void RLF(int f, int d) {
        int result = getRegister(f);
        int tmp = BinaryNumberHelper.getBit(getRegister(f), 7);
        int carryold = getSTATUSbitC();
        setSTATUSbitC(tmp);
        result = result << 1;
        result = BinaryNumberHelper.setBit(result, 0, carryold);
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RRF(int f, int d) {
        int result = getRegister(f);
        int tmp = BinaryNumberHelper.getBit(getRegister(f), 0);
        int carryold = getSTATUSbitC();
        setSTATUSbitC(tmp);
        result = result >> 1;
        result = BinaryNumberHelper.setBit(result, 7, carryold);
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SUBWF(int f, int d) {
        int result = getRegister(f) - getWRegister();
        if (result == 0) {
            setSTATUSbitZ(1);
        }  else {
            setSTATUSbitZ(0);
        }
        if (isBorrow(getRegister(f), getWRegister())) {
            setSTATUSbitC(1);
        } else {
            setSTATUSbitC(0);
        }
        if (isDigitBorrow(getRegister(f), getWRegister())) {
            setSTATUSbitDC(1);
        } else {
            setSTATUSbitDC(0);
        }
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SWAPF(int f, int d) {
        int result = getRegister(f);
        int front2back = (result & 0x000000F0) >> 4;
        int back2front = (result & 0x0000000F) << 4;
        result = front2back + back2front;
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void XORWF(int f, int d) {
        int result = getWRegister() ^ getRegister(f);
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        if (d == 0) {
            setWRegister(result);
        } else {
            setRegister(f, result);
        }
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*BIT-ORIENTED FILE REGISTER OPERATIONS*/
    public void BCF(int f, int b) {
        int result = getRegister(f);
        result = BinaryNumberHelper.setBit(result, b, 0);
        setRegister(f, result);
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void BSF(int f, int b) {
        int result = getRegister(f);
        result = BinaryNumberHelper.setBit(result, b, 1);
        setRegister(f, result);
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void BTFSC(int f, int b) {
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void BTFSS(int f, int b) {
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*LITERAL AND CONTROL OPERATIONS*/
    public void ADDLW(int k) {
        int result = getWRegister() + k;
        if (isCarry(getWRegister(), k)) {
            setSTATUSbitC(1);
        }
        else {
            setSTATUSbitC(0);
        }
        if (isDigitCarry(getWRegister(), k)) {
            setSTATUSbitDC(1);
        }
        else {
            setSTATUSbitDC(0);
        }
        if (result == 0) {
            setSTATUSbitZ(1);
        }
        else {
            setSTATUSbitZ(0);
        }
        setWRegister(result);
        nextCycle();
    }
    
    public void ANDLW(int k) {
        int result = getWRegister() & k;
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        setWRegister(result);
        nextCycle();
    }
    
    public void CALL(int addressk) {
        //pushStack(getPCRegister()+1);
        //don't increment by one, because it is already done after the fetch procedure
        pushStack(getPCRegister());
        int pclath = getRegister(PCLATH_REGISTER_ADDRESS_BANK0);
        addressk = BinaryNumberHelper.setBit(addressk, 11, BinaryNumberHelper.getBit(pclath, 3));
        addressk = BinaryNumberHelper.setBit(addressk, 12, BinaryNumberHelper.getBit(pclath, 4));
        setPCRegister(addressk);
        nextCycle();
        nextCycle();
    }
    
    public void CLRWDT() {
        setSTATUSbitTO(1);
        setSTATUSbitPD(1);
        nextCycle();
        // TODO add: write 00h into WDT  AND 0 into WDT prescaler
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void GOTO(int addressk) {
        int pclath = getRegister(PCLATH_REGISTER_ADDRESS_BANK0);
        addressk = BinaryNumberHelper.setBit(addressk, 11, BinaryNumberHelper.getBit(pclath, 3));
        addressk = BinaryNumberHelper.setBit(addressk, 12, BinaryNumberHelper.getBit(pclath, 4));
        setPCRegister(addressk);
        nextCycle();
        nextCycle();
    }
    
    public void IORLW(int k) {
        int result = getWRegister() | k;
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        setWRegister(result);
        nextCycle();
    }
    
    public void MOVLW(int k) {
        //no status affected
        setWRegister(k);
        nextCycle();
    }
    
    public void RETFIE() {
        nextCycle();
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void RETLW(int k) {
        int address = popStack();
        setPCRegister(address);
        setWRegister(k);
        nextCycle();
        nextCycle();
    }
    
    public void RETURN() {
        int address = popStack();
        setPCRegister(address);
        nextCycle();
        nextCycle();
    }
    
    public void SLEEP() {
        nextCycle();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SUBLW(int k) {
        int result = k - getWRegister();
        if (isBorrow(k, getWRegister())) {
            setSTATUSbitC(0); //borrow is low active
        }
        else {
            setSTATUSbitC(1); 
        }
        if (isDigitBorrow(k, getWRegister())) {
            setSTATUSbitDC(0); //borrow is low active
        } 
        else {
            setSTATUSbitDC(1);
        }
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        setWRegister(result);
        nextCycle();
    }
    
    public void XORLW(int k) {
        int result = getWRegister() ^ k;
        if (result == 0) {
            setSTATUSbitZ(1);
        } else {
            setSTATUSbitZ(0);
        }
        setWRegister(result);
        nextCycle();
    }
    
}
