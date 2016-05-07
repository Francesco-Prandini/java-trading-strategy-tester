/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.tester;

import tesi.utility.*;
import tesi.DB.Tester.TesterDBManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import com.sun.rowset.CachedRowSetImpl;
import java.math.BigDecimal;

import java.lang.reflect.Constructor;

import javax.swing.JFrame;

import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author Francesco
 *
 * La classe StrategyTester si occupa di testare un determinato algoritmo di trading sul dataset fornito
 * al costruttore.
 */
public class StrategyTester extends Thread {

    private TesterDBManager db;

    private Properties configuration=new Properties();

    private ArrayList<String> allSymbols;

    private HashMap<String,Quote> currentQuotations;

    private GregorianCalendar currentDate;

    private Portfolio currentPortfolio;

    private boolean endedSimulation=true;


    private int daysToSkip;
    private String databaseName;
    private String tableName;
    private String className;

    private BigDecimal initialCash;

    private int currentDayNumber;

    /**************************************************************************/
    private ArrayList<GraphEntry> historicalPortfolioValues=new ArrayList();
    private ArrayList<GraphEntry> historicalSharesValues=new ArrayList();
    private JFrame historicalPortfolioValueFrame;
    private JFrame historicalSharesValueFrame;
    /**************************************************************************/

    public StrategyTester(String databaseName,String tableName,String className,BigDecimal initialCash ,int daysToSkip,String configFile){
        this.databaseName=databaseName;
        this.tableName=tableName;
        this.className=className;
        this.initialCash=initialCash;
        this.daysToSkip=daysToSkip;
        initialize(databaseName,tableName);
        init_configuration(configFile);
    }
    /*
     * disegna un grafico che rappresenta l'andamento del portafoglio
     */
    public void drawPortfolio(){
        JFrame f=new JFrame(className+": Portfolio");
        f.add(new GraphPanel(historicalPortfolioValues));
        f.setSize(800,600);
        f.setVisible(true);
        historicalPortfolioValueFrame=f;
    }
    /*
     * Disegna un grafico che rappresenta l'andamento delle azioni possedute
     */
     public void drawShares(){
        JFrame f=new JFrame(className+": Shares");
        f.add(new GraphPanel(historicalSharesValues));
        f.setSize(800,600);
        f.setVisible(true);
        historicalSharesValueFrame=f;
    }
    private GregorianCalendar getGregorianCalendar(String date){
        String[] record=date.split("-");
        return new GregorianCalendar(Integer.parseInt(record[0]),Integer.parseInt(record[1])-1,Integer.parseInt(record[2]));
    }

    private void setCalendar(String date){
        currentDate=getGregorianCalendar(date);
    }

    private void setCurrentDayQuotes(){
        int i;
        try{
            ArrayList<Quote> newQuotes; //quotazioni nel nuovo giorno corrente
            newQuotes=db.getAllQuotes(tableName,getCurrentDate());
            for(i=0;i<newQuotes.size();i++){
                currentQuotations.put(newQuotes.get(i).getTickerSymbol(),newQuotes.get(i));
            }
        }catch(Exception e){System.out.println(e);}
    }
    private String format(int i){
        if(i<10) return "0"+i;
        else return ""+i;
    }
    public String getCurrentDate(){
        return currentDate.get(Calendar.YEAR)+"-"+format(currentDate.get(Calendar.MONTH)+1)+"-"+format(currentDate.get(Calendar.DAY_OF_MONTH));
    }

    /*
     * confronta la data corrente a date e restituisce un intero secondo la definizione del metodo compareTo
     * della classe GregorianCalendar:
     * 0--->le due date corrispondono,
     * <0---> currentDate è precedente a date,
     * >0---> currentDate è successiva a date.
     */
    public int compareCurrentDateTo(String date){
        int result;
        GregorianCalendar comparingDate=getGregorianCalendar(date);
        return currentDate.compareTo(comparingDate);
    }

     /*
     * Il metodo skip() passa al giorno successivo e aggiorna le quotazioni correnti;
     * possono esserci dei buchi (un titolo può non avere quotazioni per un certo giorno) e
     * possono esserci titoli che sono quotati solo da un certo giorno in poi.
     *
     * titolo quotato per la prima volta nel giorno corrente ---> la quotazione viene aggiunta
     * titolo già quotato ---> la quotazione viene aggiornata
     * titolo quotato precedentemente ma senza quotazione nel giorno corrente ---> la quotazione rimane quella del giorno precedente
     */
    public void skip(){
        try{
            System.out.println("***********************************************");
            System.out.println("***********************************************");
            System.out.println(getCurrentDate());
            System.out.println("Current Cash:"+getPortfolio().getCurrentCash());
            System.out.println("Current Shares Value: "+getPortfolio().getCurrentSharesValue());
            if(compareCurrentDateTo(db.getLastDate(tableName))!=0){
                currentDate.add(Calendar.DAY_OF_MONTH,daysToSkip);
                setCurrentDayQuotes();
            }else{
                endSimulation();
            }
            currentDayNumber++;
            System.out.println("***********************************************");
            System.out.println();
        }catch(Exception e){System.out.println(e);}


    }
    public void skipToDate(String date){
        setCalendar(date);
        try{
            if(compareCurrentDateTo(db.getLastDate(tableName))!=0){
                setCurrentDayQuotes();
            }else{
                endSimulation();
            }
            currentDayNumber++;
        }catch(Exception e){System.out.println(e);}

    }
    /*
     * Esegue una skip() finchè tutti i simboli contenuti in allSymbols
     * non hanno una quotazione in currentQuotations.
     * (Alcuni titoli possono avere un buco nei primi giorni della simulazione)
     */
    public void waitForAll(){
        int i;
        boolean begin=false;
        while(!begin){
            begin=true;
            for(i=0;i<allSymbols.size();i++){
                String s=allSymbols.get(i);
                if(currentQuotations.get(allSymbols.get(i))==null){
                    begin=false;
                    skip();
                    break;
                }
            }
        }
    }
    public  void endSimulation(){
        currentPortfolio.sellAll();
        System.out.println("Initial cash:"+initialCash+" -----> Final cash:"+currentPortfolio.getCurrentCash());
        endedSimulation=true;
    }
    private void init_configuration(String path){
        try{configuration.load(new FileInputStream(path));}catch(Exception e){System.out.println(e);}
    }
    private void initialize(String databaseName,String tableName){
        int i;
        BigDecimal zero=new BigDecimal(0);
        try{
            db=new TesterDBManager("org.sqlite.JDBC","jdbc:sqlite:"+databaseName); //inizializza il DBManager
            setCalendar(db.getFirstDate(tableName)); //inizializza currentDate al valore della prima data della tabella tableName
            allSymbols=db.getAllSymbols(tableName); //inizializza l'ArrayList contenente tutti i tickerSymbol contenuti in tableName
            currentQuotations=db.getAllQuotesHashMap(tableName, getCurrentDate());

            for(i=0;i<allSymbols.size();i++){ //inizializza con una quotazione nulla tutti i titoli
                if(currentQuotations.get(allSymbols.get(i))==null){    //che non sono quotati il primo giorno
                    Quote nullQuote=new Quote(allSymbols.get(i),getCurrentDate(),zero,zero,zero,zero,0,zero);
                    currentQuotations.put(nullQuote.getTickerSymbol(), nullQuote);
                }
            }
            currentPortfolio=new Portfolio(initialCash);
            endedSimulation=false;
            currentDayNumber=0;
        }catch(Exception e){System.out.println(e);}
    }

    public ArrayList<String> getAllSymbols(){
        return allSymbols;
    }
    public Quote getCurrentQuote(String symbol){
        return currentQuotations.get(symbol);
    }
    public ArrayList<Quote> getAllQuotes()
    {
        return new ArrayList(currentQuotations.values());
    }

    public HashMap<String,Quote> getAllCurrentQuotes(){
        HashMap<String,Quote> hsm=new HashMap();
        hsm.putAll(currentQuotations);
        return hsm;
    }


    public BigDecimal getInitialBudget(){
        return initialCash;
    }
    public Portfolio getPortfolio(){
        return currentPortfolio;
    }
    public boolean endedSimulation(){
        return endedSimulation;
    }
    public void addCurrentPortfolioValue(){
        historicalPortfolioValues.add(new GraphEntry(getCurrentDate(),currentPortfolio.getPortfolioValue()));
    }
    public void addCurrentSharesValue(){
        historicalSharesValues.add(new GraphEntry(getCurrentDate(),currentPortfolio.getCurrentSharesValue()));
    }
    public String getProperty(String prop){
        return configuration.getProperty(prop);
    }

    public void run() {

        try{
            Class c=Class.forName(className);
            Constructor[] s=c.getConstructors();
            TradingStrategy ts=(TradingStrategy)s[0].newInstance(this);
            ts.applyStrategy();
        }catch(Exception e){System.out.println(e);}

    }

    /*
     * La classe portfolio rappresenta il portafoglio dell'utente durante il processo di testing.
     * Fornisce inoltre i metodi per comprare e vendere azioni.
     */
    public class Portfolio {
        private BigDecimal currentCash;
        private ArrayList<Stock> shares;
        private long currentBuyingIndex=0;

        private Portfolio(BigDecimal initialBudget){
            this.currentCash=initialBudget;
            shares=new ArrayList();
        }

        public ArrayList<Stock> getHoldingShares(String symbol){
            int i;
            ArrayList<Stock> s=new ArrayList();

            for(i=0;i<shares.size();i++){
                if(shares.get(i).getReferenceQuote().getTickerSymbol().compareTo(symbol)==0){
                    s.add(shares.get(i).clone());
                }
            }
            return s;
        }
        public ArrayList<Stock> getAllHoldingShares(){
            int i;
            ArrayList<Stock> s=new ArrayList();

            for(i=0;i<shares.size();i++){
                s.add(shares.get(i).clone());
            }

            return s;
        }
        public BigDecimal getCurrentCash(){
            return currentCash;
        }
        public BigDecimal getCurrentSharesValue(){
            int i;
            BigDecimal bd=new BigDecimal(0);

            for(i=0;i<shares.size();i++){
                Quote currentQuotation=currentQuotations.get(shares.get(i).getReferenceQuote().getTickerSymbol());
                bd=bd.add(currentQuotation.getAvgPrice().multiply(new BigDecimal(shares.get(i).getVolumeBought())));
            }
            return bd;
        }
        public BigDecimal getPortfolioValue(){
            return getCurrentCash().add(getCurrentSharesValue());
        }
    /*
     * buy restituisce true se l'acquisto è andato a buon fine: cioè se
     * viene trovata una quotazione corrispondente a symbol.
     *
     */

        public boolean buy(Quote q,int volume){
            if(q!=null){
                Stock buying=new Stock(q,volume,currentBuyingIndex);
                if(buying.getInitialValue().compareTo(currentCash)!=1){
                    shares.add(buying);
                    currentCash=currentCash.subtract(buying.getInitialValue());
                    currentBuyingIndex++;
                    System.out.println("Bought: "+q+" volume: "+volume+" current cash: "+getCurrentCash()+
                        " current shares value: "+getCurrentSharesValue());
                    return true;
                }
            }
            System.out.println("Tried to buy: "+q+" volume: "+volume+" current cash: "
                    +getCurrentCash()+" current shares value: "+getCurrentSharesValue());
            return false;
        }
        public boolean sell(Stock s){
            int i;
            Quote currentQuotation=currentQuotations.get(s.getReferenceQuote().getTickerSymbol());

            for(i=0;i<shares.size();i++){
                if(shares.get(i).getBuyingIndex()==s.getBuyingIndex()){
                    if(shares.remove(i)!=null){
                        currentCash=currentCash.add(currentQuotation.getAvgPrice().multiply(new BigDecimal(s.getVolumeBought())));
                        System.out.println("Sold: "+s.getReferenceQuote()+" volume: "
                                +s.getVolumeBought()+" current cash: "+getCurrentCash()+" current shares value: "+getCurrentSharesValue());
                        return true;
                    }
                    System.out.println("Tried to sell: "+s.getReferenceQuote()+
                            " volume: "+s.getVolumeBought()+" current cash: "+getCurrentCash()+" current shares value: "+getCurrentSharesValue());
                    return false;
                }
            }
            return false;
        }
        public void sellAll(){
            int i;
            for(i=shares.size()-1;i>=0;i--){
                sell(shares.get(i));
            }
        }
    }

}
