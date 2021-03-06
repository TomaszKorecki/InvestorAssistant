package investor;

import com.google.gson.Gson;
import investor.data.DataRange;
import investor.data.Index;
import investor.network.DataType;
import investor.network.NetworkManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import investor.views.CompaniesView;
import investor.views.CurrenciesCorrelationsView;
import investor.views.CurrenciesView;
import investor.views.GoodsView;
import investor.views.InvestorView;
import investor.views.MarketIndicisesView;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Główna klasa z GUI. Są w niej tworzone wszystkie widoki i zakładki.
 *
 *
 */
public class MainMenu extends Application {

    TabPane tabs;
    MarketIndicisesView marketIndicicesView = new MarketIndicisesView();
    CompaniesView companiesView = new CompaniesView();
    CurrenciesView currenciesView = new CurrenciesView();
    CurrenciesCorrelationsView currenciesCorrelationView = new CurrenciesCorrelationsView();
    GoodsView goodsView = new GoodsView();

    InvestorView[] views = {marketIndicicesView, companiesView, currenciesView, goodsView};

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Asystent Inwestora");
        
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setMaximized(true);
        
//        primaryStage.setFullScreen(true);
        
        tabs = CreateTabs();

        BorderPane borderPane = new BorderPane();
        

        borderPane.setTop(tabs);

        for (InvestorView v : views) {
            v.InitView();
        }

        borderPane.setCenter(marketIndicicesView.getPane());

        //Event handler do zakładek. 
        tabs.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        if (t1 == marketIndices) {
                            borderPane.setCenter(marketIndicicesView.getPane());
                        } else if (t1 == companies) {
                            borderPane.setCenter(companiesView.getPane());
                        } else if (t1 == currencies) {
                            borderPane.setCenter(currenciesView.getPane());
                        } else if (t1 == goods) {
                            borderPane.setCenter(goodsView.getPane());
                        }
//                        } else if (t1 == currenciesCorrelations) {
//                            borderPane.setCenter(currenciesCorrelationView.getPane());
//                        }
                    }
                }
        );

        Scene scene = new Scene(borderPane, 800, 450);
        primaryStage.setScene(scene);

        //scene.getStylesheets().add(MainMenu.class.getResource("login.css").toExternalForm());
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    Tab marketIndices = new Tab("Wskaźniki giełdowe");
    Tab companies = new Tab("Spółki");
    Tab currencies = new Tab("Waluty");
    Tab currenciesCorrelations = new Tab("Korelacje walut");
    Tab goods = new Tab("Towary");

    private TabPane CreateTabs() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(marketIndices, companies, currencies, goods);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }

    public static void main(String[] args) throws IOException, JSONException {
        launch(args);
    }
}
