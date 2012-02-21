/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelTests;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahwan
 */
public class ModelReaderHelper {
    private BufferedReader assoc_bReader, model_bReader;
    private File assocFile;
    private File modelFile;
    private List testType;
    private int numOfSnps;
    
    final private int chrCol_model = 1;
    final private int snpCol_model = 2;
    final private int testCol = 5;
    final private int pValueCol_model = 10;
    final private int snpCol_assoc = 2;
    final private int oddsRatioCol_assoc = 10;
    
    private List<String[]> allTopModelSnps; //contains all the top N snps
    private Map<String, String[]> assocMap; //index---> 0:Odds Ratio
    private Map<String, Double> allSnpsPvalueMap; //contains the Double parsed pValues
    
    public ModelReaderHelper(File model, List typeOfTest, int count){
        modelFile = model;
        assocFile = new File(modelFile.getPath().replace(".model", ".assoc"));
        testType = typeOfTest;
        numOfSnps = count;
    }
    
    public void readModelFile(){
        try {
            model_bReader = new BufferedReader(new FileReader(modelFile.getPath()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ModelReaderHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String line;
        allTopModelSnps= new ArrayList<>(); // stores the resulting top N snps
        List<String[]> snpArray = new ArrayList(); //stores the tests for each SNP
        Map<String, Double> currentSnpPvalueMap = new HashMap(); //stores Double casted p-values for all test of the the current SNP
        allSnpsPvalueMap = new HashMap(); //stores Double casted p-values for all the top Snps
        
        int line_num = 0;
        try {
            model_bReader.readLine();
            while((line = model_bReader.readLine()) != null){
                //each snp at a time
                if(line_num<4){
                    String thisSnp[] = line.split("\\s+");
                    if(testType.contains(thisSnp[testCol]) && (!thisSnp[pValueCol_model].equals("NA"))){
                        snpArray.add(thisSnp);
                        currentSnpPvalueMap.put(thisSnp[testCol],Double.parseDouble(thisSnp[pValueCol_model]));
                    }
                    line_num++;
                    
                //when we have all the tests for each snp
                }else{
                    String thisSnp[] = line.split("\\s+");
                    if(testType.contains(thisSnp[testCol]) && (!thisSnp[pValueCol_model].equals("NA"))){
                        snpArray.add(thisSnp);
                        currentSnpPvalueMap.put(thisSnp[testCol],Double.parseDouble(thisSnp[pValueCol_model]));
                    }
                    
                    if(!currentSnpPvalueMap.isEmpty()){
                        String[] selectedSnp = snpArray.get(0);
                        //check the array for the smallest p-value for each snp
                        for(int i = 1; i<snpArray.size();i++){
                            String tempSnp[] = snpArray.get(i);
                            if(currentSnpPvalueMap.get(tempSnp[testCol]) < currentSnpPvalueMap.get(selectedSnp[testCol]))
                                selectedSnp = tempSnp;
                        }
                        allSnpsPvalueMap.put(selectedSnp[snpCol_model],currentSnpPvalueMap.get(selectedSnp[testCol]));

                        if(allTopModelSnps.size()<numOfSnps)
                            allTopModelSnps.add(selectedSnp);
                        else{
                            allTopModelSnps.add(selectedSnp);
                            String[] largest = selectedSnp;
                            for(int i = 0; i< allTopModelSnps.size()-1; i++){
                                String[] compareThis = allTopModelSnps.get(i);
                                if(allSnpsPvalueMap.get(compareThis[snpCol_model]) > allSnpsPvalueMap.get(largest[snpCol_model]))
                                    largest = compareThis;
                            }
                            allTopModelSnps.remove(largest);
                            allSnpsPvalueMap.remove(largest[snpCol_model]);
                        }
                    }
                    snpArray.clear();
                    currentSnpPvalueMap.clear();
                    line_num=0;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ModelReaderHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
//        for(int i = 0; i<allTopModelSnps.size();i++){
//                for(int j = 0; j<allTopModelSnps.get(i).length;j++){
//                    System.out.print(allTopModelSnps.get(i)[j]+"\t");
//                }
//                System.out.println();
//            }  
    }
 
    /**
     * Read the required data values from the association file for top Stored SNPs
     */
    public void updateAssociationResults(){
        try {
            assoc_bReader = new BufferedReader(new FileReader(assocFile.getPath()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ModelReaderHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        assocMap = new HashMap();
        String line;
        try {
            assoc_bReader.readLine();
            while((line = assoc_bReader.readLine()) != null){
                String thisSnp[] = line.split("\\s+");
                if(allSnpsPvalueMap.containsKey(thisSnp[snpCol_assoc])){
                    String assocData[] = {thisSnp[oddsRatioCol_assoc]};
                    assocMap.put(thisSnp[snpCol_assoc], assocData);
                }
                    
            }
        } catch (IOException ex) {
            Logger.getLogger(ModelReaderHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        for(int i = 0; i<allTopModelSnps.size();i++){
//            for(int j = 0; j<allTopModelSnps.get(i).length;j++){
//                    System.out.print(allTopModelSnps.get(i)[j]+"\t");
//            }
//            System.out.print(assocMap.get(allTopModelSnps.get(i)[snpCol_model])[0]);
//            System.out.println();
//        }   
    }
    
    public List<String[]>getTopModelSnps(){
        return allTopModelSnps;
    }
    
    public Map<String, String[]> getAssocMap(){
        return assocMap;
    }
    
    public int getChrCol(){
        return chrCol_model;
    }
    
    public int getSnpNameCol(){
        return snpCol_model;
    }            
    
    public int getTestTypeCol(){
        return testCol;
    }
    
    public int getPvalueCol(){
        return pValueCol_model;
    }
    
    public int getOddsRatioCol(){
        return 0;
    }
    
//    public static void main(String[] args){
//        
//        File assocFilename = new File("modelAndAssociation//test.model");
//        List testType = new ArrayList();
//        testType.add("TREND");
//        //testType.add("GENO");
//        //testType.add("ALLELIC");
//        //testType.add("DOM");
//        //testType.add("REC");
//        
//        ModelReaderHelper frh = new ModelReaderHelper(assocFilename,testType,100);
//        frh.readModelFile();
//        frh.updateAssociationResults();
//        
//    }
}
