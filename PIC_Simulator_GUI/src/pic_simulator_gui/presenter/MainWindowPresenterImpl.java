/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui.presenter;

import pic_simulator.interfaces.Model;
import pic_simulator.interfaces.ModelPresenter;
import pic_simulator_gui.interfaces.MainWindowPresenter;
import pic_simulator_gui.interfaces.MainWindowView;


public class MainWindowPresenterImpl 
        implements MainWindowPresenter, ModelPresenter {

    private Model _model;
    private MainWindowView _view;
    
    @Override
    public void setModel(Model model) {
        _model = model;
    }
    
    @Override
    public void setView(MainWindowView view) {
        _view = view;
    }
    
    @Override
    public void initializeView() {
        _view.initializeView();
    }
    
    @Override
    public void resetPIC() {
        _model.resetPIC();
    }
    
    @Override
    public void powerResetPIC() {
        _model.powerResetPIC();
    }

    @Override
    public void setAutomaticSteppingMode(boolean b) {
        _model.setAutomaticSteppingMode(b);
    }

    @Override
    public void setAutomaticSteppingInterval(int ms) {
        _model.setAutomaticSteppingInterval(ms);
    }

    @Override
    public void stepIn() {
        _model.stepIn();
    }

    @Override
    public void stepOut() {
        _model.stepOut();
    }

    @Override
    public void stepOver() {
        _model.stepOver();
    }

    @Override
    public void ignoreStep() {
        _model.ignoreStep();
    }

    @Override
    public void setOscillatorFrequency(double f) {
        _model.setOscillatorFrequency(f);
    }

    @Override
    public void resetRunningTimeStopWatch() {
        _model.resetRunningTimeStopWatch();
    }

    @Override
    public void setBreakOnWatchdogTriggered(boolean b) {
        _model.setBreakOnWatchdogTrigger(b);
    }

    @Override
    public void setBreakOnInterrupt(boolean b) {
        _model.setBreakOnInterrupt(b);
    }
    
    @Override
    public void setLSTFile(String filePath) {
        _model.setLSTFile(filePath);
    }

    @Override
    public void displayRunningTime(int microSeconds) {
        _view.displayRunningTime(microSeconds);
    }

    @Override
    public void displayWRegister(int value) {
        _view.displayWRegister(value);
    }

    @Override
    public void displayFSRRegister(int value) {
        _view.displayFSRRegister(value);
    }

    @Override
    public void displayPCRegsiter(int value) {
        _view.displayPCRegsiter(value);
    }

    @Override
    public void displayPCLRegister(int value) {
        _view.displayPCLRegister(value);
    }

    @Override
    public void displayPCLATHRegsiter(int value) {
        _view.displayPCLATHRegsiter(value);
    }

    @Override
    public void displaySTATUSRegister(int value) {
        _view.displaySTATUSRegister(value);
    }

    @Override
    public void displayOPTIONRegister(int value) {
        _view.displayOPTIONRegister(value);
    }

    @Override
    public void displayINTCONRegister(int value) {
        _view.displayINTCONRegister(value);
    }

    @Override
    public void displayAutomaticSteppingMode(boolean b) {
        _view.displayAutomaticSteppingMode(b);
    }

    @Override
    public void displayAutomaticSteppingIntervall(int ms) {
        _view.displayAutomaticSteppingIntervall(ms);
    }

    @Override
    public void displayOscillatorFrequency(double megaHz) {
        _view.displayOscillatorFrequency(megaHz);
    }

    @Override
    public void displayBreakOnWatchdogTrigger(boolean b) {
        _view.displayBreakOnWatchdogTrigger(b);
    }

    @Override
    public void displayBreakOnInterrupt(boolean b) {
        _view.displayBreakOnInterrupt(b);
    }

    @Override
    public void addCodeLine(Integer address, Integer instruction, String sourceCode) {
        _view.addCodeLine(address, instruction, sourceCode);
    }
    
}