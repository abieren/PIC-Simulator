/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui;

import pic_simulator.Model;
import pic_simulator.ModelPresenter;


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
        _model.stepOut();
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
        _view.setRunningTime(microSeconds);
    }

    @Override
    public void displayWRegister(int value) {
        _view.setWRegister(value);
    }

    @Override
    public void displayFSRRegister(int value) {
        _view.setFSRRegister(value);
    }

    @Override
    public void displayPCRegsiter(int value) {
        _view.setPCRegsiter(value);
    }

    @Override
    public void displayPCLRegister(int value) {
        _view.setPCLRegister(value);
    }

    @Override
    public void displayPCLATHRegsiter(int value) {
        _view.setPCLATHRegsiter(value);
    }

    @Override
    public void displaySTATUSRegister(int value) {
        _view.setSTATUSRegister(value);
    }

    @Override
    public void displayOPTIONRegister(int value) {
        _view.setOPTIONRegister(value);
    }

    @Override
    public void displayINTCONRegister(int value) {
        _view.setINTCONRegister(value);
    }

    @Override
    public void displayAutomaticSteppingMode(boolean b) {
        _view.setAutomaticSteppingMode(b);
    }

    @Override
    public void displayAutomaticSteppingIntervall(int ms) {
        _view.setAutomaticSteppingIntervall(ms);
    }

    @Override
    public void displayOscillatorFrequency(double megaHz) {
        _view.setOscillatorFrequency(megaHz);
    }

    @Override
    public void displayBreakOnWatchdogTrigger(boolean b) {
        _view.setBreakOnWatchdogTrigger(b);
    }

    @Override
    public void displayBreakOnInterrupt(boolean b) {
        _view.setBreakOnInterrupt(b);
    }
    
}
