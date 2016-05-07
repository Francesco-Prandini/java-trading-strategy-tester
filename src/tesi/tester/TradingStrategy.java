/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.tester;

import tesi.utility.Quote;
import tesi.utility.Stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigDecimal;

/**
 *
 * @author Francesco
 */
public abstract class TradingStrategy {

    private StrategyTester simulationAccess;

    public TradingStrategy(){}

    public TradingStrategy(StrategyTester tp){
        this.simulationAccess=tp;
    }

    public String getCurrentDate(){
        return simulationAccess.getCurrentDate();
    }
    public int compareCurrentDateTo(String date){
        return simulationAccess.compareCurrentDateTo(date);
    }
    public void skip(){
        simulationAccess.skip();
    }
    public void skipToDate(String date){
        simulationAccess.skipToDate(date);
    }
    public void waitForAll(){
        simulationAccess.waitForAll();
    }
    public ArrayList<String> getAllSymbols(){
        return simulationAccess.getAllSymbols();
    }
    public Quote getCurrentQuote(String Symbol){
        return simulationAccess.getCurrentQuote(Symbol);
    }
    public ArrayList<Quote> getAllQuotes(){
        return simulationAccess.getAllQuotes();
    }
    public HashMap<String,Quote> getAllCurrentQuotes(){
        return simulationAccess.getAllCurrentQuotes();
    }
    public ArrayList<Stock> getHoldingShares(String symbol){
        return simulationAccess.getPortfolio().getHoldingShares(symbol);
    }
    public boolean buy(Quote q,int volume){
        return simulationAccess.getPortfolio().buy(q, volume);
    }
    public boolean sell(Stock s){
        return simulationAccess.getPortfolio().sell(s);
    }
    public BigDecimal getCurrentCash(){
        return simulationAccess.getPortfolio().getCurrentCash();
    }
    public BigDecimal getInitialBudget(){
        return simulationAccess.getInitialBudget();
    }
    public BigDecimal getCurrentSharesValue(){
        return simulationAccess.getPortfolio().getCurrentSharesValue();
    }
    public ArrayList<Stock> getHoldingShares(){
        return simulationAccess.getPortfolio().getAllHoldingShares();
    }
    public boolean endedSimulation(){
        return simulationAccess.endedSimulation();
    }
    public void endSimulation(){
        simulationAccess.endSimulation();
    }
    public void addCurrentPortfolioValue(){
        simulationAccess.addCurrentPortfolioValue();
    }
    public void addCurrentSharesValue(){
        simulationAccess.addCurrentSharesValue();
    }
    public String getProperty(String prop){
        return simulationAccess.getProperty(prop);
    }
    public void drawPortfolio(){
        simulationAccess.drawPortfolio();
    }
    public void drawShares(){
        simulationAccess.drawShares();
    }

    public abstract void applyStrategy();

}
