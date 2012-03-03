/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HisTree;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;

/**
 *
 * @author ahwan
 */
public class Analysis {
    
    private TreeItem<String> rootAnalysisTree;
    private Map<String,AnalysisProperties> thisAnalysis = new HashMap();
    
    public Analysis(String n, File f){
        thisAnalysis.put(n, new AnalysisProperties(n,f));
        rootAnalysisTree = thisAnalysis.get(n).getTreeItem();
    }
    
    public void addToAnalysisTree(String n, File f){
        thisAnalysis.put(n, new AnalysisProperties(n,f));
        rootAnalysisTree.getChildren().add(thisAnalysis.get(n).getTreeItem());
    }
    
    public TreeItem<String> getAnalysisTree(){
        return rootAnalysisTree;
    }
    
    public Map<String,AnalysisProperties> getThisAnalysis(){
        return thisAnalysis;
    }
    
    public void removeSubAnalysis(String subName){
        rootAnalysisTree.getChildren().remove(thisAnalysis.get(subName).getTreeItem());
        thisAnalysis.remove(subName);
    }
    
    public static class AnalysisProperties {
        
        private final File dataFile;
        private final SimpleStringProperty analysisName;
        private TreeItem<String> item;
        
        private AnalysisProperties(String n, File f) {
            this.dataFile = f;
            this.analysisName = new SimpleStringProperty(n);
            this.item = new TreeItem<>(n);
        }
        
        public File getFile(){
            return dataFile;
        }
        
        public String getAnalysisName(){
            return analysisName.get();
        }
        
        public TreeItem<String> getTreeItem(){
            return item;
        }
        
    }
    
}
