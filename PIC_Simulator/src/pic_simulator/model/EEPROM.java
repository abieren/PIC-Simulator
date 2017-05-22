/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import java.util.HashMap;
import java.util.Map;
import pic_simulator.interfaces.Notifier;
import pic_simulator.utils.BinaryNumberHelper;

/**
 *
 * @author Alex
 */
public class EEPROM {
    
    private static final int MAX_ADDRESS = 0x3F;
    private static final int DEFAULT_VALUE = 0x3F;
    private Map<Integer, Integer> _registers = new HashMap<>();
    private Notifier _notifier;

    public EEPROM(Notifier notifier) {
        _notifier = notifier;
    }
    
    
    private int shrinkAddress(int address) {
        return BinaryNumberHelper.truncateToNBit(address, 6);
    }
    
    public void setRegsiter(int address, int value) {
        address = shrinkAddress(address);
        value = BinaryNumberHelper.truncateToNBit(value, 8);
        _registers.put(address, value);
        _notifier.changedEEPROM(address,value);
    };
    
    public int getRegsiter(int address) {
        address = shrinkAddress(address);
        Integer result = _registers.get(address);
        if (result == null) {
            setRegsiter(address, DEFAULT_VALUE);
            return DEFAULT_VALUE;
        }
        return result;
    }
    
}