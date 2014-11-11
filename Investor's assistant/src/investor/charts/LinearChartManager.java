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
     parametry dla generateXLabels (trzeba wtedy tez dostosowac rozmiar tablicy z wartosciami):
     1 - miesiace
     2 - 30 dni
     3 - dni tygodnia
     4 - 24 godziny
     */
    public static String[] generateXLabels(int version) {
        String labels[];

        switch (version) {
            case 1:
                labels = new String[]{"Styczeń",
                    "Luty",
                    "Marzec",
                    "Kwiecień",
                    "Maj",
                    "Czerwiec",
                    "Lipiec",
                    "Sierpień",
                    "Wrzesień",
                    "Październik",
                    "Listopad",
                    "Grudzień"
                };

                return labels;

            case 2:
                labels = new String[30];
                for (int i = 0; i < 30; i++) {
                    labels[i] = Integer.toString(i + 1);
                }
                return labels;

            case 3:
                labels = new String[]{"Poniedziałek",
                    "Wtorek",
                    "Środa",
                    "Czwartek",
                    "Piątek",
                    "Sobota",
                    "Niedziela"
                };
                return labels;

            case 4:
                labels = new String[24];
                for (int i = 0; i < 24; i++) {
                    labels[i] = Integer.toString(i);
                }
                return labels;

            default:
                labels = new String[]{};
                return labels;

        }

    }

}
