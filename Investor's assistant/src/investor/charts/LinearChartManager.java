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

    public static enum Range {
    DAY, WEEK, MONTH, THREEMONTHS;
    }

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

            public static BarChart<String, Number> bar(XYChart.Series s) {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
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
     trzeba  tez dostosowac rozmiar tablicy z wartosciami
     */
    public static String[] generateXLabels(Range r) {
        String labels[];
        int days; 
        
        switch (r) {
            case THREEMONTHS:
                days = 120;
                break;
                
            case MONTH:
                days = 30;
                break;
                
            case WEEK:
                days = 7;
                break;
                
            default:
                days = 0;
                break;

        }
        
                labels = new String[days];
                
                SimpleDateFormat formatter = new SimpleDateFormat( "dd-MM-YYYY" );
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -(days+1));
                
                for (int i = 0; i < days; i++){
                    cal.add(Calendar.DATE, +1);
                    labels[i] = formatter.format( cal.getTime() );
                }
                
                return labels;
    }

}
