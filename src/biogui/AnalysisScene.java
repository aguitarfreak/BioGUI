package biogui;

import MemoryView.MemoryPieChart;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The class that handles the Analaysis scene along with all the components 
 * (accordion, title, resources etc..)
 * @author ahwan
 */
public class AnalysisScene {
    
    Scene analysisScene;
    private GuiHelper gh;
    private Stage pri;
    
    Accordion analysisAccordion;
    Label titleAnalysisLbl;
    ProgressIndicator pin;
    HBox tophbox;
    
    private MemoryPieChart mpc;
    private StackPane memoryPieChartStack; 
    
    //--------PANES---------//
    FilePane filePane;
    FilterPane filterPane;
    
     /**
     * The constructor takes an instance of the GuiHelper class and the primary stage.
     * @param guiHelper An instance of the GuIHelper class
     * @param primary The primary stage
     */
    public AnalysisScene(GuiHelper guiHelper, Stage primary){
        pri = primary;
        gh = guiHelper;
        
        BorderPane layout = new BorderPane();
        
        //------------TITLE AREA-------------------------//
        
        StackPane topStack = new StackPane();
        topStack.setMaxSize(gh.sharedInfo.getGuiWidth(), .1*gh.sharedInfo.getGuiHeight());
        topStack.setStyle("-fx-background-color: steelblue");
        titleAnalysisLbl = new Label(gh.sharedInfo.getAnalysisName());
        titleAnalysisLbl.setStyle("-fx-text-fill: white");
        titleAnalysisLbl.setFont(new Font("Arial", 30));
        pin = new ProgressIndicator();
        pin.setMinSize(30, 30);
        pin.setMaxSize(30, 30);
        HBox titleHbox = new HBox();
        titleHbox.getChildren().addAll(titleAnalysisLbl,pin);
        titleHbox.setAlignment(Pos.CENTER);
        mpc = new MemoryPieChart();
        memoryPieChartStack = mpc.getChart();
        topStack.getChildren().addAll(titleHbox,memoryPieChartStack);
        topStack.setAlignment(Pos.CENTER_RIGHT);
        
        //-------------RIGHT AREA----------------------//
        /*VBox rightVbox = new VBox();
        rightVbox.setPrefSize(.2*gh.sharedInfo.getGuiWidth(), .9*gh.sharedInfo.getGuiHeight());
        rightVbox.setStyle("-fx-background-color: powderblue");*/
        
        
        //--------------ACCORDION AREA----------------//
        analysisAccordion = new Accordion();
        analysisAccordion.setPrefSize(gh.sharedInfo.getGuiWidth(), .9*gh.sharedInfo.getGuiHeight());
        filePane = new FilePane(gh);
        filterPane = new FilterPane(gh);
        analysisAccordion.getPanes().addAll(filePane.getfilePane(),filterPane.getfilterPane());
        analysisAccordion.setExpandedPane(filePane.getfilePane());
        
        
        //-------------ADD TO THE LAYOUT------------//
        //layout.setTop(topGroup);
        layout.setTop(topStack);
        //layout.setRight(rightVbox);
        layout.setLeft(analysisAccordion);
        
         //------------ACTION EVENTS--------------------------//
        //FILEPANE
        filePane.setNameOfAnalysisBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(!filePane.nameOfAnalysisTf.getText().isEmpty()){
                    gh.sharedInfo.setAnalysisName(filePane.nameOfAnalysisTf.getText());
                    titleAnalysisLbl.setText(gh.sharedInfo.getAnalysisName());
                }else{
                    titleAnalysisLbl.setText(gh.sharedInfo.getAnalysisName());
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
        
        final Service runPlinkFlags = new Service() {
            @Override protected Task createTask() {
                return new Task() {
                    @Override 
                    protected Object call() throws InterruptedException {
                        gh.sharedInfo.setPlinkRunning(true);
                        
                        LinkedHashMap<String,Object> dataFlags = new LinkedHashMap<>();
                        
                        dataFlags.put(gh.getFileTypeFlag(),gh.sharedInfo.plinkDataExtensionStripped());
                        for(CheckBox c : filePane.cbs)
                            if(c.isSelected())
                                dataFlags.put(" "+c.getText(),"");
                        
                        dataFlags.put(" --out ",gh.createDirectory("1_read_file"));
                        
                        gh.sharedInfo.setPlinkRunning(true);
                        gh.runPLINKcmd(dataFlags,"readfile");
                        gh.sharedInfo.setFileInputAnalysisDone(true);
                        gh.sharedInfo.setPlinkRunning(false);
                        
                        return true;
                    }
                };
            }
        };
        
        
        
        filePane.readFileBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                if (!runPlinkFlags.isRunning()) {
                    runPlinkFlags.restart();
                    //runPlinkFlags.start();
                }
            }
        });
        
        filePane.readFileBtn.disableProperty().bind(runPlinkFlags.runningProperty());
        filePane.loadFileBtn.disableProperty().bind(runPlinkFlags.runningProperty());
        pin.visibleProperty().bind(gh.sharedInfo.getPlinkRunning());
        
           /*
        filePane.readFileBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
            
                
            }
        });
        */
        
        //---------------------------------------------------//
        //ON MAIN WINDOW CLOSE
        pri.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event){
                if(runPlinkFlags.isRunning())
                    runPlinkFlags.cancel();
                mpc.timer.cancel();
            }
        });
        
        
        analysisScene = new Scene(layout, gh.sharedInfo.getGuiWidth()
                                                    ,gh.sharedInfo.getGuiHeight());
        //analysisScene.getStylesheets().add("file:"+gh.sharedInfo.getStyleFile().getPath());
        
    }
}