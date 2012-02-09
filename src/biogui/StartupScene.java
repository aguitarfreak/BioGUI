package biogui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The startup scene for the GUI. Contains a load analysis option(not yet implemented),
 * and a new analysis option
 * @see BioGUI
 * @see GuiHelper
 * @author Ahwan Pandey
 */
public class StartupScene {
    
    public Button buttonNew;
    public Button buttonLoad;
    public Label status;
    public Label os;
    public Scene startupScene;
    private GuiHelper gh;
    private Stage pri;
    private HBox EnCore;

    /**
     * The constructor takes an instance of the GuiHelper class and the primary stage.
     * @param guiHelp An instance of the GuIHelper class
     * @param primary The primary stage
     */
    public StartupScene(GuiHelper guiHelp,Stage primary){
        pri = primary;
        gh = guiHelp;
        VBox optionsBox = new VBox();
        optionsBox.setPadding(new Insets(10,10,10,10));
        optionsBox.setSpacing(10);
        buttonLoad = new Button("Load Analysis");
        buttonNew = new Button("New Analysis");
        status = new Label("");
        os = new Label(guiHelp.sharedInfo.getOSName());
        
        //Create the EcCore logo---------------------------------------//
        Text En = new Text("En");
        En.setStyle("-fx-font-size: 80pt");
        Stop[] stops1 = new Stop[] { new Stop(0, Color.ORANGERED), new Stop(1, Color.MAROON)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops1);
        En.setFill(lg1);
        
        Text Core = new Text("Core");
        Core.setStyle("-fx-font-size: 80pt");
        Stop[] stops2 = new Stop[] { new Stop(0, Color.CYAN), new Stop(1, Color.DODGERBLUE)};
        LinearGradient lg2 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops2);
        Core.setFill(lg2);
        DropShadow ds = new DropShadow();
        //ds.setOffsetY(3.0);
        ds.setRadius(50);
        ds.setSpread(0.25);
        ds.setColor(Color.DODGERBLUE);
        Core.setEffect(ds);
        Core.setCache(true);
        
        EnCore = new HBox();
        EnCore.getChildren().addAll(En,Core);
        EnCore.setAlignment(Pos.CENTER);
        //--------------------------------------------------------------//
        
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.getChildren().addAll(os,EnCore,buttonLoad,buttonNew,status);
        
        startupScene = new Scene(optionsBox,guiHelp.sharedInfo.getGuiWidth(),
                                    guiHelp.sharedInfo.getGuiHeight());
        
    }
    
}
