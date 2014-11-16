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
import investor.network.IndexType;
import investor.network.NetworkManager;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.json.JSONException;

/**
 *
 * @author Tomasz
 */
public class MarketIndicisesView extends InvestorView {

    private LineChart lineChart;
    private TableView table;

    public void InitView() {
        pane = new VBox();
        //Label label = new Label("Wskaźniki giełdowe");

//        String month[] = {"Jan", "Feb", "Mar", "Apr", "May"};              //przykladowe dane do wykresow
//        double v1[] = {1000.1, 1000.4, 1000.8, 1000.2, 1000.5, 1000.3, 1000.6};
//        double v2[] = {1000.4, 1000.1, 1000.3, 1000.6, 1000.9, 1000.4, 1000.3};
//
//        XYChart.Series s = LinearChartManager.createSeries("jeden", month, v1);
//        XYChart.Series s1 = LinearChartManager.createSeries("dwa", month, v2);
        lineChart = LinearChartManager.linear();
        lineChart.setTitle("");
//        LinearChartManager.addSeries(lineChart, s);
//        LinearChartManager.addSeries(lineChart, s1);

        table = new TableView();

        Index index = new Index();
        index.setSymbol("WIG20");
        index.setName("WIG20");
        index.setDay(new Date().toString());
        index.setHour("17:15");
        index.setMin_val(10);
        index.setMax_val(10);
        index.setOpen_val(10);
        index.setClose_val(10);

        table.getItems().add(index);

        table.getColumns().addAll(initColumns());

//         table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
//            //Check whether item is selected and set value of selected item to Label
//            if (table.getSelectionModel().getSelectedItem() != null) {
//                
//                
//            }
//        });
        table.setRowFactory(tv -> {
            TableRow<Index> row = new TableRow<Index>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Index rowData = row.getItem();
                    //System.out.println(rowData);
                    try {
                        Index[] data = NetworkManager.showMore(IndexType.WIG20, DataRange.THREEMONTH);
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

    public LineChart GetChart() {
        return lineChart;
    }

}
