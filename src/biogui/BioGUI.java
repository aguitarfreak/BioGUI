/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biogui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 *
 * @author ahwan
 */
public class BioGUI extends Application {
    
    GuiHelper guiHelper;
    StartupScene startup;
    AnalysisScene analysis;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("BioGUI");
        primaryStage.setResizable(false);
        //create the classfile with gui helper methods
        guiHelper = new GuiHelper();
        System.out.println(guiHelper.sharedInfo.getOSName());
        //create the startup scene class
        startup = new StartupScene(guiHelper,primaryStage);
        primaryStage.setScene(startup.startupScene);
                //analysis = new AnalysisScene(guiHelper,primaryStage);
                //primaryStage.setScene(analysis.analysisScene);
        
        startup.buttonNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                guiHelper.chooseWorkingDirectory(primaryStage);
                if(guiHelper.sharedInfo.getWorkingDirectory()==null)
                    startup.status.setText("Directory not chosen!");
                else{
                    startup.status.setText("Directory Chosen!");
                    analysis = new AnalysisScene(guiHelper,primaryStage);
                    primaryStage.setScene(analysis.analysisScene);
                    primaryStage.setTitle("BioGUI: "+guiHelper.sharedInfo.getWorkingDirectory().getAbsolutePath());
                }
            }
        });
        
        
        primaryStage.show();
    }
}
