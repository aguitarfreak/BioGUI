package biogui;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import MemoryView.MemoryPieChart;
import javafx.scene.layout.VBox;

/**
 * A helper class to bridge the GUI and the background functionality of the code.
 * @see SharedInfo
 * @see DataProperties
 * @author ahwan
 */
public class GuiHelper {
    private DirectoryChooser dc;
    private FileChooser fc;
    public SharedInfo sharedInfo;
    public DataProperties dataProperties;
    private RunPlink rp;
    private String extension;
    
    private final File windowsPLINKfile = new File("."+File.separator+
                                            "plinkBinaries"+File.separator+
                                            "Windows"+File.separator+"plink.exe");
    private final File linuxPLINKfile = new File("."+File.separator+
                                            "plinkBinaries"+File.separator+
                                            "Linux"+File.separator+"plink");
    private final File macPLINKfile = new File("."+File.separator+
                                            "plinkBinaries"+File.separator+
                                            "Mac"+File.separator+"plink");

    //Specifies which file command to use when running PLINK (--bfile, --file etc..)
    private HashMap<String,String> extensionCommand;
    
    /**
     * The default constructor
     */
    public GuiHelper(){
        sharedInfo = new SharedInfo();
        dataProperties = new DataProperties();
        dc = new DirectoryChooser();
        
        //Set OS Nane and PLINK executable path
        String OSname = (new GetOSName()).GetOSName();
        sharedInfo.setOSName(OSname);
        if(sharedInfo.getOSName().equalsIgnoreCase("Windows 7"))
            sharedInfo.setPlinkExecutable(windowsPLINKfile);
        else{
            System.err.println("OS not yet implemented!");
            System.exit(1);
        }
        
        extensionCommand = new HashMap<>();
        extensionCommand.put("bed"," --bfile ");
        extensionCommand.put("ped"," --file ");
        extensionCommand.put("regain"," --regain-file ");
        
        rp = new RunPlink(sharedInfo.getPlinkExecutable(),dataProperties);
        
        
    }
    
    /**
     * Opens up a DirectoryChooser dialog to pick a working directory for
     * the analysis.
     * @param s To create a modal view
     */
    public void chooseWorkingDirectory(Stage s){
        dc.setTitle("Select Working Directory");
        File d = new File(getClass().getResource("."+File.separator).getFile());
        //dc.setInitialDirectory(new File("D://Work//NetBeansProjects//testOutput"));
        sharedInfo.setWorkingDirectory(dc.showDialog(s));
    }
    
    /**
     * This method creates a directory for each analysis step, and sets up the name
     * of the output file in the directory (corresponds with the name of the analysis)
     * @param strDirectory The directory to be made
     * @return The full path of the output file.
     */
    public String createDirectory(String strDirectory){
        String output = sharedInfo.getWorkingDirectory().toString()
                        +File.separator+strDirectory;
        boolean success = (new File(output)).mkdir();
                            if (success) {
                                System.out.println("Directory: " 
                                    + strDirectory + " created");
                            } 
                            
        String analysisReNamed = File.separator+sharedInfo.getAnalysisName().replace(" ", "_");
        return (output+File.separator+analysisReNamed);
    }
    
    /**
     * Opens a dialog to select a data file.
     * @param s The stage for a modal view
     * @param ext a list of ExtensionFilters to limit file options.
     */
    public void chooseDataFile(Stage s, List<FileChooser.ExtensionFilter> ext){
        fc = new FileChooser();
        fc.setTitle("Select Data File");
        File d = new File(getClass().getResource("."+File.separator).getFile());
        //fc.setInitialDirectory(new File("D://Work//NetBeansProjects//large_beds"));
        for (FileChooser.ExtensionFilter ef: ext)
            fc.getExtensionFilters().add(ef);
        File f = fc.showOpenDialog(s);
        if(f!=null){
            sharedInfo.setDataFile(f);
            //----Strip file of extension-----//
            String str = sharedInfo.getDataFile().getPath();
            File plinkDataExtensionStripped = new File(str.substring(0, str.lastIndexOf('.')));
            extension = str.substring(str.lastIndexOf('.')+1,str.length());
            sharedInfo.setPlinkDataExtensionStripped(plinkDataExtensionStripped);
            sharedInfo.setPlinkDataExtension(extension);
        }
    }
    
    /**
     * This method executes the RunPlink class.
     * @see RunPlink
     * @param cmd A LinkedHashMap of parameters and values (if they exist)
     * @param mode The mode to parse the subsequent log output of the parameters.
     */
    public void runPLINKcmd(LinkedHashMap<String, Object> cmd,String mode){
        try {
            rp.runPlinkCmd(cmd,mode);
            /*System.out.println("markers: " +dataProperties.getNumMarkers());
            System.out.println("individuals: "+dataProperties.getNumIndividuals());
            System.out.println("cases: "+dataProperties.getNumCases());
            System.out.println("controls: "+dataProperties.getNumControls());
            System.out.println("missing: "+dataProperties.getNumUnspecifiedSex());
            System.out.println("males: "+dataProperties.getNumMales());
            System.out.println("females: "+dataProperties.getNumFemales());
            System.out.println("unspecified sex: "+dataProperties.getNumUnspecifiedSex());*/
        } catch (Exception ex) {
            Logger.getLogger(GuiHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * A method to select the file parameter for PLINK (--bfile, --file etc..)
     * @return The corresponding parameter for a given file type
     */
    public String getFileTypeFlag(){
        return extensionCommand.get(extension);
    }
    
    /**
     * 
     * @return The Pie chart containing memory info.
     */
    
}
