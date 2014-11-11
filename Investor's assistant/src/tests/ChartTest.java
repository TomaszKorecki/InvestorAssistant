package tests;

import java.time.LocalDate;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
 
public class ChartTest extends Application {
    LocalDate ld;
 String month[] = {"Jan","Feb","Mar","Apr","May"};              //przykladowe dane do wykresow
 int v1[] = {1000, 1070, 1100, 1130, 1200};
 int v2[] = {1400, 1170, 1170, 1240, 1500};
 
    @Override public void start(Stage stage) {
        
                DatePicker dp = new DatePicker();
                dp.setPromptText("data początkowa");                
        dp.setOnAction(e -> {
            ld = dp.getValue();
            System.out.println("wybrana data: " + ld);
                });
        
        
        
        stage.setTitle("Tytuł okna");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
         xAxis.setLabel("oś X");
         yAxis.setLabel("oś Y");
        final LineChart<String,Number> lineChart = 
                new LineChart<String,Number>(xAxis,yAxis);
        lineChart.setCreateSymbols(false);
        lineChart.setTitle("Tytuł");
        
        StackPane root = new StackPane();
root.getChildren().addAll(lineChart, dp);
//root.getChildren().add(btn);
              
        Scene scene  = new Scene(root,800,600);       
       
        lineChart.getData().addAll(addLine("pierwszy",month,v2),                    //wywolanie funkcji rysujacej wykres
                                   addLine("drugi",month, v1));
       
        stage.setScene(scene);
        

        stage.show();
    }
 
    private XYChart.Series addLine(String name, String date[], int value[]) {           //rysowanie wykresu, dane jako argumenty
        XYChart.Series series = new XYChart.Series();
        series.setName("wykres z funkcji: " + name);
        for (int i = 0; i < date.length; i++) {
        series.getData().add(new XYChart.Data(date[i], value[i]));
        }
        return series;
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}