/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.datalogger;

import tesi.DB.Datalogger.*;

import com.sun.rowset.CachedRowSetImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.StringReader;

import java.net.*;


import java.util.ArrayList;

/**
 *
 * @author Francesco
 */
public class Datalogger {
 private static final String DATE_SEPARATOR="-";

    private static DataloggerDBManager db;

   // private static ArrayList<String> notResponsedQueries=new ArrayList();

    private static int blockTryOuts=3;




    private static String get(String File) throws Exception
    {
        URI fileURI = new URI(File);

        if(fileURI.getScheme()==null||fileURI.getHost()==null||fileURI.getPath()==null) throw new MalformedURLException();

        URL fileURL = fileURI.toURL();
        InputStreamReader input = new InputStreamReader(fileURL.openStream());
        StringWriter sw = new StringWriter();

        int c = 0, dim = 0;

        while ((c = input.read()) != -1) {
            sw.write(c);
            dim++;
        }
        return sw.toString();
    }

    public static void printAll(CachedRowSetImpl c)
    {
        try{
            while(c.next()){
                System.out.println(c.getString("companyName")+" "+c.getString("ticker"));
            }
        }catch(Exception e){System.out.println(e);}
    }


    /*
     * Download della lista di titoli nasdaq dal sito http://it.advfn.com
     */
    public static void nasdaqList(){
        String nasdaqHtmlFile="";
        String nasdaqHostURL="http://it.advfn.com";
        String nasdaqByLetterHtmlFile="";
        String nasdaqByLetterBaseURL="/nasdaq/nasdaq.asp?listaazioni=";
        String nasdaqTickerSymbolURL="/nasdaq/Quotazione.asp?azione=";
        String nasdaqByLetterURL="";
        String currentTickerSymbol="";
        String currentCompanyName="";

        BufferedWriter bf1;

        int letterIndex=0,index,count=0;
        try{
                bf1=new BufferedWriter(new FileWriter(new File("nasdaq.csv")));
                nasdaqHtmlFile=get("http://it.advfn.com/nasdaq/nasdaq.asp?companies");

                /*
                 * il ciclo più esterno scorre la lista delle lettere
                 * (i titoli sono ordinati alfabeticamente, a ogni lettera corrisponde una pagina html)
                 */
                while((letterIndex=nasdaqHtmlFile.indexOf(nasdaqByLetterBaseURL,letterIndex))!=-1){
                    nasdaqByLetterURL=nasdaqHostURL+nasdaqByLetterBaseURL+nasdaqHtmlFile.charAt(letterIndex+nasdaqByLetterBaseURL.length());
                    nasdaqByLetterHtmlFile=get(nasdaqByLetterURL);
                    index=0;
                    /*
                     * il ciclo più interno scorre i titoli contenuti nella tabella attuale
                     */
                    while((index=nasdaqByLetterHtmlFile.indexOf(nasdaqTickerSymbolURL,index))!=-1){
                        currentTickerSymbol=nasdaqByLetterHtmlFile.substring
                                (index+nasdaqTickerSymbolURL.length(),nasdaqByLetterHtmlFile.indexOf("\"",index));
                        currentCompanyName=nasdaqByLetterHtmlFile.substring
                                (nasdaqByLetterHtmlFile.indexOf(">",index)+1,nasdaqByLetterHtmlFile.indexOf("<",index));

                        /*
                         * scrive su file i risultati ottenuti
                         */
                        if(currentTickerSymbol.compareTo(currentCompanyName)!=0){
                            try{
                                if(currentCompanyName.charAt(currentCompanyName.length()-1)==' '){
                                    bf1.write(currentCompanyName.substring(0,currentCompanyName.length()-1)+","+currentTickerSymbol);
                                }else bf1.write(currentCompanyName+","+currentTickerSymbol);
                                bf1.newLine();
                                bf1.flush();
                            }catch(Exception e){System.out.println(e);}
                            System.out.println(currentCompanyName+","+currentTickerSymbol);
                        }
                        index=index+nasdaqTickerSymbolURL.length();
                        count++;
                    }
                 letterIndex=letterIndex+nasdaqByLetterBaseURL.length();
             }
             System.out.println(count);

        }catch(Exception e){System.out.println(e);}
    }

    /*
     * Richiede a Yahoo storici delle quotazioni dei titoli contenuti nel file csvFile a partire dalla data dateStart
     * fino alla data dateEnd.
     * I dati ricevuti vengono registrati nel database jfdbm.
     */
    public static void getHistoricalQuotes(DataloggerDBManager jfdbm,String tableName,String csvFile,String dateStart,String dateEnd){
        String buf;
        String currentLine;
        String[] record;
        String[] csvRecord;
        String quotation="",s,symbol="",yahooCsvQuery,quotationDateTime,quotationDate[];
        BufferedReader bf1;
        BufferedReader csvReader;

        int[] start,end;

        boolean endReached=false;
        boolean blockDownloaded=false;

        CachedRowSetImpl cs;

        int i,j,k;

        try{
           // String buf;

            start=convertDate(dateStart,DATE_SEPARATOR);
            end=convertDate(dateEnd,DATE_SEPARATOR);

            csvReader=new BufferedReader(new FileReader(new File(csvFile)));

            while((currentLine=csvReader.readLine())!=null){

                csvRecord=currentLine.split(",");
                symbol=csvRecord[1];

                //Forma la Query
                yahooCsvQuery="http://ichart.yahoo.com/table.csv?s="+symbol+"&a="+(start[1]-1)
                        +"&b="+start[0]+"&c="+start[2]+"&d="+(end[1]-1)+"&e="+end[0]+"&f="+end[2]+"&g=d&ignore=.csv";
                blockDownloaded=false;
                j=0;

                    //Esegue la query fino a blockTryouts volte o fino a quando non riesce
                    //while(j<blockTryOuts&&!blockDownloaded){
                        try{
                            j++;
                            quotation="";
                            quotation=get(yahooCsvQuery);
                            blockDownloaded=true;
                            System.out.println("Query: ["+symbol+"] from: "+dateStart+" to: "+dateEnd);

                            bf1=new BufferedReader(new StringReader(quotation));
                            bf1.readLine(); //leggo la prima linea a vuoto per eliminare i nomi delle colonne

                            //immette nel database i dati ottenuti dalla query
                            while((buf=bf1.readLine())!=null){
                                try{
                                    s="";
                                    record=buf.split(",");

                                    jfdbm.insertIntoHistoricalQuotes(tableName,symbol, record[0], record[1],
                                            record[2], record[3], record[4], record[5],record[6]);
                                }catch(Exception e){
                                    System.out.println(e);
                                }

                            }
                        }catch(Exception e){
                            System.out.println(e);
                        }
                   // }
            }

        }catch(Exception e){
            System.out.println(e);
        }
    }


    /*
     * Richiede a yahoo le proprietà quoteProperties delle quotazioni correnti dei titoli contenuti nel file csvFile
     * e le registra nel database jfdbm.
     */
    public static void getQuotes(DataloggerDBManager db,String csvFile,String tableName,String quoteProperties){
        String buf;
        String[] record;
        String quotation="",s,symbol="",yahooCsvQuery,quotationDateTime,quotationDate[];
        String currentLine,currentUpdate;
        BufferedReader bf1;
        BufferedReader csvReader;

        boolean endReached=false;
        boolean blockDownloaded=false;

        CachedRowSetImpl cs;

        int i,j,k;

        try{
            csvReader=new BufferedReader(new FileReader(new File(csvFile)));
            while(!endReached){
                symbol="";

                //Aggrega i ticker symbol a 50 per volta
                for(i=0;i<50;i++){
                    try{
                        if((currentLine=csvReader.readLine())!=null){
                            if(symbol.compareTo("")==0) symbol=currentLine.split(",")[1];
                            else symbol=symbol+","+currentLine.split(",")[1];
                        }
                        else {
                            endReached=true;
                            break;
                        }
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }

                //Forma la Query
                yahooCsvQuery="http://download.finance.yahoo.com/d/quotes.csv?s="+symbol+"&f="+quoteProperties+"&e=.csv";
                blockDownloaded=false;
                j=0;

                    //Esegue la query fino a blockTryouts volte o fino a quando non riesce
                    while(j<blockTryOuts&&!blockDownloaded){
                        try{
                            j++;
                            quotation="";
                            quotation=get(yahooCsvQuery);
                            blockDownloaded=true;
                            System.out.println(yahooCsvQuery);

                            bf1=new BufferedReader(new StringReader(quotation));

                            //immette nel database i dati ottenuti dalla query
                            while((buf=bf1.readLine())!=null){
                                try{
                                    s="";
                                    currentUpdate="insert into "+tableName+" values(";
                                    record=buf.split(",");
                                    /*
                                    for(k=0;k<record.length;k++){
                                        if(k==0) {
                                            if(record[k].indexOf("N/A")!=-1){
                                                currentUpdate=currentUpdate+"null";
                                            }else currentUpdate=currentUpdate+record[k];
                                        }
                                        else{
                                            if(record[k].indexOf("N/A")!=-1){
                                                currentUpdate=currentUpdate+",null";
                                            }else currentUpdate=currentUpdate+","+record[k];
                                        }
                                    }
                                    currentUpdate=currentUpdate+")";*/

                                    //System.out.println(currentUpdate);

                                    db.insertQuotes(tableName,buf);
                                }catch(Exception e){System.out.println(e);}

                            }
                        }catch(Exception e){
                            System.out.println(e);
                        }
                    }
            }

        }catch(Exception e){
            System.out.println(e);
        }

    }


    //deve lanciare un'eccezione nel caso il formato della data non sia giusto...
    public static int[] convertDate(String date,String separator){
        int[] result;
        String[] buf;
        int i;

        buf=date.split(separator);
        result=new int[buf.length];

        for(i=0;i<buf.length;i++){
            result[i]=Integer.parseInt(buf[i]);
        }


        return result;
    }



    public static void main(String[] args) {
        try{

            //createDatabase("provaDownload2.db");
            //getQuotes(jfdbm,"prova.csv","quotes","sd1ov");

           /* db=new DataloggerDBManager("org.sqlite.JDBC","jdbc:sqlite:provaMoltoPiccolo.db");
            db.createDataset("historicalQuotes");
            getHistoricalQuotes(db,"historicalQuotes","prova.csv","1-1-2000","31-12-2013");*/

            /*
             * historicalQuotes:
             * arg[0]="-hq"
             * arg[1]--->file csv in input
             * arg[2]---->databaseName
             * arg[3]---->tableName
             * arg[4]---->dateStart
             * arg[5]---->dateEnd
             *
             * Quotes:
             * arg[0]--->file csv in input
             * arg[1]--->databaseName
             * arg[2]--->tableName
             * arg[3]--->quoteProperties
             */

            if(args[0].compareTo("-hq")==0){
                db=new DataloggerDBManager("org.sqlite.JDBC","jdbc:sqlite:"+args[2]);
                try{db.createDataset(args[3]);}catch(Exception e){System.out.println(e);}
                getHistoricalQuotes(db,args[3],args[1],args[4],args[5]);
            }else{
                db=new DataloggerDBManager("org.sqlite.JDBC","jdbc:sqlite:"+args[1]);
                getQuotes(db,args[0],args[2],args[3]);
            }

        }catch(Exception e){System.out.println(e);}
    }

}
