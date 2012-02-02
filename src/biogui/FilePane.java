/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biogui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author ahwan
 */
public class FilePane{
    
    public TitledPane filePane;
    public TabPane fileTabPanes;
    private GuiHelper guiHelp;
    
    protected TextField nameOfAnalysisTf;
    protected Button setNameOfAnalysisBtn;
    protected TextArea descriptionOfAnalysisTa;
    protected Button updateDescriptionBtn;
    
    protected Button loadFileBtn;
    protected TextField loadFileTf;
    protected Button readFileBtn;
    protected GridPane loadFileGrid;
    
    protected CheckBox[] cbs;
    
    public FilePane(GuiHelper gh){
        guiHelp = gh;
        
        filePane = new TitledPane();
        filePane.setText("File Options");
        
        fileTabPanes = new TabPane();
        fileTabPanes.setSide(Side.LEFT);
        fileTabPanes.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
//        Rectangle r = new Rectangle(gh.sharedInfo.getGuiWidth()-200,
//                                                    gh.sharedInfo.getGuiHeight(),Color.LIGHTSTEELBLUE);
        
        //--------------ANALYSIS INFO TAB---------------------//
        
        Label nameAnalysisLbl = new Label("Name your Analysis: ");
        nameOfAnalysisTf = new TextField();
        nameOfAnalysisTf.requestFocus();
        nameOfAnalysisTf.setPromptText("Unnamed Analysis");
        nameOfAnalysisTf.setMaxSize(300, 20);
        setNameOfAnalysisBtn = new Button("Set Name");
        Separator separator = new Separator();
        separator.setHalignment(HPos.CENTER);
        Label analysisDescriptionLbl = new Label("Describe your data: ");
        descriptionOfAnalysisTa = new TextArea();
        descriptionOfAnalysisTa.setWrapText(true);
        descriptionOfAnalysisTa.setPrefRowCount(10);
        descriptionOfAnalysisTa.setMaxWidth(300);
        updateDescriptionBtn = new Button ("Update Description");
        
        GridPane infoGrid = new GridPane();
        infoGrid.setPadding(new Insets(10,10,10,10));
        infoGrid.setVgap(10);
        infoGrid.setHgap(10);
        infoGrid.add(nameAnalysisLbl,0,0);
        infoGrid.add(nameOfAnalysisTf,1,0);
        infoGrid.add(setNameOfAnalysisBtn,2,0);
        infoGrid.add(separator,0,1,3,1);
        infoGrid.add(analysisDescriptionLbl,0,2);
        infoGrid.add(descriptionOfAnalysisTa,1,2);
        infoGrid.add(updateDescriptionBtn,2,2);
        
        Group infoRoot = new Group();
        //infoRoot.getChildren().add(r);
        infoRoot.getChildren().add(infoGrid);
        Tab analysisNameTab = new Tab("Analysis Info");
        analysisNameTab.setContent(infoRoot);
        
        //-------------ANALYSIS LOAD FILE---------------------//
        
        loadFileTf = new TextField();
        loadFileTf.requestFocus();
        loadFileTf.setPromptText("Load Data File");
        loadFileTf.setStyle("-fx-background-color: paleturquoise");
        loadFileTf.setEditable(false);
        //loadFileTf.setMaxWidth(300);
        loadFileBtn = new Button("...");
        readFileBtn = new Button("Read File");
                
        Label dataFlagsLbl = new Label("Data Flags: ");
        dataFlagsLbl.underlineProperty().set(true);
        String[] dataFlagsCbx = guiHelp.sharedInfo.getDataPropertiesForCheckBox();
        cbs = new CheckBox[dataFlagsCbx.length];
        GridPane cbxGridPane = new GridPane();
        cbxGridPane.setVgap(10);
        cbxGridPane.setHgap(10);
        for(int i = 0; i< dataFlagsCbx.length; i++){
            cbs[i] = new CheckBox(dataFlagsCbx[i]);
            cbs[i].setDisable(true);
        }
        int r = 0;int c = -1;
        for(int i = 0; i< dataFlagsCbx.length; i++){
            if(i%3!=0){
                r++;
            }else{
                r=0;
                c++;
            }
            GridPane.setConstraints(cbs[i], c, r);
        }
        cbxGridPane.getChildren().addAll(cbs);
        
                
        loadFileGrid = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints(300,20,Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        loadFileGrid.getColumnConstraints().addAll(column1);
        loadFileGrid.setPadding(new Insets(10,10,10,10));
        loadFileGrid.setHgap(10);
        loadFileGrid.setVgap(10);
        loadFileGrid.add(loadFileTf,0,0);
        loadFileGrid.add(loadFileBtn,2,0);
        loadFileGrid.add(dataFlagsLbl,0,1);
        loadFileGrid.add(cbxGridPane,0,2);
        
        Tab loadFileTab = new Tab("Load File");
        loadFileTab.setContent(loadFileGrid);
        
        fileTabPanes.getTabs().addAll(analysisNameTab,loadFileTab);
        filePane.setContent(fileTabPanes);
    }
}
