import java.sql.*;
import java.util.Scanner;
/**
 * Main entry to program.
 */
public class p1 {
	private static Scanner scan;
	private static String customerID = "";

	public static void main(String argv[]) {

		if (argv.length < 1) {
			System.out.println("Need database properties filename");
		} else {
			BankingSystem.init(argv[0]);
			BankingSystem.testConnection();
			System.out.println();
		}

		scan = new Scanner(System.in);
		welcomeMenu();
	}

	public static void welcomeMenu() {
		System.out.println("Welcome to the Self Services Banking System!");
		System.out.println("1. New Customer\n" + "2. Customer Login\n" + "3. Exit\n");
		System.out.println("Please enter the Corresponding Number from the given choice:");

		boolean done = false;
		while (!done) {
			try {
				String input = scan.nextLine();
				int i = Integer.parseInt(input);
				if (i == 1) {
					newCusMenu();
					done = true;
				} else if (i == 2) {
					loginMenu();
					done = true;
				} else if (i == 3) {
					System.out.println("Exit - Hope you have a good day!\n");
					done = true;
				} else {
					System.out.println("Error - Input Must Be 1, 2, or 3\n" + "Please enter again: ");
				}
			} catch (Exception e) {
				System.out.println("Error - Invalid Input\n" + "Please enter again: ");
			}
		}
	}

	public static void newCusMenu() {
		System.out.println("\n\n\n***********************************************\n" +
				"New Customer - Create your account\n");
		boolean done = false;
		while (!done) {
			try {
				System.out.println("Enter y to continue: ");
				String input = scan.nextLine();
				if (!"y".equals(input)){
					welcomeMenu();
					return;
				}
				System.out.println("Please Enter your Name: ");
				String name = scan.nextLine();
				System.out.println("Please Enter your Gender (M or F): ");
				String gender = scan.nextLine();
				System.out.println("Please Enter your Age (Number Only): ");
				String age = scan.nextLine();
				System.out.println("Please Enter your Pin (Number Only): ");
				String pin = scan.nextLine();
				int id = BankingSystem.newCustomer(name, gender, age, pin);
				if (id != 0) {
					System.out.println("\n*************************************\n" + "THIS IS YOUR CUSTOMER ID: " + id + "\n\n\n");
					done = true;
				}
			} catch (Exception e) {
				System.out.println("Error - Invalid Input");
			}
		}
		welcomeMenu();
	}

	public static void loginMenu() {
		System.out.println("\n\n\n***********************************************\n" +
				"Login - Please Provide Your Information\n");
		boolean done = false;
		while (!done) {
			try {
				System.out.println("Enter y to continue: ");
				String input = scan.nextLine();
				if (!"y".equals(input)){
					welcomeMenu();
					return;
				}
				System.out.println("Please Enter your Customer ID (Number Only): ");
				String id = scan.nextLine();
				customerID = id;
				System.out.println("Please Enter your Pin (Number Only): ");
				String pin = scan.nextLine();
				if ("0".equals(id) && "0".equals(pin)){
					adminMainMenu();
					return;
				}
				done = BankingSystem.login(id, pin);
				System.out.println("\n");
			} catch (Exception e) {
				System.out.println("Error - Invalid Input");
			}
		}
		System.out.println("Successfully Login!");
		cusMainMenu(customerID);
	}

	public static void cusMainMenu(String id){

		boolean done = false;
		while (!done) {
			try {
				System.out.println("\n\nCustomer Main Menu!");
				System.out.println("1. Open Account\n" + "2. Close Account\n" + "3. Deposite\n" + "4. Withdraw\n" + "5. Transfer\n" + "6. Account Summary\n" + "7. Exist\n");
				System.out.println("Please enter the Corresponding Number from the given choice:");
				String input = scan.nextLine();
				int i = Integer.parseInt(input);
				if (i == 1) {
					System.out.println("Please Enter the Customer ID for the new Account: ");
					String cusid = scan.nextLine();
					System.out.println("Please Enter Account Type (C for Checking, or S for Saving): ");
					String type = scan.nextLine();
					System.out.println("Please Enter your Initial Deposite: ");
					String balance = scan.nextLine();
					int num = BankingSystem.openAccount(cusid, type, balance);
					if (num != 0) {
						System.out.println("\n*************************************\n" + "THIS IS YOUR ACCOUNT NUMBER: " + num + "\n\n\n");
						System.out.println("Open Account Successfully!\n");
					}
				} else if (i == 2){
					System.out.println("Please Enter Account Number: ");
					String number = scan.nextLine();
					boolean close = BankingSystem.closeAccount(id, number);
					if (close){
						System.out.println("Close Account Successfully!\n");
					}
				} else if (i == 3) {
					System.out.println("Please Enter Account Number: ");
					String accNum = scan.nextLine();
					System.out.println("Please Enter Deposit Amount: ");
					String depoAmount = scan.nextLine();
					boolean deposite = BankingSystem.deposit(accNum, depoAmount);
					if (deposite){
						System.out.println("Desposit Successfully!\n");
					}
				} else if (i == 4){
					System.out.println("Please Enter Account Number: ");
					String accNum = scan.nextLine();
					System.out.println("Please Enter Withdraw Amount: ");
					String withdrawAmount = scan.nextLine();
					boolean withdraw = BankingSystem.withdraw(accNum, id, withdrawAmount);
					if (withdraw){
						System.out.println("Withdraw Successfully!\n");
					}
				} else if (i == 5){
					System.out.println("Please Enter Source Account Number: ");
					String srcAccNum = scan.nextLine();
					System.out.println("Please Enter Destination Account Amount: ");
					String destAccNum = scan.nextLine();
					System.out.println("Please Enter Transfer Amount: ");
					String transferAmou = scan.nextLine();
					boolean transfer = BankingSystem.transfer(id, srcAccNum, destAccNum, transferAmou);
					if (transfer){
						System.out.println("Transfer Successfully\n");
					}

				} else if (i == 6){
					boolean summary = BankingSystem.accountSummary(id);
					if (summary){
						System.out.println("Summary Created Successfully");
					}
				} else if (i == 7) {
					System.out.println("Exit - Hope you have a good day!\n\n");
					done = true;
				} else {
					System.out.println("Error - Input Must Be 1, 2, 3, 4 ,5 ,6 or 7\n" + "Please enter again: ");
				}
			} catch (Exception e) {
				System.out.println("Error - Invalid Input\n" + "Please retry again: ");
			}
		}
		welcomeMenu();
	}

	public static void adminMainMenu(){

		boolean done = false;
		while (!done) {
			try {
				System.out.println("\n\nCustomer Main Menu!");
				System.out.println("1. Account Summary for a Customer\n" + "2. Report A\n" + "3. Report B\n" + "4. Exist\n");
				System.out.println("Please enter the Corresponding Number from the given choice:");
				String input = scan.nextLine();
				int i = Integer.parseInt(input);
				if (i == 1){
					System.out.println("Please Eneter Customer ID: ");
					String id = scan.nextLine();
					boolean summary = BankingSystem.accountSummary(id);
					if (summary){
						System.out.println("Summary Created Successfully");
					}
				} else if (i == 2){
					boolean RA = BankingSystem.reportA();
					if (RA){
						System.out.println("Report A Created Successfully");
					}
				} else if (i == 3){
					System.out.println("Please Eneter Minimum age: ");
					String min = scan.nextLine();
					System.out.println("Please Eneter Maximum age: ");
					String max = scan.nextLine();
					boolean RB = BankingSystem.reportB(min, max);
					if (RB){
						System.out.println("Report B Created Successfully");
					}
				} else if (i == 4){
					System.out.println("Exit - Hope you have a good day!\n\n");
					done = true;
				} else {
					System.out.println("Error - Input Must Be 1, 2, 3 or 4\n" + "Please enter again: ");
				}

			} catch (Exception e) {
				System.out.println("Error - Invalid Input\n" + "Please retry again: ");
			}
		}
		welcomeMenu();
	}
}