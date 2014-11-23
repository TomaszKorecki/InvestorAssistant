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
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Tomasz
 */
public class CurrenciesView extends InvestorView {

    private LineChart lineChart;
    private LineChart lineChartSD;
    private TableView table;

    public void InitView() {
        selectedRange = DataRange.THREEMONTH;
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

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(lineChart);
        borderPane.setRight(addMenuButtons());

        vBox.getChildren().add(table);
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
