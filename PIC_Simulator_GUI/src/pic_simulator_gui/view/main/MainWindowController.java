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
    private TextField tf_automaticSteppingIntervall;
    @FXML
    private TextField tf_oscillatorFrequency;
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
    private Label lb_statusBar;
    @FXML
    private ListView lv_sideBar;
    @FXML
    private ListView lv_sourceCode;
    @FXML
    private TableView tv_portMap;
    @FXML
    private TableView tv_stack;
    @FXML
    private GridPane gp_register_view_container;

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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registerListeners();
    }

    public void initializeData(Stage primaryStage, MainWindowPresenter presenter) {
        _primaryStage = primaryStage;
        _presenter = presenter;
        displayStatusMessage(MessageType.CONFIRMATION, "PIC simulator successfully started");
    }

    private void registerListeners() {
        tf_automaticSteppingIntervall.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == false) {
                //textbox has lost focus, apply changes now
                tf_automaticSteppingIntervall_onChanged();
            }
        });

        tf_oscillatorFrequency.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == false) {
                //textbox has lost focus, apply changes now
                tf_oscillatorFrequency_onChanged();
            }
        });
    }

    private void initializeCodeView() {
        _sideBarAnnotations = FXCollections.observableArrayList();
        _sourceCode = FXCollections.observableArrayList();
        lv_sideBar.setItems(_sideBarAnnotations);
        lv_sourceCode.setItems(_sourceCode);
    }
    
    private void initializePortMapView() {
        //initialize port map table view
        _portMapColumns = new ArrayList<>(); //init,reset
        _portMapColumns.add(new TableColumn<>());
        _portMapColumns.add(new TableColumn<>("7"));
        _portMapColumns.add(new TableColumn<>("6"));
        _portMapColumns.add(new TableColumn<>("5"));
        _portMapColumns.add(new TableColumn<>("4"));
        _portMapColumns.add(new TableColumn<>("3"));
        _portMapColumns.add(new TableColumn<>("2"));
        _portMapColumns.add(new TableColumn<>("1"));
        _portMapColumns.add(new TableColumn<>("0"));
        _portMapColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("description"));
        _portMapColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("bit7"));
        _portMapColumns.get(2).setCellValueFactory(new PropertyValueFactory<>("bit6"));
        _portMapColumns.get(3).setCellValueFactory(new PropertyValueFactory<>("bit5"));
        _portMapColumns.get(4).setCellValueFactory(new PropertyValueFactory<>("bit4"));
        _portMapColumns.get(5).setCellValueFactory(new PropertyValueFactory<>("bit3"));
        _portMapColumns.get(6).setCellValueFactory(new PropertyValueFactory<>("bit2"));
        _portMapColumns.get(7).setCellValueFactory(new PropertyValueFactory<>("bit1"));
        _portMapColumns.get(8).setCellValueFactory(new PropertyValueFactory<>("bit0"));
        tv_portMap.getColumns().clear(); //init,reset
        tv_portMap.getColumns().addAll(_portMapColumns);
        _portMapRecords = FXCollections.observableArrayList(); //init,reset
        tv_portMap.setItems(_portMapRecords);
        //set style
        for (TableColumn column : _portMapColumns) {
            column.setMinWidth(20);
            column.setMaxWidth(20);
        }
        _portMapColumns.get(0).setMinWidth(50);
        _portMapColumns.get(0).setMaxWidth(50);
        _portMapRecords.add(new PortMapRecord("RA Tris", "0", "0", "0", "0", "1", "1", "1", "1"));
    }
    
    private void initializeRegisterMapView() {
        //reinitialize the grid panel
        gp_register_view_container.getChildren().clear();
        //genrate labels inside the grid
        for (int row = 0; row < 17; row++) {
            for (int col = 0; col < 17; col++) {
                if (col == 0 && row == 0) {
                    continue;
                }
                Label lb = new Label();
                lb.setMinHeight(19);
                lb.setMaxWidth(19);
                lb.setMinHeight(19);
                lb.setMaxHeight(19);
                lb.setStyle("-fx-background-color: white;");
                //fill upper table head and left table head
                if (row == 0 && col > 0) {
                    lb.setText(BinaryNumberHelper.formatToDisplayableHex(col-1, 1, false));
                    lb.setStyle("-fx-font-weight: bold;");
                    gp_register_view_container.add(lb, col, row);
                    continue;
                } else if (col == 0 && row > 0) {
                    lb.setText(BinaryNumberHelper.formatToDisplayableHex(row-1, 1, true));
                    lb.setStyle("-fx-font-weight: bold;");
                    gp_register_view_container.add(lb, col, row);
                    continue;
                }
                lb.setText(BinaryNumberHelper.formatToDisplayableHex(0, 2, true));
                //use id to get the address which the label displays
                lb.setId(Integer.toString((row-1)*16+col-1));
                //add onclick handler to change value of the register
                lb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                    Label label = (Label)event.getSource();
                    int address = Integer.parseInt(label.getId());
                    setRegisterValueDialog(address);
                });
                gp_register_view_container.add(lb, col, row);
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
        String newValue = tf_automaticSteppingIntervall.getText();
        int value;
        try {
            value = Integer.parseInt(newValue);
        } catch (NumberFormatException e) {
            //don't respect the value in simulation if it is invalid and just
            //ignore it
            displayStatusMessage(MessageType.ERROR, "\"" + newValue + "\" is not a valid number");
            return;
        }
        _presenter.setAutomaticSteppingInterval(value);
    }

    private void tf_oscillatorFrequency_onChanged() {
        String newValue = tf_oscillatorFrequency.getText();
        double value;
        try {
            value = Double.parseDouble(newValue);
        } catch (NumberFormatException e) {
            //don't respect the value in simulation if it is invalid and just
            //ignore it
            displayStatusMessage(MessageType.ERROR, "\"" + newValue + "\" is not a valid number");
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
    
    @Override
    public void displayStatusMessage(MessageType level, String message) {
        lb_statusBar.setText(message);
        String bgColor;
        switch (level) {
            case CONFIRMATION:
                bgColor = MessageColor.COLOR_CONFIRMATION;
                break;
            case ERROR:
                bgColor = MessageColor.COLOR_ERROR;
                break;
            case WARNING:
                bgColor = MessageColor.COLOR_WARNING;
                break;
            case NOTIFICATION:
                bgColor = MessageColor.COLOR_NOTIFICATION;
                break;
            case INFO:
            default:
                bgColor = MessageColor.COLOR_INFO;
                break;
        }
        lb_statusBar.setStyle("-fx-background-color: " + bgColor);
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
    public void displayRunningTime(int microSeconds) {
        lb_runningTime.setText(Integer.toString(microSeconds));
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
        tf_automaticSteppingIntervall.setText(Integer.toString(ms));
    }

    @Override
    public void displayOscillatorFrequency(double megaHz) {
        tf_oscillatorFrequency.setText(Double.toString(megaHz));
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
        int nthChild = (row+1)*17+col;
        
        ObservableList<Node> nodes = gp_register_view_container.getChildren();
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

    private void setRegisterValueDialog(int address) {
        System.out.println(address);
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Set register value");
        String register = BinaryNumberHelper.formatToDisplayableHex(address, 2, true);
        dialog.setHeaderText("Set new value of register 0x" + register);
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
    
}
