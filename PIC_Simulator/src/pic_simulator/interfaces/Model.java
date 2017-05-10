/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.interfaces;

import java.util.Optional;

/**
 *
 * @author Alex
 */
public interface Model {
    
    public void initialize();

    public void resetPIC();

    public void powerResetPIC();

    public void stepIn();

    public void stepOut();

    public void ignoreStep();

    public void setOscillatorFrequency(double f);

    public void resetRunningTimeStopWatch();

    public void setBreakOnWatchdogTrigger(boolean b);

    public void setBreakOnInterrupt(boolean b);
    
    public void setLSTFile(String filePath);

    public void setAutomaticSteppingInterval(int ms);

    public void setAutomaticSteppingMode(boolean b);

    public void stepOver();

    public void setRegister(int address, int value);

    public void setPortTris(String port, int value);
    
    public Optional<Integer> getPortTris(String port);

    public void setPortLatch(String port, int value);
    
    public Optional<Integer> getPortLatch(String port);

    public void setPortEnvironment(String port, int value);
    
    public Optional<Integer> getPortEnvironment(String port);
    
}
