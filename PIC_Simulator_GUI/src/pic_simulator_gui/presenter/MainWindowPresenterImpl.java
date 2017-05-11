/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui.presenter;

import java.util.List;
import javafx.collections.ObservableList;
import pic_simulator.interfaces.Model;
import pic_simulator.interfaces.ModelPresenter;
import pic_simulator.utils.BinaryNumberHelper;
import pic_simulator_gui.interfaces.MainWindowPresenter;
import pic_simulator_gui.interfaces.MainWindowView;
import pic_simulator_gui.view.main.StackRecord;


public class MainWindowPresenterImpl 
        implements MainWindowPresenter, ModelPresenter {

    private Model _model;
    private MainWindowView _view;
    
    //records of the stack view
    ObservableList<StackRecord> _stackRecords;
    
    //port values
    private int _portALatch;
    
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
    public void displayRunningTime(double microSeconds) {
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

    @Override
    public void displayExecutedCodeLine(int line) {
        _view.displayExecutedCodeLine(line);
    }

    @Override
    public void displayRegister(int register, int value) {
        _view.displayRegister(register, value);
    }

    @Override
    public void displayStack(List<Integer> stack) {
        _view.displayStack(stack);
    }

    @Override
    public void pushStack(int value, boolean isOverflow) {
        _view.pushStack(value, isOverflow);
    }

    @Override
    public void popStack(int value, boolean isUnderflow) {
        _view.popStack(value, isUnderflow);
    }

    @Override
    public void setRegister(int address, int value) {
        _model.setRegister(address, value);
    }
    
    @Override
    public void setPortTris(String port, int value) {
        _model.setPortTris(port, value);
    }

    @Override
    public void setPortLatch(String port, int value) {
        _model.setPortLatch(port, value);
    }

    @Override
    public void setPortInOut(String port, int value) {
        
    }

    @Override
    public void setPortEnv(String port, int value) {
        _model.setPortEnvironment(port, value);
    }

    @Override
    public void displayPortInOut(String port, int oldValue, int newValue) {
        for (int i = 0; i < 8; i++) {
            boolean b = BinaryNumberHelper.parseBoolean(BinaryNumberHelper.getBit(newValue, i));
            _view.displayPortInOutBit(port, i, b);
        }
    }

    @Override
    public void displayPortLatch(String port, int oldValue, int newValue) {
        for (int i = 0; i < 8; i++) {
            boolean b = BinaryNumberHelper.parseBoolean(BinaryNumberHelper.getBit(newValue, i));
            
            _view.displayPortLatchBit(port, i, b);
        }
    }

    @Override
    public void displayPortTris(String port, int oldValue, int newValue) {
        for (int i = 0; i < 8; i++) {
            boolean b = BinaryNumberHelper.parseBoolean(BinaryNumberHelper.getBit(newValue, i));
            _view.displayPortTrisBit(port, i, b);
        }
    }

    @Override
    public void displayPortEnvironment(String port, int oldValue, int newValue) {
        for (int i = 0; i < 8; i++) {
            boolean b = BinaryNumberHelper.parseBoolean(BinaryNumberHelper.getBit(newValue, i));
            _view.displayPortEnvironmentBit(port, i, b);
        }
    }

    @Override
    public void setPortLatchBit(String port, int bit, boolean value) {
        Integer newLatch = _model.getPortLatch(port).orElse(null);
        if (newLatch == null) return;
        int b = 1;
        if (value == false) b=0;
        newLatch = BinaryNumberHelper.setBit(newLatch, bit, b);
        _model.setPortLatch(port, newLatch);
    }

    @Override
    public void setPortTrisBit(String port, int bit, boolean value) {
        Integer newTris = _model.getPortTris(port).orElse(null);
        if (newTris == null) return;
        int b = 1;
        if (value == false) b=0;
        newTris = BinaryNumberHelper.setBit(newTris, bit, b);
        _model.setPortTris(port, newTris);
    }
    
    @Override
    public void setPortEnvironmentBit(String port, int bit, boolean value) {
        Integer newTris = _model.getPortEnvironment(port).orElse(null);
        if (newTris == null) return;
        int b = 1;
        if (value == false) b=0;
        newTris = BinaryNumberHelper.setBit(newTris, bit, b);
        _model.setPortEnvironment(port, newTris);
    }
}
