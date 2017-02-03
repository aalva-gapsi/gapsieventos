package example.jpa;

public class SSLTEST {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ServerName = "75.126.155.153";
		int PortNumber = 50000;
		String DatabaseName = "SQLDB";
		String user = "user13014";
		String userPassword = "u0JmLzkwNurd";
		String traceFileLocation = "";

		java.util.Properties properties = new java.util.Properties();

		properties.put("user", "user ID that has access to SQLDB");
		properties.put("password", "password for the user ID that has access to SQLDB");
		properties.put("sslConnection", "true");
		// db2://user13014:u0JmLzkwNurd@75.126.155.153:50000/SQLDB
		String url = "jdbc:db2://" + ServerName + ":" + PortNumber + "/" + DatabaseName + ":" + traceFileLocation + ";";
		String url2 = "jdbc:db2://user13014:u0JmLzkwNurd@75.126.155.153:50000/SQLDB";

		java.sql.Connection con = null;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
		} catch (Exception e) {
			System.out.println("Error: failed to load Db2 jcc driver.");
		}

		try {
			System.out.println("url: " + url);
			con = java.sql.DriverManager.getConnection(url, properties);
			if (con != null) {
				System.out.println("Success");
			} else {
				System.out.println("Failed to make the connection");
			}
			con.close();
		} catch (Exception e) {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}
}