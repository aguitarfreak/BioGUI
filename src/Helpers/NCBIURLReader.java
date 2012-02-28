/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ahwan
 */
public class NCBIURLReader {
    
    private Map<String,String> rsSnpGene = new HashMap<>();
    private Map<String,String> affySnpGene = new HashMap<>();
    private Map<String,String> snpGene = new HashMap<>();
    private int success = 0;
        
    public NCBIURLReader(String rsparam,List<String> affyparam){
        if(!rsparam.equals("")){
            rsSearch(rsparam);
        }
        if(!affyparam.isEmpty()){
            affySearch(affyparam);
        }
        
        snpGene.putAll(rsSnpGene);
        snpGene.putAll(affySnpGene);
    }
    
    protected void rsSearch(String param){
        try{
            String url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?";
            String charset = "UTF-8";
            String value = "db=snp&id=";
            String param1 = URLEncoder.encode(param.replace("rs",""),charset);
            String query = value+param1;
        
            HttpURLConnection connection = (HttpURLConnection)(new URL(url).openConnection());
            connection.setDoOutput(true);
            //connection.setRequestMethod("POST");
            //connection.setRequestProperty("Accept-Charset", charset);
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            //connection.setRequestProperty("Input-Type", "submit");

            OutputStream output = null;
            output = connection.getOutputStream();
            output.write(query.getBytes(charset));
            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {};

            InputStream response = connection.getInputStream();
            BufferedReader reader = null;
            
            String thisSnp = "";
            String thisGene = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response, charset));
                    reader.readLine();reader.readLine();reader.readLine();
                    for (String line; (line = reader.readLine()) != null;) {
                        if(line.contains("ERROR")){
                            thisSnp = "rs"+line.substring(line.indexOf("=")+1,line.indexOf(":"));
                            thisGene = "";
                            rsSnpGene.put(thisSnp, thisGene);
                        }
                        else if(line.contains("Id")){
                            thisSnp = "rs"+line.substring(line.indexOf(">")+1, line.indexOf("</"));
                        }
                        
                        else if(line.contains("\"GENE\"")){
                            thisGene = line.substring(line.indexOf(">")+1, line.indexOf("</"));
                            rsSnpGene.put(thisSnp, thisGene);
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
    
    protected void affySearch(List<String> param){
        try{
            String charset = "UTF-8";
            
            String searchUrl ="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?";
            String searchUrlStartValue = "db=snp&term=";
            String searchUrlEndValue = "&usehistory=y&retmax=1";
            
            String retrieveUrl = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?";
            String retrieveUrlStartValue = "db=snp&WebEnv=";
            String retrieveUrlEndValue = "&query_key=";
            
            for(String snp: param){
                //create connection for esearch---------------------//
                    HttpURLConnection searchConnection = (HttpURLConnection)(new URL(searchUrl).openConnection());
                    searchConnection.setDoOutput(true);
                    OutputStream searchOutput = null;
                    searchOutput = searchConnection.getOutputStream();

                    String SearchQuery = searchUrlStartValue+snp+searchUrlEndValue;
                    searchOutput.write(SearchQuery.getBytes(charset));
                    if (searchOutput != null) try { searchOutput.close(); } catch (IOException logOrIgnore) {};

                    InputStream searchResponse = searchConnection.getInputStream();
                    BufferedReader searchReader = null;
                //-------------------------------------------------//
                    
                try {
                    String webEnv = "";
                    String queryKey = "";
                    String count = "";
                    searchReader = new BufferedReader(new InputStreamReader(searchResponse, charset));
                    searchReader.readLine();searchReader.readLine();
                    for (String line; (line = searchReader.readLine()) != null;) {
                        if(line.contains("<eSearchResult>")){
                            count = line.substring(line.indexOf("<Count>")+7,line.indexOf("</Count>"));
                            if(!count.equals("0")){
                                webEnv = line.substring(line.indexOf("<WebEnv>")+8,line.indexOf("</WebEnv>"));
                                queryKey = line.substring(line.indexOf("<QueryKey>")+10,line.indexOf("</QueryKey>"));
                                
                                //create connection for esummary--------------------------//
                                    HttpURLConnection retrieveConnection = (HttpURLConnection)(new URL(retrieveUrl).openConnection());
                                    retrieveConnection.setDoOutput(true);
                                    OutputStream retrieveOutput = null;
                                    retrieveOutput = retrieveConnection.getOutputStream();

                                    String retrieveQuery = retrieveUrlStartValue+webEnv+retrieveUrlEndValue+queryKey+"&retmax=1";
                                    retrieveOutput.write(retrieveQuery.getBytes(charset));
                                    if (retrieveOutput != null) try { retrieveOutput.close(); } catch (IOException logOrIgnore) {};

                                    InputStream retrieveResponse = retrieveConnection.getInputStream();
                                    BufferedReader retrieveReader = null;
                                //--------------------------------------------------------//
                                
                                String thisGene = "";
                                try {
                                    retrieveReader = new BufferedReader(new InputStreamReader(retrieveResponse, charset));
                                    retrieveReader.readLine();retrieveReader.readLine();retrieveReader.readLine();
                                    for (String retrieveLine; (retrieveLine = retrieveReader.readLine()) != null;) {
                                        if(retrieveLine.contains("\"GENE\"")){
                                            thisGene = retrieveLine.substring(retrieveLine.indexOf(">")+1, retrieveLine.indexOf("</"));
                                            affySnpGene.put(snp, thisGene);
                                        }
                                    }
                                }finally {
                                    if (retrieveReader != null) try { retrieveReader.close(); } catch (IOException err) {}
                                }
                            }else{
                                affySnpGene.put(snp,"");
                            }
                        }
                    }
                } finally {
                    if (searchReader != null) try { searchReader.close(); } catch (IOException err) {}
                }
            }   
        }catch(Exception ex) {
            System.err.println("Exception caught:\n"+ ex.toString());
        }
            
    }
    
    protected int getSuccesssful(){
        return success;
    }
    
    public Map<String,String> getSnpGeneMap(){
        return snpGene;
    }
    
//    public static void main(String[] args){
//        String rsid = "rs1000050,rs10047059,rs45549452348,rs709932";
//        List<String> Affyid = new ArrayList<>();
//        Affyid.add("SNP_A-1782686");Affyid.add("SNP_A-1800852");Affyid.add("SNP_A-17825656686");Affyid.add("SNP_A-1907583");Affyid.add("SNP_A-2072760");
//        
//        NCBIURLReader ncbi = new NCBIURLReader(rsid,Affyid);
//        System.out.println(ncbi.getSnpGeneMap());
//    }
}