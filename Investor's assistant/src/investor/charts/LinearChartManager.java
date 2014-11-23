package investor.charts;

import investor.data.DataRange;
import investor.data.Index;
import investor.indicators.Indicators;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.scene.chart.BarChart;
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
    public static XYChart.Series createSeries(String name, String date[], double value[]) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        for (int i = 0; i < date.length; i++) {
            series.getData().add(new XYChart.Data(date[i], value[i]));
        }

        return series;
    }

    /*
     Utworzenie i dodanie serii danych do wykresu
     */
    public static LineChart<String, Number> linear() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setForceZeroInRange(false);
        xAxis.setLabel("oś X");
        yAxis.setLabel("oś Y");

        final LineChart<String, Number> lineChart
                = new LineChart<String, Number>(xAxis, yAxis);

        lineChart.setCreateSymbols(false);
        lineChart.setTitle("Tytuł");

        return lineChart;
    }

    public static BarChart<String, Number> bar(XYChart.Series s) {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setForceZeroInRange(false);
        xAxis.setLabel("oś X");
        yAxis.setLabel("oś Y");

        final BarChart<String, Number> barChart
                = new BarChart<String, Number>(xAxis, yAxis);

        //lineChart.setCreateSymbols(false);
        barChart.setTitle("Tytuł");
        barChart.getData().addAll(s);

        return barChart;
    }


    /*
     dodanie punktu do wykresu
     */
    public static void addPoint(LineChart<String, Number> lineChart, String date, Number value, boolean removeFirstPoint) {
        XYChart.Series<String, Number> s = lineChart.getData().get(0);

        if (removeFirstPoint) {
            s.getData().remove(0);   //jesli chcemy rowniez usunac najstarszy element 
        }

        s.getData().add(new LineChart.Data<String, Number>(date, value));
    }

    /*
     dodanie kolejnej linii do wykresu
     */
    public static void addSeries(LineChart<String, Number> lineChart, XYChart.Series series) {
        lineChart.getData().addAll(series);
    }

    /*
     trzeba  tez dostosowac rozmiar tablicy z wartosciami
     */
    public static String[] generateXLabels(DataRange r) {
        String labels[];
        int days;

        switch (r) {
            case FIVEDAYS:
                days = 5;
                break;

            case TENDAYS:
                days = 10;
                break;

            case ONEMONTH:
                days = 30;
                break;
            case THREEMONTH:
                days = 90;
                break;

            default:
                days = 0;
                break;

        }

        labels = new String[days];

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -(days + 1));

        for (int i = 0; i < days; i++) {
            cal.add(Calendar.DATE, +1);
            labels[i] = formatter.format(cal.getTime());
        }

        return labels;
    }

    public static String[] generateXLabels(Index[] data) {
        String labels[] = new String[data.length];
        for (int i = 0; i < data.length; i++) {
//            cal.add(Calendar.DATE, +1);
//            labels[i] = formatter.format(cal.getTime());

            labels[i] = data[i].getDay();
        }
        return labels;
    }
    
    public static void addSeries(LineChart<String, Number> lineChart, Index[] data) {
        //lineChart.getData().addAll(series);

        String labels[] = generateXLabels(data);
        double values[] = new double[data.length];
        
        for(int i=0; i < data.length; i++){
            values[i] = data[i].getClose_val();
        }

        XYChart.Series s = LinearChartManager.createSeries("SD", labels, values);      
        
        lineChart.getData().addAll(s);
    }
    
    

}
