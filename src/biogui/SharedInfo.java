package biogui;

import java.io.File;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


/**
 * This class contains information to be shared between classes
 * @author Ahwan Pandey
 */
public class SharedInfo {
    
    private final String separator = File.separator;
    private File workingDirectory;
    private File dataFile;
    private File plinkExecutable;
    private BooleanProperty fileInputAnalysisDone = new SimpleBooleanProperty(false);
    private final int guiWidth = 1024;
    private final int guiHeight = 768;
    private String analysisName = "Unnamed analysis";
    private String OSName;
    private final String[] dataFlags = new String[]{"--no-sex", "--no-fid", "--no-parents",
                                                        "--allow-no-sex", "--missing-genotype ?", "--map3"};
    private File plinkDataExtensionStripped;
    private String plinkDataExtension;
    private BooleanProperty isPlinkRunning = new SimpleBooleanProperty(false);
    private final File styleFile = new File("."+File.separator+"resources"+
                                            File.separator+"styles"+
                                            File.separator+"biostyle.css");
    
    /**
     * Method to set working directory
     * @param wd The working directory
     */
    public void setWorkingDirectory(File wd){
        workingDirectory  = wd;
    }
    
    /**
     * Method to set PLINK executable
     * @param pE The PLINK executable file
     */
    public void setPlinkExecutable(File pE){
        plinkExecutable = pE;
    }
    
    /**
     * Method to set final analysis portion was completed
     * @param fad Boolean representing status of file analysis
     */
    public void setFileInputAnalysisDone(boolean fad){
        fileInputAnalysisDone.set(fad);
    }
    
    /**
     * Set the name of the analysis
     * @param name Name of analysis
     */
    public void setAnalysisName(String name){
        analysisName = name;
    }
    
    /**
     * Set the data file of the analysis
     * @param data Data file
     */
    public void setDataFile(File data){
        dataFile = data;
    }
    
    /**
     * Set the OS Name
     * @param osName Name of OS
     */
    public void setOSName(String osName) {
        OSName = osName;
    }
    
    /**
     * Set the plink data file with extension stripped (required for PLINK --file,
     * --bfile etc..)
     * @param pdes Data file with extension stripped
     */
    public void setPlinkDataExtensionStripped(File pdes){
        plinkDataExtensionStripped = pdes;
    }
    
    /**
     * Set the extension of the PLINK data file
     * @param ext PLINK data extension
     */
    public void setPlinkDataExtension(String ext){
        plinkDataExtension = ext;
    }
    
    /**
     * Check to see if PLINK is running
     * @param isRunning Set to true of false
     */
    public void setPlinkRunning(boolean isRunning){
        isPlinkRunning.set(isRunning);
    }
    
    //-------------------------------------
    /**
     * 
     * @return The separator character for the OS
     */
    public String getSeparator(){
        return separator;
    }
    
    /**
     * 
     * @return The working directory for the analysis
     */
    public File getWorkingDirectory(){
        return workingDirectory;
    }
    
    /**
     * 
     * @return The PLINK executable for the corresponding OS
     */
    public File getPlinkExecutable(){
        return plinkExecutable;
    }
    
    /**
     * 
     * @return The css style file for the GUI
     */
    public File getStyleFile(){
        return styleFile;
    }
    
    /**
     * 
     * @return Status of File Input section of analysis
     */
    public BooleanProperty getfileInputComplete(){
        return fileInputAnalysisDone;
    }
    
    /**
     * 
     * @return Name of the analysis
     */
    public String getAnalysisName(){
        return analysisName;
    }
    
    /**
     * @return the data file for analysis
     */
    public File getDataFile(){
        return dataFile;
    }
    
    /**
     * 
     * @return The width of the GUI
     */
    public int getGuiWidth(){
        return guiWidth;
    }
    
    /**
     * 
     * @return The height of the GUI
     */
    public int getGuiHeight(){
        return guiHeight;
    }
    
    /**
     * 
     * @return The OS name
     */
    public String getOSName() {
        return OSName;
    }
    
    /**
     * 
     * @return The flags to read in the data file 
     */
    public String[] getDataFlagsForCheckBox(){
        return dataFlags;
    }
    
    /**
     * 
     * @return The plink data file with extension stripped
     */
    public File plinkDataExtensionStripped(){
        return plinkDataExtensionStripped;
    }
    
    /**
     * 
     * @return The extension of the PLINK data file
     */
    public String getPlinkDataExtension(){
        return plinkDataExtension;
    }
    
    /**
     * 
     * @return Check to see whether PLINK is running
     */
    public BooleanProperty getPlinkRunning(){
        return isPlinkRunning;
    }
}
