/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biogui;

import java.io.File;


/**
 *
 * @author ahwan
 */
public class SharedInfo {
    
    private File workingDirectory;
    private File dataFile;
    private File plinkExecutable;
    private boolean fileAnalysisDone = false;
    private final int guiWidth = 1000;
    private final int guiHeight = 600;
    private String analysisName = "Unnamed analysis";
    private String OSName;
    private final String[] dataProperties = new String[]{"--no-sex", "--no-fid", "--no-parents",
                                                        "--allow-no-sex", "--missing-genotype ?", "--map3"};
    private File plinkDataExtensionStripped;
    private String plinkDataExtension;
    
    
    public void setWorkingDirectory(File wd){
        workingDirectory  = wd;
    }
    
    public void setPlinkExecutable(File pE){
        plinkExecutable = pE;
    }
    
    public void setFileAnalysisDone(boolean fad){
        fileAnalysisDone = fad;
    }
    
    public void setAnalysisName(String name){
        analysisName = name;
    }
    
    public void setDataFile(File data){
        dataFile = data;
    }
    
    public void setOSName(String osName) {
        OSName = osName;
    }
    
    public void setPlinkDataExtensionStripped(File pdes){
        plinkDataExtensionStripped = pdes;
    }
    
    public void setPlinkDataExtension(String ext){
        plinkDataExtension = ext;
    }
    
    //-------------------------------------
    public File getWorkingDirectory(){
        return workingDirectory;
    }
    
    public File getPlinkExecutable(){
        return plinkExecutable;
    }
    
    public boolean getfileInputComplete(){
        return fileAnalysisDone;
    }
    
    public String getAnalysisName(){
        return analysisName;
    }
    
    public File getDataFile(){
        return dataFile;
    }
    
    public int getGuiWidth(){
        return guiWidth;
    }
    
    public int getGuiHeight(){
        return guiHeight;
    }
    
    public String getOSName() {
        return OSName;
    }
    
    public String[] getDataPropertiesForCheckBox(){
        return dataProperties;
    }
    
    public File plinkDataExtensionStripped(){
        return plinkDataExtensionStripped;
    }

    public String getPlinkDataExtension(){
        return plinkDataExtension;
    }
    
}
