/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biogui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

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
    
}
