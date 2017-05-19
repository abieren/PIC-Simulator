/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui.interfaces;

/**
 *
 * @author Alex
 */
public interface RS232View {
 
    public void displayPortASended(String text);
    public void displayPortBSended(String text);
    public void displayTrisASended(String text);
    public void displayTrisBSended(String text);
    public void displayPortAReceived(String text);
    public void displayPortBReceived(String text);
    
}
