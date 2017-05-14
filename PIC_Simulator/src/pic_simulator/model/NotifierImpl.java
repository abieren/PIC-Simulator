/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import pic_simulator.interfaces.Notifier;
import pic_simulator.interfaces.ModelPresenter;


public class NotifierImpl implements Notifier {

    private final ModelPresenter _presenter;
    private final Simulator _simulator;

    public NotifierImpl(ModelPresenter presenter, Simulator simulator) {
        _presenter = presenter;
        _simulator = simulator;
    }
    
    @Override
    public void changedWRegister(int oldValue, int newValue) {
        _presenter.displayWRegister(newValue);
    }

    @Override
    public void changedPCRegister(int oldValue, int newValue) {
        _presenter.displayPCRegsiter(newValue);
    }

    @Override
    public void changedInstructionRegister(int oldValue, int newValue) {
        //instruction register is not displayed yet
    }

    @Override
    public void changedRegister(int register, int oldValue, int newValue) {
        _presenter.displayRegister(register, newValue);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nextCycle() {
        _simulator.nextCycle();
    }

    @Override
    public void popStack(int value, boolean isUnderflow) {
        _presenter.popStack(value, isUnderflow);
    }

    @Override
    public void pushStack(int value, boolean isOverflow) {
        _presenter.pushStack(value, isOverflow);
    }

    @Override
    public void invalidInstruction(int instruction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    @Override
    public void changedSTATUSRegister(int oldValue, int newValue) {
        _presenter.displaySTATUSRegister(newValue);
    }

    @Override
    public void changedFSRRegister(int oldValue, int newValue) {
        _presenter.displayFSRRegister(newValue);
    }

    @Override
    public void changedINTCONRegister(int oldValue, int newValue) {
        _presenter.displayINTCONRegister(newValue);
    }

    @Override
    public void changedOPTIONRegister(int oldValue, int newValue) {
        _presenter.displayOPTIONRegister(newValue);
    }

    @Override
    public void changedPortALatch(int oldValue, int newValue) {
        _presenter.displayPortLatch("A", oldValue, newValue);
    }

    @Override
    public void changedPortAInOut(int oldValue, int newValue) {
        _presenter.displayPortInOut("A", oldValue, newValue);
    }

    @Override
    public void changedPortATris(int oldValue, int newValue) {
        _presenter.displayPortTris("A", oldValue, newValue);
    }

    @Override
    public void changedPortBLatch(int oldValue, int newValue) {
        _presenter.displayPortLatch("B", oldValue, newValue);
    }

    @Override
    public void changedPortBTris(int oldValue, int newValue) {
        _presenter.displayPortTris("B", oldValue, newValue);
    }

    @Override
    public void changedPortBInOut(int oldValue, int newValue) {
        _presenter.displayPortInOut("B", oldValue, newValue);
    }

    @Override
    public void changedPortAEnvironment(int oldValue, int newValue) {
        _presenter.displayPortEnvironment("A", oldValue, newValue);
    }

    @Override
    public void changedPortBEnvironment(int oldValue, int newValue) {
        _presenter.displayPortEnvironment("B", oldValue, newValue);
    }
    
}
