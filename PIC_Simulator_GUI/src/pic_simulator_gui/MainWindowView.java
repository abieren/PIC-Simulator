/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui;

/**
 *
 * @author Alex
 */
public interface MainWindowView {
    
    public void initializeView();
    public void showStatusMessage(MessageType level, String message);

    public void addCodeLine(Integer address, Integer instruction, String sourceCode);
    public void setCurrentCodeLine(int line);
    //public void setRegisterMap(Map<Integer, Integer> registers);
    //public void setRegister(int register, int value);
    public void setRunningTime(int microSeconds);
    public void setWRegister(int value);
    public void setFSRRegister(int value);
    public void setPCRegsiter(int value);
    public void setPCLRegister(int value);
    public void setPCLATHRegsiter(int value);
    public void setSTATUSRegister(int value);
    public void setOPTIONRegister(int value);
    public void setINTCONRegister(int value);
    //public void setPortTrisBit(int port, int bit, boolean value);
    //public void setPortValueBit(int port, int bit, boolean value);

    public void setAutomaticSteppingMode(boolean b);

    public void setAutomaticSteppingIntervall(int ms);

    public void setOscillatorFrequency(double megaHz);

    public void setBreakOnWatchdogTrigger(boolean b);

    public void setBreakOnInterrupt(boolean b);
}
