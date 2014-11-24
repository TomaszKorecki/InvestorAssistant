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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONException;

/**
 * Pierwszy widok z indeksami giełdowymi
 * @author Tomasz
 */
public class MarketIndicisesView extends InvestorView {

    //Widok posiada wykres liniowy oraz tabelke
    
    //TODO:
    //Tu powinny się jeszcze znaleźć referencję do wykresu świecowego i ewentualnie dla drugiego liniowego dla wskaźnika SD
    private CandleChart candleChart;
    private CandleChart.CandleStickChart chart;
    private LineChart lineChart;
    private LineChart lineChartSD;
    private TableView table;
    BorderPane borderPane;
    private Index[] lastData;

    //Metoda generująca wykres
    public void InitView() {
        pane = new VBox();
        
        //Stworzenie wykresu
        lineChart = LinearChartManager.linear();
        lineChart.setTitle("");
        
        //Poczatkowo zakres dat to 3M
        selectedRange = DataRange.THREEMONTH;
        
        selectedChart = "line";
        //Stworzenie tabelki
        table = new TableView();

        //Sciągamy dane dla tabelki z serwera i populujemy tabelke
        try {
            table.getItems().addAll(NetworkManager.show(DataType.WSK));
        } catch (Exception e) {
            System.out.println("Something went wrong when populating market indicisies view");
        }
        
        //Dodajemy kolumny do tabelki
        table.getColumns().addAll(initColumns());
        
        
        //Obsługa kliknięcia wiersza w tabelce
        table.setRowFactory(tv -> {
            TableRow<Index> row = new TableRow<Index>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    
                    //Zapamiętujemy wybrany wiersz dla widoku
                    Index rowData = row.getItem();
                    selectedIndex = rowData;
                    //System.out.println(rowData);
                    try {
                        
                        //Sciągamy dane z serwera dla danego wiersza i dodajemy je do wykresu
                        lastData = NetworkManager.showMore(rowData.getSymbol(), selectedRange);
                        System.out.println(lastData.length);

                        if(selectedChart.equals("line")) {
                            lineChart.getData().clear();
                            LinearChartManager.addSeries(lineChart, lastData, selectedRange);
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
        
        //Panel dla wykresu i przycisków konfiguracyjnych
        borderPane = new BorderPane();
        
        //Po środku wykres
        borderPane.setCenter(lineChart);
        
        //Na prawo przyciski konfiguracyjne
        borderPane.setRight(addMenuButtons());
        
        //Dodanie do panelu widoku tabelki
        vBox.getChildren().add(table);
        
        //Dodanie do panelu widoku panelu z wykresem i przyciskami konfiguracyjnymi
        vBox.getChildren().add(borderPane);
    }
    
    public LineChart GetChart() {
        return lineChart;
    }

    public TableView getTable() {
        return table;
    }
    
    //Metoda wywoływana po zmianie zakresu dat. Ściągane są z serwera nowe dane dla wykresu
    //TODO:
    //Powinna działać na dwa różne sposoby, w zależności od pokazanego wykresu
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
                    LinearChartManager.addSeries(lineChart, lastData, selectedRange);
                    borderPane.setCenter(lineChart);
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
            return;
        }
        
        if("1D".equals(selectedRange)) {
            return;
        }

        if (chartType.equals("line")) {
            lineChart.getData().clear();
            LinearChartManager.addSeries(lineChart, lastData, selectedRange);
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
            return;
        }

        Index[] dataPointer = new Index[lastData.length];
        for (int i = 0; i < lastData.length; i++) {
            dataPointer[i] = new Index(lastData[i]);
        }

        double[] data1;
        double[][] data2;
        int size = lastData.length;

        lineChart.getData().clear();
        LinearChartManager.addSeries(lineChart, lastData, selectedRange);

        switch (pointerType) {
            case "MA":
                data1 = Indicators.MA(dataPointer, size);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data1[i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer, "MA", selectedRange);
                break;
            case "bollinger":
                data2 = Indicators.Bollinger(dataPointer, size, K);

                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[0][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer, "Bollinger1", selectedRange);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[1][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer, "Bollinger2", selectedRange);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[2][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer, "Bollinger3", selectedRange);
                break;

            case "koperta":
                data2 = Indicators.Bollinger(dataPointer, size, P);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[0][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer, "Koperta1", selectedRange);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data2[1][i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer, "Koperta2", selectedRange);
                break;
            case "EMA":
                double alpha = 2 / (size + 1);
                data1 = Indicators.EMA(dataPointer, size, alpha);
                for (int i = 0; i < size; i++) {
                    dataPointer[i].setClose_val(data1[i]);
                }
                LinearChartManager.addSeries(lineChart, dataPointer, "EMA", selectedRange);
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
                return;
            }

            Index[] dataPointer = new Index[lastData.length];
            for (int i = 0; i < lastData.length; i++) {
                dataPointer[i] = new Index(lastData[i]);
            }

            lineChartSD = LinearChartManager.linear();
            lineChartSD.setTitle("SD");
            lineChartSD.setMaxHeight(250);

            int size = lastData.length;

            double[] data1 = Indicators.SD(dataPointer, size);
            for (int i = 0; i < size; i++) {
                dataPointer[i].setClose_val(data1[i]);
            }

            LinearChartManager.addSeries(lineChartSD, dataPointer, "SD", selectedRange);
            BorderPane bPane = (BorderPane) pane.getChildren().get(1);
            bPane.setBottom(lineChartSD);
        } else { //let's hide it
            lineChartSD = null;
            BorderPane bPane = (BorderPane) pane.getChildren().get(1);
            bPane.setBottom(null);
        }
    }
}
