package blob4vstorage;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import javax.servlet.annotation.WebServlet;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

@WebServlet(name="BLOB4VStorage", urlPatterns={"/"})
public class BLOB4VStorage
{
  public static String dbUSN;
  public static String dbPWD;
  public static String dbSTR;
  
  public static void main(String[] args)
    throws Exception
  {
    int port = Integer.parseInt(args[0]);
    dbUSN = args[1];
    dbPWD = args[2];
    dbSTR = args[3];
    Connection initialConnection = OraDS.getConnection();
    initialConnection.close();
    
    System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
    System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
    
    Server server = new Server(port);
    
    MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
    server.addBean(mbContainer);
    
    ServletHandler handler = new ServletHandler();
    server.setHandler(handler);
    handler.addServletWithMapping(RequestProcessor.class, "/*");
    server.start();
    
    server.dumpStdErr();
    
    server.join();
  }
}
