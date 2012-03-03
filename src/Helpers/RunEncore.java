/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import biogui.DataProperties;
import java.io.File;
import java.util.LinkedHashMap;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;

/**
 *
 * @author ahwan
 */
public class RunEncore {
    private File encorePath;
    private StringBuilder sb;
    private DefaultExecutor executor;
    private RunEncore.OutputMonitor outputMonitor;
    private PumpStreamHandler streamHandler;
    private int exitValue;
    private DataProperties dataProperties;
    
    /**
     * The constructor takes the Encore executable being used and and instance of the 
     * DataProperties class that contains the various properties of the data being
     * analyzed.
     * @see DataProperties
     * @param ep The Encore executable
     * @param dp An instance of the DataProperties class
     */
    public RunEncore(File ep, DataProperties dp) {
        encorePath = ep;
        executor = new DefaultExecutor();
        this.dataProperties = dp;
        //executor.setExitValue(1);
    }
    
    /**
     * This method parses the HashMap of commands, executes Encore with the given parameters,
     * and sets up the StreamHandler that parses the Encore log in real-time.
     * @param cmd The Encore command itself in LinkedHashMap form
     * @param mode Identifies the type of Encore log to parse.
     * @return The exitvalue of the execution.
     * @throws Exception 
     */
    public int runEncoreCmd(LinkedHashMap<String, Object> cmd,String mode) throws Exception{
        outputMonitor = new RunEncore.OutputMonitor(this.dataProperties,mode);
        streamHandler = new PumpStreamHandler(outputMonitor);
        executor.setStreamHandler(streamHandler);
        CommandLine command = CommandLine.parse(command(cmd));
        exitValue = executor.execute(command);
        return exitValue;
    }
    
    /**
     * A helper method to convert the HashMap of Encore parameters into a string.
     * @param cmd LinkedHashMap of Encore parameters
     * @return A string form of Encore parameters in use.
     */
    private String command(LinkedHashMap<String, Object> cmd){
        sb = new StringBuilder();
        sb.append(encorePath.getPath());
        
        for (String c: cmd.keySet()){
            sb.append(c);
            sb.append(cmd.get(c));
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    
    private class OutputMonitor extends LogOutputStream {
        DataProperties dataProperties;
        String mode;
        
        /**
         * 
         * @param dp An instance of the DataProperties class
         * @param md The mode to read the Encore log in
         */
        public OutputMonitor(DataProperties dp, String md){
            dataProperties = dp;
            mode = md;
        }
        
        /**
         * This method parses the log output of Encore in real-time. Uses
         * modes to identify how to parse each log output.
         * @param line Each line passes one by one from the Encore log
         * @param level 
         */
        @Override
        protected void processLine(String line, int level) {
            //System.out.println(line);
            switch(mode){
                case "readfile":
                    if(line.toUpperCase().contains("markers to be included from".toUpperCase()))
                        dataProperties.setNumMarkers(getValueBetween(line, 0,"markers"));
                    else if(line.toUpperCase().contains("individuals read from".toUpperCase()))
                        dataProperties.setNumIndividuals(getValueBetween(line,0,"individuals"));
                    else if(line.toUpperCase().contains("controls and".toUpperCase()) &&
                            !(line.toUpperCase().contains("After filtering".toUpperCase()))){
                        dataProperties.setNumCases(getValueBetween(line,0,"cases"));
                        dataProperties.setNumControls(getValueBetween(line,",","controls"));
                        dataProperties.setNumMissing(getValueBetween(line,"and","missing"));
                    }
                    else if(line.toUpperCase().contains("males".toUpperCase()) &&
                            !(line.toUpperCase().contains("After filtering".toUpperCase()))){
                        dataProperties.setNumMales(getValueBetween(line,0,"males"));
                        dataProperties.setNumFemales(getValueBetween(line,",","females"));
                        dataProperties.setNumUnspecifiedSex(getValueBetween(line,"and","of"));
                    }
                    else if(line.toUpperCase().contains("founders".toUpperCase())){
                            dataProperties.setNumFounders(getValueBetween(line, 0,"founders"));
                            dataProperties.setNumNonFounders(getValueBetween(line, "and","non-founders"));
                    }
                   break;
                    
                case "extract_or_include":
                    if(line.toUpperCase().contains("Reading individuals".toUpperCase()))
                        dataProperties.setExcludeOrIncludeSubjCount(getValueBetween(line, "...","read"));
                    else if(line.toUpperCase().contains("Reading list of SNPs".toUpperCase()))
                        dataProperties.setExcludeOrIncludeSnpCount(getValueBetween(line, "...","read"));
                    
                    break;
                    
                case "data_driven":
                    if(line.toUpperCase().contains("low genotyping".toUpperCase()))
                        dataProperties.setNumOfSubjsRemovedByddFilter(getValueBetween(line, 0,"of"));
                    else if(line.toUpperCase().contains("failed missingness test".toUpperCase()))
                        dataProperties.setNumOfSnpsRemovedByddFilter(getValueBetween(line, 0,"SNPs"));
                    else if(line.toUpperCase().contains("failed frequency test".toUpperCase()))
                        dataProperties.setNumOfSnpsRemovedByddFilter(getValueBetween(line, 0,"SNPs"));
                    break;
                 
                case "ld_prune":
                    if(line.toUpperCase().contains("Scanning from chromosome".toUpperCase())){
                        if((line.substring(line.indexOf("to")+"to".length()).trim()).equals("MT")){
                            dataProperties.setTotalChromosomes(26);
                        }
                        else
                            dataProperties.setTotalChromosomes(getValueBetween(line,"to"));
                    }
                    else if(line.toUpperCase().contains("Scan region".toUpperCase())){  
                        int chr = getValueBetween(line, "chromosome","from");
                        dataProperties.setOnCromosomeLd(chr);
                        dataProperties.updateLdProgressbar();
                        dataProperties.setScanningRegion(line);
                        
                    }
                    else if(line.toUpperCase().contains("Analysis finished".toUpperCase())){
                        dataProperties.setLdProgressDone();
                    }
                    break;
                
                case "ld_extract": //just so we creata a new data files after ld pruning
                    break;
                
                case "model_tests":
                    break;
                    
            }
        }
        
        protected int getValueBetween(String l, String a, String b){
            return Integer.parseInt(l.substring(l.indexOf(a)+(a.length()+1), l.indexOf(b)-1).trim());
        }
        
        protected int getValueBetween(String l, int a, String b){
            return Integer.parseInt(l.substring(a, l.indexOf(b)-1).trim());
        }
        
        protected int getValueBetween(String l, String a){
            return Integer.parseInt(l.substring(l.indexOf(a)+a.length()).trim() );
        }
    }
}
