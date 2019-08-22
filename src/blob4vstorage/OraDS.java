package blob4vstorage;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.OracleConnection;

public final class OraDS
{
  private static final ComboPooledDataSource cpds = new ComboPooledDataSource();
  
  static
  {
    try
    {
      cpds.setDriverClass("oracle.jdbc.driver.OracleDriver");
    }
    catch (PropertyVetoException ex)
    {
      ex.printStackTrace();
    }
    Properties props = new Properties();
    props.setProperty(OracleConnection.CONNECTION_PROPERTY_USER_NAME, BLOB4VStorage.dbUSN);
    props.setProperty(OracleConnection.CONNECTION_PROPERTY_PASSWORD, BLOB4VStorage.dbPWD);
    props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_CONNECT_TIMEOUT, "10000");
    props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_READ_TIMEOUT, "60000");
    props.setProperty(OracleConnection.CONNECTION_PROPERTY_AUTOCOMMIT, "false");
    cpds.setJdbcUrl(BLOB4VStorage.dbSTR);
    cpds.setMinPoolSize(16);
    cpds.setAcquireIncrement(8);
    cpds.setMaxPoolSize(256);
    cpds.setMaxIdleTime(60);
    cpds.setNumHelperThreads(16);
    cpds.setForceSynchronousCheckins(true);
    cpds.setProperties(props);
  }
  
  public static Connection getConnection()
    throws SQLException
  {
    return cpds.getConnection();
  }
}
