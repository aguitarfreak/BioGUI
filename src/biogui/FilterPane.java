package biogui;

import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class sets up graphical elements of the filtering section of the accordion
 * component
 * @author Ahwan Pandey
 */
public class FilterPane {
    
    private TitledPane filterPane;
    private TabPane filterTabPanes;
    private GuiHelper guiHelp;
    
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
        preFiltersTab.setContent(preFilterVBox);
            //----EXCLUSION FILTERS------//
            Label plinkExclusionFiltersLbl = new Label("PLINK Exclusion/Inclusion Filters: ");
            plinkExclusionFiltersLbl.setUnderline(true);
            
            Label snpLbl = new Label("SNP: ");
                ToggleGroup groupExSnp = new ToggleGroup();
                ToggleButton excludeSnpTBtn = new ToggleButton("Exclude"); excludeSnpTBtn.setToggleGroup(groupExSnp);
                ToggleButton extractSnpTBtn = new ToggleButton("Extract"); extractSnpTBtn.setToggleGroup(groupExSnp);
                HBox snpExToggleHbox = new HBox();
                snpExToggleHbox.getChildren().addAll(excludeSnpTBtn,extractSnpTBtn);
            TextField excludeSnpTf = new TextField();
            excludeSnpTf.setMinWidth(500);
            excludeSnpTf.setPromptText("Load list of Snps to exclude/extract");
            excludeSnpTf.setEditable(false);
            Button excludeSnpBtn = new Button("...");
            excludeSnpBtn.disableProperty().bind(groupExSnp.selectedToggleProperty().isNull());

            Label subLbl = new Label("SUB: ");
                ToggleGroup groupExSub = new ToggleGroup();
                ToggleButton excludeSubTBtn = new ToggleButton("Exclude"); excludeSubTBtn.setToggleGroup(groupExSub);
                ToggleButton extractSubTBtn = new ToggleButton("Extract"); extractSubTBtn.setToggleGroup(groupExSub);
                HBox subExToggleHbox = new HBox();
                subExToggleHbox.getChildren().addAll(excludeSubTBtn,extractSubTBtn);
            TextField excludeSubTf = new TextField();
            excludeSubTf.setMinWidth(500);
            excludeSubTf.setPromptText("Load list of Subjects to exclude/extract");
            excludeSubTf.setEditable(false);
            Button excludeSubBtn = new Button("...");
            excludeSubBtn.disableProperty().bind(groupExSub.selectedToggleProperty().isNull());
            
            GridPane plinkExclusionFiltersGrid = new GridPane();
            plinkExclusionFiltersGrid.setPadding(new Insets(50,10,50,10));
            plinkExclusionFiltersGrid.setStyle("-fx-background-color: aliceblue");
            plinkExclusionFiltersGrid.setVgap(10); plinkExclusionFiltersGrid.setHgap(10);
            plinkExclusionFiltersGrid.add(plinkExclusionFiltersLbl, 1, 0);
                plinkExclusionFiltersGrid.add(snpLbl,0,1);
                plinkExclusionFiltersGrid.add(snpExToggleHbox,1,1);
                plinkExclusionFiltersGrid.add(excludeSnpTf,2,1);
                plinkExclusionFiltersGrid.add(excludeSnpBtn,3,1);
                plinkExclusionFiltersGrid.add(subLbl,0,2);
                plinkExclusionFiltersGrid.add(subExToggleHbox,1,2);
                plinkExclusionFiltersGrid.add(excludeSubTf,2,2);
                plinkExclusionFiltersGrid.add(excludeSubBtn,3,2);
            preFilterVBox.getChildren().add(plinkExclusionFiltersGrid);
            
            //----DATA FILTERS------//
            Label plinkDataFiltersLbl = new Label("PLINK Data-driven Filters: ");
            plinkDataFiltersLbl.setUnderline(true);
            HBox rowOne = new HBox();
            rowOne.setSpacing(5);
                ToggleButton pruneTglBtn = new ToggleButton("prune");
                ToggleButton filtFoundTglBtn = new ToggleButton("Filter Founders");
                rowOne.setSpacing(15);
                rowOne.getChildren().addAll(pruneTglBtn,filtFoundTglBtn);
            HBox rowTwo = new HBox();
            rowTwo.setSpacing(5);
                CheckBox mindCbx = new CheckBox("--mind");
                TextField mindTf = new TextField(); mindTf.setPromptText("[0-1]");
                    mindTf.disableProperty().bind(mindCbx.selectedProperty().not());
                    mindTf.setText("0.1");
                    mindTf.setMaxWidth(50);
                CheckBox genoCbx = new CheckBox("--geno");
                TextField genoTf = new TextField(); genoTf.setPromptText("[0-1]");
                    genoTf.disableProperty().bind(mindCbx.selectedProperty().not());
                    genoTf.setText("0.1");
                    genoTf.setMaxWidth(50);
                CheckBox mafCbx = new CheckBox("--maf");
                TextField mafTf = new TextField(); mafTf.setPromptText("[0-1]");
                    mafTf.disableProperty().bind(mindCbx.selectedProperty().not());
                    mafTf.setText("0.05");
                    mafTf.setMaxWidth(50);
                CheckBox ciCbx = new CheckBox("--ci");
                TextField ciTf = new TextField(); ciTf.setPromptText("[0-1]");
                    ciTf.disableProperty().bind(mindCbx.selectedProperty().not());
                    ciTf.setText("0.95");
                    ciTf.setMaxWidth(50);
                rowTwo.getChildren().addAll(mindCbx,mindTf,genoCbx,genoTf,
                                            mafCbx,mafTf,ciCbx,ciTf);
           HBox rowThree = new HBox();
           rowThree.setSpacing(5);
                CheckBox ldPruneCbx = new CheckBox("LD-PRUNE: ");
                TextField ldPruneWindowTf = new TextField(); ldPruneWindowTf.setText("50"); ldPruneWindowTf.setMaxWidth(50);
                    ldPruneWindowTf.disableProperty().bind(ldPruneCbx.selectedProperty().not());
                TextField ldPruneStepTf = new TextField(); ldPruneStepTf.setText("5");ldPruneStepTf.setMaxWidth(50);
                    ldPruneStepTf.disableProperty().bind(ldPruneCbx.selectedProperty().not());
                TextField ldPruneThresholdTf = new TextField(); ldPruneThresholdTf.setText("0.5");ldPruneThresholdTf.setMaxWidth(50);
                    ldPruneThresholdTf.disableProperty().bind(ldPruneCbx.selectedProperty().not());
                rowThree.getChildren().addAll(ldPruneCbx,ldPruneWindowTf,ldPruneStepTf,ldPruneThresholdTf);
           
                
           VBox dataFiltersVbx = new VBox();
           dataFiltersVbx.setPadding(new Insets(50,10,50,10));
           dataFiltersVbx.setStyle("-fx-background-color: azure");
           dataFiltersVbx.setSpacing(15);
           dataFiltersVbx.getChildren().addAll(plinkDataFiltersLbl,rowOne,rowTwo,rowThree);
           preFilterVBox.getChildren().add(dataFiltersVbx);
            
            
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
