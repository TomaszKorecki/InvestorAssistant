package investor.charts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import investor.data.Index;

/**
 *
 * @author Michal
 * klasa służąca do tworzenia wykresu świeczkowego
 */
public class CandleChart {
    
    private static double[][] data;
    
    
    //metoda przyjmująca tablicę indexów, generująca dane dla wykresu
    public static void generateData(Index[] indexData) {
        data = new double[indexData.length][6];
        for (int i = 0; i < indexData.length; i++) {

            data[i][0] = i+1;
            data[i][1] = indexData[i].getOpen_val();
            data[i][2] = indexData[i].getClose_val();
            data[i][3] = indexData[i].getMin_val();
            data[i][4] = indexData[i].getMax_val();
            data[i][5] = indexData[i].getVol_val();
        }        
    }

    public CandleStickChart createChart() {
        final NumberAxis xAxis = new NumberAxis(0,data.length+1,1);
        
        xAxis.setMinorTickCount(0);
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setForceZeroInRange(false);
        final CandleStickChart bc = new CandleStickChart(xAxis,yAxis);

        xAxis.setLabel("Dzień");
        yAxis.setLabel("Wartość");
        // dodanie poczatkowych danych
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        for (int i=0; i< data.length; i++) {
            double[] day = data[i];
            series.getData().add(
                new XYChart.Data<Number,Number>(day[0],day[1],new CandleStickValues(day[2],day[3],day[4],day[5]))
            );
        }
        ObservableList<XYChart.Series<Number,Number>> data = bc.getData();
        if (data == null) {
            data = FXCollections.observableArrayList(series);
            bc.setData(data);
        } else {
            bc.getData().add(series);
        }
        return bc;
    }

    public class CandleStickChart extends XYChart<Number, Number> {

        public CandleStickChart(Axis<Number> xAxis, Axis<Number> yAxis) {
            super(xAxis, yAxis);
            setAnimated(false);
            xAxis.setAnimated(false);
            yAxis.setAnimated(false);
        }

        public CandleStickChart(Axis<Number> xAxis, Axis<Number> yAxis, ObservableList<XYChart.Series<Number, Number>> data) {
            this(xAxis, yAxis);
            setData(data);
        }

        /** 
         * Metoda wywoływana, żeby zaktualizowac i ustawic dziecko na wykresie 
         */
        @Override 
        protected void layoutPlotChildren() {
            if (getData() == null) {
                return;
            }
            for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
                XYChart.Series<Number, Number> series = getData().get(seriesIndex);
                Iterator<XYChart.Data<Number, Number>> iter = getDisplayedDataIterator(series);
                Path seriesPath = null;
                if (series.getNode() instanceof Path) {
                    seriesPath = (Path) series.getNode();
                    seriesPath.getElements().clear();
                }
                while (iter.hasNext()) {
                    XYChart.Data<Number, Number> item = iter.next();
                    double x = getXAxis().getDisplayPosition(getCurrentDisplayedXValue(item));
                    double y = getYAxis().getDisplayPosition(getCurrentDisplayedYValue(item));
                    Node itemNode = item.getNode();
                    CandleStickValues extra = (CandleStickValues) item.getExtraValue();
                    if (itemNode instanceof Candle && extra != null) {
                        Candle candle = (Candle) itemNode;

                        double close = getYAxis().getDisplayPosition(extra.getClose());
                        double high = getYAxis().getDisplayPosition(extra.getHigh());
                        double low = getYAxis().getDisplayPosition(extra.getLow());
                        double candleWidth = -1;
                        if (getXAxis() instanceof NumberAxis) {
                            NumberAxis xa = (NumberAxis) getXAxis();
                            candleWidth = xa.getDisplayPosition(xa.getTickUnit()) * 0.40;
                        }
                        candle.update(close - y, high - y, low - y, candleWidth);

                        candle.setLayoutX(x);
                        candle.setLayoutY(y);
                    }
                    if (seriesPath != null) {
                        if (seriesPath.getElements().isEmpty()) {
                            seriesPath.getElements().add(new MoveTo(x, getYAxis().getDisplayPosition(extra.getAverage())));
                        } else {
                            seriesPath.getElements().add(new LineTo(x, getYAxis().getDisplayPosition(extra.getAverage())));
                        }
                    }
                }
            }
        }

        @Override 
        protected void dataItemChanged(XYChart.Data<Number, Number> item) {}

        @Override 
        protected void dataItemAdded(XYChart.Series<Number, Number> series, int itemIndex, XYChart.Data<Number, Number> item) {}

        @Override 
        protected void dataItemRemoved(XYChart.Data<Number, Number> item, XYChart.Series<Number, Number> series) {}

        @Override 
        protected void seriesRemoved(XYChart.Series<Number, Number> series) {}

        @Override 
        protected void seriesAdded(XYChart.Series<Number, Number> series, int seriesIndex) {
            for (int j = 0; j < series.getData().size(); j++) {
                XYChart.Data item = series.getData().get(j);
                Node candle = createCandle(seriesIndex, item, j);
                getPlotChildren().add(candle);
            }
        }

        /**
         * Tworzenie pojedynczej swiecy
         */
        private Node createCandle(int seriesIndex, final XYChart.Data item, int itemIndex) {
            Node candle = item.getNode();
            if (candle instanceof Candle) {
                ((Candle) candle).setSeriesAndDataStyleClasses("series" + seriesIndex, "data" + itemIndex);
            } else {
                candle = new Candle("series" + seriesIndex, "data" + itemIndex);
                item.setNode(candle);
            }
            return candle;
        }

        /**
         * Metoda sluzaca do uaktualniania zakresow
         */
        @Override
        protected void updateAxisRange() {
            final Axis<Number> xa = getXAxis();
            final Axis<Number> ya = getYAxis();
            List<Number> xData = null;
            List<Number> yData = null;
            if (xa.isAutoRanging()) {
                xData = new ArrayList<Number>();
            }
            if (ya.isAutoRanging()) {
                yData = new ArrayList<Number>();
            }
            if (xData != null || yData != null) {
                for (XYChart.Series<Number, Number> series : getData()) {
                    for (XYChart.Data<Number, Number> data : series.getData()) {
                        if (xData != null) {
                            xData.add(data.getXValue());
                        }
                        if (yData != null) {
                            CandleStickValues extras = (CandleStickValues) data.getExtraValue();
                            if (extras != null) {
                                yData.add(extras.getHigh());
                                yData.add(extras.getLow());
                            } else {
                                yData.add(data.getYValue());
                            }
                        }
                    }
                }
                if (xData != null) {
                    xa.invalidateRange(xData);
                }
                if (yData != null) {
                    ya.invalidateRange(yData);
                }
            }
        }
    }

    private class CandleStickValues {
        private double close;
        private double high;
        private double low;
        private double average;

        public CandleStickValues(double close, double high, double low, double average) {
            this.close = close;
            this.high = high;
            this.low = low;
            this.average = average;
        }

        public double getClose() {
            return close;
        }

        public double getHigh() {
            return high;
        }

        public double getLow() {
            return low;
        }

        public double getAverage() {
            return average;
        }
    }

    private class Candle extends Group {
        private Line highLowLine = new Line();
        private Region bar = new Region();
        private String seriesStyleClass;
        private String dataStyleClass;
        private boolean openAboveClose = true;

        private Candle(String seriesStyleClass, String dataStyleClass) {
            setAutoSizeChildren(false);
            getChildren().addAll(highLowLine, bar);
            this.seriesStyleClass = seriesStyleClass;
            this.dataStyleClass = dataStyleClass;
            updateStyleClasses();
        }

        public void setSeriesAndDataStyleClasses(String seriesStyleClass, String dataStyleClass) {
            this.seriesStyleClass = seriesStyleClass;
            this.dataStyleClass = dataStyleClass;
            updateStyleClasses();
        }

        public void update(double closeOffset, double highOffset, double lowOffset, double candleWidth) {
            openAboveClose = closeOffset > 0;
            updateStyleClasses();
            highLowLine.setStartY(highOffset);
            highLowLine.setEndY(lowOffset);
            if (candleWidth == -1) {
                candleWidth = bar.prefWidth(-1);
            }
            if (openAboveClose) {
                bar.resizeRelocate(-candleWidth / 2, 0, candleWidth, closeOffset);
            } else {
                bar.resizeRelocate(-candleWidth / 2, closeOffset, candleWidth, closeOffset * -1);
            }
        }

        private void updateStyleClasses() {
            getStyleClass().setAll("candlestick-candle", seriesStyleClass, dataStyleClass);
            highLowLine.getStyleClass().setAll("candlestick-line", seriesStyleClass, dataStyleClass,
                    openAboveClose ? "open-above-close" : "close-above-open");
            bar.getStyleClass().setAll("candlestick-bar", seriesStyleClass, dataStyleClass,
                    openAboveClose ? "open-above-close" : "close-above-open");
        }
    }

}