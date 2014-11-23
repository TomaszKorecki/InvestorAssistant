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
import investor.indicators.Indicators;
import investor.network.DataType;
import investor.network.NetworkManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.json.JSONException;

/**
 *
 * @author Tomasz
 */
public class CompaniesView extends InvestorView {

    private BorderPane borderPane;
    private LineChart lineChart;
    private LineChart lineChartSD;
    private TableView table;
    private CandleChart candleChart;
    private CandleChart.CandleStickChart chart;
    private Index[] lastData;

    public void InitView() {
        selectedRange = DataRange.THREEMONTH;
        selectedChart = "line";
        pane = new VBox();
        lineChart = LinearChartManager.linear();
        lineChart.setTitle("");

        table = new TableView();

        try {
            table.getItems().addAll(NetworkManager.show(DataType.SPOL));
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
                        lastData = NetworkManager.showMore(rowData.getSymbol(), selectedRange);
                        System.out.println(lastData.length);

                    if(selectedChart.equals("line")) {
                        lineChart.getData().clear();
                        LinearChartManager.addSeries(lineChart, lastData);
                        if (pointerType!=null && pointerType != "hide") {
                                OnPointerChange();
                        }
                    } else {
                        CandleChart.generateData(lastData);
                        candleChart = new CandleChart(); 
                        CandleChart.CandleStickChart chart = candleChart.createChart();

                        pane.getStylesheets().add("resources/css/CandleStickChart.css"); 
                        borderPane.setCenter(chart);
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
 
    public LineChart GetChart() {
        return lineChart;
    }
    
    public TableView getTable() {
        return table;
    }

    protected void OnDataRangeChanged() {
        if (selectedIndex != null) {
            try {
                lastData = NetworkManager.showMore(selectedIndex.getSymbol(), selectedRange);
                lineChart.getData().clear();

                if (pointerType!=null && !pointerType.equals("hide")) {
                    OnPointerChange();
                    if (sdChartShowed) {
                        OnSDPointer(true);
                    }
                } else if(selectedChart.equals("line")){
                    LinearChartManager.addSeries(lineChart, lastData);
                }
                else {
                    CandleChart.generateData(lastData);
                    //candleChart = new CandleChart();
                    chart = candleChart.createChart();
                    //pane.getStylesheets().add("resources/css/CandleStickChart.css");
                    borderPane.setCenter(chart);
                }

            } catch (Exception ex) {
                Logger.getLogger(GoodsView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
  protected void OnChartTypeChanged(String chartType) {
        selectedChart = chartType;
        if (lastData == null) {
            System.out.println("Original data is null, downloading it");
            OnDataRangeChanged();
        }

        if (chartType.equals("line")) {
            lineChart.getData().clear();
            LinearChartManager.addSeries(lineChart, lastData);
            borderPane.setCenter(lineChart);
        } else if (chartType.equals("candle")) {
            CandleChart.generateData(lastData);
            candleChart = new CandleChart();
            chart = candleChart.createChart();
            pane.getStylesheets().add("resources/css/CandleStickChart.css");
            borderPane.setCenter(chart);
            selectedChart = chartType;
        }
    }

    protected void OnPointerChange() {
        if (lastData == null) {
            System.out.println("Original data is null, downloading it");
            OnDataRangeChanged();
        }

        Index[] dataPointer = lastData.clone();

        double[] data1;
        double[][] data2;
        int size = lastData.length;

        lineChart.getData().clear();
        LinearChartManager.addSeries(lineChart, lastData);

        switch (pointerType) {
            case "MA":
                data1 = Indicators.MA(dataPointer, size);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data1[i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer);
                break;
            case "bollinger":
                data2 = Indicators.Bollinger(dataPointer, size, K);

                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[0][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[1][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[2][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer);
                break;

            case "koperta":
                data2 = Indicators.Bollinger(dataPointer, size, P);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[0][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[1][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer);
                break;
            case "EMA":
                double alpha = 2 / (size + 1);
                data1 = Indicators.EMA(dataPointer, size, alpha);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data1[i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer);
                break;
            case "hide":
                break;
            default:
                break;
        }
    }
    
    protected void OnSDPointer(boolean action) {
        sdChartShowed = action;
        //true for showing sd chart
        if (action) {
            if (lastData == null) {
                System.out.println("Original data is null, downloading it");
                OnDataRangeChanged();
            }

            Index[] dataPointer = lastData.clone();

            lineChartSD = LinearChartManager.linear();
            lineChartSD.setTitle("SD");
            lineChartSD.setMaxHeight(250);

            int size = lastData.length;

            double[] data1 = Indicators.SD(dataPointer, size);
            for (int i = 0; i < size; i++) {
                dataPointer[i].setClose_val(data1[i]);
            }

            LinearChartManager.addSeries(lineChartSD, dataPointer);
            BorderPane bPane = (BorderPane) pane.getChildren().get(1);
            bPane.setBottom(lineChartSD);
        } else { //let's hide it
            lineChartSD = null;
            BorderPane bPane = (BorderPane) pane.getChildren().get(1);
            bPane.setBottom(null);
        }
    }
}
