/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import investor.data.Currency;
import investor.data.Index;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

/**
 *
 * @author Tomasz
 */
public abstract class InvestorView {

    protected Pane pane;

    public Pane getPane() {
        return pane;
    }

    public abstract void InitView();

    protected TableColumn[] initColumns() {
        TableColumn symbol = new TableColumn("Symbol");
        symbol.setCellValueFactory(new PropertyValueFactory<Index, String>("symbol"));

        TableColumn name = new TableColumn("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<Currency, String>("name"));

        TableColumn day = new TableColumn("Dzień");
        day.setCellValueFactory(new PropertyValueFactory<Currency, String>("day"));

        TableColumn hour = new TableColumn("Godzina");
        hour.setCellValueFactory(new PropertyValueFactory<Currency, String>("hour"));

        TableColumn open_val = new TableColumn("Wartość otwarcia");
        open_val.setCellValueFactory(new PropertyValueFactory<Currency, String>("open_val"));

        TableColumn close_val = new TableColumn("Wartość zamknięcia");
        close_val.setCellValueFactory(new PropertyValueFactory<Currency, String>("close_val"));

        TableColumn min_val = new TableColumn("Wartość minimalna");
        min_val.setCellValueFactory(new PropertyValueFactory<Currency, String>("min_val"));

        TableColumn max_val = new TableColumn("Wartość maksymalna");
        max_val.setCellValueFactory(new PropertyValueFactory<Currency, String>("max_val"));

        TableColumn vol_val = new TableColumn("Wartość");
        vol_val.setCellValueFactory(new PropertyValueFactory<Currency, String>("vol_val"));

        TableColumn[] columns = {symbol, name, day, hour, open_val, close_val, min_val, max_val, vol_val};
        return columns;
    }
}
