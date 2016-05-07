/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.DB.Datalogger;

import tesi.DB.DBManager;

import java.sql.*;
import com.sun.rowset.CachedRowSetImpl;

/**
 *
 * @author Francesco
 */
public class DataloggerDBManager extends DBManager {
    public DataloggerDBManager(String driver,String connection) throws ClassNotFoundException,SQLException
    {
        super(driver,connection);
    }
    public void createDataset(String tableName) throws SQLException
  {
      update("CREATE TABLE "+tableName+"(ticker VARCHAR(45),date DATE," +
        "open REAL,high REAL,low REAL,close REAL," +
         "volume INTEGER,adjClose REAL, PRIMARY KEY (ticker,date))");
   }

  public String getCurrentDate() throws SQLException
  {
        ResultSet rs=query("select datetime('now')");
        CachedRowSetImpl Answer=new CachedRowSetImpl();
        Answer.populate(rs);
        if(Answer.next()){
            return Answer.getString(1);
        }
        return null;
  }
  public CachedRowSetImpl getAll()throws SQLException
  {
        ResultSet rs=query("select * from companies");
        CachedRowSetImpl Answer=new CachedRowSetImpl();
        Answer.populate(rs);
        rs.close();
        return Answer;
  }
  public void insertIntoHistoricalQuotes(String tableName,String symbol,String date,String open,String high,String low,
          String close,String volume,String adjClose)throws SQLException
  {
        update("insert into "+tableName+"(ticker,date,open,high,low,close,volume,adjClose)" +
                                            " values(\""+symbol+"\","+"\""+date+"\""+","+open+","
                                            +high+","+low+","+close+","+volume+","+adjClose+")");
  }
  public void insertHistoricalQuotes(){

  }
  public void insertQuotes(String tableName,String quote) throws SQLException
  {
        int k;
        String s="";
        String currentUpdate="insert into "+tableName+" values(";
        String[] record=quote.split(",");

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
        currentUpdate=currentUpdate+")";
        update(currentUpdate);
  }
}
