/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import org.gjt.mm.mysql.Driver;

/**
 *
 * @author U6013046
 */
public class DBConnect {
    public static Connection getConnection(){   
  Connection conn = null;   
  PreparedStatement preparedstatement = null;   
  try {   
   Class.forName("org.gjt.mm.mysql.Driver").newInstance();   
   String dbname = "dss_extractions";   
   String url ="jdbc:mysql://10.35.34.28/"+dbname+"?user=reuters1&password=reuters1" ;   
   conn = DriverManager.getConnection(url);   
  } catch (Exception e) {   
   e.printStackTrace();   
  }   
  return conn;   
}   
}
