/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.tester;

import java.math.BigDecimal;

/**
 *
 * @author Francesco
 */
public class GraphEntry {
    private String date;
    private BigDecimal value;

    public GraphEntry(String date, BigDecimal value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public BigDecimal getValue() {
        return value;
    }

}
