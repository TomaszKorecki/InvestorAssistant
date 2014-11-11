package investor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import investor.charts.LinearChartManager;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Tomasz
 */
public class MainMenu extends Application {

    TabPane tabs;
    Pane marketIndicesPane;
    Pane companiesPane;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Asystent Inwestora");

        tabs = CreateTabs();

        BorderPane borderPane = new BorderPane();

        borderPane.setTop(tabs);

        marketIndicesPane = CreateMarketIndicesPane();
        companiesPane = CreateCompaniesPane();

        borderPane.setCenter(marketIndicesPane);
        //borderPane.setCenter(label);

        tabs.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        if (t1 == marketIndices) {
                            borderPane.setCenter(marketIndicesPane);
                        } else if (t1 == companies) {
                            borderPane.setCenter(companiesPane);
                        }
                    }
                }
        );

        Scene scene = new Scene(borderPane, 800, 450);
        primaryStage.setScene(scene);

        //scene.getStylesheets().add(JavaFXApplication1.class.getResource("login.css").toExternalForm());
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    Tab marketIndices = new Tab("Wskaźniki giełdowe");
    Tab companies = new Tab("Spółki");
    Tab currencies = new Tab("Wauty");
    Tab currenciesCorrelations = new Tab("Korelacje walut");

    private TabPane CreateTabs() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(marketIndices, companies, currencies, currenciesCorrelations);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }

    private BorderPane CreateMarketIndicesPane() {
        BorderPane borderPane = new BorderPane();
        //Label label = new Label("Wskaźniki giełdowe");

        String month[] = {"Jan", "Feb", "Mar", "Apr", "May"};              //przykladowe dane do wykresow
        int v1[] = {1000, 1070, 1100, 1130, 1200};

        XYChart.Series s = LinearChartManager.createSeries("jeden", month, v1);
        LineChart chart = LinearChartManager.linear(s);

        borderPane.setCenter(chart);
        return borderPane;
    }

    private BorderPane CreateCompaniesPane() {
        BorderPane borderPane = new BorderPane();
        Label label = new Label("Spółki");
        borderPane.setCenter(label);
        return borderPane;
    }

//    private TableView CreateCurrenciesTable() {
//
//    }
    public static void main(String[] args) {
        launch(args);
    }

}
