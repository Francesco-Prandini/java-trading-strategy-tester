/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.DB.Tester;

import tesi.DB.DBManager;
import tesi.utility.Quote;

import java.sql.*;
import com.sun.rowset.CachedRowSetImpl;

import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author Francesco
 */
public class TesterDBManager extends DBManager {

    public TesterDBManager(String driver,String connection) throws ClassNotFoundException,SQLException
    {
        super(driver,connection);
    }
    public String getFirstDate(String tableName) throws SQLException
  {
      CachedRowSetImpl Answer=query("select min(date) from "+tableName);
        if(Answer.next()){
            return Answer.getString(1);
        }
        return "";

  }
  public String getLastDate(String tableName) throws SQLException
  {
      CachedRowSetImpl Answer=query("select max(date) from "+tableName);
        if(Answer.next()){
            return Answer.getString(1);
        }
        return "";

  }
  public String getCurrentDate() throws SQLException
  {
        CachedRowSetImpl Answer=query("select datetime('now')");
        if(Answer.next()){
            return Answer.getString(1);
        }
        return null;
  }
  
  public Quote getQuote(String tableName,String symbol,String date) throws SQLException
  {
      CachedRowSetImpl Answer=query("select * from "+tableName+" where ticker = \""+symbol+"\"" +
              " and date = \""+date+"\"");
      if(Answer.next()){
          return new Quote(Answer.getString(1),Answer.getString(2),Answer.getBigDecimal(3),Answer.getBigDecimal(4),Answer.getBigDecimal(5)
                  ,Answer.getBigDecimal(6),Answer.getInt(7),Answer.getBigDecimal(8));
      }
      return new Quote();
  }
  public ArrayList<String> getAllSymbols(String tableName) throws SQLException
  {
      ArrayList<String> s=new ArrayList();
      CachedRowSetImpl Answer=query("select distinct(ticker) from "+tableName);
      while(Answer.next()){
          s.add(Answer.getString(1));
      }
      return s;
  }
  public ArrayList<Quote> getAllQuotes(String tableName,String date) throws SQLException
  {
      ArrayList<Quote> s=new ArrayList();
      CachedRowSetImpl Answer=query("select * from "+tableName+" where date = \""+date+"\"");
      while(Answer.next()){
          s.add(new Quote(Answer.getString(1),Answer.getString(2),Answer.getBigDecimal(3),Answer.getBigDecimal(4),Answer.getBigDecimal(5)
                  ,Answer.getBigDecimal(6),Answer.getInt(7),Answer.getBigDecimal(8)));
      }
      return s;
  }
  public HashMap<String,Quote> getAllQuotesHashMap(String tableName,String date) throws SQLException
  {
      HashMap<String,Quote> s=new HashMap();
      CachedRowSetImpl Answer=query("select * from "+tableName+" where date = \""+date+"\"");
      while(Answer.next()){
          s.put(Answer.getString(1),new Quote(Answer.getString(1),Answer.getString(2),Answer.getBigDecimal(3),Answer.getBigDecimal(4),Answer.getBigDecimal(5)
                  ,Answer.getBigDecimal(6),Answer.getInt(7),Answer.getBigDecimal(8)));
      }
      return s;
  }



}
