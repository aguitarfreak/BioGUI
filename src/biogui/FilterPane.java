package biogui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * This class sets up graphical elements of the filtering section of the accordion
 * component
 * @author Ahwan Pandey
 */
public class FilterPane {
    
    private TitledPane filterPane;
    private TabPane filterTabPanes;
    private GuiHelper guiHelp;
    
    protected Button excludeSnpBtn;
    protected ToggleButton excludeSnpTBtn;
    protected ToggleButton extractSnpTBtn;
    protected TextField excludeSnpTf;
    
    protected Button excludeSubjBtn;
    protected ToggleButton excludeSubjTBtn;
    protected ToggleButton extractSubjTBtn;
    protected TextField excludeSubjTf;
    
    protected ToggleButton pruneTglBtn;
    protected ToggleButton filtFoundTglBtn;
    protected TextField mindTf;
    protected TextField genoTf;
    protected TextField mafTf;
    protected TextField ciTf;
    protected CheckBox ldPruneCbx;
    TextField ldPruneWindowTf;             
    TextField ldPruneStepTf;
    TextField ldPruneThresholdTf;
    
    protected Button runPlinkBtn;
    protected ProgressIndicator extractPin;
    protected ProgressIndicator ddPin;
    protected ProgressBar ldBar;
    
    protected CheckBox exclusionFiltersCbx;
    protected CheckBox dataFiltersCbx;
    
    FilterPane(GuiHelper gh) {
        guiHelp = gh;
        
        filterPane = new TitledPane();
        filterPane.setText("Data Filtering");
        
        filterTabPanes = new TabPane();
        filterTabPanes.setSide(Side.LEFT);
        filterTabPanes.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        
        
        //------------PREFILTERING--------------------------//
        
        Tab preFiltersTab = new Tab("Pre Filters");
        VBox preFilterVBox = new VBox();
        preFilterVBox.setPadding(new Insets(5,5,5,5));
        preFiltersTab.setContent(preFilterVBox);
            //----EXCLUSION FILTERS------//
            HBox exclusionFiltersHbox = new HBox();
            exclusionFiltersHbox.setPadding(new Insets(10,10,10,10));
            exclusionFiltersHbox.setAlignment(Pos.BASELINE_LEFT);
            exclusionFiltersCbx = new CheckBox();
            
                Label plinkExclusionFiltersLbl = new Label("PLINK Exclusion/Inclusion Filters: ");
                plinkExclusionFiltersLbl.setUnderline(true);

                Label snpLbl = new Label("SNP: ");
                    ToggleGroup groupExSnp = new ToggleGroup();
                    excludeSnpTBtn = new ToggleButton("Exclude"); excludeSnpTBtn.setToggleGroup(groupExSnp); 
                    extractSnpTBtn = new ToggleButton("Extract"); extractSnpTBtn.setToggleGroup(groupExSnp);
                    HBox snpExToggleHbox = new HBox();
                    snpExToggleHbox.getChildren().addAll(excludeSnpTBtn,extractSnpTBtn);
                excludeSnpTf = new TextField();
                excludeSnpTf.setMinWidth(500);
                excludeSnpTf.setPromptText("Load list of Snps to exclude/extract");
                excludeSnpTf.setEditable(false);
                excludeSnpBtn = new Button("...");
                final Tooltip excludeSnpTooltip = new Tooltip();
                excludeSnpTooltip.setText("File with the following snplist format:"
                                        + "\nsnp1\nsnp2\nsnp3\n\n"
                                        + "i.e. one SNP per line");
                excludeSnpBtn.setTooltip(excludeSnpTooltip);
                excludeSnpBtn.disableProperty().bind(groupExSnp.selectedToggleProperty().isNull());

                Label subjLbl = new Label("SUB: ");
                    ToggleGroup groupExSubj = new ToggleGroup();
                    excludeSubjTBtn = new ToggleButton("Exclude"); excludeSubjTBtn.setToggleGroup(groupExSubj);
                    extractSubjTBtn = new ToggleButton("Extract"); extractSubjTBtn.setToggleGroup(groupExSubj);
                    HBox subjExToggleHbox = new HBox();
                    subjExToggleHbox.getChildren().addAll(excludeSubjTBtn,extractSubjTBtn);
                excludeSubjTf = new TextField();
                excludeSubjTf.setMinWidth(500);
                excludeSubjTf.setPromptText("Load list of Subjects to exclude/extract");
                excludeSubjTf.setEditable(false);
                excludeSubjBtn = new Button("...");
                final Tooltip excludeSubjTooltip = new Tooltip();
                excludeSubjTooltip.setText("File with the following subject-list format:"
                        + "\nFID1\t\tIID1\nFID2\t\tIID2\nFID3\t\tIID3"
                        + "\n\ni.e. one Family ID / Individual ID pair per line"
                        + "\ni.e. one person per line");
                excludeSubjBtn.setTooltip(excludeSubjTooltip);
                excludeSubjBtn.disableProperty().bind(groupExSubj.selectedToggleProperty().isNull());

                GridPane plinkExclusionFiltersGrid = new GridPane();
                plinkExclusionFiltersGrid.disableProperty().bind(exclusionFiltersCbx.selectedProperty().not());
                plinkExclusionFiltersGrid.setPadding(new Insets(30,10,30,10));
                plinkExclusionFiltersGrid.setStyle("-fx-background-color: aliceblue");
                plinkExclusionFiltersGrid.setVgap(10); plinkExclusionFiltersGrid.setHgap(10);
                plinkExclusionFiltersGrid.add(plinkExclusionFiltersLbl, 1, 0);
                    plinkExclusionFiltersGrid.add(snpLbl,0,1);
                    plinkExclusionFiltersGrid.add(snpExToggleHbox,1,1);
                    plinkExclusionFiltersGrid.add(excludeSnpTf,2,1);
                    plinkExclusionFiltersGrid.add(excludeSnpBtn,3,1);
                    plinkExclusionFiltersGrid.add(subjLbl,0,2);
                    plinkExclusionFiltersGrid.add(subjExToggleHbox,1,2);
                    plinkExclusionFiltersGrid.add(excludeSubjTf,2,2);
                    plinkExclusionFiltersGrid.add(excludeSubjBtn,3,2);
                //preFilterVBox.getChildren().add(plinkExclusionFiltersGrid);
            exclusionFiltersHbox.getChildren().addAll(exclusionFiltersCbx,plinkExclusionFiltersGrid);
            preFilterVBox.getChildren().add(exclusionFiltersHbox);
            
            //----DATA FILTERS------//
            HBox dataFiltersHbox = new HBox();
            dataFiltersHbox.setPadding(new Insets(10,10,10,10));
            dataFiltersHbox.setAlignment(Pos.BASELINE_LEFT);
            dataFiltersCbx = new CheckBox();
                    Label plinkDataFiltersLbl = new Label("PLINK Data-driven Filters: ");
                    plinkDataFiltersLbl.setUnderline(true);
                    HBox rowOne = new HBox();
                    rowOne.setSpacing(5);
                        pruneTglBtn = new ToggleButton("prune");
                        filtFoundTglBtn = new ToggleButton("Filter Founders");
                        rowOne.setSpacing(15);
                        rowOne.getChildren().addAll(pruneTglBtn,filtFoundTglBtn);
                    HBox rowTwo = new HBox();
                    rowTwo.setSpacing(5);
                        CheckBox mindCbx = new CheckBox("--mind");
                        mindTf = new TextField(); mindTf.setPromptText("[0-1]");
                            mindTf.disableProperty().bind(mindCbx.selectedProperty().not());
                            mindTf.setText("0.1");
                            mindTf.setMaxWidth(50);
                        CheckBox genoCbx = new CheckBox("--geno");
                        genoTf = new TextField(); genoTf.setPromptText("[0-1]");
                            genoTf.disableProperty().bind(genoCbx.selectedProperty().not());
                            genoTf.setText("0.1");
                            genoTf.setMaxWidth(50);
                        CheckBox mafCbx = new CheckBox("--maf");
                        mafTf = new TextField(); mafTf.setPromptText("[0-1]");
                            mafTf.disableProperty().bind(mafCbx.selectedProperty().not());
                            mafTf.setText("0.05");
                            mafTf.setMaxWidth(50);
                        CheckBox ciCbx = new CheckBox("--ci");
                        ciTf = new TextField(); ciTf.setPromptText("[0-1]");
                            ciTf.disableProperty().bind(ciCbx.selectedProperty().not());
                            ciTf.setText("0.95");
                            ciTf.setMaxWidth(50);
                        rowTwo.getChildren().addAll(mindCbx,mindTf,genoCbx,genoTf,
                                                    mafCbx,mafTf,ciCbx,ciTf);
                VBox dataFiltersVbx = new VBox();
                dataFiltersVbx.disableProperty().bind(dataFiltersCbx.selectedProperty().not());
                dataFiltersVbx.setPadding(new Insets(30,10,30,10));
                dataFiltersVbx.setStyle("-fx-background-color: azure");
                dataFiltersVbx.setSpacing(15);
                dataFiltersVbx.getChildren().addAll(plinkDataFiltersLbl,rowOne,rowTwo);
                dataFiltersHbox.getChildren().addAll(dataFiltersCbx,dataFiltersVbx);
                preFilterVBox.getChildren().add(dataFiltersHbox);
                        
                HBox rowThree = new HBox();
                rowThree.setPadding(new Insets(10,10,10,10));
                rowThree.setAlignment(Pos.BASELINE_LEFT);
                rowThree.setStyle("-fx-background-color: lightblue");
                rowThree.setSpacing(5);
                        ldPruneCbx = new CheckBox("LD-PRUNE: ");
                        ldPruneWindowTf = new TextField(); ldPruneWindowTf.setText("50"); ldPruneWindowTf.setMaxWidth(50);
                            ldPruneWindowTf.disableProperty().bind(ldPruneCbx.selectedProperty().not());
                        ldPruneStepTf = new TextField(); ldPruneStepTf.setText("5");ldPruneStepTf.setMaxWidth(50);
                            ldPruneStepTf.disableProperty().bind(ldPruneCbx.selectedProperty().not());
                        ldPruneThresholdTf = new TextField(); ldPruneThresholdTf.setText("0.5");ldPruneThresholdTf.setMaxWidth(50);
                            ldPruneThresholdTf.disableProperty().bind(ldPruneCbx.selectedProperty().not());
                        rowThree.getChildren().addAll(ldPruneCbx,ldPruneWindowTf,ldPruneStepTf,ldPruneThresholdTf);

               preFilterVBox.getChildren().add(rowThree); 
               
                //RUN PLINK
                HBox rowFour = new HBox();
                rowFour.setPadding(new Insets(2,2,2,2));
                rowFour.setAlignment(Pos.BASELINE_LEFT);
                        runPlinkBtn = new Button("Run PLINK");
                        rowFour.setPadding(new Insets(10,10,10,10));
                        rowFour.setStyle("-fx-background-color: lightsteelblue");
                        rowFour.setAlignment(Pos.CENTER);
                        rowFour.getChildren().add(runPlinkBtn);
                preFilterVBox.getChildren().add(rowFour);
           
           //rowFive
            GridPane runPlinkGrid = new GridPane();
                ColumnConstraints col1 = new ColumnConstraints(200); //col1.setHalignment(HPos.RIGHT);
                ColumnConstraints col2 = new ColumnConstraints(200); //col2.setHalignment(HPos.LEFT);
                ColumnConstraints col3 = new ColumnConstraints(70); //col3.setHalignment(HPos.RIGHT);
                ColumnConstraints col4 = new ColumnConstraints(70);
                runPlinkGrid.getColumnConstraints().addAll(col1,col2,col3,col4);
                runPlinkGrid.setPadding(new Insets(10,10,10,10));
                runPlinkGrid.setVgap(2);
                runPlinkGrid.setStyle("-fx-background-color: lightcyan");

                Label extractFilterLbl = new Label("Running extraction/inclusion filters: ");
                extractPin = new ProgressIndicator(0);
                extractPin.setMinSize(40, 40); extractPin.setMaxSize(40, 40);
                extractPin.setVisible(false);
                runPlinkGrid.add(extractPin,4,0);
                runPlinkGrid.add(extractFilterLbl,0,0);
                    Label snpsExtOrIncLbl = new Label("SNPs Extracted/Excluded: ");
                    runPlinkGrid.add(snpsExtOrIncLbl,1,1);
                    
                    Text snpsExtOrIncTxt = new Text(); runPlinkGrid.add(snpsExtOrIncTxt,2,1);
                    snpsExtOrIncTxt.textProperty().bind(gh.dataProperties.getExcludeOrIncludeSnpCount().asString());
                    
                    Label subjExtOrIncLbl = new Label("Subjects Extracted/Excluded: ");
                    runPlinkGrid.add(subjExtOrIncLbl,1,2);
                    
                    Text subjExtOrIncTxt = new Text(); runPlinkGrid.add(subjExtOrIncTxt,2,2);
                    subjExtOrIncTxt.textProperty().bind(gh.dataProperties.getExcludeOrIncludeSubjCount().asString());
                    
                Label ddFiltersLbl = new Label("Running data driven filters: ");
                ddPin = new ProgressIndicator(0); 
                ddPin.setMinSize(40, 40); ddPin.setMaxSize(40, 40);
                ddPin.setVisible(false);
                runPlinkGrid.add(ddFiltersLbl,0,3);
                runPlinkGrid.add(ddPin,4,3);
                    Label ddFilterSnpsLbl = new Label("No. of SNPs removed: "); runPlinkGrid.add(ddFilterSnpsLbl,1,4);
                    Text ddFilterSnpsTxt = new Text(); runPlinkGrid.add(ddFilterSnpsTxt,2,4);
                    ddFilterSnpsTxt.textProperty().bind(gh.dataProperties.getNumOfSnpsRemovedByddFilter().asString());
                    
                    Label ddFilterSubjLbl = new Label("No. of Subjects removed: "); runPlinkGrid.add(ddFilterSubjLbl,1,5);
                    Text ddFilterSubjTxt = new Text(); runPlinkGrid.add(ddFilterSubjTxt,2,5);
                    ddFilterSubjTxt.textProperty().bind(gh.dataProperties.getNumOfSubjsRemovedByddFilter().asString());
              preFilterVBox.getChildren().add(runPlinkGrid);
              
             //rowSix
             GridPane ldPruneStatsGrid = new GridPane();
             ldPruneStatsGrid.setStyle("-fx-background-color: aliceblue");
             ldPruneStatsGrid.setPadding(new Insets(10,10,10,10));
             ColumnConstraints col1ld = new ColumnConstraints(100); //col1.setHalignment(HPos.RIGHT);
             ColumnConstraints col2ld = new ColumnConstraints(400); //col2.setHgrow(Priority.ALWAYS);
             ColumnConstraints col3ld = new ColumnConstraints(100);
             ldPruneStatsGrid.getColumnConstraints().addAll(col1ld,col2ld,col3ld);
                Label ldPruneLbl = new Label("LD-pruning data: ");
                ldBar = new ProgressBar(0);
                ldBar.setVisible(false);
                ldBar.setPrefSize(400,400);
                HBox ldStatusHbox = new HBox();
                ldStatusHbox.setAlignment(Pos.CENTER);
                ldStatusHbox.setStyle("-fx-background-color: lightskyblue");
                Text ldStatusTxt = new Text();
                ldStatusTxt.textProperty().bind(gh.dataProperties.getOnCromosomeLd().asString());
                Text numofChromosomes = new Text();
                numofChromosomes.textProperty().bind(gh.dataProperties.getNumOfChromosomes().asString());
                ldStatusHbox.getChildren().addAll(ldStatusTxt, new Label("/"),numofChromosomes);
                ldPruneStatsGrid.add(ldPruneLbl,0,0);
                ldPruneStatsGrid.add(ldBar,1,0);
                ldPruneStatsGrid.add(ldStatusHbox,2,0);
             preFilterVBox.getChildren().add(ldPruneStatsGrid);   
                    
          
        //-------------------------------------------------//
        
        //-----------FILTERING ALGORITHMS------------------//
        
        
        
        
        //-------------------------------------------------//
        
        filterTabPanes.getTabs().addAll(preFiltersTab);
        filterPane.setContent(filterTabPanes);
        filterPane.disableProperty().bind(gh.sharedInfo.getfileInputComplete().not());
    }
    
    public TitledPane getfilterPane(){
        return filterPane;
    }
    
}
