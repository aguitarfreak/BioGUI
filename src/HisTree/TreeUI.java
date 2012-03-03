/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HisTree;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import biogui.GuiHelper;


/**
 *
 * @author ahwan
 */
public class TreeUI {
    
    private Map<String,Analysis> hisTree = new HashMap<>();
    private TreeItem<String> rootTree;
    private TreeView tree;
    private VBox treeVbox;
    
    private Button removeAnalysisBtn;
    public Button setAnalysisBtn;
    
    
    GuiHelper guiHelp;
      
    public TreeUI(GuiHelper gh){
        rootTree = new TreeItem<>("All Analyses");
        tree = new TreeView(rootTree);
        tree.setRoot(rootTree);
        tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        rootTree.setExpanded(true);
        
        setAnalysisBtn = new Button("Set as current Analysis");
        setAnalysisBtn.setDisable(true);
        removeAnalysisBtn = new Button("Remove selected analysis");
        
        treeVbox = new VBox();
        treeVbox.setStyle("-fx-background-color: powderblue");
        treeVbox.setSpacing(5);
        treeVbox.setPadding(new Insets(10,10,10,10));
        treeVbox.getChildren().add(tree);
        treeVbox.getChildren().addAll(setAnalysisBtn,removeAnalysisBtn);
        
        guiHelp = gh;
        setupActionEvents();
    }
    
    public void addNewAnalysis(String name, File data ){
        hisTree.put(name, new Analysis(name, data));
        rootTree.getChildren().add(hisTree.get(name).getAnalysisTree());
    }
    
    public void addNewSubAnalysis(String nameOfAnalysis, String name, File data){
        hisTree.get(nameOfAnalysis).addToAnalysisTree(name, data);
    }
    
    public void removeAnalysis(String name){
        //remove folders and files
        File fdir = hisTree.get(name).getThisAnalysis().get(name).getFile();
        deleteDir(fdir);
        //remove from application
        rootTree.getChildren().removeAll(hisTree.get(name).getAnalysisTree());
        hisTree.remove(name);
        
    }
    
    public void removeSubAnalysis(String name, String subName){
        //remove folders and files
        String dir = hisTree.get(name).getThisAnalysis().get(subName).getFile().getParent();
        File fdir = new File(dir);
        deleteDir(fdir);
        //remove from application
        hisTree.get(name).removeSubAnalysis(subName);
    }
    
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
    
    public Map<String,Analysis> getAnalyses(){
        return hisTree;
    }
    
    public VBox getTreeVbox(){
        return treeVbox;
    }
    
    //-----------Action Events------------------------//
    private void setupActionEvents(){
        
        removeAnalysisBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(!tree.getSelectionModel().isEmpty()){
                    //The all analysis root will clear all analyses (had to loop twice to avoid concurrent modification errors)
                    if(tree.getSelectionModel().isSelected(0)){
                        ArrayList<String> names = new ArrayList();
                        for(String name: hisTree.keySet()){
                            names.add(name);
                        }
                        for(String name: names){
                            removeAnalysis(name);
                        }
                    //if it is a leaf and is not a new analysis
                    }else if(tree.getTreeItem(tree.getSelectionModel().getSelectedIndex()).isLeaf()
                            && !(tree.getTreeItem(tree.getSelectionModel().getSelectedIndex()).getParent().valueProperty().get().toString().equals("All Analyses"))) {
                        String selectedAnalysis = tree.getTreeItem(tree.getSelectionModel().getSelectedIndex()).valueProperty().get().toString();
                        String parent = tree.getTreeItem(tree.getSelectionModel().getSelectedIndex()).getParent().valueProperty().get().toString();
                        removeSubAnalysis(parent, selectedAnalysis);
                    //if it is a new analysis with no leaves
                    }else{
                        String selectedAnalysis = tree.getTreeItem(tree.getSelectionModel().getSelectedIndex()).valueProperty().get().toString();
                        removeAnalysis(selectedAnalysis);
                    }
                }
            }
        });
        
         
        setAnalysisBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                int index = tree.getSelectionModel().getSelectedIndex();
                if(tree.getTreeItem(index).isLeaf()){
                    String selectedAnalysis = tree.getTreeItem(index).valueProperty().get().toString();
                    if(selectedAnalysis.equals("1_read_file")){
                        guiHelp.resetDataFile(guiHelp.sharedInfo.getOriginalDataFile());
                    }else{
                        String parent = tree.getTreeItem(index).getParent().valueProperty().get().toString();
                        File f = hisTree.get(parent).getThisAnalysis().get(selectedAnalysis).getFile();
                        guiHelp.sharedInfo.setDataFile(f);
                    }
                }
                
            }
        });        
    } 
}
