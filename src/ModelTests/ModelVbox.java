/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelTests;

import biogui.GuiHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import Helpers.InsilicoURLReader;
import Helpers.NCBIURLReader;

/**
 *
 * @author ahwan
 */
public class ModelVbox {
    
    private VBox modelVbox;
    
    private TableView<SnpTableProperties> table;
    private ObservableList<SnpTableProperties> list = FXCollections.observableArrayList();
    
    private TextField searchField;
    
    private GuiHelper gh;
    
    public Button runModelBtn;
    private CheckBox trendCbx, genoCbx, allelicCbx, domCbx, recCbx;
    private TextField numMarkersTxt;
    
    public Label runModelLbl;
    public DoubleProperty runningModelTest = new SimpleDoubleProperty(0.1);
    public DoubleProperty runningAssocTest = new SimpleDoubleProperty(0.1);
    private DoubleProperty calculatingTopSnps = new SimpleDoubleProperty(0.1);
    
    private ChoiceBox geneDbCb;
    
    public ModelVbox(GuiHelper guiHelp){
        gh = guiHelp;
        
        modelVbox = new VBox();
        modelVbox.setPadding(new Insets(10,10,10,10));
        modelVbox.setSpacing(10);
        
        HBox choiceHbox = new HBox();
        choiceHbox.setSpacing(10);
        choiceHbox.setAlignment(Pos.CENTER_LEFT);
        VBox choiceVbox = new VBox();
            choiceVbox.setAlignment(Pos.CENTER);
            choiceVbox.getChildren().add(new Label("Select type of tests to include"));
            HBox choicesHbox= new HBox();
                choicesHbox.setPadding(new Insets(10,10,10,10));
                choicesHbox.setSpacing(15);
                trendCbx = new CheckBox("TREND"); 
                genoCbx = new CheckBox("GENO");
                allelicCbx = new CheckBox("ALLELIC");
                domCbx = new CheckBox("DOM");
                recCbx = new CheckBox("REC");
                VBox includeHbox = new VBox();
                    includeHbox.getChildren().add(new Label("Number of markers to keep"));
                    numMarkersTxt = new TextField();
                    includeHbox.getChildren().add(numMarkersTxt);
            choicesHbox.getChildren().addAll(trendCbx,genoCbx,allelicCbx,domCbx,recCbx);
        choiceVbox.getChildren().add(choicesHbox);
        runModelBtn = new Button("Run model test");
        VBox statusVbox = new VBox();
            Label runModelLbl = new Label("Running model tests"); runModelLbl.opacityProperty().bind(runningModelTest);
            Label runAssocLbl = new Label("Running association test"); runAssocLbl.opacityProperty().bind(runningAssocTest);
            Label calcTopSnpsLbl = new Label("Calculating top snps"); calcTopSnpsLbl.opacityProperty().bind(calculatingTopSnps);
        statusVbox.getChildren().addAll(runModelLbl,runAssocLbl,calcTopSnpsLbl);
        choiceHbox.getChildren().addAll(choiceVbox,includeHbox,runModelBtn,statusVbox);
        modelVbox.getChildren().add(choiceHbox);
        
        TableColumn chromosomeCol = new TableColumn("CHR");
        chromosomeCol.setMinWidth(40);
        chromosomeCol.setCellValueFactory(
            new PropertyValueFactory<SnpTableProperties,String>("snpChr")
        );
 
        TableColumn snpNameCol = new TableColumn("SNP");
        snpNameCol.setCellValueFactory(
            new PropertyValueFactory<SnpTableProperties,String>("snpName")
        );
        
        TableColumn testTypeCol = new TableColumn("TEST");
        testTypeCol.setCellValueFactory(
            new PropertyValueFactory<SnpTableProperties,String>("snpTestType")
        );
        
        TableColumn pValueCol = new TableColumn("P-VALUE");
        pValueCol.setCellValueFactory(
            new PropertyValueFactory<SnpTableProperties,String>("snpPvalue")
        );
        
        TableColumn oddsRatioCol = new TableColumn("OR");
        oddsRatioCol.setCellValueFactory(
            new PropertyValueFactory<SnpTableProperties,String>("snpOddsRatio")
        );
        
        TableColumn geneNameCol = new TableColumn("GENE");
        geneNameCol.setMinWidth(40);
        geneNameCol.setCellValueFactory(
            new PropertyValueFactory<SnpTableProperties,String>("geneName")
        );
        Callback<TableColumn, TableCell> cellFactory = 
            new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn p) {
                    return new EditingCell();
            }
        };
        geneNameCol.setCellFactory(cellFactory);
        
        table = new TableView<SnpTableProperties>();
        table.setEditable(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(chromosomeCol, snpNameCol, geneNameCol, testTypeCol,pValueCol,oddsRatioCol);
        geneNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<SnpTableProperties, String>>() {
            @Override public void handle(TableColumn.CellEditEvent<SnpTableProperties, String> t) {
                ((SnpTableProperties)t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setGeneName(t.getNewValue());
            }
        });
        modelVbox.getChildren().add(table);
        
        HBox bottomHbox = new HBox();
        bottomHbox.setSpacing(200);
            HBox searchHbox = new HBox();
                searchField = new TextField();
                searchField.setPromptText("gene/snp");
                Button searchBtn = new Button("Search");
                searchHbox.getChildren().addAll(searchField,searchBtn);
            HBox geneHbox = new HBox();
            geneHbox.setSpacing(5);
                Button geneNamesBtn = new Button("Get GENE names");
                geneDbCb = new ChoiceBox();
                    geneDbCb.setItems(FXCollections.observableArrayList("NCBI", "Insilico"));
                    geneDbCb.setValue(geneDbCb.getItems().get(1));
                geneHbox.getChildren().addAll(geneNamesBtn,geneDbCb);
                HBox geneProgHbox = new HBox();
                geneProgHbox.setSpacing(5);
                    ProgressIndicator genePin = new ProgressIndicator(-1);
                    genePin.setMaxSize(5, 5);
                    Button geneCancelBtn = new Button("Cancel");
                    geneProgHbox.getChildren().addAll(genePin,new Label("Retrieving gene names..."),geneCancelBtn);
                geneHbox.getChildren().add(geneProgHbox);
        bottomHbox.getChildren().addAll(searchHbox,geneHbox);
        modelVbox.getChildren().add(bottomHbox);
        
        
        //-------------actions----------------------------//
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent paramT) {
                    String str = searchField.getText();
                    table.getItems().clear();
                    if(str!=null && str.length()>0){
                        for (SnpTableProperties snp : list) {
                            if(snp.getSnpName().toLowerCase().contains(str.toLowerCase()) ||
                                    snp.getGeneName().toLowerCase().contains(str.toLowerCase())){
                                table.getItems().add(snp);
                            }
                        }
                    }else{
                        table.getItems().addAll(list);
                    }
                }
            });
        searchBtn.disableProperty().bind(updateSnpInfo.runningProperty());
        
        geneNamesBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if(!updateSnpInfo.isRunning())
                    updateSnpInfo.restart();
            }
        });
        geneCancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if(updateSnpInfo.isRunning()){
                    updateSnpInfo.cancel();
                    runModelBtn.setDisable(false);
                }
            }
        });
        
        geneNamesBtn.disableProperty().bind(updateSnpInfo.runningProperty());
        geneProgHbox.visibleProperty().bind(updateSnpInfo.runningProperty());
    }
    
    public void updateModelData(){
        
        calculatingTopSnps.set(1);
        
        File modelFilename = gh.sharedInfo.getPlinkModelFile();
        List testType = new ArrayList();
        if(trendCbx.isSelected())
            testType.add("TREND");
        if(genoCbx.isSelected())
            testType.add("GENO");
        if(allelicCbx.isSelected())
            testType.add("ALLELIC");
        if(domCbx.isSelected())
            testType.add("DOM");
        if(recCbx.isSelected())
            testType.add("REC");
        
        ModelReaderHelper frh = new ModelReaderHelper(modelFilename,testType,Integer.parseInt(numMarkersTxt.getText()));
        frh.readModelFile();
        frh.updateAssociationResults();
        
        List<String[]> model = frh.getTopModelSnps();
        Map<String, String[]> assoc = frh.getAssocMap();
        
        table.getItems().clear();
        list.clear();
        
        for(String[] m: model){
            list.add(new SnpTableProperties(m[frh.getChrCol()],
                        m[frh.getSnpNameCol()],
                        m[frh.getTestTypeCol()],
                        Double.parseDouble(m[frh.getPvalueCol()]),
                        Double.parseDouble(assoc.get(m[frh.getSnpNameCol()])[frh.getOddsRatioCol()]),
                        ""));
            
        }
        table.getItems().addAll(list);
        
        calculatingTopSnps.set(0.1);
        
    }
    
    final public Service updateSnpInfo = new Service(){
        @Override 
        protected Task createTask() {
            return new Task() {
            @Override 
                protected Object call() throws InterruptedException {
                    runModelBtn.setDisable(true);
                
                    String separator;
                    if(geneDbCb.getSelectionModel().getSelectedItem().equals("NCBI")){
                        separator = ",";
                        StringBuilder rsid = new StringBuilder();
                        List<String> affyId = new ArrayList<>();
                        for(SnpTableProperties snp : table.getItems()){
                            if(snp.getGeneName().equals("") && !snp.getGeneName().contains("*"))
                                if(snp.getSnpName().contains("rs"))
                                    rsid.append(snp.getSnpName()).append(separator);
                                else if(snp.getSnpName().contains("SNP"))
                                    affyId.add(snp.getSnpName());
                        }
                        NCBIURLReader ncbiDb = new NCBIURLReader(rsid.toString(),affyId);
                        for(SnpTableProperties snp : table.getItems()){
                            if(snp.getGeneName().equals("") && !snp.getGeneName().contains("*"))
                                snp.setGeneName(ncbiDb.getSnpGeneMap().get(snp.getSnpName()));
                        }
                        
                    }else if(geneDbCb.getSelectionModel().getSelectedItem().equals("Insilico")){
                        separator = "\n";
                        StringBuilder snps = new StringBuilder();
                        for(SnpTableProperties snp : table.getItems()){
                            if(snp.getGeneName().equals("") && !snp.getGeneName().contains("*"))
                                snps.append(snp.getSnpName()).append(separator);
                        }
                        InsilicoURLReader insilicoDb = new InsilicoURLReader(snps.toString());
                        for(SnpTableProperties snp : table.getItems()){
                            if(snp.getGeneName().equals("") && !snp.getGeneName().contains("*"))
                                snp.setGeneName(insilicoDb.getSnpGeneMap().get(snp.getSnpName()));
                        }
                    }
                    
                    runModelBtn.setDisable(false);
                    return true;
                }
            };
        }
    };
    
    public VBox getModelVbox(){
        return modelVbox;
    }
    
}
