/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

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
    private LineChart lineChart;
    private LineChart lineChartSD;
    private TableView table;

    //Metoda generująca wykres
    public void InitView() {
        pane = new VBox();
        
        //Stworzenie wykresu
        lineChart = LinearChartManager.linear();
        lineChart.setTitle("");
        
        //Poczatkowo zakres dat to 3M
        selectedRange = DataRange.THREEMONTH;
        
        
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
                        Index[] data = NetworkManager.showMore(rowData.getSymbol(), selectedRange);
                        System.out.println(data.length);

                        lineChart.getData().clear();
                        LinearChartManager.addSeries(lineChart, data);
                        OnPointerChange();

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
        BorderPane borderPane = new BorderPane();
        
        //Po środku wykres
        borderPane.setCenter(lineChart);
        
        //Na prawo przyciski konfiguracyjne
        borderPane.setRight(addMenuButtons());
        
        //Dodanie do panelu widoku tabelki
        vBox.getChildren().add(table);
        
        //Dodanie do panelu widoku panelu z wykresem i przyciskami konfiguracyjnymi
        vBox.getChildren().add(borderPane);
    }
    
    public void ChangeViewSD() {
        pane = new VBox();
        lineChart = LinearChartManager.linear();
        lineChart.setTitle("");
        lineChartSD = LinearChartManager.linear();
        lineChartSD.setTitle("SD");
        lineChartSD.setMaxHeight(250);

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
                        Index[] data = NetworkManager.showMore(rowData.getSymbol(), selectedRange);
                        System.out.println(data.length);

                        lineChart.getData().clear();
                        LinearChartManager.addSeries(lineChart, data);

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

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(lineChart);
        borderPane.setBottom(lineChartSD);
        borderPane.setRight(addMenuButtons());

        vBox.getChildren().add(table);
        vBox.getChildren().add(borderPane);
    }
        
        public void ChangeView() {
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
                        Index[] data = NetworkManager.showMore(rowData.getSymbol(), selectedRange);
                        System.out.println(data.length);

                        lineChart.getData().clear();
                        LinearChartManager.addSeries(lineChart, data);

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

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(lineChart);
        borderPane.setRight(addMenuButtons());

        vBox.getChildren().add(table);
        vBox.getChildren().add(borderPane);
    }

    public LineChart GetChart() {
        return lineChart;
    }

    
    //Metoda wywoływana po zmianie zakresu dat. Ściągane są z serwera nowe dane dla wykresu
    //TODO:
    //Powinna działać na dwa różne sposoby, w zależności od pokazanego wykresu
    protected void OnDataRangeChanged() {
        if (selectedIndex != null) {
            System.out.println("Preparing for downloading for " + selectedRange.toString());
            Index[] data = null;
            try {
                data = NetworkManager.showMore(selectedIndex.getSymbol(), selectedRange);
            } catch (Exception ex) {
                Logger.getLogger(CompaniesView.class.getName()).log(Level.SEVERE, null, ex);
            }

            lineChart.getData().clear();
            LinearChartManager.addSeries(lineChart, data);
        }
    }

    protected void OnPointerChange() {
            Index[] dataPointer = null;
            double[] data1;
            double[][] data2;
            Indicators ptr = new Indicators();
            int size;
            try {
                dataPointer = NetworkManager.showMore(selectedIndex.getSymbol(), selectedRange);
            } catch (Exception ex) {
                Logger.getLogger(CompaniesView.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            size = dataPointer.length;
            
            switch (pointerType) {
                case "MA": if (lastPointerType == "SD")
                                ChangeView();
                           data1 = ptr.MA(dataPointer, size);
                           for(int i=0; i < size; i++){
                                dataPointer[i].setClose_val(data1[i]);
                           }
                           OnDataRangeChanged();
                           LinearChartManager.addSeries(lineChart, dataPointer);
                           break;
                case "SD": ChangeViewSD();
                           data1 = ptr.SD(dataPointer, size);
                           for(int i=0; i < size; i++){
                                dataPointer[i].setClose_val(data1[i]);
                           }
                           OnDataRangeChanged();
                           LinearChartManager.addSeries(lineChartSD, dataPointer);
                           break;
                case "bollinger": if (lastPointerType == "SD")
                                ChangeView();
                            data2 = ptr.Bollinger(dataPointer, size, K);
                           for(int i=0; i < size; i++){
                                dataPointer[i].setClose_val(data2[0][i]);
                           }
                           OnDataRangeChanged();
                           LinearChartManager.addSeries(lineChart, dataPointer);
                           for(int i=0; i < size; i++){
                                dataPointer[i].setClose_val(data2[1][i]);
                           }
                           LinearChartManager.addSeries(lineChart, dataPointer);
                           for(int i=0; i < size; i++){
                                dataPointer[i].setClose_val(data2[2][i]);
                           }
                           LinearChartManager.addSeries(lineChart, dataPointer);
                           break;
                case "koperta": if (lastPointerType == "SD")
                                ChangeView();
                            data2 = ptr.Bollinger(dataPointer, size, P);
                           for(int i=0; i < size; i++){
                                dataPointer[i].setClose_val(data2[0][i]);
                           }
                           OnDataRangeChanged();
                           LinearChartManager.addSeries(lineChart, dataPointer);
                           for(int i=0; i < size; i++){
                                dataPointer[i].setClose_val(data2[1][i]);
                           }
                           LinearChartManager.addSeries(lineChart, dataPointer);
                           break;
                case "EMA":if (lastPointerType == "SD")
                                ChangeView();
                            double alpha = 2/(size + 1);
                           data1 = ptr.EMA(dataPointer, size, alpha);
                           for(int i=0; i < size; i++){
                                dataPointer[i].setClose_val(data1[i]);
                           }
                           OnDataRangeChanged();
                           LinearChartManager.addSeries(lineChart, dataPointer);
                           break;
                case "hide": if (lastPointerType == "SD")
                                ChangeView();
                            OnDataRangeChanged();
                             break;
                default  : break;
            }
    }
}
