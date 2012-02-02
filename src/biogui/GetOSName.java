/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biogui;

/**
 *
 * @author ahwan
 */

public class GetOSName{
    private String osName;
    
    public String GetOSName(){
        try{
            osName= System.getProperty("os.name");
        }catch (Exception e){
            System.out.println("Exception caught ="+e.getMessage());
            System.exit(1);
        }
        return osName;
    }
}