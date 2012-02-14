/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biogui;

import javafx.beans.property.*;

/**
 *
 * @author ahwan
 */
public class DataProperties {
    
    private IntegerProperty numMarkers = new SimpleIntegerProperty();
    private IntegerProperty numIndividuals = new SimpleIntegerProperty();
    private IntegerProperty numCases = new SimpleIntegerProperty();
    private IntegerProperty numControls = new SimpleIntegerProperty();
    private IntegerProperty numMissing = new SimpleIntegerProperty();
    private IntegerProperty numMales = new SimpleIntegerProperty();
    private IntegerProperty numFemales = new SimpleIntegerProperty();
    private IntegerProperty numUnspecifiedSex = new SimpleIntegerProperty();
    
    private IntegerProperty excludeOrIncludeSnpCount = new SimpleIntegerProperty();
    private IntegerProperty excludeOrIncludeSubjCount = new SimpleIntegerProperty();
    private IntegerProperty ddFilterSnpCount = new SimpleIntegerProperty();
    private IntegerProperty ddFilterSubjCount = new SimpleIntegerProperty();
    
    private IntegerProperty totalChromosomes = new SimpleIntegerProperty();
    private IntegerProperty onChromosomeLd = new SimpleIntegerProperty();
    private DoubleProperty updateLdProgressBar = new SimpleDoubleProperty();
    
    //-----------------SETTERS------------------//
    public void setNumMarkers(int num){
        numMarkers.set(num);
    }
    
    public void setNumIndividuals(int num){
        numIndividuals.set(num);
    }
    
    public void setNumMissing(int num) {
        numMissing.set(num);
    }
    
    public void setNumCases(int num){
        numCases.set(num);
    }
    
    public void setNumControls(int num){
        numControls.set(num);
    }
    
    public void setNumMales(int num) {
        numMales.set(num);
    }

    public void setNumFemales(int num) {
        numFemales.set(num);
    }
    
    public void setNumUnspecifiedSex(int num){
        numUnspecifiedSex.set(num);
    }
    
    /**
     * 
     * @return Set number of snps that will be incuded or excluded.
     */
    public void setExcludeOrIncludeSnpCount(int num){
        excludeOrIncludeSnpCount.set(num);
    }
    
    /**
     * 
     * @return Set number of subjects that will be incuded or excluded.
     */
    public void setExcludeOrIncludeSubjCount(int num){
        excludeOrIncludeSubjCount.set(num);
    }
    
    public void setNumOfSnpsRemovedByddFilter(int num){
        ddFilterSnpCount.set(ddFilterSnpCount.get()+num);
    }
    
    public void setNumOfSubjsRemovedByddFilter(int num){
        ddFilterSubjCount.set(num);
    }
    
    public void setTotalChromosomes(int num){
        totalChromosomes.set(num);
    }
    
    public void setOnCromosomeLd(int chr){
        onChromosomeLd.set(chr);
    }
    
    public void resertLdProgress(){
        updateLdProgressBar.set(0);
    }
    
    public void updateLdProgressbar(){
        updateLdProgressBar.set((updateLdProgressBar.get()+onChromosomeLd.get())/totalChromosomes.get());
    }
    
    //-------------GETTERS-----------------------//
    public IntegerProperty getNumMarkers(){
        return numMarkers;
    }
    
    public IntegerProperty getNumIndividuals(){
        return numIndividuals;
    }
    
    public IntegerProperty getNumMissing() {
        return numMissing;
    }
    
    public IntegerProperty getNumCases(){
        return numCases;
    }
    
    public IntegerProperty getNumControls(){
        return numControls;
    }

    public IntegerProperty getNumMales() {
        return numMales;
    }

    public IntegerProperty getNumFemales() {
        return numFemales;
    }
    
    public IntegerProperty getNumUnspecifiedSex(){
        return numUnspecifiedSex;
    }
    
    /**
     * 
     * @return Number of snps that will be excluded or included
     */
    public IntegerProperty getExcludeOrIncludeSnpCount(){
        return excludeOrIncludeSnpCount;
    }
    /**
     * 
     * @return Number of snps that will be excluded or included
     */
    public IntegerProperty getExcludeOrIncludeSubjCount(){
        return excludeOrIncludeSubjCount;
    }
    
    public IntegerProperty getNumOfSnpsRemovedByddFilter(){
        return ddFilterSnpCount;
    }
    
    public IntegerProperty getNumOfSubjsRemovedByddFilter(){
        return ddFilterSubjCount;
    }
    
    public IntegerProperty getOnCromosomeLd(){
        return onChromosomeLd;
    }
    
    public IntegerProperty getNumOfChromosomes(){
        return totalChromosomes;
    }
    
    public DoubleProperty getLdProgress(){
        return updateLdProgressBar;
    }
}
