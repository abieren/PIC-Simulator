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
public interface Timer {
    
    public void clear();
    public boolean hasTriggered();
    public void setPrescalerAssignment(boolean PSAbit);
    public void setPrescalerRate(boolean PS0bit, boolean PS1bit, boolean PS2bit);
    public void setWorkingMode(boolean isWorkingAsTimer);
    public void setIncrementingEdgeMode(boolean incrementOnFallingEdge);
    public void notifyEdge(boolean isFallingEdge);
    public void notifyCycle();
    
    public void setTimerCount(int value, boolean doClear);
    public int getTimerCount();
    
}
