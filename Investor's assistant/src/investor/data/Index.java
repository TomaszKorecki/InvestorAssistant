package investor.data;

import com.google.gson.annotations.Expose;
import java.util.Date;
import org.json.*;
/**
 * Created by on 2014-11-11.
 */
import com.google.gson.annotations.SerializedName;

public class Index {
    
    @Override
    public String toString(){
        System.out.println("here I am");
        System.out.println(symbol == null);
        return symbol + "   " + name;
    }

    @Expose
    private String symbol;

    @SerializedName("nazwa")
    private String name;

    @SerializedName("dzien")
    private Date day;

    @SerializedName("godzina")
    private Date hour;
    private double open_val;
    private double close_val;
    private double min_val;
    private double max_val;
    private double vol_val;

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the day
     */
    public Date getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(Date day) {
        this.day = day;
    }

    /**
     * @return the hour
     */
    public Date getHour() {
        return hour;
    }

    /**
     * @param hour the hour to set
     */
    public void setHour(Date hour) {
        this.hour = hour;
    }

    /**
     * @return the open_val
     */
    public double getOpen_val() {
        return open_val;
    }

    /**
     * @param open_val the open_val to set
     */
    public void setOpen_val(double open_val) {
        this.open_val = open_val;
    }

    /**
     * @return the close_val
     */
    public double getClose_val() {
        return close_val;
    }

    /**
     * @param close_val the close_val to set
     */
    public void setClose_val(double close_val) {
        this.close_val = close_val;
    }

    /**
     * @return the min_val
     */
    public double getMin_val() {
        return min_val;
    }

    /**
     * @param min_val the min_val to set
     */
    public void setMin_val(double min_val) {
        this.min_val = min_val;
    }

    /**
     * @return the max_val
     */
    public double getMax_val() {
        return max_val;
    }

    /**
     * @param max_val the max_val to set
     */
    public void setMax_val(double max_val) {
        this.max_val = max_val;
    }

    /**
     * @return the vol_val
     */
    public double getVol_val() {
        return vol_val;
    }

    /**
     * @param vol_val the vol_val to set
     */
    public void setVol_val(double vol_val) {
        this.vol_val = vol_val;
    }

 
   
}
