/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui.view.main;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.control.TextField;
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
    private CheckBox cb_breakOnWatchdogTrigger;
    @FXML
    private CheckBox cb_breakOnInterrupt;
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
    private ListView lv_sideBar;
    @FXML
    private ListView lv_sourceCode;
    @FXML
    private TableView tv_stack;
    @FXML
    private GridPane gp_port_view;
    @FXML
    private GridPane gp_register_view;

    //Stage that is used by the view of this controller
    private Stage _primaryStage;
    //presenter of the view managed by this controller
    private MainWindowPresenter _presenter;
    //lists to add the code lines and breakpoints for the list views
    ObservableList<String> _sideBarAnnotations;
    ObservableList<String> _sourceCode = FXCollections.observableArrayList();
    //table columns of the port map
    List<TableColumn> _portMapColumns;
    //records of the port map
    ObservableList<PortMapRecord> _portMapRecords;
    //records of the stack view
    ObservableList<StackRecord> _stackRecords;
    //port values
    int _portALatch;
    int _portATris;
    int _portAEnv;
    int _portAInOut;
    int _portBLatch;
    int _portBTris;
    int _portBEnv;
    int _portBInOut;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initializeData(Stage primaryStage, MainWindowPresenter presenter) {
        _primaryStage = primaryStage;
        _presenter = presenter;
    }

    private void initializeCodeView() {
        _sideBarAnnotations = FXCollections.observableArrayList();
        _sourceCode = FXCollections.observableArrayList();
        lv_sideBar.setItems(_sideBarAnnotations);
        lv_sourceCode.setItems(_sourceCode);
    }
    
    private void initializePortMapView() {
        //reinitialize the grid panel
        gp_register_view.getChildren().clear();
        //genrate labels inside the grid
        Label lb;
        String style = "-fx-background-color: white; -fx-alignment: center;";
        
        //fill port A headline
        int row = 0;
        for (int i = 0; i < 8; i++) {
            lb = new Label();
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+7-i,row);
        }
        //fill port A latch
        row++;
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+7-i,row);
            lb.setId(Integer.toString(7-i));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                int oldBitValue = BinaryNumberHelper.getBit(_portALatch, bit);
                _portALatch = BinaryNumberHelper.setBit(_portALatch, bit, ~oldBitValue);
                _presenter.setPortLatch("A", _portALatch);
            });
        }
        //fill port A tris
        row++;
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+7-i,row);
            lb.setId(Integer.toString(7-i));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                int oldBitValue = BinaryNumberHelper.getBit(_portATris, bit);
                _portATris = BinaryNumberHelper.setBit(_portATris, bit, ~oldBitValue);
                _presenter.setPortTris("A", _portATris);
            });
        }
        //fill port A environment
        row++;
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+7-i,row);
            lb.setId(Integer.toString(7-i));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                int oldBitValue = BinaryNumberHelper.getBit(_portAEnv, bit);
                _portAEnv = BinaryNumberHelper.setBit(_portAEnv, bit, ~oldBitValue);
                _presenter.setPortEnv("A", _portAEnv);
            });
        }
        //fill port A in/out
        row++;
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+i,row);
        }
        //fill port B headline
        row++;
        for (int i = 0; i < 8; i++) {
            lb = new Label();
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+7-i,row);
        }
        //fill port B latch
        row++;
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+7-i,row);
            lb.setId(Integer.toString(7-i));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                int oldBitValue = BinaryNumberHelper.getBit(_portBLatch, bit);
                _portBLatch = BinaryNumberHelper.setBit(_portBLatch, bit, ~oldBitValue);
                _presenter.setPortLatch("B", _portBLatch);
            });
        }
        //fill port B tris
        row++;
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+7-i,row);
            lb.setId(Integer.toString(7-i));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                int oldBitValue = BinaryNumberHelper.getBit(_portBTris, bit);
                _portBTris = BinaryNumberHelper.setBit(_portBTris, bit, ~oldBitValue);
                _presenter.setPortTris("B", _portBTris);
            });
        }
        row++;
        //fill port B environment
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+7-i,row);
            lb.setId(Integer.toString(7-i));
            lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Label label = (Label)event.getSource();
                int bit = Integer.parseInt(label.getId());
                int oldBitValue = BinaryNumberHelper.getBit(_portBEnv, bit);
                _portBEnv = BinaryNumberHelper.setBit(_portBEnv, bit, ~oldBitValue);
                _presenter.setPortEnv("B", _portBEnv);
            });
        }
        //fill port B in/out
        row++;
        for (int i = 0; i < 8; i++) {
            lb = new Label("0");
            lb.setMinHeight(20);
            lb.setMaxWidth(20);
            lb.setMinHeight(20);
            lb.setMaxHeight(20);
            lb.setStyle(style);
            gp_port_view.add(lb,1+i,row);
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
                    setRegisterValueDialog(address);
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
    private void cb_breakOnWatchdogTrigger_onClicked() {
        boolean value = cb_breakOnWatchdogTrigger.isSelected();
        _presenter.setBreakOnWatchdogTriggered(value);
    }
    
    @FXML
    private void cb_breakOnInterrupt_onClicked() {
        boolean value = cb_breakOnInterrupt.isSelected();
        _presenter.setBreakOnInterrupt(value);
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

    public void placeBreakPointMarker(int line) {
        _sideBarAnnotations.set(line, "X");
    }

    public void removeBreakPointMarker(int line) {
        _sideBarAnnotations.set(line, "");
    }
    
    private void setRegisterValueDialog(int address) {
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
    public void addCodeLine(Integer address, Integer instruction, String sourceCode) {
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
        _sideBarAnnotations.add("");
        _sourceCode.add(addressStr + "  " + instructionStr + "    " + sourceCode);
    }

    @Override
    public void displayExecutedCodeLine(int line) {
        lv_sideBar.getSelectionModel().select(line-1);
        lv_sourceCode.getSelectionModel().select(line-1);
    }

    @Override
    public void initializeView() {
        //initialize lists and tables views
        initializeCodeView();
        initializePortMapView();
        initializeRegisterMapView();
        initializeStackView();
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
    public void displayBreakOnWatchdogTrigger(boolean b) {
        cb_breakOnWatchdogTrigger.selectedProperty().set(b);
    }

    @Override
    public void displayBreakOnInterrupt(boolean b) {
        cb_breakOnInterrupt.selectedProperty().set(b);
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
    public void displayPortOutput(String port, int oldValue, int newValue) {
        Label label;
        switch (port) {
            case "A":
                label = (Label)gp_port_view.getChildren().get(7);
                label.setText(BinaryNumberHelper.formatToDisplayableHex(newValue, 2, true));
                break;
            case "B":
                label = (Label)gp_port_view.getChildren().get(15);
                label.setText(BinaryNumberHelper.formatToDisplayableHex(newValue, 2, true));
                break;
            default:
        }
    }

    @Override
    public void displayPortLatch(String port, int oldValue, int newValue) {
        Label label;
        switch (port) {
            case "A":
                label = (Label)gp_port_view.getChildren().get(3);
                label.setText(BinaryNumberHelper.formatToDisplayableHex(newValue, 2, true));
                break;
            case "B":
                label = (Label)gp_port_view.getChildren().get(11);
                label.setText(BinaryNumberHelper.formatToDisplayableHex(newValue, 2, true));
                break;
            default:
        }
    }

    @Override
    public void displayPortTris(String port, int oldValue, int newValue) {
        Label label;
        switch (port) {
            case "A":
                label = (Label)gp_port_view.getChildren().get(5);
                label.setText(BinaryNumberHelper.formatToDisplayableHex(newValue, 2, true));
                break;
            case "B":
                label = (Label)gp_port_view.getChildren().get(13);
                label.setText(BinaryNumberHelper.formatToDisplayableHex(newValue, 2, true));
                break;
            default:
        }
    }
}
