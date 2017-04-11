/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.utils;

import java.util.List;
import java.util.Map;

/**
 *
 * @author DominikSchmitt
 */
public class ParseResult {
    
    public List<String> fileLines;
    public List<Integer> address;
    public List<Integer> instruction;
    public List<String> sourceCode;
    public Map<Integer, Integer> addressToLineNumber;
    
}
