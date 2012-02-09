package biogui;

/**
 * A helper class to retrieve the name of the OS.
 * @author Ahwan Pandey
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