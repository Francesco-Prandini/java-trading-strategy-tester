/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.utility;

import java .math.BigDecimal;

/**
 *
 * @author Francesco
 * Modella una certa quantità di azioni di una specifica azienda comprate in un'unica tranche
 * (quindi allo stesso prezzo e con riferimento a un'unica quotazione)
 */
public class Stock {
    private Quote referenceQuote;
    private int volumeBought;
    private BigDecimal initialValue;
    private long buyingIndex;

    public Stock(Quote referenceQuote, int volumeBought,long buyingIndex) {
        this.referenceQuote = referenceQuote;
        this.volumeBought = volumeBought;
        this.initialValue=referenceQuote.getAvgPrice().multiply(new BigDecimal(volumeBought));
        this.buyingIndex=buyingIndex;
    }

    public Quote getReferenceQuote() {
        return referenceQuote;
    }

    public int getVolumeBought() {
        return volumeBought;
    }

    public BigDecimal getInitialValue(){
        return initialValue;
    }
    public long getBuyingIndex(){
        return buyingIndex;
    }
    public Stock clone(){
        return new Stock(referenceQuote.clone(),volumeBought,buyingIndex);
    }

}
