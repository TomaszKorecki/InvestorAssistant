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
        int v1[] = {1000, 1070, 1100, 1130, 1200};

        XYChart.Series s = LinearChartManager.createSeries("jeden", month, v1);
        lineChart = LinearChartManager.linear(s);
        
        borderPane.setCenter(lineChart);
        //return pane;
    }
    
    public LineChart GetChart(){
        return lineChart;
    }
}
