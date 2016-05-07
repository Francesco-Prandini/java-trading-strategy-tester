/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tesi.DB;

import java.util.*;
import java.sql.*;
import com.sun.rowset.CachedRowSetImpl;

/**
 *
 * @author Francesco
 *
 * La classe DBManager racchiude i metodi elementari di accesso al database
 *
 */
public class DBManager {

    private Statement stat;
    private Connection conn;

public DBManager(String driver,String connection) throws ClassNotFoundException,SQLException
  {
        Class.forName(driver);
        conn = DriverManager.getConnection(connection);
        stat = conn.createStatement();
  }
  public CachedRowSetImpl query(String s) throws SQLException
  {
      ResultSet rs=stat.executeQuery(s);
      CachedRowSetImpl Answer=new CachedRowSetImpl();
      Answer.populate(rs);
      return Answer;
  }
  public synchronized int update(String s) throws SQLException
  {
      return stat.executeUpdate(s);
  }
  public void close() throws SQLException
  {
      conn.close();
  }

}