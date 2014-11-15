/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import investor.charts.CandleChart;
import investor.charts.CandleChart.CandleStickChart;
import investor.charts.LinearChartManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Tomasz
 */
public class CurrenciesCorrelationsView extends InvestorView {
    
    private LineChart lineChart;
    
    public void InitView(){
  
            pane = new BorderPane();
            //Label label = new Label("Wskaźniki giełdowe");
            
            String month[] = {"Jan", "Feb", "Mar", "Apr", "May"};              //przykladowe dane do wykresow
            double v1[] = {1000.1, 1000.4, 1000.8, 1000.2, 1000.5, 1000.3, 1000.6};
            double v2[] = {1000.4, 1000.1, 1000.3, 1000.6, 1000.9, 1000.4, 1000.3};
            
            
            XYChart.Series s = LinearChartManager.createSeries("jeden", month, v1);
            XYChart.Series s1 = LinearChartManager.createSeries("dwa", month, v2);
            
            lineChart = LinearChartManager.linear();
            LinearChartManager.addSeries(lineChart, s);
            LinearChartManager.addSeries(lineChart, s1);
            
            
            ////////utworzenie wykresu swiecowego
            
            BorderPane bPane = (BorderPane)pane;
            
            CandleChart candle = new CandleChart();
            CandleStickChart chart = candle.createChart();
            pane.getStylesheets().add("resources/css/CandleStickChart.css"); 
            
            bPane.setCenter(chart);
            
            /////////////////////////////////////
    }
    
    public LineChart GetChart(){
        return lineChart;
    }
}
