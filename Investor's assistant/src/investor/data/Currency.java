/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.data;


/**
 *
 * @author Tomasz
 */
public class Currency {
    
    private String name;
    private String symbol;
    private float rate;
    private float change;
    
    public Currency(String name, String symbol, float rate, float change){
        this.name = name;
        this.symbol = symbol;
        this.rate = rate;
        this.change = change;
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
     * @return the rate
     */
    public float getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(float rate) {
        this.rate = rate;
    }

    /**
     * @return the change
     */
    public float getChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    public void setChange(float change) {
        this.change = change;
    }
}
