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
public interface Watchdog {
    
    public void enable();
    public void disable();
    public void clear();
    public boolean hasTriggered();
    public void setPostscalerAssignment(boolean PSAbit);
    public void setPostscalerRate(boolean PS0bit, boolean PS1bit, boolean PS2bit);
    public void notifyTimeAdvanced(double timeDeltaMs);
    
}
