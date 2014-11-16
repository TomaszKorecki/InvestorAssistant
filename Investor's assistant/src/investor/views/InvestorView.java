/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import investor.data.Currency;
import investor.data.DataRange;
import investor.data.Index;
import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author Tomasz
 */
public abstract class InvestorView {

    protected Pane pane;

    protected DataRange selectedRange;
    protected Index selectedIndex;

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

    protected Pane addMenuButtons() {

        GridPane mainPane = new GridPane();
        mainPane.setVgap(20);

        HBox chartGroupPane = new HBox();
        chartGroupPane.setAlignment(Pos.CENTER);

        final ToggleGroup chartGroup = new ToggleGroup();

        Image image1 = new Image(getClass().getResourceAsStream("lineChartIcon.png"));
        Image image2 = new Image(getClass().getResourceAsStream("candleChartIcon.png"));

        ToggleButton line = new ToggleButton();
        line.setGraphic(new ImageView(image1));

        ToggleButton candle = new ToggleButton();
        candle.setGraphic(new ImageView(image2));

        line.setToggleGroup((chartGroup));
        candle.setToggleGroup((chartGroup));

        chartGroupPane.getChildren().addAll(line, candle);

        chartGroup.selectToggle(line);

        final ToggleGroup rangeGroup = new ToggleGroup();
        List<DataRange> enumList = Arrays.asList(DataRange.values());

        FlowPane flowPane = new FlowPane();
        flowPane.setPrefWrapLength(70);

        for (DataRange d : enumList) {
            ToggleButton button = new ToggleButton(getDataRangeShortName(d));
            button.setMinSize(48, 48);
            button.setUserData(d.toString());
            button.setToggleGroup(rangeGroup);
            flowPane.getChildren().add(button);
        }

        rangeGroup.selectToggle(rangeGroup.getToggles().get(3));
        rangeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                
                System.out.println(new_toggle.toString());
                
                selectedRange = DataRange.valueOf((String)new_toggle.getUserData());
                OnDataRangeChanged();
            }
        });

        mainPane.add(chartGroupPane, 0, 0);
        mainPane.add(flowPane, 0, 1);

        return mainPane;
    }

    String getDataRangeShortName(DataRange range) {
        switch (range) {
            case FIVEDAYS:
                return "5D";
            case ONEMONTH:
                return "1M";
            case TENDAYS:
                return "10D";
            case THREEMONTH:
                return "3M";
        }
        return null;
    }
    
    

    protected void ChangeDataRange(DataRange range) {
        selectedRange = range;
        OnDataRangeChanged();
    }

    protected abstract void OnDataRangeChanged();

}
