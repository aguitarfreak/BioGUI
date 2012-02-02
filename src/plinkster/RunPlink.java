/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plinkster;

import biogui.GuiHelper;
import java.io.File;
import java.util.LinkedHashMap;
import org.apache.commons.exec.*;

/**
 *
 * @author ahwan
 */
public class RunPlink {
    private File plinkPath;
    private StringBuilder sb;
    
    public RunPlink(File pp){
        plinkPath = pp;
        
    }
    
    public void runPlink(LinkedHashMap<String, Object> cmd,boolean blocking) throws Exception{
        CommandLine command = CommandLine.parse(command(cmd));
        DefaultExecutor executor = new DefaultExecutor();
        if(blocking)
            executor.execute(command);
        else{
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            executor.execute(command,resultHandler);
            executor.notifyAll();
        }
    }
    
    public String command(LinkedHashMap<String, Object> cmd){
        sb = new StringBuilder();
        sb.append(plinkPath.getPath());
        
        for (String c: cmd.keySet()){
            sb.append(c);
            sb.append(cmd.get(c));
        }
        
        return sb.toString();
    }
    
}
