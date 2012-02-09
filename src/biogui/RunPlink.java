package biogui;

import java.io.File;
import java.util.LinkedHashMap;
import org.apache.commons.exec.*;

/**
 * This class handles the execution of plink and parsing of it's log.
 * @author Ahwan Pandey
 */
public class RunPlink{
    
    private File plinkPath;
    private StringBuilder sb;
    private DefaultExecutor executor;
    private OutputMonitor outputMonitor;
    private PumpStreamHandler streamHandler;
    private int exitValue;
    private DataProperties dataProperties;
    
    /**
     * The constructor takes the plink executable being used and and instance of the 
     * DataProperties class that contains the various properties of the data being
     * analyzed.
     * @see DataProperties
     * @param pp The Plink executable
     * @param dp An instance of the DataProperties class
     */
    public RunPlink(File pp, DataProperties dp) {
        plinkPath = pp;
        executor = new DefaultExecutor();
        this.dataProperties = dp;
        //executor.setExitValue(1);
    }
    
    /**
     * This method parses the HashMap of commands, executes PLINK with the given parameters,
     * and sets up the StreamHandler that parses the PLINK log in real-time.
     * @param cmd The plink command itself in LinkedHashMap form
     * @param mode Identifies the type of PLINK log to parse.
     * @return The exitvalue of the execution.
     * @throws Exception 
     */
    public int runPlinkCmd(LinkedHashMap<String, Object> cmd,String mode) throws Exception{
        outputMonitor = new OutputMonitor(this.dataProperties,mode);
        streamHandler = new PumpStreamHandler(outputMonitor);
        executor.setStreamHandler(streamHandler);
        CommandLine command = CommandLine.parse(command(cmd));
        exitValue = executor.execute(command);
        return exitValue;
    }
    
    /**
     * A helper method to convert the HashMap of PLINK parameters into a string.
     * @param cmd LinkedHashMap of PLINK parameters
     * @return A string form of PLINK parameters in use.
     */
    private String command(LinkedHashMap<String, Object> cmd){
        sb = new StringBuilder();
        sb.append(plinkPath.getPath());
        
        for (String c: cmd.keySet()){
            sb.append(c);
            sb.append(cmd.get(c));
        }
        return sb.toString();
    }
    
    /**
     * This class monitors the log output of PLINK in real-time and updates the DataProperties
     * instance.
     */
    private class OutputMonitor extends LogOutputStream {
        DataProperties dataProperties;
        String mode;
        
        /**
         * 
         * @param dp An instance of the DataProperties class
         * @param md The mode to read the PLINK log in
         */
        public OutputMonitor(DataProperties dp, String md){
            dataProperties = dp;
            mode = md;
        }
        
        /**
         * This method parses the PLINK log output of PLINK in real-time. Uses
         * modes to identify how to parse each log output.
         * @param line Each line passes one by one from the PLINK log
         * @param level 
         */
        @Override
        protected void processLine(String line, int level) {
            switch(mode){
                case "readfile":
                    if(line.toUpperCase().contains("markers to be included from".toUpperCase()))
                        dataProperties.setNumMarkers(getValue(line, 0,"markers"));
                    else if(line.toUpperCase().contains("individuals read from".toUpperCase()))
                        dataProperties.setNumIndividuals(getValue(line,0,"individuals"));
                    else if(line.toUpperCase().contains("controls and".toUpperCase()) &&
                            !(line.toUpperCase().contains("After filtering".toUpperCase()))){
                        dataProperties.setNumCases(getValue(line,0,"cases"));
                        dataProperties.setNumControls(getValue(line,",","controls"));
                        dataProperties.setNumMissing(getValue(line,"and","missing"));
                    }
                    else if(line.toUpperCase().contains("males".toUpperCase()) &&
                            !(line.toUpperCase().contains("After filtering".toUpperCase()))){
                        dataProperties.setNumMales(getValue(line,0,"males"));
                        dataProperties.setNumFemales(getValue(line,",","females"));
                        dataProperties.setNumUnspecifiedSex(getValue(line,"and","of"));
                    }
                   break;
            }
        }
        
        protected int getValue(String l, String a, String b){
            return Integer.parseInt(l.substring(l.indexOf(a)+(a.length()+1), l.indexOf(b)-1).trim());
        }
        
        protected int getValue(String l, int a, String b){
            return Integer.parseInt(l.substring(a, l.indexOf(b)-1).trim());
        }
    }
    
    
}
