package blob4vstorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="BLOB4VStorage", urlPatterns={"/"})
public class RequestProcessor
  extends HttpServlet
{
  private static final String QUERY = "select name,size_in_bytes,body from sm_meta.t_file_body where body is not null and rownum=1";
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    Map<String, String[]> parametersMap = new TreeMap();
    
    parametersMap = request.getParameterMap();
    
    response.setContentType("image/jpg");
    try
    {
      Connection con = OraDS.getConnection();
      PreparedStatement stmt = con.prepareStatement(QUERY);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        response.setHeader("Content-disposition", "attachment; filename=" + rs.getString(1));
        OutputStream out = response.getOutputStream();
        InputStream in = rs.getBlob(3).getBinaryStream();
        
        response.setContentLength(rs.getInt(2));
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0) {
          out.write(buffer, 0, length);
        }
        in.close();
        out.flush();
        out.close();
      }
      rs.close();
      stmt.close();
      con.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      response.sendError(503);
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
