/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class RegisterAddressDecoder {
    static public List<Integer> getAllRegistersForAddress(int address) {
        ArrayList<Integer> result = new ArrayList<>();
        //PCL
        if (address == 0x02 || address == 0x82) {
            result.add(0x02);
            result.add(0x82);
            return result;
        }
        ///STATUS
        if (address == 0x03 || address == 0x83) {
            result.add(0x03);
            result.add(0x83);
            return result;
        }
        //FSR
        if (address == 0x04 || address == 0x84) {
            result.add(0x04);
            result.add(0x84);
            return result;
        }
        //PCLATH
        if (address == 0x0A || address == 0x8A) {
            result.add(0x0A);
            result.add(0x8A);
            return result;
        }
        //INTCON
        if (address == 0x0B || address == 0x8B) {
            result.add(0x0B);
            result.add(0x8B);
            return result;
        }
        //General Purposee Registers
        if (address >= 0x0C && 0x4F >= address) {
            result.add(address);
            result.add(address + 0x80);
            return result;
        }
        if (address >= 0x8C && 0xCF >= address) {
            result.add(address);
            result.add(address - 0x80);
            return result;
        }
        //unimplemented memory addresses
        if ( (address >= 0x50 && 0x7f >= address) ||
                (address >= 0xD0 && 0xFF >= address) ||
                address == 0x70 ||
                address == 0x87) {
            return result;
        }
        //every other address
        result.add(address);
        return result;
    }
}
