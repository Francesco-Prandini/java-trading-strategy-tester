/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.utility;

import java.math.BigDecimal;

/**
 *
 * @author Francesco
 *
 * Modella uno dei record della tabella del database,
 * rappresenta una quotazione riferita ad una specifica azienda, in una specifica data.
 */
public class Quote {
    private String tickerSymbol;
    private String date;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private int volume;
    private BigDecimal adjClose;

    public Quote(){}

    public Quote(String tickerSymbol, String date, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, int volume, BigDecimal adjClose) {
        this.tickerSymbol = tickerSymbol;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.adjClose = adjClose;
    }

    public BigDecimal getAdjClose() {
        return adjClose;
    }

    public BigDecimal getClose() {
        return close;
    }

    public String getDate() {
        return date;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public int getVolume() {
        return volume;
    }

    public BigDecimal getAvgPrice(){
        if(high.doubleValue()==0&&low.doubleValue()==0) return new BigDecimal(0);
        return high.add(low).divide(new BigDecimal(2));
    }

    public String toString(){
        return getTickerSymbol()+" "+getDate()+" "+getAvgPrice();
    }

    public Quote clone(){
        return new Quote(tickerSymbol,date,open,high,low,close,volume,adjClose);
    }
}
