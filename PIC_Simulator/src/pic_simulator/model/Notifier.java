/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

/**
 *
 * @author Alex
 */
public interface Notifier {
    
    void changedWRegister(int oldValue, int newValue);

    void changedPCRegister(int oldValue, int newValue);

    void changedInstructionRegister(int oldValue, int newValue);

    void changedRegister(int register, int oldValue, int newValue);

    void nextCycle();

    public void popStack(int popedValue, boolean isUnderflow);

    public void pushStack(int pushedValue, boolean isOverflow);

    public void invalidInstruction(int instruction);
    
}
