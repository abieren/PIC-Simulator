/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator_gui;

import pic_simulator_gui.presenter.MainWindowPresenterImpl;
import pic_simulator_gui.view.main.MainWindowController;
import java.awt.SplashScreen;
import pic_simulator.interfaces.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pic_simulator.model.Simulator;

/**
 *
 * @author Alex
 */
public class MC_Sim_GUI_Application extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        SplashScreen splashScreen = null;
        try {
            splashScreen = SplashScreen.getSplashScreen();
        } catch (Exception e) {
        }
//        if (splashScreen != null) {
//            //pause for displaying the splash screen
//            Thread.sleep(1000);
//        }
        
        //Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main/MainWindow.fxml"));
        Parent root = loader.load();
        
        //instanciate presenter
        MainWindowPresenterImpl presenter = new MainWindowPresenterImpl();        
        //instanciate model and reference presenter
        Model simulator = new Simulator(presenter);
        //initialize controller data and reference presenter and primary stage
        MainWindowController controller = loader.<MainWindowController>getController();
        controller.initializeData(primaryStage, presenter);
        //set references for the presenter
        presenter.setModel(simulator);
        presenter.setView(controller);
        //initialize simulator
        simulator.initialize();
        
        //show in stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PIC SIMULATOR");
        primaryStage.show();
        
        if (splashScreen != null) {
            splashScreen.close();
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
