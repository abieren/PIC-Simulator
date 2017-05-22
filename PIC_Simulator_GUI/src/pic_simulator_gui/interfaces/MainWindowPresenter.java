/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui.interfaces;

import pic_simulator.interfaces.Model;

/**
 *
 * @author Alex
 */
public interface MainWindowPresenter {

    //public void setStatusBit(int bit, boolean value);
    //public void setPortTrisBit(int port, int bit, boolean value);
    //public void setPortValueBit(int port, int bit, boolean value;
    
    public void setModel(Model model);

    public void setView(MainWindowView view);

    public void setLSTFile(String filePath);

    public void resetPIC();

    public void powerResetPIC();

    public void setAutomaticSteppingMode(boolean b);

    public void setAutomaticSteppingInterval(int ms);

    public void stepIn();

    public void stepOut();

    public void stepOver();

    public void ignoreStep();

    public void setOscillatorFrequency(double f);

    public void resetRunningTimeStopWatch();

    public void setRegister(int address, int value);

    public void setPortTris(String port, int value);

    public void setPortLatch(String port, int value);

    public void setPortInOut(String port, int value);

    public void setPortEnv(String port, int value);
    
    public void setPortLatchBit(String port, int bit, boolean value);
    
    public void setPortTrisBit(String port, int bit, boolean value);
    
    public void setPortEnvironmentBit(String port, int bit, boolean value);
    
    public void addBreakpoint(int address);
    
    public void removeBreakpoint(int address);

    public void setSynchronizePortsWithRS232(boolean b);
    
}
