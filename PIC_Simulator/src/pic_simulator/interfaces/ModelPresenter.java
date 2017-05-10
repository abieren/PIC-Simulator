/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.interfaces;

import java.util.List;

/**
 *
 * @author Alex
 */
public interface ModelPresenter {
    
    public void initializeView();
    
    public void addCodeLine(Integer address, Integer instruction, String sourceCode);
    
    public void displayExecutedCodeLine(int line);

    public void displayAutomaticSteppingMode(boolean b);

    public void displayAutomaticSteppingIntervall(int ms);

    public void displayRunningTime(double microSeconds);

    public void displayOscillatorFrequency(double megaHz);

    public void displayBreakOnWatchdogTrigger(boolean b);

    public void displayBreakOnInterrupt(boolean b);

    public void displayWRegister(int value);

    public void displayFSRRegister(int value);

    public void displayPCRegsiter(int value);

    public void displayPCLRegister(int value);

    public void displayPCLATHRegsiter(int value);

    public void displaySTATUSRegister(int value);

    public void displayOPTIONRegister(int value);

    public void displayINTCONRegister(int value);

    public void displayRegister(int register, int value);
    
    public void displayStack(List<Integer> stack);

    public void pushStack(int value, boolean isOverflow);

    public void popStack(int value, boolean isUnderflow);

    public void displayPortLatch(String port, int oldValue, int newValue);

    public void displayPortOutput(String port, int oldValue, int newValue);

    public void displayPortTris(String port, int oldValue, int newValue);

    public void displayPortEnvironment(String a, int oldValue, int newValue);
    
}
