package blob4vstorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="BLOB4VStorage", urlPatterns={"/"})
public class RequestProcessor
  extends HttpServlet
{
  private static final String QUERY = "select name,body from sm_meta.t_file_body where body is not null and id_file_body = ? ";
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    
    long imageId;
    Connection con;
    
    if ( request.getParameter("id") == null){
        return;
    }else{
        imageId = Long.parseLong(request.getParameter(("id")));
    }
    
    
    
    try{
        con = OraDS.getConnection();
    }catch(Exception e){
        response.sendError(503,"Could not aquire database connection!");
        return;
    }
    try
    {
      PreparedStatement stmt = con.prepareStatement(QUERY);
      stmt.setLong(1, imageId);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        response.setContentType("image/jpg");
        response.setHeader("Content-disposition", "attachment; filename=" + rs.getString(1));
        //response.setContentLength(rs.getInt(2));
        
        OutputStream out = response.getOutputStream();
        InputStream in = rs.getBlob(2).getBinaryStream();        
        byte[] buffer = new byte[4096];
        int length;        
        try{
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
                out.flush();
            }
        }catch(Exception e){
          System.out.println(e.getMessage());
        }
        in.close();
        rs.close();
        //out.flush();
        out.close();
      }else{
          response.sendError(404);
      }
      stmt.close();
      con.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      response.sendError(503, e.getMessage());
    }
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }
  
  public String getServletInfo()
  {
    return "Spool blob from Oracle Database as image";
  }
}
