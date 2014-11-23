/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import investor.charts.CandleChart;
import investor.charts.LinearChartManager;
import investor.data.DataRange;
import investor.data.Index;
import investor.network.DataType;
import investor.network.NetworkManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Tomasz
 */
public class CurrenciesView extends InvestorView {

    private LineChart lineChart;
    private TableView table;
    private CandleChart candleChart;
    private CandleChart.CandleStickChart chart;
    BorderPane borderPane;

    public void InitView() {
        selectedRange = DataRange.THREEMONTH;
        selectedChart = "line";
        pane = new VBox();
        lineChart = LinearChartManager.linear();
        lineChart.setTitle("");

        table = new TableView();

        try {
            table.getItems().addAll(NetworkManager.show(DataType.FOREX));
        } catch (Exception e) {
            System.out.println("Something went wrong when populating market indicisies view");
        }

        table.getColumns().addAll(initColumns());

        table.setRowFactory(tv -> {
            TableRow<Index> row = new TableRow<Index>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Index rowData = row.getItem();
                    selectedIndex = rowData;
                    //System.out.println(rowData);
                    try {
                        Index[] data = NetworkManager.showMore(rowData.getSymbol(), selectedRange);
                        System.out.println(data.length);

                        if(selectedChart.equals("line")) {
                                lineChart.getData().clear();
                                LinearChartManager.addSeries(lineChart, data);
                           } else {
                               CandleChart.generateData(data);
            candleChart = new CandleChart(); 
            CandleChart.CandleStickChart chart = candleChart.createChart();

            pane.getStylesheets().add("resources/css/CandleStickChart.css"); 
            
            //if(borderPane.getChildren().contains(lineChart)) {
                borderPane.getChildren().remove(lineChart);
                borderPane.setCenter(chart);
            //}
                           }

                    } catch (Exception ex) {
                        System.out.println("Error while downloading indicise " + ex.toString());
                    }
                }
            });
            return row;
        });

        //table.setItems(initRows());
        table.setEditable(false);

        VBox vBox = (VBox) pane;

        borderPane = new BorderPane();
        borderPane.setCenter(lineChart);
        borderPane.setRight(addMenuButtons());

        vBox.getChildren().add(table);
        vBox.getChildren().add(borderPane);
    }

    public LineChart getChart() {
        return lineChart;
    }

    public TableView getTable() {
        return table;
    }

    protected void OnDataRangeChanged() {
        if (selectedIndex != null) {
            Index[] data = null;
            try {
                data = NetworkManager.showMore(selectedIndex.getSymbol(), selectedRange);
            } catch (Exception ex) {
                Logger.getLogger(CompaniesView.class.getName()).log(Level.SEVERE, null, ex);
            }

            if(selectedChart.equals("line")) {
                lineChart.getData().clear();
                LinearChartManager.addSeries(lineChart, data);
            } else {
                CandleChart.generateData(data);
            candleChart = new CandleChart(); 
            CandleChart.CandleStickChart chart = candleChart.createChart();

            pane.getStylesheets().add("resources/css/CandleStickChart.css"); 
            
            //if(borderPane.getChildren().contains(lineChart)) {
                borderPane.getChildren().remove(lineChart);
                borderPane.setCenter(chart);
            //}
            }
        }
    }
    
    protected void OnChartTypeChanged(String chartType) {
  
            if(chartType.equals("line")) {
        if (selectedIndex != null) {
            System.out.println("Preparing for downloading for " + selectedRange.toString());
            Index[] data = null;
            try {
                data = NetworkManager.showMore(selectedIndex.getSymbol(), selectedRange);
            } catch (Exception ex) {
                Logger.getLogger(CompaniesView.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            lineChart = LinearChartManager.linear();
            lineChart.setTitle("");
        
            lineChart.getData().clear();
            LinearChartManager.addSeries(lineChart, data);
            
            //if(borderPane.getChildren().contains(chart)) {
                borderPane.getChildren().remove(chart);
                borderPane.setCenter(lineChart);
            //}
                
                selectedChart = chartType;
        }
        }
        
        if(chartType.equals("candle")) {
            if (selectedIndex != null) {
            Index[] data = null;
            try {
                data = NetworkManager.showMore(selectedIndex.getSymbol(), selectedRange);
            } catch (Exception ex) {
                Logger.getLogger(CompaniesView.class.getName()).log(Level.SEVERE, null, ex);
            }
            CandleChart.generateData(data);
            
            candleChart = new CandleChart();
            
            CandleChart.CandleStickChart chart = candleChart.createChart();
            
            
            pane.getStylesheets().add("resources/css/CandleStickChart.css"); 
            
            //if(borderPane.getChildren().contains(lineChart)) {
                borderPane.getChildren().remove(lineChart);
                borderPane.setCenter(chart);
            //}
                
                selectedChart = chartType;
        }
        }
    }
}
