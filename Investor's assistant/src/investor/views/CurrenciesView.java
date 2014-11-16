/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import investor.charts.LinearChartManager;
import investor.data.Currency;
import investor.data.DataRange;
import investor.data.Index;
import investor.network.DataType;
import investor.network.IndexType;
import investor.network.NetworkManager;
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

/**
 *
 * @author Tomasz
 */
public class CurrenciesView extends InvestorView {

    private LineChart lineChart;
    private TableView table;

    public void InitView() {
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
                    //System.out.println(rowData);
                    try {
                        Index[] data = NetworkManager.showMore(rowData.getSymbol().substring(1), DataRange.THREEMONTH);
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
        vBox.getChildren().add(table);
        vBox.getChildren().add(lineChart);
    }

    public LineChart getChart() {
        return lineChart;
    }

    public TableView getTable() {
        return table;
    }

//    protected TableColumn[] initColumns() {
//        TableColumn name = new TableColumn("Nazwa");
//        name.setCellValueFactory(new PropertyValueFactory<Currency, String>("name"));
//
//        TableColumn symbol = new TableColumn("Symbol");
//        symbol.setCellValueFactory(new PropertyValueFactory<Currency, String>("symbol"));
//
//        TableColumn rate = new TableColumn("Kurs");
//        rate.setCellValueFactory(new PropertyValueFactory<Currency, String>("rate"));
//
//        TableColumn change = new TableColumn("Zmiana");
//        change.setCellValueFactory(new PropertyValueFactory<Currency, String>("change"));
//
//        TableColumn[] columns = {name, symbol, rate, change};
//        return columns;
//    }
//
//    private ObservableList<Currency> initRows() {
//        ObservableList<Currency> data
//                = FXCollections.observableArrayList(
//                        new Currency("Dolar amerykański", "1 USD", 3.124f, 0.94f),
//                        new Currency("Euro", "1 EUR", 4.214f, 0.007f),
//                        new Currency("Frank szwajcarski", "1 CHF", 3.507f, 0.11f)
//                );
//        return data;
//    }
}
