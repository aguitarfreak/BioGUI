package biogui;

import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This class sets up the graphical elements of the Read File section of the accordion
 * component
 * @see AnalysisScene
 * @author Ahwan Pandey
 */
public class FilePane{
    
    private TitledPane filePane;
    private TabPane fileTabPanes;
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
    /**
     * The default constructor
     * @param gh GuiHelper instance
     */
    public FilePane(GuiHelper gh){
        guiHelp = gh;

        //--------------ANALYSIS INFO TAB---------------------//
        
        Label nameAnalysisLbl = new Label("Name your Analysis: ");
        nameOfAnalysisTf = new TextField();
        nameOfAnalysisTf.requestFocus();
        nameOfAnalysisTf.setPromptText("Unnamed Analysis");
        //nameOfAnalysisTf.setMaxSize(300, 20);
        setNameOfAnalysisBtn = new Button("Set Name");
        setNameOfAnalysisBtn.setMaxWidth(Double.MAX_VALUE);
        Separator separator = new Separator();
        separator.setHalignment(HPos.CENTER);
        Label analysisDescriptionLbl = new Label("Describe your data: ");
        descriptionOfAnalysisTa = new TextArea();
        descriptionOfAnalysisTa.setWrapText(true);
        descriptionOfAnalysisTa.setPrefRowCount(10);
        //descriptionOfAnalysisTa.setMaxWidth(300);
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
        infoRoot.getChildren().add(infoGrid);
        Tab analysisNameTab = new Tab("Analysis Info");
        analysisNameTab.setContent(infoRoot);
        
        //-------------ANALYSIS LOAD FILE---------------------//
        //SELECT FILE SECTION
        loadFileTf = new TextField();
        loadFileTf.requestFocus();
        loadFileTf.setPromptText("Load Data File");
        loadFileTf.setStyle("-fx-background-color: paleturquoise");
        loadFileTf.setEditable(false);
        //loadFileTf.setMaxWidth(300);
        loadFileBtn = new Button("..."); 
        readFileBtn = new Button("Read File"); 
        
        //DATA FLAGS SECTION
        String[] dataFlagsCbx = guiHelp.sharedInfo.getDataFlagsForCheckBox();
        cbs = new CheckBox[dataFlagsCbx.length];
        Label dataFlagsLbl = new Label("Data Flags: ");
        dataFlagsLbl.underlineProperty().set(true);
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
        
        //VBox flagChecks = new VBox();
        //flagChecks.getChildren().addAll(cbs);
        TitledPane flags = new TitledPane();
        flags.setText("Data Flags");
        flags.setContent(cbxGridPane);
        
        //DATA INFO SECTION
        //----
        GridPane dataInfoGrid = new GridPane();
        dataInfoGrid.visibleProperty().bind(gh.sharedInfo.getfileInputComplete());
        ColumnConstraints col1 = new ColumnConstraints(130); //col1.setHalignment(HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints(80); //col2.setHalignment(HPos.LEFT);
        ColumnConstraints col3 = new ColumnConstraints(70); //col3.setHalignment(HPos.RIGHT);
        ColumnConstraints col4 = new ColumnConstraints(80); //col4.setHalignment(HPos.LEFT);
        ColumnConstraints col5 = new ColumnConstraints(70); //col5.setHalignment(HPos.RIGHT);
        ColumnConstraints col6 = new ColumnConstraints(80); //col6.setHalignment(HPos.LEFT);
        dataInfoGrid.getColumnConstraints().addAll(col1,col2,col3,col4,col5,col6);
        dataInfoGrid.setPadding(new Insets(10,10,10,10));
        dataInfoGrid.setVgap(10);
        //dataInfoGrid.setGridLinesVisible(true);
        //----
        Label numMarkersLbl = new Label("Number of Markers: "); dataInfoGrid.add(numMarkersLbl, 0, 0);
        Text numMarkersTxt = new Text();  dataInfoGrid.add(numMarkersTxt, 1, 0);
        numMarkersTxt.textProperty().bind(gh.dataProperties.getNumMarkers().asString());
        Label numIndividualsLbl = new Label("Number of Individuals: "); dataInfoGrid.add(numIndividualsLbl, 0, 1);
        Text numIndividualsTxt = new Text();  dataInfoGrid.add(numIndividualsTxt, 1, 1);
        numIndividualsTxt.textProperty().bind(gh.dataProperties.getNumIndividuals().asString());
        Label numCasesLbl = new Label("Number of Cases: "); dataInfoGrid.add(numCasesLbl, 0, 2);
        Text numCasesTxt = new Text(); dataInfoGrid.add(numCasesTxt, 1, 2);
        numCasesTxt.textProperty().bind(gh.dataProperties.getNumCases().asString());
        Label numControlsLbl = new Label("Controls: "); dataInfoGrid.add(numControlsLbl, 2, 2);
        Text numControlsTxt = new Text(); dataInfoGrid.add(numControlsTxt, 3, 2);
        numControlsTxt.textProperty().bind(gh.dataProperties.getNumControls().asString());
        Label numMissingLbl = new Label("Missing: "); dataInfoGrid.add(numMissingLbl, 4, 2);
        Text numMissingTxt = new Text(); dataInfoGrid.add(numMissingTxt, 5, 2);
        numMissingTxt.textProperty().bind(gh.dataProperties.getNumMissing().asString());
        Label numMalesLbl = new Label("Number of Males: "); dataInfoGrid.add(numMalesLbl, 0, 3);
        Text numMalesTxt = new Text(); dataInfoGrid.add(numMalesTxt, 1, 3);
        numMalesTxt.textProperty().bind(gh.dataProperties.getNumMales().asString());
        Label numFemalesLbl = new Label("Females: "); dataInfoGrid.add(numFemalesLbl, 2, 3);
        Text numFemalesTxt = new Text(); dataInfoGrid.add(numFemalesTxt, 3, 3);
        numFemalesTxt.textProperty().bind(gh.dataProperties.getNumFemales().asString());
        Label numUnspecifiedSexLbl = new Label("Unspecified: "); dataInfoGrid.add(numUnspecifiedSexLbl, 4, 3);
        Text numUnspecifiedSexTxt = new Text(); dataInfoGrid.add(numUnspecifiedSexTxt, 5, 3);
        numUnspecifiedSexTxt.textProperty().bind(gh.dataProperties.getNumUnspecifiedSex().asString());
        //------
        
        loadFileGrid = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints(300,20,Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        loadFileGrid.getColumnConstraints().addAll(column1);
        loadFileGrid.setPadding(new Insets(10,10,10,10));
        loadFileGrid.setHgap(10);
        loadFileGrid.setVgap(10);
        loadFileGrid.add(loadFileTf,0,0);
        loadFileGrid.add(loadFileBtn,2,0);
        //loadFileGrid.add(dataFlagsLbl,0,1);
        //loadFileGrid.add(cbxGridPane,0,2);
        loadFileGrid.add(flags,0,1);
        
        BorderPane loadFileLayout = new BorderPane();
        loadFileLayout.setTop(loadFileGrid);
        loadFileLayout.setCenter(dataInfoGrid);
        
        
        filePane = new TitledPane();
        filePane.setText("File Options");
        
        fileTabPanes = new TabPane();
        fileTabPanes.setSide(Side.LEFT);
        fileTabPanes.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab loadFileTab = new Tab("Load File");
        loadFileTab.setContent(loadFileLayout);
        
        fileTabPanes.getTabs().addAll(analysisNameTab,loadFileTab);
        filePane.setContent(fileTabPanes);
    }
    
    public TitledPane getfilePane(){
        return filePane;
    }
}
