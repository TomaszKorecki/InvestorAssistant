/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import investor.data.DataRange;
import investor.data.Index;
import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.chart.Chart;
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
 * Klasa, po której dziedziczą wszystkie nasze widoki
 */
public abstract class InvestorView {

    //Panel widoku
    protected Pane pane;

    //Wybrany zakres danych dla danego widoku
    protected DataRange selectedRange;
    
    //Wybrany typ wykresu
    protected String selectedChart;

    //rodzaj wybranego wskaznika
    protected String pointerType;

    //rodzaj ostatniego wybranego wskaznika
    protected String lastPointerType = "hide";

    //współczynnik K dla metody bollingera
    protected static int K = 2;

    //współczynnik P dla metody koperty
    protected static int P = 5;

    //Wybrany index dla danego widoku, może być nullem jeśli jeszcze nic nie wybraliśmy
    protected Index selectedIndex;
    
    protected boolean sdChartShowed = false;

    public Pane getPane() {
        return pane;
    }

    //Metoda abstrakcyjna, w której musi zostać napisany kod do generowania widoku
    public abstract void InitView();

    //Stworzenie kolumn do tabelki
    protected TableColumn[] initColumns() {
        TableColumn symbol = new TableColumn("Symbol");
        symbol.setCellValueFactory(new PropertyValueFactory<Index, String>("symbol"));

        TableColumn name = new TableColumn("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<Index, String>("name"));

        TableColumn day = new TableColumn("Dzień");
        day.setCellValueFactory(new PropertyValueFactory<Index, String>("day"));

        TableColumn hour = new TableColumn("Godzina");
        hour.setCellValueFactory(new PropertyValueFactory<Index, String>("hour"));

        TableColumn open_val = new TableColumn("Wartość otwarcia");
        open_val.setCellValueFactory(new PropertyValueFactory<Index, String>("open_val"));

        TableColumn close_val = new TableColumn("Wartość zamknięcia");
        close_val.setCellValueFactory(new PropertyValueFactory<Index, String>("close_val"));

        TableColumn min_val = new TableColumn("Wartość minimalna");
        min_val.setCellValueFactory(new PropertyValueFactory<Index, String>("min_val"));

        TableColumn max_val = new TableColumn("Wartość maksymalna");
        max_val.setCellValueFactory(new PropertyValueFactory<Index, String>("max_val"));

        TableColumn vol_val = new TableColumn("Wolumen");
        vol_val.setCellValueFactory(new PropertyValueFactory<Index, String>("vol_val"));

        TableColumn[] columns = {symbol, name, day, hour, open_val, close_val, min_val, max_val, vol_val};
        return columns;
    }

    //Dodanie przycisków obsługujących wykresy
    protected Pane addMenuButtons() {

        GridPane mainPane = new GridPane();
        mainPane.setVgap(20);

        HBox chartGroupPane = new HBox();
        chartGroupPane.setAlignment(Pos.CENTER);

        //Stworzenie przycisków do wyboru typu wykresu (liniowy, świecowy)
        final ToggleGroup chartGroup = new ToggleGroup();

        Image image1 = new Image(getClass().getResourceAsStream("lineChartIcon.png"));
        Image image2 = new Image(getClass().getResourceAsStream("candleChartIcon.png"));

        ToggleButton line = new ToggleButton();
        line.setGraphic(new ImageView(image1));
        line.setUserData("line");

        ToggleButton candle = new ToggleButton();
        candle.setGraphic(new ImageView(image2));
        candle.setUserData("candle");

        line.setToggleGroup((chartGroup));
        candle.setToggleGroup((chartGroup));

        chartGroupPane.getChildren().addAll(line, candle);

        //Na wstepie wybrany zostaje wykres liniowy
        selectedChart = "line";
        chartGroup.selectToggle(line);
        ////////////////////

        //Stworzenie przycisków do wyboru zakresu dat
        final ToggleGroup rangeGroup = new ToggleGroup();
        List<DataRange> enumList = Arrays.asList(DataRange.values());

        FlowPane flowPane = new FlowPane();
        flowPane.setPrefWrapLength(70);

        //Przyciski tworze w pętli lecąc po wartośćiach enuma
        for (DataRange d : enumList) {
            ToggleButton button = new ToggleButton(getDataRangeShortName(d));
            button.setMinSize(48, 48);
            button.setUserData(d.toString());
            button.setToggleGroup(rangeGroup);
            flowPane.getChildren().add(button);
        }

        rangeGroup.selectToggle(rangeGroup.getToggles().get(3));

        //Obsługa przycisków do wyboru zakresu dat
        //W klasach dziedziczących po InvestorView wywoływana jest abstrakcyjna metoda OnDataRangeChanged
        rangeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                if(new_toggle==null) {
                    toggle.setSelected(true);
                    return;
                }
                System.out.println(new_toggle.toString());

                selectedRange = DataRange.valueOf((String) new_toggle.getUserData());
                OnDataRangeChanged();
            }
        });      
        chartGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {

                 if(new_toggle==null) {
                    toggle.setSelected(true);
                    return;
                }
                //System.out.println(DataRange.valueOf((String)new_toggle.getUserData()));
                //selectedRange = DataRange.valueOf((String)new_toggle.getUserData());
                selectedChart = new_toggle.getUserData().toString();
                //System.out.println("!!!!!!!!!!!!!!!"+selectedChart);
                OnChartTypeChanged(selectedChart);
            }
        });

        //----------------------------------------------------------------------
        //Buttony do wskaźników
        //----------------------------------------------------------------------
        FlowPane pointerGroupPane = new FlowPane();
        pointerGroupPane.setAlignment(Pos.CENTER);
        pointerGroupPane.setPrefWrapLength(70);

        //Stworzenie przycisków do wyboru wskaźników
        final ToggleGroup pointerGroup = new ToggleGroup();

        ToggleButton MA = new ToggleButton();
        MA.setMinSize(48, 48);
        MA.setText("MA");
        MA.setUserData("MA");

        ToggleButton SD = new ToggleButton();
        SD.setMinSize(48, 48);
        SD.setText("SD");
        SD.setUserData("SD");

        ToggleButton bollinger = new ToggleButton();
        bollinger.setMinSize(48, 48);
        bollinger.setText("Bollinger");
        bollinger.setUserData("bollinger");

        ToggleButton koperta = new ToggleButton();
        koperta.setMinSize(48, 48);
        koperta.setText("Koperta");
        koperta.setUserData("koperta");

        ToggleButton EMA = new ToggleButton();
        EMA.setMinSize(48, 48);
        EMA.setText("EMA");
        EMA.setUserData("EMA");

        ToggleButton hide = new ToggleButton();
        hide.setMinSize(48, 48);
        hide.setText("hide");
        hide.setUserData("hide");

        MA.setToggleGroup((pointerGroup));

        //FIX
        //Ten przycisk nie powinien być w grupie z innymi!!!!!
        //SD.setToggleGroup((pointerGroup));
        bollinger.setToggleGroup((pointerGroup));
        koperta.setToggleGroup((pointerGroup));
        EMA.setToggleGroup((pointerGroup));
        hide.setToggleGroup((pointerGroup));

        pointerGroup.selectToggle(hide);

        pointerGroupPane.getChildren().addAll(MA, SD, bollinger, koperta, EMA, hide);

        pointerGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                if(new_toggle != null){
                    System.out.println(new_toggle.toString());
                    //selectedRange = DataRange.valueOf((String)new_toggle.getUserData());
                    lastPointerType = pointerType;
                    pointerType = (String) new_toggle.getUserData();
                } else {
                    lastPointerType = pointerType;
                    pointerType = "";
                }
                    OnPointerChange();
            }
        });

        SD.selectedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                OnSDPointer(newValue);
            }
        });
        
        
        
        //----------------------------------------------------------------------
        
        mainPane.add(chartGroupPane, 0, 0);
        mainPane.add(flowPane, 0, 1);
        mainPane.add(pointerGroupPane, 0, 2);

        return mainPane;
    }

    //Skróty dla enuma DataRange
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

    //Zmiana zakresu dat
    protected void ChangeDataRange(DataRange range) {
        selectedRange = range;
        OnDataRangeChanged();
    }

    //Metoda wywoływana w klasach pochodnych po zmianie zakresu dat
    protected abstract void OnDataRangeChanged();

    protected abstract void OnPointerChange();
    
    protected abstract void OnSDPointer(boolean action);

    protected abstract void OnChartTypeChanged(String chartType);
}
