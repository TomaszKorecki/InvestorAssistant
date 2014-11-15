/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import investor.charts.LinearChartManager;
import investor.data.Currency;
import investor.data.DataRange;
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

/**
 *
 * @author Tomasz
 */
public class CurrenciesView extends InvestorView {

    private LineChart lineChart;
    private TableView table;

    public void InitView() {
        pane = new VBox();
        //Label label = new Label("Wskaźniki giełdowe");

        String month[] = {"Jan", "Feb", "Mar", "Apr", "May"};              //przykladowe dane do wykresow
        double v1[] = {1000.1, 1000.4, 1000.8, 1000.2, 1000.5, 1000.3, 1000.6};
        double v2[] = {1000.4, 1000.1, 1000.3, 1000.6, 1000.9, 1000.4, 1000.3};
        
        XYChart.Series s = LinearChartManager.createSeries("jeden", LinearChartManager.generateXLabels(DataRange.WEEK), v1);
        XYChart.Series s1 = LinearChartManager.createSeries("drugi", LinearChartManager.generateXLabels(DataRange.WEEK), v2);
        lineChart = LinearChartManager.linear();
        
        LinearChartManager.addSeries(lineChart, s);
        
        //LinearChartManager.addSeries(lineChart, s1, "drugi");

        table = new TableView();
        table.getColumns().addAll(initColumns());
        
        table.setItems(initRows());
        table.setEditable(false);

       Button button1 = new Button("Dodaj");
       button1.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
       button1.setOnAction(new EventHandler<ActionEvent>() {
           @Override public void handle(ActionEvent e) {
               LinearChartManager.addSeries(lineChart, s1);
           }
       });

        VBox vBox = (VBox) pane;
        vBox.getChildren().add(table);
        vBox.getChildren().add(lineChart);
        vBox.getChildren().add((button1));
    }

    public LineChart getChart() {
        return lineChart;
    }

    public TableView getTable() {
        return table;
    }

    private TableColumn[] initColumns() {
        TableColumn name = new TableColumn("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<Currency, String>("name"));
        
        TableColumn symbol = new TableColumn("Symbol");
        symbol.setCellValueFactory(new PropertyValueFactory<Currency, String>("symbol"));
        
        TableColumn rate = new TableColumn("Kurs");
        rate.setCellValueFactory(new PropertyValueFactory<Currency, String>("rate"));
        
        TableColumn change = new TableColumn("Zmiana");
        change.setCellValueFactory(new PropertyValueFactory<Currency, String>("change" ));
        
        TableColumn[] columns = {name, symbol, rate, change};
        return columns;
    }

    private ObservableList<Currency> initRows() {
        ObservableList<Currency> data
                = FXCollections.observableArrayList(
                        new Currency("Dolar amerykański", "1 USD", 3.124f, 0.94f),
                        new Currency("Euro", "1 EUR", 4.214f, 0.007f),
                        new Currency("Frank szwajcarski", "1 CHF", 3.507f, 0.11f)
                );
        return data;
    }
}
