/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import pic_simulator.interfaces.ModelPresenter;


public class NotifierImpl implements Notifier {

    private final ModelPresenter _presenter;

    public NotifierImpl(ModelPresenter presenter) {
        _presenter = presenter;
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
}
