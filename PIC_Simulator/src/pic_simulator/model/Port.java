/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import pic_simulator.utils.BinaryNumberHelper;

/**
 *
 * @author Alex
 */
public class Port {
    
    private int _latch;  //saves state internal pin latches
    private int _tris;   //saves tris state that determines which pins are output or input pins
    private int _environment; //saves the state of the environment that tries to set the port
    private int _inout; //saves input/ouput state of pins
    private int _flanks; //saves whether a flank on a pin has been detected
    
    private void updateOutput() {
        //output of pins depend on the tris bits
        //tris=0 means pin is output, tris=1 means bit is input
        //create mask that results the right output if it is combined with latch
        //during an AND operation
        int oldInOut = _inout;
        _inout = 0;
        _inout = _inout | (~_tris) & _latch;
        _inout = _inout | _tris & _environment;
        //xor old in-out value with new in-out value to determine flanks
        _flanks = oldInOut ^ _inout;
    }

    public Port() {
        _latch = 0;
        _tris = ~0; //set every bit to 1 to mark every pin as input as default
        updateOutput();
    }

    public Port(int latch, int tris) {
        _latch = latch;
        _tris = tris;
        updateOutput();
    }
    
    public void setLatch(int value) {
        _latch = value;
        updateOutput();
    }
    
    public int getLatch() {
        return _latch;
    }
    
    public void setTris(int value) {
        _tris = value;
        updateOutput();
    }
    
    public int getTris() {
        return _tris;
    }  
    
    public void setEnvironment(int value) {
        _environment = value;
        updateOutput();
    }
    
    public int getEnvironment() {
        return _environment;
    }
    
    public int getInOut() {
        return _inout;
    }
    
    /**
     * 
     * @param pin specifies which pin to retrieve
     * @return -1 for falling flank, 0 for no flank, 1 for rising flank
     */
    public int getFlank(int pin) {
        boolean flankBit = BinaryNumberHelper.getBitBoolean(_flanks, pin);
        boolean pinBit = BinaryNumberHelper.getBitBoolean(_inout, pin);
        if (flankBit == false) return 0;
        if (pinBit == false) {
            //falling flank
            return -1;
        } else {
            //rising flank
            return 1;
        }
    }
    
}
