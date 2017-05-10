/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import pic_simulator.interfaces.Model;
import pic_simulator.interfaces.ModelPresenter;
import pic_simulator.utils.FileParser;
import pic_simulator.utils.ParseResult;

/**
 *
 * @author Alex
 */
public class Simulator implements Model {

    private final ModelPresenter _presenter;
    private final Notifier _notifier;
    private final PICSimulator _pic;
    private String _lstFilePath;
    private ParseResult _parseResult;
    
    private boolean _automaticSteppingMode;
    private int _automaticSteppingInterval;     //interval in ms
    private double _oscillatorFrequency;        //frequency in mega hz
    private double _runningTime;                   //running time in micro seconds
    private boolean _breakOnWatchdogTrigger;
    private boolean _breakOnInterrupt;

    public Simulator(ModelPresenter presenter) {
        _presenter = presenter;
        _notifier = new NotifierImpl(_presenter, this);
        _pic = new PICSimulator(_notifier);
    }
    
    public void nextCycle() {
        _runningTime = _runningTime + 1/_oscillatorFrequency;
        _presenter.displayRunningTime(_runningTime);
    }
    
    @Override
    public void initialize() {
        _presenter.initializeView();
        _pic.initialize();
        
        _automaticSteppingMode = false;
        _presenter.displayAutomaticSteppingMode(_automaticSteppingMode);
        _automaticSteppingInterval = 1500;
        _presenter.displayAutomaticSteppingIntervall(_automaticSteppingInterval);
        _oscillatorFrequency = 4.0;
        _presenter.displayOscillatorFrequency(_oscillatorFrequency);
        _runningTime = 0;
        _presenter.displayRunningTime(_runningTime);
        _breakOnWatchdogTrigger = true;
        _presenter.displayBreakOnWatchdogTrigger(_breakOnWatchdogTrigger);
        _breakOnInterrupt = true;
        _presenter.displayBreakOnInterrupt(_breakOnInterrupt);
    }
    
    @Override
    public void resetPIC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void powerResetPIC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stepIn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stepOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void stepOver() {
        int pc = _pic.getPCRegister();
        int line = _parseResult.addressToLineNumber.get(pc);
        _presenter.displayExecutedCodeLine(line);
        _pic.makeStep();
    }
    
    @Override
    public void ignoreStep() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOscillatorFrequency(double f) {
        _oscillatorFrequency = f;
    }

    @Override
    public void resetRunningTimeStopWatch() {
        _runningTime = 0;
        _presenter.displayRunningTime(_runningTime);
    }

    @Override
    public void setBreakOnWatchdogTrigger(boolean b) {
        _breakOnWatchdogTrigger = b;
    }
    
    @Override
    public void setBreakOnInterrupt(boolean b) {
        _breakOnInterrupt = b;
    }

    @Override
    public void setLSTFile(String filePath) {
        //reset the simulator before loading new file
        initialize();
        _lstFilePath = filePath;
        ParseResult pr = FileParser.parse(filePath);
        _parseResult = pr;
        for (int i = 0; i < pr.fileLines.size(); i++) {
            _presenter.addCodeLine(pr.address.get(i), pr.instruction.get(i), pr.sourceCode.get(i));
            if (pr.address.get(i) != null) {
                _pic.setInstructionToProgramMemory(pr.address.get(i), pr.instruction.get(i));
            }
        }
    }

    @Override
    public void setAutomaticSteppingInterval(int ms) {
        _automaticSteppingInterval = ms;
    }

    @Override
    public void setAutomaticSteppingMode(boolean b) {
        _automaticSteppingMode = b;
    }

    @Override
    public void setRegister(int address, int value) {
        _pic.setRegister(address, value, false);
    }

    @Override
    public void setPortTris(String port, int value) {
        switch (port) {
            case "A":
                _pic.setRegister(_pic.PORTA_REGISTER_BANK0, value, false);
                break;
            case "B":
                _pic.setRegister(_pic.PORTB_REGISTER_BANK0, value, false);
            default:
        }
    }

    @Override
    public void setPortLatch(String port, int value) {
        switch (port) {
            case "A":
                _pic.setRegister(_pic.PORTA_REGISTER_BANK0, value, false);
                break;
            case "B":
                _pic.setRegister(_pic.PORTB_REGISTER_BANK0, value, false);
            default:
        }
    }

    @Override
    public void setPortEnvironment(String port, int value) {
        switch (port) {
            case "A":
                _pic.setPortAEnvironment(value);
                break;
            case "B":
                _pic.setPortBEnvironment(value);
            default:
        }
    }
}
