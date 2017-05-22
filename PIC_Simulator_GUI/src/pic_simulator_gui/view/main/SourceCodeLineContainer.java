/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui.view.main;

import javafx.scene.control.CheckBox;

/**
 *
 * @author Alex
 */
public class SourceCodeLineContainer {
    
    public String codeline;
    public CheckBox checkbox;

    public SourceCodeLineContainer(String codeline, CheckBox checkbox) {
        this.codeline = codeline;
        this.checkbox = checkbox;
    }

    public String getCodeline() {
        return codeline;
    }

    public void setCodeline(String codeline) {
        this.codeline = codeline;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }
    
}
