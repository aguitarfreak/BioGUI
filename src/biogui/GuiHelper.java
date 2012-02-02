/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biogui;

import plinkster.RunPlink;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
/**
 *
 * @author ahwan
 */
public class GuiHelper {
    private DirectoryChooser dc;
    private FileChooser fc;
    public SharedInfo sharedInfo;
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
    
    private HashMap<String,String> extensionCommand;
    
    public GuiHelper(){
        sharedInfo = new SharedInfo();
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
        
        rp = new RunPlink(sharedInfo.getPlinkExecutable());
        
        extensionCommand = new HashMap<>();
        extensionCommand.put("bed"," --bfile ");
        extensionCommand.put("ped"," --file ");
        extensionCommand.put("regain"," --regain-file ");
    }
    
    public void chooseWorkingDirectory(Stage s){
        dc.setTitle("Select Working Directory");
        sharedInfo.setWorkingDirectory(dc.showDialog(s));
    }
    
    public void chooseDataFile(Stage s, List<FileChooser.ExtensionFilter> ext){
        fc = new FileChooser();
        fc.setTitle("Select Data File");
        File d = new File(getClass().getResource(".").getFile());
        fc.setInitialDirectory(d);
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
    
    public void runPLINKcmd(LinkedHashMap<String, Object> cmd){
        try {
            rp.runPlink(cmd,true);
        } catch (Exception ex) {
            Logger.getLogger(GuiHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getFileTypeFlag(){
        return extensionCommand.get(extension);
    }
    
}
