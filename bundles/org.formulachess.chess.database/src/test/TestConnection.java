package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnection {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver"); //$NON-NLS-1$
			Connection conn = DriverManager.getConnection("jdbc:mysql://mysql.cercleechecshull.org/ceh_test?" + "user=javasql&password=123456"); //$NON-NLS-1$ //$NON-NLS-2$
			// Do something with the Connection
			Statement stmt = conn.createStatement();
//			final String creerTableCafe = "CREATE TABLE CAFE(NOM_CAFE VARCHAR(32),FO_ID INTEGER," //$NON-NLS-1$
//					+ "PRIX FLOAT, VENTES INTEGER, TOTAL INTEGER)"; //$NON-NLS-1$
//			stmt.executeUpdate(creerTableCafe);
//			stmt.executeUpdate("INSERT INTO CAFE VALUES ('Colombian', 101, 7.99, 0, 0)"); //$NON-NLS-1$
//			stmt.executeUpdate("INSERT INTO CAFE VALUES ('French_Roast', 49, 8.99, 0, 0)"); //$NON-NLS-1$
//			stmt.executeUpdate("INSERT INTO CAFE VALUES ('Espresso', 150, 9.99, 0, 0)"); //$NON-NLS-1$
//			stmt.executeUpdate("INSERT INTO CAFE VALUES ('Colombian_Decaf', 101, 8.99, 0, 0)"); //$NON-NLS-1$
//			stmt.executeUpdate("INSERT INTO CAFE VALUES ('French_Roast_Decaf', 49, 9.99, 0, 0)"); //$NON-NLS-1$
			String requete = "SELECT NOM_CAFE, PRIX FROM CAFE"; //$NON-NLS-1$
			ResultSet rs = stmt.executeQuery(requete);
			while (rs.next()) {
				String s = rs.getString("NOM_CAFE"); //$NON-NLS-1$
				float n = rs.getFloat("PRIX"); //$NON-NLS-1$
				System.out.println(s + " " + n); //$NON-NLS-1$
			}
			conn.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage()); //$NON-NLS-1$
			System.out.println("SQLState: " + ex.getSQLState()); //$NON-NLS-1$
			System.out.println("VendorError: " + ex.getErrorCode()); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
