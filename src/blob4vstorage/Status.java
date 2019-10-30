package blob4vstorage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Status extends HttpServlet{
  private static final String QUERY = "select 1 from dual";
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    Connection con;
    try{
        con = OraDS.getConnection();
    }catch(Exception e){
        response.sendError(503,"Could not aquire database connection! "+e.getMessage());
        return;
    }
    Statement stmt;
    try {
        stmt = con.createStatement();
    } catch (SQLException e) {
        response.sendError(503,"Could not prepare statement! "+e.getMessage());
        return;
    }
    ResultSet rs;
    PrintWriter out;
    try {
        rs = stmt.executeQuery(QUERY);
        rs.close();
        out = response.getWriter();
        out.println("OK");
    } catch (SQLException e) {
        response.sendError(503,"Error executing test query! "+e.getMessage());
    }
    try{
        stmt.close();
        con.close();
    }catch(Exception e){}
  }    
    
}
