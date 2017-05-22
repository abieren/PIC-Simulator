/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;
//import jssc.SerialPortList;

import jssc.SerialPort;
import jssc.SerialPortException;
import pic_simulator.utils.BinaryNumberHelper;

/**
 *
 * @author DominikSchmitt
 */
public class RS232 {

    public static String sendUpdateToCOM(String hexstring) {
        SerialPort serialPort = new SerialPort("COM4");
        String antwort = "";
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_4800,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.writeBytes(hexStringToByteArray(hexstring)); //Write data to port
            byte[] buffer = serialPort.readBytes(5); //Read 5 bytes from serial port ( 2*2 + CR)
            serialPort.closePort();//Close serial port after all actions are performed
            antwort = bytetohex(buffer);
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
        return antwort;
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static String bytetohex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    public static String genHexString(Port a, Port b) {
        String hexstring = "";
        
        int portatris = a.getTris();
        int portatrisupper = BinaryNumberHelper.extractBits(portatris, 4, 7) + 32 + 16;
        int portatrislower = BinaryNumberHelper.extractBits(portatris, 0, 3) + 32 + 16;
        int portbtris = b.getTris();
        int portbtrisupper = BinaryNumberHelper.extractBits(portbtris, 4, 7) + 32 + 16;
        int portbtrislower = BinaryNumberHelper.extractBits(portbtris, 0, 3) + 32 + 16;
        int portaenv = a.getEnvironment();
        int portaenvupper = BinaryNumberHelper.extractBits(portaenv, 4, 7) + 32 + 16;
        int portaenvlower = BinaryNumberHelper.extractBits(portaenv, 0, 3) + 32 + 16;
        int portbenv = b.getEnvironment();
        int portbenvupper = BinaryNumberHelper.extractBits(portbenv, 4, 7) + 32 + 16;
        int portbenvlower = BinaryNumberHelper.extractBits(portbenv, 0, 3) + 32 + 16;
        
        hexstring = Integer.toHexString(portatrisupper) + Integer.toHexString(portatrislower) + Integer.toHexString(portaenvupper) + Integer.toHexString(portaenvlower) +Integer.toHexString(portbtrisupper) +Integer.toHexString(portbtrislower) +Integer.toHexString(portbenvupper) +Integer.toHexString(portbenvlower) + "0D";

        return hexstring;
    }
    
    public static void updatePorts(Port a, Port b) {
        
        String anfrage = genHexString(a,b);
        String antwort = "";
        antwort = sendUpdateToCOM(anfrage);
        System.out.println(antwort);
    }
}
