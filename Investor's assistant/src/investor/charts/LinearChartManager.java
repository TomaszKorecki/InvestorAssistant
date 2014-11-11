package investor.charts;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 *
 * @author mrudnicky
 */
public class LinearChartManager {

    /*
     Tworzenie serii danych, dane jako argumenty
     */
    public static XYChart.Series createSeries(String name, String date[], int value[]) {
        XYChart.Series series = new XYChart.Series();
        series.setName("wykres z funkcji: " + name);
        for (int i = 0; i < date.length; i++) {
            series.getData().add(new XYChart.Data(date[i], value[i]));
        }

        return series;
    }

    /*
     Utworzenie i dodanie serii danych do wykresu
     */
    public static LineChart<String, Number> linear(XYChart.Series s) {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("oś X");
        yAxis.setLabel("oś Y");

        final LineChart<String, Number> lineChart
                = new LineChart<String, Number>(xAxis, yAxis);

        lineChart.setCreateSymbols(false);
        lineChart.setTitle("Tytuł");
        lineChart.getData().addAll(s);

        return lineChart;
    }

    public static void addPoint(LineChart<String, Number> lineChart, String date, Number value, boolean removeFirstPoint) {   //dodanie punktu do wykresu
        XYChart.Series<String, Number> s = lineChart.getData().get(0);
        
        if (removeFirstPoint) {
            s.getData().remove(0);   //jesli chcemy rowniez usunac najstarszy element 
        }
        
        s.getData().add(new LineChart.Data<String, Number>(date, value));
    }

}
