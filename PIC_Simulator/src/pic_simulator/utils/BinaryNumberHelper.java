/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.utils;

/**
 *
 * @author Alex
 */
public class BinaryNumberHelper {
    
    public static int normalizeToNBitNumber(int value, int nBits) {
        return value & ((1 << (nBits + 1)) - 1);
    }
    
    public static int setBitsToZero(int value, Integer... bits) {
        for (Integer bit : bits) {
            value = value & (~(1 << bit));
        }
        return value;
    }
    
    public static int setBitsToOne(int value, Integer... bits) {
        for (Integer bit : bits) {
            value = value | (1 << bit);
        }
        return value;
    }
    
    public static boolean areBitsZero(int value, Integer... bits) {
        for (Integer bit : bits) {
            int temp = (value >> bit) & 1;
            if (temp != 0) return false;
        }
        return true;
    }
    
    public static boolean areBitsOne(int value, Integer... bits) {
        for (Integer bit : bits) {
            int temp = (value >> bit) & 1;
            if (temp != 1) return false;
        }
        return true;
    }
    
}
