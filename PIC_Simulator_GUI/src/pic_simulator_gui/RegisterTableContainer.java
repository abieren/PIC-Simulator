/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui;

/**
 *
 * @author Alex
 */
public class RegisterTableContainer {
    private String fullName;
    private String shortName;
    private long value;

    public RegisterTableContainer() {
    }

    public RegisterTableContainer(String fullName, String shortName, long value) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.value = value;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
    
}
