/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import java.util.HashSet;
import pic_simulator.interfaces.Notifier;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
    
    private Set<Integer> _breakpoints;
    private boolean _breakOnWatchdogTrigger;
    private boolean _breakOnInterrupt;

    public Simulator(ModelPresenter presenter) {
        _presenter = presenter;
        _notifier = new NotifierImpl(_presenter, this);
        _pic = new PICSimulator(_notifier);
    }
    
    @Override
    public void initialize() {
        _presenter.initializeView();
        _pic.initialize();
        
        _automaticSteppingMode = false;
        _presenter.displayAutomaticSteppingMode(_automaticSteppingMode);
        _automaticSteppingInterval = 1500;
        _presenter.displayAutomaticSteppingIntervall(_automaticSteppingInterval);
        _pic.setOscillatorFrequency(4.0);
        _presenter.displayOscillatorFrequency(_pic.getOscillatorFrequency());
        _presenter.displayRunningTime(_pic._runningTime);
        _breakpoints = new HashSet<>();
        _breakOnWatchdogTrigger = true;
        _breakOnInterrupt = true;
        
    }
    
    @Override
    public void resetPIC() {
        _pic.resetByMCLR(false, false);
    }

    @Override
    public void powerResetPIC() {
        _pic.resetByPower();
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
        //check in automatic stepping mode if breakpoint is reached
        if (_automaticSteppingMode && _breakpoints.contains(_pic.getPCRegister())) {
            setAutomaticSteppingMode(false);
            return; //abort method to halt at the breakpoint
        }
        _pic.makeStep();
    }
    
    @Override
    public void ignoreStep() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOscillatorFrequency(double f) {
        _pic.setOscillatorFrequency(f);
    }

    @Override
    public void resetRunningTimeStopWatch() {
        _pic._runningTime = 0;
        _presenter.displayRunningTime(_pic._runningTime);
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
        _lstFilePath = filePath;
        ParseResult pr = FileParser.parse(filePath);
        _parseResult = pr;
        //reset the simulator before loading new file
        initialize();
        powerResetPIC();
        programPIC();
        _presenter.displayCodeLines(pr);
    }
    
    private void programPIC() {
         for (int i = 0; i < _parseResult.fileLines.size(); i++) {
            if (_parseResult.address.get(i) != null) {
                _pic.setInstructionToProgramMemory(_parseResult.address.get(i), _parseResult.instruction.get(i));
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
        _presenter.displayAutomaticSteppingMode(b);
        if (_automaticSteppingMode) {
            System.out.println("AUTOMATIC==============");
            //start timer
            Thread t1 = new Thread() {
                public void run() {
                    while (_automaticSteppingMode) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI here.
                                stepOver();
                            }
                        });
                        try {
                            Thread.sleep(_automaticSteppingInterval);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            t1.start();
        }
    }

    @Override
    public void setRegister(int address, int value) {
        _pic.setRegister(address, value, false);
    }

    @Override
    public void setPortTris(String port, int value) {
        switch (port) {
            case "A":
                _pic.setRegister(_pic.TRISA_REGISTER_BANK1, value, false);
                break;
            case "B":
                _pic.setRegister(_pic.TRISB_REGISTER_BANK1, value, false);
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

    @Override
    public Optional<Integer> getPortLatch(String port) {
        switch (port) {
            case "A":
                return Optional.of(_pic._portA.getLatch());
            case "B":
                return Optional.of(_pic._portB.getLatch());
            default:
                return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> getPortTris(String port) {
        switch (port) {
            case "A":
                return Optional.of(_pic._portA.getTris());
            case "B":
                return Optional.of(_pic._portB.getTris());
            default:
                return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> getPortEnvironment(String port) {
        switch (port) {
            case "A":
                return Optional.of(_pic._portA.getEnvironment());
            case "B":
                return Optional.of(_pic._portB.getEnvironment());
            default:
                return Optional.empty();
        }
    }

    @Override
    public void addBreakpoint(int address) {
        _breakpoints.add(address);
    }

    @Override
    public void removeBreakpoint(int address) {
        _breakpoints.remove(address);
    }
}
