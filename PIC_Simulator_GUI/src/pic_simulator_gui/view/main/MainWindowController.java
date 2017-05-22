/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui.view.main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pic_simulator.utils.BinaryNumberHelper;
import pic_simulator_gui.interfaces.MainWindowPresenter;
import pic_simulator_gui.interfaces.MainWindowView;

/**
 *
 * @author Alex
 */
public class MainWindowController 
        implements Initializable, MainWindowView {

    @FXML 
    private ToggleButton bt_automaticSteppingMode;
    @FXML
    private Label lb_automaticSteppingInterval;
    @FXML
    private Label lb_oscillatorFrequency;
    @FXML
    private Label lb_runningTime;
    @FXML
    private Label lb_wRegHexValue;
    @FXML
    private Label lb_fsrRegHexValue;
    @FXML
    private Label lb_pcRegHexValue;
    @FXML
    private Label lb_pclRegHexValue;
    @FXML
    private Label lb_pclathRegHexValue;
    @FXML
    private Label lb_statusRegHexValue;
    @FXML
    private Label lb_statusRegDecValue;
    @FXML
    private Label lb_statusRegBit0;
    @FXML
    private Label lb_statusRegBit1;
    @FXML
    private Label lb_statusRegBit2;
    @FXML
    private Label lb_statusRegBit3;
    @FXML
    private Label lb_statusRegBit4;
    @FXML
    private Label lb_statusRegBit5;
    @FXML
    private Label lb_statusRegBit6;
    @FXML
    private Label lb_statusRegBit7;
    @FXML
    private Label lb_optionRegHexValue;
    @FXML
    private Label lb_optionRegDecValue;
    @FXML
    private Label lb_optionRegBit0;
    @FXML
    private Label lb_optionRegBit1;
    @FXML
    private Label lb_optionRegBit2;
    @FXML
    private Label lb_optionRegBit3;
    @FXML
    private Label lb_optionRegBit4;
    @FXML
    private Label lb_optionRegBit5;
    @FXML
    private Label lb_optionRegBit6;
    @FXML
    private Label lb_optionRegBit7;
    @FXML
    private Label lb_intconRegHexValue;
    @FXML
    private Label lb_intconRegDecValue;
    @FXML
    private Label lb_intconRegBit0;
    @FXML
    private Label lb_intconRegBit1;
    @FXML
    private Label lb_intconRegBit2;
    @FXML
    private Label lb_intconRegBit3;
    @FXML
    private Label lb_intconRegBit4;
    @FXML
    private Label lb_intconRegBit5;
    @FXML
    private Label lb_intconRegBit6;
    @FXML
    private Label lb_intconRegBit7;
    @FXML
    private TableView tv_sourceCode;
    @FXML
    private TableView tv_stack;
    @FXML
    private GridPane gp_port_view;
    @FXML
    private GridPane gp_register_view;
    @FXML
    private GridPane gp_eeprom_view;
    @FXML
    private CheckBox cb_synchronizePorts;

    //Stage that is used by the view of this controller
    private Stage _primaryStage;
    //presenter of the view managed by this controller
    private MainWindowPresenter _presenter;
    //lists to add the code lines and breakpoints for the list views
    ObservableList<SourceCodeLineContainer> _sourceCode;
    //table columns of the port map
    List<TableColumn> _portMapColumns;
    //records of the port map
    ObservableList<PortMapRecord> _portMapRecords;
    //records of the stack view
    ObservableList<StackRecord> _stackRecords;
    //maps for managing labels in port map view
    Map<Integer, Label> _portALatchLabels;
    Map<Integer, Label> _portATrisLabels;
    Map<Integer, Label> _portAInOutLabels;
    Map<Integer, Label> _portAEnvLabels;
    Map<Integer, Label> _portBLatchLabels;
    Map<Integer, Label> _portBTrisLabels;
    Map<Integer, Label> _portBInOutLabels;
    Map<Integer, Label> _portBEnvLabels;
    //map for managing labels in eeprom view
    Map<Integer, Label> _eepromLabels;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initializeData(Stage primaryStage, MainWindowPresenter presenter) {
        _primaryStage = primaryStage;
        _presenter = presenter;
    }

    private void initializeCodeView() {
        //reset collections
        _sourceCode = FXCollections.observableArrayList();
        //reset table view
        tv_sourceCode.getColumns().clear();
        //build table view
        TableColumn checkboxCol = new TableColumn("Breakpoint");
        checkboxCol.setStyle("-fx-alignment: center;");
        checkboxCol.setCellValueFactory(
                new PropertyValueFactory<SourceCodeLineContainer, String>("checkbox"));
        TableColumn codelineCol = new TableColumn("Source Code");
        codelineCol.setStyle("-fx-min-width: 500px;");
        codelineCol.setCellValueFactory(
                new PropertyValueFactory<SourceCodeLineContainer, String>("codeline"));
        tv_sourceCode.getColumns().addAll(checkboxCol, codelineCol);
        tv_sourceCode.setItems(_sourceCode);
        Label placeholder = new Label("Please open a program.");
        tv_sourceCode.setPlaceholder(placeholder);
    }
    
    private void initializePortMapView() {
        //reinitialize the grid panel
        _portALatchLabels = new HashMap<>();
        _portATrisLabels = new HashMap<>();
        _portAInOutLabels = new HashMap<>();
        _portAEnvLabels = new HashMap<>();
        _portBLatchLabels = new HashMap<>();
        _portBTrisLabels = new HashMap<>();
        _portBInOutLabels = new HashMap<>();
        _portBEnvLabels = new HashMap<>();
        gp_register_view.getChildren().clear();
        //genrate labels inside the grid
        Label lb;
        int id;
        int position;
        String cellStyle = "-fx-min-height: 20px; -fx-max-height: 20px; "
                + "-fx-min-width: 20px; -fx-max-height: 20px; "
                + "-fx-background-color: white; "
                + "-fx-alignment: center;";
        String propertyLegendStyle = "-fx-min-height: 20px; -fx-max-height: 20px; "
                + "-fx-min-width: 80px; -fx-max-height: 80px; "
                + "-fx-background-color: rgb(200,200,200); "
                + "-fx-alignment: center;";
        String portLegendStyle = "-fx-min-height: 20px; -fx-max-height: 20px; "
                + "-fx-min-width: 80px; -fx-max-height: 80px; "
                + "-fx-background-color: rgb(200,200,200); "
                + "-fx-font-weight: bold;";
        
        //fill legend
        int row = 0;
        lb = new Label("Port A");
        lb.setStyle(portLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port A headline
        for (int i = 0; i < 8; i++) {
            lb = new Label();
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
        }
        //fill legend
        row++;
        lb = new Label("Latch");
        lb.setStyle(propertyLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port A latch
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            id = i; 
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
            lb.setId(Integer.toString(id));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                boolean bitValue = BinaryNumberHelper.parseBoolean(Integer.parseInt(label.getText()));
                bitValue = !bitValue;
                _presenter.setPortLatchBit("A", bit, bitValue);
            });
            _portALatchLabels.put(id, lb);
        }
        //fill legend
        row++;
        lb = new Label("Tris");
        lb.setStyle(propertyLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port A tris
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            id = i; 
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
            lb.setId(Integer.toString(id));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                boolean bitValue = true;
                if (label.getText().equals("o")) bitValue = false;
                bitValue = !bitValue;
                _presenter.setPortTrisBit("A", bit, bitValue);
            });
            _portATrisLabels.put(id, lb);
        }
        //fill legend
        row++;
        lb = new Label("Env");
        lb.setStyle(propertyLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port A environment
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            id = i; 
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
            lb.setId(Integer.toString(id));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                boolean bitValue = BinaryNumberHelper.parseBoolean(Integer.parseInt(label.getText()));
                bitValue = !bitValue;
                _presenter.setPortEnvironmentBit("A", bit, bitValue);
            });
            _portAEnvLabels.put(id, lb);
        }
        //fill legend
        row++;
        lb = new Label("In/Out");
        lb.setStyle(propertyLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port A in/out
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            id = i;
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
            lb.setId(Integer.toString(id));
            _portAInOutLabels.put(id, lb);
        }
        //fill legend
        row++;
        lb = new Label("Port B");
        lb.setStyle(portLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port B headline
        for (int i = 0; i < 8; i++) {
            lb = new Label();
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
        }
        //fill legend
        row++;
        lb = new Label("Latch");
        lb.setStyle(propertyLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port B latch
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            id = i; 
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
            lb.setId(Integer.toString(id));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                
                boolean bitValue = BinaryNumberHelper.parseBoolean(Integer.parseInt(label.getText()));
                bitValue = !bitValue;
                _presenter.setPortLatchBit("B", bit, bitValue);
            });
            _portBLatchLabels.put(id, lb);
        }
        //fill legend
        row++;
        lb = new Label("Tris");
        lb.setStyle(propertyLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port B tris
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            id = i;
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
            lb.setId(Integer.toString(id));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                boolean bitValue = true;
                if (label.getText().equals("o")) bitValue = false;
                bitValue = !bitValue;
                _presenter.setPortTrisBit("B", bit, bitValue);
            });
            _portBTrisLabels.put(id, lb);
        }
        //fill legend
        row++;
        lb = new Label("Env");
        lb.setStyle(propertyLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port B environment
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            id = i; 
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
            lb.setId(Integer.toString(id));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                boolean bitValue = BinaryNumberHelper.parseBoolean(Integer.parseInt(label.getText()));
                bitValue = !bitValue;
                _presenter.setPortEnvironmentBit("B", bit, bitValue);
            });
            _portBEnvLabels.put(id, lb);
        }
        //fill legend
        row++;
        lb = new Label("In/Out");
        lb.setStyle(propertyLegendStyle);
        gp_port_view.add(lb,0,row);
        //fill port B in/out
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            id = i; 
            position = 7-i+1;
            lb.setStyle(cellStyle);
            gp_port_view.add(lb,position,row);
            _portBInOutLabels.put(id, lb);
        }
    }
    
    private void initializeRegisterMapView() {
        //reinitialize the grid panel
        gp_register_view.getChildren().clear();
        //genrate labels inside the grid
        for (int row = 0; row < 17; row++) {
            for (int col = 0; col < 17; col++) {
                Label lb = new Label();
                lb.setMinHeight(20);
                lb.setMaxWidth(20);
                lb.setMinHeight(20);
                lb.setMaxHeight(20);
                //fill upper table head and left table head
                if (col == 0 && row == 0) {
                    //add empty label
                    lb.setStyle("-fx-background-color: rgb(200,200,200);");
                    gp_register_view.add(lb, col, row);
                    continue;
                }
                if (row == 0 && col > 0) {
                    lb.setText(BinaryNumberHelper.formatToDisplayableHex(col-1, 1, false));
                    lb.setStyle("-fx-background-color: rgb(200,200,200); -fx-font-weight: bold; -fx-alignment: center;");
                    gp_register_view.add(lb, col, row);
                    continue;
                } else if (col == 0 && row > 0) {
                    lb.setText(BinaryNumberHelper.formatToDisplayableHex(row-1, 1, true));
                    lb.setStyle("-fx-background-color: rgb(200,200,200); -fx-font-weight: bold; -fx-alignment: center;");
                    gp_register_view.add(lb, col, row);
                    continue;
                }
                lb.setStyle("-fx-background-color: white; -fx-alignment: center;");
                lb.setText(BinaryNumberHelper.formatToDisplayableHex(0, 2, true));
                //use id to get the address which the label displays
                lb.setId(Integer.toString((row-1)*16+col-1));
                //add onclick handler to change value of the register
                lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                    Label label = (Label)event.getSource();
                    int address = Integer.parseInt(label.getId());
                    setNewRegisterValueDialog(address);
                });
                gp_register_view.add(lb, col, row);
            }
        }
    }
    
    private void initializeStackView() {
        TableColumn tc = new TableColumn<>("PC values");
        tc.setCellValueFactory(new PropertyValueFactory<>("value"));
        tc.setMinWidth(98);
        tc.setMaxWidth(98);
        tv_stack.getColumns().clear(); //init,reset
        tv_stack.getColumns().add(tc);
        _stackRecords = FXCollections.observableArrayList();
        tv_stack.setItems(_stackRecords);
        _stackRecords.add(new StackRecord("top"));
        _stackRecords.add(new StackRecord("bottom"));
    }
    
    private void initializeEEPROMView() {
        //reinitialize the grid panel
        gp_eeprom_view.getChildren().clear();
        _eepromLabels  = new HashMap<>();
        
        String styleEmptyLabel = "-fx-background-color: rgb(200,200,200);";
        String styleTableHead = "-fx-background-color: rgb(200,200,200); -fx-font-weight: bold; -fx-alignment: center;";
        String styleEepromValue = "-fx-background-color: white; -fx-alignment: center;";
        
        //genrate labels inside the grid
        int maxRow = 8;
        int maxCol = 8;
        for (int row = 0; row < maxRow+1; row++) {
            for (int col = 0; col < maxCol+1; col++) {
                Label lb = new Label();
                lb.setMinHeight(20);
                lb.setMaxWidth(20);
                lb.setMinHeight(20);
                lb.setMaxHeight(20);
                //fill upper table head and left table head
                if (col == 0 && row == 0) {
                    //add empty label
                    lb.setStyle(styleEmptyLabel);
                    gp_eeprom_view.add(lb, col, row);
                    continue;
                }
                if (row == 0 && col > 0) {
                    lb.setText(BinaryNumberHelper.formatToDisplayableHex(col-1, 1, false));
                    lb.setStyle(styleTableHead);
                    gp_eeprom_view.add(lb, col, row);
                    continue;
                } else if (col == 0 && row > 0) {
                    lb.setText(BinaryNumberHelper.formatToDisplayableHex((row-1)*0x08, 2, true));
                    lb.setStyle(styleTableHead);
                    gp_eeprom_view.add(lb, col, row);
                    continue;
                }
                //fill in eeprom register labels
                lb.setStyle(styleEepromValue);
                lb.setText(BinaryNumberHelper.formatToDisplayableHex(0, 2, true));
                //use id to get the address which the label displays
                int id = (row-1)*maxRow+(col-1);
                lb.setId(Integer.toString(id));
                //add onclick handler to change value of the register
                lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                    Label label = (Label)event.getSource();
                    int address = Integer.parseInt(label.getId());
                    Optional<Integer> value = showSetNewValueDialog();
                    if (!value.isPresent()) return;
                    _presenter.setEEPROM(address, value.get());
                });
                
                gp_eeprom_view.add(lb, col, row);
                _eepromLabels.put(id, lb);
            }
        }
    }

    @FXML
    private void bt_resetPIC_onClicked() {
        _presenter.resetPIC();
    }

    @FXML
    private void bt_powerResetPIC_onClicked() {
        _presenter.powerResetPIC();
    }

    @FXML
    private void bt_stepOver_onClicked() {
        _presenter.stepOver();
    }

    @FXML
    private void bt_ignoreStep_onClicked() {
        _presenter.ignoreStep();
    }

    @FXML
    private void cb_synchronizePorts_onClicked() {
        boolean isChecked = cb_synchronizePorts.selectedProperty().get();
        _presenter.setSynchronizePortsWithRS232(isChecked);
    }
    
    @FXML
    private void bt_stepIn_onClicked() {
        _presenter.stepIn();
    }

    @FXML
    private void bt_stepOut_onClicked() {
        _presenter.stepOut();
    }

    @FXML
    private void bt_automaticSteppingMode_onClicked() {
        boolean value = bt_automaticSteppingMode.isSelected();
        _presenter.setAutomaticSteppingMode(value);
    }

    @FXML
    private void bt_resetRunningTimeStopWatch() {
        _presenter.resetRunningTimeStopWatch();
    }
    
    @FXML
    private void bt_changeAutomaticSteppingInterval() {
        TextInputDialog dialog = new TextInputDialog("1000");
        dialog.setTitle("Set automatic stepping interval");
        dialog.setHeaderText("Set automatic stepping interval");
        dialog.setContentText("Please enter interval in ms:");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                int newValue = Integer.parseInt(result.get());
                _presenter.setAutomaticSteppingInterval(newValue);
                lb_automaticSteppingInterval.setText(Integer.toString(newValue));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Number");
                alert.setHeaderText("Invlaid Number");
                alert.setContentText("\""+ result.get() +"\" is not a valid interval in ms!");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    private void bt_changeOscillatorFrequency() {
        TextInputDialog dialog = new TextInputDialog("4.0");
        dialog.setTitle("Set oscillator frequency");
        dialog.setHeaderText("Set oscillator frequency");
        dialog.setContentText("Please enter frequency in MHz:");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                double newValue = Double.parseDouble(result.get());
                _presenter.setOscillatorFrequency(newValue);
                lb_oscillatorFrequency.setText(Double.toString(newValue));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Number");
                alert.setHeaderText("Invlaid Number");
                alert.setContentText("\""+ result.get() +"\" is not a valid frequency in MHz!");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    private void menu_openPDF_onClicked() {
        openPDF();
    }
    
    private void openPDF() {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("./manual.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }

    @FXML
    private void menu_openProgram_onClicked() {
        choseLSTFile();
    }

    private void choseLSTFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open LST File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("LST file (*.lst)", "*.LST", "*.lst");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(_primaryStage);
        if (file != null) {
            String filePath = file.getAbsolutePath();
            _presenter.setLSTFile(filePath);
            _primaryStage.setTitle("PIC SIMULATOR - " + filePath);
        }
    }

    private void tf_automaticSteppingIntervall_onChanged() {
        String newValue = lb_automaticSteppingInterval.getText();
        int value;
        try {
            value = Integer.parseInt(newValue);
        } catch (NumberFormatException e) {
            //don't respect the value in simulation if it is invalid and just
            //ignore it
            return;
        }
        _presenter.setAutomaticSteppingInterval(value);
    }

    private void tf_oscillatorFrequency_onChanged() {
        String newValue = lb_oscillatorFrequency.getText();
        double value;
        try {
            value = Double.parseDouble(newValue);
        } catch (NumberFormatException e) {
            //don't respect the value in simulation if it is invalid and just
            //ignore it
            return;
        }
        _presenter.setOscillatorFrequency(value);
    }
    
    private void setNewRegisterValueDialog(int address) {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Set register value");
        String register = BinaryNumberHelper.formatToDisplayableHex(address, 2, true);
        dialog.setHeaderText("Set new value for register 0x" + register);
        dialog.setContentText("Please enter new hex value:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                int newValue = Integer.parseInt(result.get(),16);
                _presenter.setRegister(address, newValue);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Number");
                alert.setHeaderText("Invlaid Number");
                alert.setContentText("\""+ result.get() +"\" is not a valid hex number!");
                alert.showAndWait();
            }
            
        }
    }
    
    private Optional<Integer> showSetNewValueDialog() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Set new value");
        dialog.setHeaderText("Set new value");
        dialog.setContentText("Please enter new hex value:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                int newValue = Integer.parseInt(result.get(),16);
                return Optional.of(newValue);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Number");
                alert.setHeaderText("Invlaid Number");
                alert.setContentText("\""+ result.get() +"\" is not a valid hex number!");
                alert.showAndWait();
            }
        }
        return Optional.empty();
    }

    @Override
    public void displaySTATUSRegister(int value) {
        lb_statusRegHexValue.setText(BinaryNumberHelper.formatToDisplayableHex(value, 2, true));
        lb_statusRegDecValue.setText(Integer.toString(value));
        Integer b0 = (value & 1);
        Integer b1 = (value & 2) >> 1;
        Integer b2 = (value & 4) >> 2;
        Integer b3 = (value & 8) >> 3;
        Integer b4 = (value & 16) >> 4;
        Integer b5 = (value & 32) >> 5;
        Integer b6 = (value & 64) >> 6;
        Integer b7 = (value & 128) >> 7;
        lb_statusRegBit0.setText(b0.toString());
        lb_statusRegBit1.setText(b1.toString());
        lb_statusRegBit2.setText(b2.toString());
        lb_statusRegBit3.setText(b3.toString());
        lb_statusRegBit4.setText(b4.toString());
        lb_statusRegBit5.setText(b5.toString());
        lb_statusRegBit6.setText(b6.toString());
        lb_statusRegBit7.setText(b7.toString());
    }

    @Override
    public void displayRunningTime(double microSeconds) {
        //jusst show the first three fractional digits
        microSeconds = Math.round(microSeconds * 1000);
        microSeconds = microSeconds / 1000;
        lb_runningTime.setText(Double.toString(microSeconds));
    }

    @Override
    public void displayWRegister(int value) {
        lb_wRegHexValue.setText(BinaryNumberHelper.formatToDisplayableHex(value, 2, true));
    }

    @Override
    public void displayFSRRegister(int value) {
        lb_fsrRegHexValue.setText(BinaryNumberHelper.formatToDisplayableHex(value, 2, true));
    }

    @Override
    public void displayPCRegsiter(int value) {
        lb_pcRegHexValue.setText(BinaryNumberHelper.formatToDisplayableHex(value, 3, true));
    }

    @Override
    public void displayPCLRegister(int value) {
        lb_pclRegHexValue.setText(BinaryNumberHelper.formatToDisplayableHex(value, 2, true));
    }

    @Override
    public void displayPCLATHRegsiter(int value) {
        lb_pclathRegHexValue.setText(BinaryNumberHelper.formatToDisplayableHex(value, 2, true));
    }

    @Override
    public void displayOPTIONRegister(int value) {
        lb_optionRegHexValue.setText(BinaryNumberHelper.formatToDisplayableHex(value, 2, true));
        lb_optionRegDecValue.setText(Integer.toString(value));
        Integer b0 = (value & 1);
        Integer b1 = (value & 2) >> 1;
        Integer b2 = (value & 4) >> 2;
        Integer b3 = (value & 8) >> 3;
        Integer b4 = (value & 16) >> 4;
        Integer b5 = (value & 32) >> 5;
        Integer b6 = (value & 64) >> 6;
        Integer b7 = (value & 128) >> 7;
        lb_optionRegBit0.setText(b0.toString());
        lb_optionRegBit1.setText(b1.toString());
        lb_optionRegBit2.setText(b2.toString());
        lb_optionRegBit3.setText(b3.toString());
        lb_optionRegBit4.setText(b4.toString());
        lb_optionRegBit5.setText(b5.toString());
        lb_optionRegBit6.setText(b6.toString());
        lb_optionRegBit7.setText(b7.toString());
    }

    @Override
    public void displayINTCONRegister(int value) {
        lb_intconRegHexValue.setText(BinaryNumberHelper.formatToDisplayableHex(value, 2, true));
        lb_intconRegDecValue.setText(Integer.toString(value));
        Integer b0 = (value & 1);
        Integer b1 = (value & 2) >> 1;
        Integer b2 = (value & 4) >> 2;
        Integer b3 = (value & 8) >> 3;
        Integer b4 = (value & 16) >> 4;
        Integer b5 = (value & 32) >> 5;
        Integer b6 = (value & 64) >> 6;
        Integer b7 = (value & 128) >> 7;
        lb_intconRegBit0.setText(b0.toString());
        lb_intconRegBit1.setText(b1.toString());
        lb_intconRegBit2.setText(b2.toString());
        lb_intconRegBit3.setText(b3.toString());
        lb_intconRegBit4.setText(b4.toString());
        lb_intconRegBit5.setText(b5.toString());
        lb_intconRegBit6.setText(b6.toString());
        lb_intconRegBit7.setText(b7.toString());
    }
    
    @Override
    public void addCodeLine(Integer address, Integer instruction, String sourceCode, boolean breakpointSetable) {
        String addressStr;
        String instructionStr;
        if (address == null) {
            addressStr = "    ";
        } else {
            addressStr = String.format("%04X", address);
        }
        if (instruction == null) {
            instructionStr = "    ";
        } else {
            instructionStr = String.format("%04X", instruction);
        }
        String codeline = addressStr + "  " + instructionStr + "    " + sourceCode;
        
        CheckBox checkbox = null;
        if (breakpointSetable) {
            int id = address; //determine id by program address
            checkbox = new CheckBox();
            checkbox.setId(Integer.toString(id));
            checkbox.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                CheckBox cb = (CheckBox)event.getSource();
                breakpointCheckbox_onClicked(cb);
            });
            checkbox.setSelected(false);
        }
        SourceCodeLineContainer newItem = new SourceCodeLineContainer(codeline, checkbox);
        _sourceCode.add(newItem);
    }
    
    private void breakpointCheckbox_onClicked(CheckBox checkbox) {
        //retrive breakpoint address by checkbox id
        Integer address = Integer.parseInt(checkbox.getId());
        if (checkbox.selectedProperty().get()) {
            _presenter.addBreakpoint(address);
        } else {
            _presenter.removeBreakpoint(address);
        }
    }

    @Override
    public void displayExecutedCodeLine(int line) {
        tv_sourceCode.getSelectionModel().select(line-1);
    }

    @Override
    public void initializeView() {
        //initialize lists and tables views
        initializeCodeView();
        initializePortMapView();
        initializeRegisterMapView();
        initializeStackView();
        initializeEEPROMView();
    }

    @Override
    public void displayAutomaticSteppingMode(boolean b) {
        bt_automaticSteppingMode.selectedProperty().set(b);
    }

    @Override
    public void displayAutomaticSteppingIntervall(int ms) {
        lb_automaticSteppingInterval.setText(Integer.toString(ms));
    }

    @Override
    public void displayOscillatorFrequency(double megaHz) {
        lb_oscillatorFrequency.setText(Double.toString(megaHz));
    }

    @Override
    public void displayRegister(int register, int value) {
        int row = register/16;
        int col = register%16;
        int nthChild = (row+1)*17+col+1;
        
        ObservableList<Node> nodes = gp_register_view.getChildren();
        Label lb = (Label)nodes.get(nthChild);
        lb.setText(BinaryNumberHelper.formatToDisplayableHex(value, 2, true));
    }

    @Override
    public void displayStack(List<Integer> stack) {
        //clear stack view
        initializeStackView();
        //display values in list
        Collections.reverse(stack);
        for (Integer stackValue : stack) {
            _stackRecords.add(1, new StackRecord(stackValue.toString()));
        }
    }

    @Override
    public void pushStack(int value, boolean isOverflow) {
        if (isOverflow) {
            //when overflow occurs, last stack value is being dropped and
            //the new value added
            _stackRecords.remove(_stackRecords.size()-2);
        }
        _stackRecords.add(1, new StackRecord(BinaryNumberHelper.formatToDisplayableHex(value, 2, true)));
    }
    
    @Override
    public void popStack(int value, boolean isUnderflow) {
        if (isUnderflow) {
            //stack underflow is currently not displayed in view
        } else {
            _stackRecords.remove(1);
        } 
    }

    @Override
    public void displayPortLatchBit(String port, int bit, boolean value) {
        Label l;
        switch (port) {
            case "A":
                l = _portALatchLabels.get(bit);
                break;
            case "B":
                l = _portBLatchLabels.get(bit);
                break;
            default:
                return;
        }
        if (l == null) return;
        String text = "0";
        if (value == true) text = "1";
        l.setText(text);
    }

    @Override
    public void displayPortTrisBit(String port, int bit, boolean value) {
        Label l;
        switch (port) {
            case "A":
                l = _portATrisLabels.get(bit);
                break;
            case "B":
                l = _portBTrisLabels.get(bit);
                break;
            default:
                return;
        }
        if (l == null) return;
        String text = "o";
        if (value == true) text = "i";
        l.setText(text);
    }

    @Override
    public void displayPortInOutBit(String port, int bit, boolean value) {
        Label l;
        switch (port) {
            case "A":
                l = _portAInOutLabels.get(bit);
                break;
            case "B":
                l = _portBInOutLabels.get(bit);
                break;
            default:
                return;
        }
        if (l == null) return;
        String text = "0";
        if (value == true) text = "1";
        l.setText(text);
    }

    @Override
    public void displayPortEnvironmentBit(String port, int bit, boolean value) {
        Label l;
        switch (port) {
            case "A":
                l = _portAEnvLabels.get(bit);
                break;
            case "B":
                l = _portBEnvLabels.get(bit);
                break;
            default:
                return;
        }
        if (l == null) return;
        String text = "0";
        if (value == true) text = "1";
        l.setText(text);
    }

    @Override
    public void displaySynchronizePortsWithRS232(boolean b) {
        cb_synchronizePorts.setSelected(b);
    }
    @Override
    public void displayEEPROM(int address, int value) {
        Label update = (Label) _eepromLabels.get(address);
        update.setText(BinaryNumberHelper.formatToDisplayableHex(value, 2, true));
    }
    
}
