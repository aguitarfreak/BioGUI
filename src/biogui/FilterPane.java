package biogui;

import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

/**
 * This class sets up graphical elements of the filtering section of the accordion
 * component
 * @author Ahwan Pandey
 */
public class FilterPane {
    
    private TitledPane filterPane;
    private TabPane filterTabPanes;
    private GuiHelper guiHelp;
    
    FilterPane(GuiHelper gh) {
        guiHelp = gh;
        
        filterPane = new TitledPane();
        filterPane.setText("Data Filtering");
        
        filterTabPanes = new TabPane();
        filterTabPanes.setSide(Side.LEFT);
        filterTabPanes.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        //------------PREFILTERING--------------------------//
        
        Tab preFiltersTab = new Tab("Pre Filters");
        HBox preFiltersHbox = new HBox();
        
        
        preFiltersTab.setContent(preFiltersHbox);
        
        //-------------------------------------------------//
        
        //-----------FILTERING ALGORITHMS------------------//
        
        
        
        
        //-------------------------------------------------//
        
        filterTabPanes.getTabs().addAll(preFiltersTab);
        filterPane.setContent(filterTabPanes);
        filterPane.disableProperty().bind(gh.sharedInfo.getfileInputComplete().not());
    }
    
    public TitledPane getfilterPane(){
        return filterPane;
    }
    
}
