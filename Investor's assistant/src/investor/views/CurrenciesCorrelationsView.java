/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import investor.charts.CandleChart;
import investor.charts.CandleChart.CandleStickChart;
import investor.charts.LinearChartManager;
import investor.data.Index;
import java.util.Date;
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
            
//            CandleChart candle = new CandleChart();
//            CandleStickChart chart = candle.createChart();
            
            
            //dla przykładu, dwa obiekty typu index
            Index data[] = new Index[2];
            
            Index index = new Index();
        index.setSymbol("WIG20");
        index.setName("WIG20");
        index.setDay("1");
        index.setHour("17:15");
        index.setMin_val(16);
        index.setMax_val(32);
        index.setOpen_val(25);
        index.setClose_val(20);
        index.setVol_val(30);
       
        Index index1 = new Index();
        index1.setSymbol("WIG20");
        index1.setName("WIG20");
        index1.setDay("2");
        index1.setHour("17:15");
        index1.setMin_val(22);
        index1.setMax_val(33);
        index1.setOpen_val(26);
        index1.setClose_val(30);
        index1.setVol_val(26);
            
        data[0] = index;
        data[1] = index1;
        
            CandleChart.generateData(data);
            
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
