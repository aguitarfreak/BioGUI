/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biogui;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author ahwan
 */
public class AnalysisScene {
    
    Scene analysisScene;
    private GuiHelper gh;
    private Stage pri;
    
    Accordion analysisAccordion;
    
    Text titleAnalysisTxt;
    
//    public Button fileButton;
//    TitledPane filePane;
//    TitledPane filePane2;
//    public Button fileButton2;
    
    //--------PANES---------//
    FilePane filePane;
    
    
    public AnalysisScene(GuiHelper guiHelper, Stage primary){
        pri = primary;
        gh = guiHelper;
        
        BorderPane layout = new BorderPane();
        
        //------------TITLE AREA-------------------------//
        Group topGroup = new Group();
        titleAnalysisTxt = new Text(gh.sharedInfo.getAnalysisName());
        StackPane titleStack = new StackPane();
        titleStack.getChildren().addAll(new Rectangle(gh.sharedInfo.getGuiWidth(), 30, Color.STEELBLUE),
                                        titleAnalysisTxt);
        titleStack.setAlignment(Pos.CENTER);
        topGroup.getChildren().add(titleStack);
        
        //-------------RIGHT AREA----------------------//
        Group rightGroup = new Group();
        rightGroup.getChildren().addAll(new Rectangle(300, gh.sharedInfo.getGuiHeight(), Color.ALICEBLUE));
        
        
        //--------------ACCORDION AREA----------------//
        analysisAccordion = new Accordion();
        filePane = new FilePane(gh);
        analysisAccordion.getPanes().add(filePane.filePane);
        analysisAccordion.setExpandedPane(filePane.filePane);
        
        
        //-------------ADD TO THE LAYOUT------------//
        layout.setTop(topGroup);
        layout.setRight(rightGroup);
        layout.setCenter(analysisAccordion);
        
         //------------ACTION EVENTS--------------------------//
        //FILEPANE
        filePane.setNameOfAnalysisBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(!filePane.nameOfAnalysisTf.getText().isEmpty()){
                    gh.sharedInfo.setAnalysisName(filePane.nameOfAnalysisTf.getText());
                    titleAnalysisTxt.setText(gh.sharedInfo.getAnalysisName());
                }else{
                    titleAnalysisTxt.setText(gh.sharedInfo.getAnalysisName());
                }
            }
        });
        
        filePane.nameOfAnalysisTf.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(event.getCode().ENTER))
                    filePane.setNameOfAnalysisBtn.fire();
            }
        });
        
        filePane.loadFileBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
                List<FileChooser.ExtensionFilter> extensions = new ArrayList<>();
                extensions.add(new FileChooser.ExtensionFilter("PLINK Binary file (*.bed)","*.bed"));
                extensions.add(new FileChooser.ExtensionFilter("PLINK Pedigree file (*.ped)","*.ped"));
                
                gh.chooseDataFile(pri, extensions);
                if(gh.sharedInfo.getDataFile()!=null){
                    filePane.loadFileTf.setText(gh.sharedInfo.getDataFile().getAbsolutePath());
                    if(!filePane.loadFileGrid.getChildren().contains(filePane.readFileBtn))
                        filePane.loadFileGrid.add(filePane.readFileBtn,3,0);
                    for(CheckBox c : filePane.cbs)
                        c.setDisable(false);
                }
            }
        });
        
        filePane.readFileBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                LinkedHashMap<String,Object> dataFlags = new LinkedHashMap<>();
                dataFlags.put(gh.getFileTypeFlag(),gh.sharedInfo.plinkDataExtensionStripped());
                for(CheckBox c : filePane.cbs)
                    if(c.isSelected())
                        dataFlags.put(" "+c.getText(),"");
                dataFlags.put(" --out ",
                              gh.sharedInfo.getWorkingDirectory().toString()
                              +File.separator+gh.sharedInfo.getAnalysisName().replace(" ", "_"));
                filePane.readFileBtn.setDisable(true);
                gh.runPLINKcmd(dataFlags);
                
            }
        });
        
        /*
        filePane.readFileBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
            
                
            }
        });
        */

        //---------------------------------------------------//
        
        
        
        analysisScene = new Scene(layout, gh.sharedInfo.getGuiWidth()
                                                    ,gh.sharedInfo.getGuiHeight());
    }
}