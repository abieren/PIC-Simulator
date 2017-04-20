/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.interfaces;

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
    
}
