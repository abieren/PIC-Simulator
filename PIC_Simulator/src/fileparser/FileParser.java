/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DominikSchmitt
 */
public class FileParser {
    public static ParseResult parse(String path) {
        ParseResult parseResult = new ParseResult();
        parseResult.fileLines = new ArrayList<String>();
        parseResult.address = new ArrayList<Integer>();
        parseResult.instruction = new ArrayList<Integer>();
        parseResult.sourceCode = new ArrayList<String>();
        parseResult.addressToLineNumber = new HashMap<Integer, Integer>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path), "windows-1252").useDelimiter(System.lineSeparator());
            Integer lineNumber;
            lineNumber = 1;
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                parseResult.fileLines.add(currentLine);
                
                if(currentLine.charAt(0) == '0' || currentLine.charAt(0) == '1') {
                    parseResult.address.add(Integer.decode("0x"+currentLine.substring(0,4)));
                    parseResult.instruction.add(Integer.decode("0x"+currentLine.substring(5,9)));
                    parseResult.sourceCode.add(currentLine.substring(27));
                    parseResult.addressToLineNumber.put(Integer.decode("0x"+currentLine.substring(0,4)),lineNumber);
                } else {
                    parseResult.address.add(null);
                    parseResult.instruction.add(null);
                    parseResult.sourceCode.add(currentLine.substring(27));
                }
                lineNumber = lineNumber +1 ;
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return parseResult;
    }
}
