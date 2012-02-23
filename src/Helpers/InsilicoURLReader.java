/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ahwan
 */
public class InsilicoURLReader {
    
    Map<String,String> snpGene;
    int success = 0;
        
    public InsilicoURLReader(String param){
        try{
            String url = "http://insilico.utulsa.edu/webgain/snp2gene";
            String charset = "UTF-8";
            String value = "snplist=";
            String param1 = URLEncoder.encode(param,charset);
            String query = value+param1;
        
            HttpURLConnection connection = (HttpURLConnection)(new URL(url).openConnection());
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Input-Type", "submit");

            OutputStream output = null;
            output = connection.getOutputStream();
            output.write(query.getBytes(charset));
            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {};

            InputStream response = connection.getInputStream();
            BufferedReader reader = null;
            snpGene = new HashMap<>();
            String thisSnp = "";
            String thisGene = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response, charset));
                    int atline = 1;
                    reader.readLine();

                    for (String line; (line = reader.readLine()) != null;) {
                        if(atline==3){
                            thisSnp = line.replace("<th>","").replace("</th>", "").replace("\t","");
                        }
                        else if(atline==8){
                            if(!line.equals("</table>"))
                                thisGene = line.replace("<td>","").replace("</td>", "").replace("\t","");
                            else{thisGene="";}
                        }
                        atline++;
                        if ((atline==12) || line.contains("header")){
                            snpGene.put(thisSnp,thisGene); 
                            atline=1;
                        }   
                    }
                } finally {
                    if (reader != null) try { reader.close(); } catch (IOException err) {}
                }
            success = 1;
        
        }catch(Exception ex) {
            System.err.println("Exception caught:\n"+ ex.toString());
        }
    }
    
    public int getSuccesssful(){
        return success;
    }
    
    public Map<String,String> getSnpGeneMap(){
        return snpGene;
    }
}