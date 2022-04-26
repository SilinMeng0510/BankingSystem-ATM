import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	
	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream(filename);	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");						// Load the url
			username = props.getProperty("jdbc.username");			// Load the username
			password = props.getProperty("jdbc.password");			// Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
			} catch (Exception e) {
				System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
				e.printStackTrace();
			}
	  }

	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static int newCustomer(String name, String gender, String age, String pin)
	{
		System.out.println(":: CREATE NEW CUSTOMER - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "insert into p1.customer(name, gender, age, pin) values('"+ name + "'," + "'" + gender + "'," + age + ","+ pin + ")";
			int result = stmt.executeUpdate(query);
			query = "select id from p1.customer where name = '" + name + "' and gender = '" + gender + "' and age = " + age + " and pin = " + pin;
			rs = stmt.executeQuery(query);
			System.out.println(":: CREATE NEW CUSTOMER - SUCCESS");
			int id = 0;
			while (rs.next()){
				id = rs.getInt(1);
			}
			stmt.close();
			con.close();
			rs.close();
			return id;
		} catch (Exception e) {
			System.out.println(":: CREATE NEW CUSTOMER - FAILED");
			return 0;
		}
	}

	public static boolean login(String id, String pin){
		System.out.println(":: LOGIN INTO ACCOUNT - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "select count(*) from p1.customer where id = " + id + " and pin = " + pin;
			rs = stmt.executeQuery(query);
			int num = 0;
			while (rs.next()){
				num = rs.getInt(1);
			}
			if (num == 1){
				System.out.println(":: LOGIN INTO ACCOUNT - SUCCESS");
				return true;
			}
			stmt.close();
			con.close();
			rs.close();
			System.out.println(":: LOGIN INTO ACCOUNT - FAILED : WRONG PIN OR ID");
			return false;
		} catch (Exception e) {
			System.out.println(":: LOGIN INTO ACCOUNT - FAILED : WRONG INPUT");
			return false;
		}
	}

	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static int openAccount(String id, String type, String amount)
	{
		System.out.println(":: OPEN ACCOUNT - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "insert into p1.account(id, balance, type, status) values(" + id + "," + amount + ",'" + type + "'," + "'A')";
			int result = stmt.executeUpdate(query);
			query = "select number from p1.account where id = " + id + " and type = '" + type + "' and balance = " + amount + " and status = 'A'";
			rs = stmt.executeQuery(query);
			System.out.println(":: OPEN ACCOUNT - SUCCESS");
			int num = 0;
			while (rs.next()){
				num = rs.getInt(1);
			}
			stmt.close();
			con.close();
			rs.close();
			return num;
		} catch (Exception e) {
			System.out.println(":: OPEN ACCOUNT - FAILED : WRONG INPUT");
			return 0;
		}
	}

	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static boolean closeAccount(String id, String accNum)
	{
		System.out.println(":: CLOSE ACCOUNT - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "update p1.account set status = 'I', balance = 0 where number = " + accNum + " and id = " + id;
			int result = stmt.executeUpdate(query);
			rs = stmt.executeQuery("select status from p1.account where number = " + accNum);
			String status = null;
			while (rs.next()){
				status = rs.getString(1);
			}
			stmt.close();
			con.close();
			if ("I".equals(status)){
				System.out.println(":: CLOSE ACCOUNT - SUCCESS");
				return true;
			}
			System.out.println(":: CLOSE ACCOUNT - FAILED : NO ACCESS");
			return false;
		} catch (Exception e) {
			System.out.println(":: CLOSE ACCOUNT - FAILED : WRONG INPUT");
			return false;
		}
	}

	/**
	 * Deposit into an account.
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static boolean deposit(String accNum, String amount)
	{
		System.out.println(":: DEPOSIT - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "update p1.account set balance = " + amount + " + balance where status = 'A' and number = " + accNum;
			int result = stmt.executeUpdate(query);
			if (result > 0) {
				System.out.println(":: DEPOSIT - SUCCESS");
				return true;
			}
			System.out.println(":: DEPOSIT - FAILED : INACTIVE ACCOUNT");
			stmt.close();
			con.close();
			return false;
		} catch (Exception e) {
			System.out.println(":: DEPOSIT - FAILED : WRONG INPUT");
			return false;
		}
	}

	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static boolean withdraw(String accNum, String id, String amount)
	{
		System.out.println(":: WITHDRAW - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "update p1.account set balance = balance - " + amount + " where number = " + accNum + " and id = " + id;
			int result = stmt.executeUpdate(query);
			if (result > 0) {
				System.out.println(":: WITHDRAW - SUCCESS");
				return true;
			}
			System.out.println(":: WITHDRAW - FAILED : NO ACCES");
			stmt.close();
			con.close();
			return false;
		} catch (Exception e) {
			System.out.println(":: WITHDRAW - FAILED : WRONG INPUT");
			return false;
		}
	}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static boolean transfer(String id, String srcAccNum, String destAccNum, String amount)
	{
		System.out.println(":: TRANSFER - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "update p1.account set balance = balance - " + amount + " where number = " + srcAccNum + " and id = " + id;
			int result = stmt.executeUpdate(query);
			if (result <= 0) {
				System.out.println(":: TRANSFER - FAILED : NO ACCES");
				return false;
			}
			String query1 = "update p1.account set balance = " + amount + " + balance where status = 'A' and number = " + destAccNum;
			int result1 = stmt.executeUpdate(query1);
			if (result1 <= 0) {
				stmt.executeUpdate("update p1.account set balance = balance + " + amount + " where number = " + srcAccNum);
				System.out.println(":: TRANSFER - FAILED : INACTIVE ACCOUNT");
				return false;
			}
			System.out.println(":: TRANSFER - SUCCESS");
			stmt.close();
			con.close();
			return true;
		} catch (Exception e){
			System.out.println(":: TRANSFER - FAILED : WRONG INPUT");
			return false;
		}

	}

	/**
	 * Display account summary.
	 * @param cusID customer ID
	 */
	public static boolean accountSummary(String cusID)
	{
		System.out.println(":: ACCOUNT SUMMARY - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "select number, balance from p1.account where id = " + cusID + " and status = 'A'";
			rs = stmt.executeQuery(query);
			System.out.println("NUMBER      BALANCE    ");
			System.out.println("----------- -----------");
			int total = 0;
			while (rs.next()){
				int number = rs.getInt(1);
				int balance = rs.getInt(2);
				total += balance;
				System.out.println(number + "\t\t" + balance);
			}
			System.out.println("----------- -----------");
			System.out.println("TOTAL" + "\t\t" + total);
			System.out.println(":: ACCOUNT SUMMARY - SUCCESS");
			rs.close();
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println(":: ACCOUNT SUMMARY - FAILED : WRONG INPUT");
			return false;
		}
	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static boolean reportA()
	{
		System.out.println(":: REPORT A - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "select id, name, gender, age, total from p1.customer inner join (select p1.customer.id as id1, sum(balance) as total from p1.customer inner join p1.account on p1.customer.id = p1.account.id group by p1.customer.id) on id = id1 order by total desc";
			rs = stmt.executeQuery(query);
			System.out.println("ID          NAME            GENDER AGE         TOTAL");
			System.out.println("----------- --------------- ------ ----------- -----------");
			while (rs.next()){
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String gender = rs.getString(3);
				int age = rs.getInt(4);
				int total = rs.getInt(5);
				System.out.println(id + "\t    " + name + "   \t\t" + gender + "\t" + age + "\t" + total);
			}
			System.out.println(":: REPORT A - SUCCESS");
			rs.close();
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println(":: REPORT A - FAILED : WRONG INPUT");
			return false;
		}
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing Order.
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static boolean reportB(String min, String max)
	{
		System.out.println(":: REPORT B - RUNNING");
		try{
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "select avg(sum) as averge from (select sum(balance) as sum from (select balance, p1.customer.id from p1.account inner join p1.customer on p1.customer.id = p1.account.id where age >= " + min + " and age <= " + max + ") group by id)";
			rs = stmt.executeQuery(query);
			System.out.println("AVERAGE");
			System.out.println("-----------");
			while (rs.next()){
				int average = rs.getInt(1);
				System.out.println(average);
			}
			System.out.println(":: REPORT B - SUCCESS");
			rs.close();
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println(":: REPORT B - FAILED : WRONG INPUT");
			return false;
		}
	}
}
