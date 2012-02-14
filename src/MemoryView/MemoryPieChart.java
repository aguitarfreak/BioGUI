/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MemoryView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author ahwan
 */
public class MemoryPieChart {
    public java.util.Timer timer = new java.util.Timer();
    private PieChart chart;
    private MemoryTracker mt;
    private Text usedText, freeText;
    private StackPane chartStack;
    
    public MemoryPieChart(){
        mt = new MemoryTracker();
        timer.schedule(mt, 0L, 3000L);
        
        PieChart.Data used = new PieChart.Data("Used", mt.usedMemory.getValue());
        PieChart.Data free = new PieChart.Data("Free", mt.freeMemory.getValue());
        used.pieValueProperty().bind(mt.usedMemory);
        free.pieValueProperty().bind(mt.freeMemory);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(used,free);
        
        chart = new PieChart(pieChartData);
        chart.setLabelLineLength(5);
        chart.setLegendVisible(false);
        chart.setMinSize(130, 130);
        chart.setMaxSize(100,100);
        usedText = new Text();
        freeText = new Text();
        usedText.textProperty().bind(mt.usedMemory.asString());
        freeText.textProperty().bind(mt.freeMemory.asString());

        
        HBox memTextHbox = new HBox();
        memTextHbox.setSpacing(2);
        //memTextHbox.setAlignment(Pos.TOP_CENTER);
        memTextHbox.getChildren().addAll(new Label("F:"),freeText,new Label("U:"), usedText);
        
        chartStack = new StackPane();
        chartStack.getChildren().add(memTextHbox);
        chartStack.getChildren().add(chart);
        chartStack.setMaxSize(100, 100);
        chartStack.setMinSize(100, 100);
        chartStack.setStyle("-fx-background-color: azure");
        
    }
    
    public StackPane getChart(){
        return chartStack;
    }
 
}
