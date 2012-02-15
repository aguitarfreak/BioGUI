package biogui;

import MemoryView.MemoryPieChart;
import java.io.File;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    
    //Booleans for filterPane stages
    Boolean excludeExtract = false;
    Boolean dataDriven = false;
    Boolean ld = false;
    //folders for finterPane stages
    String excludeExtractFldr = "2_extraction_inclusion_filters";
    String dataDrivenFldr = "3_data_driven_filters";
    String ldFldr = "4_ld_prune";
    
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
        pin = new ProgressIndicator(); pin.visibleProperty().bind(gh.sharedInfo.getPlinkRunning());
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
        
        analysisScene = new Scene(layout, gh.sharedInfo.getGuiWidth()
                                  ,gh.sharedInfo.getGuiHeight());
        //analysisScene.getStylesheets().add("file:"+gh.sharedInfo.getStyleFile().getPath());
        
        
         //------------ACTION EVENTS--------------------------//
        //----------------------FILEPANE
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

                //name of analysis button
                filePane.nameOfAnalysisTf.setOnKeyPressed(new EventHandler<KeyEvent>(){
                    @Override
                    public void handle(KeyEvent event) {
                        if(event.getCode().equals(event.getCode().ENTER))
                            filePane.setNameOfAnalysisBtn.fire();
                    }
                });

                //load PLINK data file button
                filePane.loadFileBtn.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {

                        List<FileChooser.ExtensionFilter> extensions = new ArrayList<>();
                        extensions.add(new FileChooser.ExtensionFilter("Binary file (*.bed)","*.bed"));
                        extensions.add(new FileChooser.ExtensionFilter("Pedigree file (*.ped)","*.ped"));

                        gh.chooseFile(pri, extensions, "data");
                        if(gh.sharedInfo.getDataFile()!=null){
                            filePane.loadFileTf.setText(gh.sharedInfo.getDataFile().getAbsolutePath());
                            gh.sharedInfo.setOriginalDataFile(gh.sharedInfo.getDataFile());
                            if(!filePane.loadFileGrid.getChildren().contains(filePane.readFileBtn))
                                filePane.loadFileGrid.add(filePane.readFileBtn,3,0);
                            for(CheckBox c : filePane.cbs)
                                c.setDisable(false);
                        }
                    }
                });

                //run plink on data file to get info.
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


                //button that runs plink 
                filePane.readFileBtn.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event){
                        if (!runPlinkFlags.isRunning()) {
                            runPlinkFlags.restart();
                            //runPlinkFlags.start();
                        }
                    }
                });

                //bindings to the PLINK run
                filePane.readFileBtn.disableProperty().bind(runPlinkFlags.runningProperty());
                filePane.loadFileBtn.disableProperty().bind(runPlinkFlags.runningProperty());

        //-----------------------------------FILTERPANE
                filterPane.excludeSnpBtn.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        List<FileChooser.ExtensionFilter> extensions = new ArrayList<>();
                        extensions.add(new FileChooser.ExtensionFilter("Snplist File (*.txt)","*.txt"));
                        gh.chooseFile(pri, extensions, "snp-list");
                        if(gh.sharedInfo.getExcludeOrIncludeSnpFile()!=null)
                            filterPane.excludeSnpTf.setText(gh.sharedInfo.getExcludeOrIncludeSnpFile().getAbsolutePath());

                    }
                });
                
                filterPane.excludeSubjBtn.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        List<FileChooser.ExtensionFilter> extensions = new ArrayList<>();
                        extensions.add(new FileChooser.ExtensionFilter("Subject-list File (*.txt)","*.txt"));
                        gh.chooseFile(pri, extensions, "subj-list");
                        if(gh.sharedInfo.getExcludeOrIncludeSubjFile()!=null)
                            filterPane.excludeSubjTf.setText(gh.sharedInfo.getExcludeOrIncludeSubjFile().getAbsolutePath());

                    }
                });
        
                final Service runPlinkDataFilters = new Service() {
                    @Override protected Task createTask() {
                        return new Task() {
                            @Override 
                            protected Object call() throws InterruptedException {
                                gh.sharedInfo.setPlinkRunning(true);
                                
                                gh.resetPlinkDataFile(gh.sharedInfo.getOriginalDataFile());
                                
                                LinkedHashMap<String,Object> dataFlags = new LinkedHashMap<>();
                                if(filterPane.exclusionFiltersCbx.isSelected()){
                                    
                                    filterPane.extractPin.progressProperty().unbind();
                                    filterPane.extractPin.setVisible(true);
                                    filterPane.extractPin.setProgress(0);
                                        dataFlags.put(gh.getFileTypeFlag(),gh.sharedInfo.plinkDataExtensionStripped());
                                        for(CheckBox c : filePane.cbs)
                                            if(c.isSelected())
                                                dataFlags.put(" "+c.getText(),"");

                                        if(gh.sharedInfo.getExcludeOrIncludeSnpFile()!=null){
                                            if(filterPane.excludeSnpTBtn.isSelected()){
                                                dataFlags.put(" --exclude ",gh.sharedInfo.getExcludeOrIncludeSnpFile().getAbsolutePath());
                                            }
                                            else if(filterPane.extractSnpTBtn.isSelected()){
                                                dataFlags.put(" --extract ",gh.sharedInfo.getExcludeOrIncludeSnpFile().getAbsolutePath());
                                            }
                                        }
                                        
                                        if(gh.sharedInfo.getExcludeOrIncludeSubjFile()!=null){
                                            if(filterPane.excludeSubjTBtn.isSelected()){
                                                dataFlags.put(" --remove ",gh.sharedInfo.getExcludeOrIncludeSubjFile().getAbsolutePath());
                                            }
                                            else if(filterPane.extractSubjTBtn.isSelected()){
                                                dataFlags.put(" --keep ",gh.sharedInfo.getExcludeOrIncludeSubjFile().getAbsolutePath());
                                            }
                                        }
                                        
                                        dataFlags.put(" --make-bed","");
                                        dataFlags.put(" --out ",gh.createDirectory(excludeExtractFldr));
                                        gh.runPLINKcmd(dataFlags,"extract_or_include");                                     
                                        gh.updatePlinkDataFile(excludeExtractFldr);

                                        
                                        if(filterPane.excludeSnpBtn.isDisabled())
                                            gh.dataProperties.setExcludeOrIncludeSnpCount(0);
                                        if(filterPane.excludeSubjBtn.isDisable())
                                            gh.dataProperties.setExcludeOrIncludeSubjCount(0);
                                    filterPane.extractPin.setProgress(1);
                                }
                                    
                                if(filterPane.dataFiltersCbx.isSelected()){
                                    filterPane.ddPin.progressProperty().unbind();
                                    filterPane.ddPin.setVisible(true);
                                    filterPane.ddPin.setProgress(0);
                                    
                                        dataFlags.clear();
                                        dataFlags.put(gh.getFileTypeFlag(),gh.sharedInfo.plinkDataExtensionStripped());
                                        for(CheckBox c : filePane.cbs)
                                            if(c.isSelected())
                                                dataFlags.put(" "+c.getText(),"");
                                        
                                        if(filterPane.pruneTglBtn.isSelected())
                                            dataFlags.put(" --prune", "");
                                        if(filterPane.filtFoundTglBtn.isSelected())
                                            dataFlags.put(" --filter-founders", "");
                                        if(!filterPane.mindTf.isDisabled() && filterPane.mindTf.getText()!=null)
                                            dataFlags.put(" --mind ", filterPane.mindTf.getText());
                                        if(!filterPane.genoTf.isDisabled() && filterPane.genoTf.getText()!=null)
                                            dataFlags.put(" --geno ", filterPane.genoTf.getText());
                                        if(!filterPane.mafTf.isDisabled() && filterPane.mafTf.getText()!=null)
                                            dataFlags.put(" --maf ", filterPane.mafTf.getText());
                                        if(!filterPane.ciTf.isDisabled() && filterPane.ciTf.getText()!=null)
                                            dataFlags.put(" --ci ", filterPane.ciTf.getText());
                                        
                                        
                                        
                                        dataFlags.put(" --make-bed","");
                                        dataFlags.put(" --out ",gh.createDirectory(dataDrivenFldr));
                                        gh.runPLINKcmd(dataFlags,"data_driven");
                                        gh.updatePlinkDataFile(dataDrivenFldr);
                                        
                                    filterPane.ddPin.setProgress(1);
                                }
                                
                                if (filterPane.ldPruneCbx.isSelected()){
                                    filterPane.ldBar.progressProperty().unbind();
                                    filterPane.ldBar.setVisible(true);
                                    filterPane.ldBar.setProgress(0);
                                    gh.dataProperties.resertLdProgress();
                                    filterPane.ldBar.progressProperty().bind(gh.dataProperties.getLdProgress());
                                    
                                    dataFlags.clear();
                                    dataFlags.put(gh.getFileTypeFlag(),gh.sharedInfo.plinkDataExtensionStripped());
                                    for(CheckBox c : filePane.cbs)
                                        if(c.isSelected())
                                            dataFlags.put(" "+c.getText(),"");

                                    dataFlags.put(" --indep-pairwise ", filterPane.ldPruneWindowTf.getText()+
                                                                    " "+filterPane.ldPruneStepTf.getText()+
                                                                    " "+filterPane.ldPruneThresholdTf.getText());
                                    //dataFlags.put(" --make-bed","");
                                    String output = gh.createDirectory(ldFldr);
                                    dataFlags.put(" --out ",output);
                                    gh.runPLINKcmd(dataFlags,"ld_prune");
                                    dataFlags.remove(" --indep-pairwise ");
                                    dataFlags.remove(" --out ");
                                    
                                    dataFlags.put(" --extract ",output+".prune.in");
                                    dataFlags.put(" --make-bed","");
                                    dataFlags.put(" --out ",output);
                                    gh.runPLINKcmd(dataFlags,"ld_extract");
                                    gh.updatePlinkDataFile(ldFldr);

                                    //filterPane.ldBar.setProgress(1);
                                }
                                
                                gh.sharedInfo.setPlinkRunning(false);
                                
                                return true;
                            }
                        };
                    }
                };
                
                filterPane.runPlinkBtn.disableProperty().bind(runPlinkDataFilters.runningProperty());
                
                filterPane.runPlinkBtn.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        if (!runPlinkDataFilters.isRunning()) {
                            runPlinkDataFilters.restart();
                            //runPlinkFlags.start();
                        }

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
        //ON MAIN WINDOW CLOSE
        pri.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event){
                if(runPlinkFlags.isRunning())
                    runPlinkFlags.cancel();
                if(runPlinkDataFilters.isRunning())
                    runPlinkDataFilters.cancel();
                mpc.timer.cancel();
            }
        });
        
        

        
        
    }
}