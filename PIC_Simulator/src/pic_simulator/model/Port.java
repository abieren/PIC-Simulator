/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

/**
 *
 * @author Alex
 */
public class Port {
    
    private int _latch;  //saves state internal pin latches
    private int _output; //saves ouput state of pins
    private int _tris;   //saves tris state that determines which pins are output or input pins
    
    private void updateOutput() {
        //output of pins depend on the tris bits
        //tris=0 means pin is output, tris=1 means bit is input
        //create mask that results the right output if it is combined with latch
        //during an AND operation
        _output = _latch & (~_tris);
    }
    
    public void setLatch(int value) {
        updateOutput();
    }
    
    public int getLatch() {
        return _latch;
    }
    
    public int getOutput() {
        return _output;
    }
    
    public void setTris(int value) {
        updateOutput();
        
    }
    
    public int getTris(int pin) {
        return _tris;
    }    
}
