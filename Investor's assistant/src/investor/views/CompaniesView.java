/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import investor.charts.LinearChartManager;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Tomasz
 */
public class CompaniesView extends InvestorView{
    //private BorderPane borderPane;
    private LineChart lineChart;
    
    public void InitView(){
         pane = new BorderPane();
         
         BorderPane borderPane = (BorderPane)pane;
        //Label label = new Label("Wskaźniki giełdowe");

        String month[] = {"Jan", "Feb", "Mar", "Apr", "May"};              //przykladowe dane do wykresow
        double v1[] = {1000.1, 1000.4, 1000.8, 1000.2, 1000.5, 1000.3, 1000.6};
        double v2[] = {1000.4, 1000.1, 1000.3, 1000.6, 1000.9, 1000.4, 1000.3};
        
        XYChart.Series s = LinearChartManager.createSeries("jeden", month, v1);   
        XYChart.Series s1 = LinearChartManager.createSeries("dwa", month, v2);
 
        lineChart = LinearChartManager.linear();
        
        LinearChartManager.addSeries(lineChart, s);
        LinearChartManager.addSeries(lineChart, s1);
        
        borderPane.setCenter(lineChart);
        //return pane;
    }
    
    public LineChart GetChart(){
        return lineChart;
    }
}
