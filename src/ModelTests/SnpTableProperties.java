/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelTests;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ahwan
 */
public class SnpTableProperties {
    
    private final SimpleStringProperty snpChr;
    private final SimpleStringProperty snpName;
    private final SimpleStringProperty geneName;
    private final SimpleStringProperty snpTestType;
    private final SimpleDoubleProperty snpPvalue;
    private final SimpleDoubleProperty snpOddsRatio;
    
    protected SnpTableProperties(String sChr, String sName, String sTestType, double sPvalue, double sOR, String gName){
        this.snpChr = new SimpleStringProperty(sChr);
        this.snpName = new SimpleStringProperty(sName);
        this.snpTestType = new SimpleStringProperty(sTestType);
        this.snpPvalue = new SimpleDoubleProperty(sPvalue);
        this.snpOddsRatio = new SimpleDoubleProperty(sOR);
        this.geneName = new SimpleStringProperty(gName);
    }
    
    public String getSnpChr(){
        return snpChr.get();
    }
    
    public String getSnpName(){
        return snpName.get();
    }
    
    public String getSnpTestType(){
        return snpTestType.get();
    }
    
    public double getSnpPvalue(){
        return snpPvalue.get();
    }
    
    public double getSnpOddsRatio(){
        return snpOddsRatio.get();
    }
    
    public SimpleStringProperty geneNameProperty(){
        return geneName;
    }
    
    public String getGeneName(){
        return geneName.get();
    }
    
    public void setGeneName(String name){
        this.geneName.set(name);
    }
    
}
