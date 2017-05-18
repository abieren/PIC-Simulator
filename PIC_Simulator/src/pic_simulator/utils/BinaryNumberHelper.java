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
    
    public static String formatToDisplayableHex(int value, int fillZeroesToLength, boolean capitalize) {
        String result = Integer.toHexString(value);
        if (capitalize) {
            result = result.toUpperCase();
        }
        while (result.length() < fillZeroesToLength) {            
            result = "0" + result;
        }
        return result;
    }
    
    public static boolean parseBoolean(int value) {
        if (value == 0) return false;
        return true;
    };
    
    public static int getBit(int value, int bit) {
        return (value >> bit) & 1;
    }
    
    public static int truncateToNBit(int value, int nBits) {
        int mask = (1 << nBits) -1;
        return value & mask;
    }
    
    public static int extractBits(int value, int from, int to) {
        int result = 0;
        for (int i = from; i <= to; i++) {
            int currentBit = getBit(value, i);
            result = result | currentBit << (i-from);
        }
        return result;
    }
    
    public static int setBit(int value, int bit, int b) {
        if (b == 0) {
            return setBitsToZero(value, bit);
        } else {
            return setBitsToOne(value, bit);
        }
    }
    
    public static int setBit(int value, int bit, boolean b) {
        if (b == true) {
            return setBitsToZero(value, bit);
        } else {
            return setBitsToOne(value, bit);
        }
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
            if (getBit(value, bit) != 0) return false;
        }
        return true;
    }
    
    public static boolean areBitsOne(int value, Integer... bits) {
        for (Integer bit : bits) {
            if (getBit(value, bit) != 1) return false;
        }
        return true;
    }
    
    public static boolean matchPattern(int value, String pattern) {
        //sample pattern: "8d_0x0x0110"
        //8 means check from the 8th bit
        //d means check bits in descending order
        //a means check bits in ascending order
        //x is dont care bit that can be 1 or 0 to fullfill the pattern
        
        //get start bit
        String[] temp = pattern.split(":");
        int startBit = Integer.parseInt(temp[0]);
        //check start bit range
        if (startBit < 0 || startBit > Integer.SIZE) {
            throw new IllegalArgumentException("invalid bit pattern");
        }
        temp = temp[1].split("_");
        boolean ascending;
        switch (temp[0]) {
            case "a":
                ascending = true;
                break;
            case "d":
                ascending = false;
                break;
            default:
                throw new IllegalArgumentException("invalid bit pattern");
        }
        
        //get bit pattern
        String bitPattern = temp[1];
        //check if patters exceeds bits, check end bit range
        if (bitPattern.length() == 0) throw new IllegalArgumentException("invalid bit pattern");
        if (bitPattern.length() > Integer.SIZE) throw new IllegalArgumentException("invalid bit pattern");
        if (ascending) {
            int endBit = (startBit+1) + bitPattern.length();
            if (endBit > Integer.SIZE) throw new IllegalArgumentException("invalid bit pattern");
        } else {
            int endBit = (startBit+1) - bitPattern.length();
            if (endBit < 0) throw new IllegalArgumentException("invalid bit pattern");
        }
        
        //check binary number with bit pattern
        if (ascending) {
            for (int i = startBit; i < startBit+bitPattern.length(); i++) {
                int currentBit = getBit(value, i);
                char expectedBit = bitPattern.charAt(i);
                boolean result = checkPatternForBit(currentBit, expectedBit);
                if (!result) return false;
            }
        } else {
            for (int i = startBit; i >= 0; i--) {
                int currentBit = getBit(value, i);
                char expectedBit = bitPattern.charAt(bitPattern.length()-1-i); //begin at start of string
                boolean result = checkPatternForBit(currentBit, expectedBit);
                if (!result) return false;
            }
        }
        return true;
    }
    
    private static boolean checkPatternForBit(int bit, char bitPattern) {
        //if 1 and 0 are allowed skip cehcking the current bit
        if (bitPattern == 'x') return true;
        //make sure bit pattern holds 0 or 1
        if (bitPattern != '0' && bitPattern != '1') {
            throw new IllegalArgumentException("invalid bit pattern");
        }
        //make sure bit as int value is either 0 or 1
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("invalid bit pattern");
        }
        if (bit == 0 && bitPattern == '0') return true;
        else if (bit == 1 && bitPattern== '1') return true;
        return false;
    };
}
