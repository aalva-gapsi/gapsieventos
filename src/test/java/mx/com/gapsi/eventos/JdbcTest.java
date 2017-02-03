package mx.com.gapsi.eventos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
@Ignore
public class JdbcTest {
	public static final String DRIVER_CLASS_NAME = "com.ibm.db2.jcc.DB2Driver";
	   public static final String DB_CONN_STRING = "jdbc:db2://75.126.155.153:50000/SQLDB";
	   public static final String USER_NAME = "user13014";
	   public static final String PASSWORD = "u0JmLzkwNurd";
	   
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
//		fail("Not yet implemented");
	      Connection result = null;
	       try {
	          Class.forName(DRIVER_CLASS_NAME).newInstance();
	       } catch (Exception ex){
	          System.err.println("Check classpath. Cannot load db driver: " + DRIVER_CLASS_NAME);
	       }
	       try {
	         result = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
	       } catch (SQLException e){
	          System.err.println( "Driver loaded, but cannot connect to db: " + DB_CONN_STRING);
	       }
	       
			org.junit.Assert.assertNotNull("JDBC Exitoso", result);
	}

}
