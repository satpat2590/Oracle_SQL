import java.util.Scanner;

import javax.xml.namespace.QName;

import java.util.InputMismatchException;
import java.io.*;
import java.sql.*;

class Customer {

	static String useName = "";
	static char[] pword;
	public static void main(String[] args) {

        Boolean bool_check = true; 
        Scanner in = new Scanner(System.in);
        while(bool_check) {


		/*

			TESTING MODE... HARD CODE DATABASE PASSWORD FOR EASE OF TESTING

		*/
			System.out.println("Please enter your Oracle username on Edgar1: ");
			useName = in.nextLine();

			System.out.println("Please enter your Oracle password on Edgar1: ");
			Console console = System.console();
			pword = console.readPassword();



			//Try-with-Resourcers block until Connection is established
            try (

				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", useName, new String(pword));
                Statement s = con.createStatement(); 
				
			) {
                bool_check = false; 
				int functional_check = 0; 
				//Established connection code block
				System.out.println("Welcome to Alset Eccentric Vehicles!\n What would you like the power to do?");
				System.out.println("\n[1] Purchase a vehicle\n[2] Service station locations\n[Any other key] Exit Alset Eccentric Directory...");
				int choose = in.nextInt();
				


				switch (choose) {
					case 1: 
						functional_check = purchaseVehicle();
						break;
					case 2: 
						serviceCheck();
						break; 
					default: 
						System.out.println("We hope to see you again soon! Have a nice day...");
						System.exit(0);
						break; 
				}

            } catch (SQLException|InputMismatchException e) {
                System.out.println("You have entered an incorrect value... Please try again...\n");
                bool_check = true; 
            } 
        }
	}


	/*
	 * MAIN MENU FUNCTION!! REROUTE FUNCTION
	 */
	public static void mainMenu() {
		Boolean bool_check = true;
		Scanner in = new Scanner(System.in);
		while (bool_check) {

			/*
			 * 
			 * TESTING MODE... HARD CODE DATABASE PASSWORD FOR EASE OF TESTING
			 * 
			 */
			// System.out.println("Please enter your Oracle username on Edgar1: ");
			// String user_name = in.nextLine();

			// System.out.println("Please enter your Oracle password on Edgar1: ");
			// Console console = System.console();
			// char[] pword = console.readPassword();

			// Try-with-Resourcers block until Connection is established
			try (

					Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",
							useName, new String(pword));
					Statement s = con.createStatement();

			) {

				bool_check = false;
				int functional_check = 0;
				// Established connection code block
				System.out.println("Welcome to Alset Eccentric Vehicles!\n What would you like the power to do?");
				System.out.println(
						"\n[1] Purchase a vehicle\n[2] Check for nearby service locations\n[Any other number] Exit Alset Eccentric Directory...");

				int choose = in.nextInt();

				// if (choose != 1 || choose != 2 || choose != 3) {
				// System.exit(0);
				// }
				switch (choose) {
					case 1:
						functional_check = purchaseVehicle();
						break;
					case 2:
						serviceCheck();
						break;
					default:
						System.out.println("We hope to see you again soon! Have a nice day...");
						System.exit(1);
				}

			} catch (Exception e) {
				System.out.println("You have entered an incorrect user name or password\n");
				bool_check = true;
			}
		}

	}


	public static int purchaseVehicle() {
		try (
			Scanner in = new Scanner(System.in);
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", useName, new String (pword));
			Statement s = con.createStatement();
		) {
			ResultSet rs;
			String q = "select distinct model from modifications";
			rs = s.executeQuery(q);
			int increment = 0;
			while (rs.next()) {
				increment++; 
				System.out.println("\n " + rs.getString("model"));
			}
			System.out.println("Select the model name from the choices above...");

			/*

				All of the user's car information (model, package, price)

				The year of the car is always new (2022 in this case). This is because some models (S and U) are
				updated every year for safety purposes. Buying older models would not be admissible in this case

			*/
			String model_input = in.nextLine(); //picked model 
			int car_price = 0; //price of the selected car 
			String car_package; //package of the selected car
			String n_query = "";

			Boolean bool_check = true; 

			while (bool_check) {

				switch (model_input) {
					case "m": 
					case "M": 
						n_query = "select * from modifications where model = 'M'";
						bool_check = false;
						model_input = "M";
						break;
					case "u":
					case "U":
						n_query = "select * from modifications where model = 'U'";
						bool_check = false;
						model_input = "U";
						break;
					case "s":
					case "S":
						n_query = "select * from modifications where model = 'S'";
						bool_check = false;
						model_input = "S";
						break;
					case "k":
					case "K":
						n_query = "select * from modifications where model = 'K'";
						bool_check = false; 
						model_input = "K";
						break;
					default:  
						System.out.println("You did not provide a proper model name!\n\tPlease try again...");
						model_input = in.nextLine();
						break;	  
				}

			}
			rs = s.executeQuery(n_query);


			System.out.println(n_query);

			String[] rows = new String[10]; 
			int user_choose;
			increment = 0; 

			while (rs.next()) {
				rows[increment] = rs.getString("package");
				System.out.println("\n[" + increment + "] " + rows[increment] + "\nDescriptions: " + rs.getString("description") + "\n----------------------------------------------------------");
				increment++; 
			}


			System.out.println("\nPick your preferred package...");
			user_choose = in.nextInt(); 
			car_package = rows[user_choose]; // Package name is saved!
			Boolean simp_check = true;
			while (simp_check) {
				if (user_choose > increment || user_choose < 0) {
					System.out.println("You did not provide a proper package... Please try again...");
					System.out.print("\tpackage number > ");
					user_choose = in.nextInt();
				} else {
					simp_check = false;
				} 
			}

			user_choose = 0; 

			n_query = "select price from modifications where package = '" + car_package + "'";
			rs = s.executeQuery(n_query);
			rs.next();
			car_price = rs.getInt("price"); //Car price is saved!
			simp_check = true; 

			System.out.println("\n\n\tYou chose the " + model_input + " model and the " + car_package + " package, for a total cost of $" + car_price);

			/*
				End of the vehicle ID insertion process
			*/
			System.out.println("Would you like to continue on with the user and payment information?\n\t [0] Yes, continue...\n\t [1] Go back to the main menu...\n\t");
			user_choose = in.nextInt();

			while (Boolean.TRUE.equals(simp_check)) {
				switch(user_choose) {
					case 0: 
						userInfo(model_input, car_package, car_price);
						simp_check = false;
						break;
					case 1: 
						mainMenu();
						simp_check = false;
						break;
					default: 
						bool_check = true;
						System.out.println("You have entered the wrong value... Please try again...");
						user_choose = in.nextInt();
						break;
				}
			}



		} catch (SQLException e) {
			System.out.println("Error");
			e.printStackTrace();
		}

		return 0; 	
		
	}


	/*
		UserPayment function... 
			Takes user information such as name, email, address, new vehicle information and inputs them 
			in the database
	*/

	public static void userInfo (String model, String car_package, int price) {

		String[] colors = {"Yellow", "Red", "Green", "Black", "Purple", "Orange", "Pink", "Light Blue", "Dark Green", "Indigo", "Fuchsia", "Lavender", "Magenta", "Mint", "White", "Grey"};
		int int_choose; 
		int county = 0;
		String color;
		String fname; 
		String lname; 
		String email; 
		String planet; 
		String landmass; 
		String city;
		ResultSet rs;
		Boolean simp_check = true;

		try (

			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", useName, new String(pword));
			Statement s = conn.createStatement();
			Scanner in = new Scanner(System.in);
		) {

			conn.setAutoCommit(true);

			/*
				Choosing the color of the car and then adding the newly created car
				into the 'Vehicle' relation of the database
			*/

			System.out.println("\n\nWhich color would you like your vehicle to be? (Choose the number)\n-----------------------------------");

			for (int i = 0; i < colors.length; i++) {
				System.out.println("[" + i + "]" + " " + colors[i]);
			}

			int_choose = in.nextInt();
			while (Boolean.TRUE.equals(simp_check)) {
				if (int_choose > colors.length || int_choose < 0) {
					System.out.println("\nPlease enter an actual value for color.\n");
					System.out.print("\tvalue >");
					int_choose = in.nextInt();
				} else {
					simp_check = false;
				}
			}
			color = colors[int_choose];

			String q = "insert into vehicle (model, color, year, package) values ('" + model + "', '" + color + "', '2022', '" + car_package + "')";
			s.executeUpdate(q);

			/*
			 * Save the vehicle ID to the database and the connect it with the user
			 */
			q = "select v_id from vehicle where model = '" + model + "' and year = '2022' and color = '" + color + "' and package = '" + car_package + "'";
			rs = s.executeQuery(q);
			int[] v_ids = new int[20];
			 
			while (rs.next()) {
				v_ids[county] = rs.getInt("v_id");
				county = county + 1;
			}
			int v_id = v_ids[0];
			for (int i = 1; i < v_ids.length; i++) {
				if (v_ids[i] > v_id)
					v_id = v_ids[i];
			}	
																							
			q = "insert into vehicle (model, color, year, package) values ('" + model + "', '"+ color + "', '2022', '" + car_package + "')"; 

			s.executeUpdate(q);


			/*
				Recording the user information such as First Name, Last Name, Email, Address
			*/
			System.out.println("\n\nWe'll go over our user information now...");

			System.out.println("\n\nWhat's your first name? ---- ");
			in.nextLine();
			fname = in.nextLine();

			System.out.println("\n\nWhat's your last name? ---- ");
			lname = in.nextLine();

			System.out.println("\n\nWhat's your email? ---- ");
			email = in.nextLine();

			q = "insert into customer (f_name, l_name, email) values ('" + fname + "', '" + lname + "', '" + email + "')";
			s.executeUpdate(q);

			q = "update customer set v_id = '" + v_id + "' where email = '" + email + "'";
			s.executeUpdate(q);
		
			/*
				Recording the address information of the customer (planet, landmass, city)
			*/
			q = "select distinct planet from address";
			rs = s.executeQuery(q);

			System.out.println("Planets\n----------------------------");
			while(rs.next()) {
				System.out.println(rs.getString("planet"));
			}

			System.out.print("\nWhich planet are you from? ---- ");
			planet = in.nextLine();

			q = "select distinct landmass from address where planet = '" + planet + "'";
			rs = s.executeQuery(q);
			String q_check = "";
			int q_check2 = 0; 
			county = 0;
			System.out.println("Landmass\n----------------------------");
			while (rs.next()) {
				county = county + 1;
				System.out.println(rs.getString("landmass"));
			}
			/*
				Block of code for handling if customers do not have any landmass associated with their planet. 
				If they don't have a planet in our system, they can't get a car from us...
			*/
			if (county == 0) {
				System.out.println("We're sorry. We're not in your location yet...\n\t...\n\t...Would you like to see a location list of available Service Stations? [Y] [N]");
				q_check = in.nextLine();
				if (q_check == "Y") {
					//insert function to show service location list
			  } else {
				  System.out.println("Would you like to go to...\n[1] Main Menu\n[2] Exit Alset");
				  q_check2 = in.nextInt();
				  if (q_check2 == 1) {
					mainMenu();
				} else if (q_check2 == 2) {
					System.out.println("Thank you for visiting Alset's Eccentric Vehicles... Have a nice day!");
					System.exit(1);
				} 
			  }
			}
			/*
				End of foreign customer handling
			*/
			Boolean add_to_database = true;


			System.out.print("\nWhich landmass? ---- ");
			landmass = in.nextLine();
			Boolean landmass_exist = true; 

			q = "select distinct city from address where landmass = '" + landmass + "'";
			rs = s.executeQuery(q);
			//String q_check = "";
			//int q_check2 = 0;
			county = 0;
			String[] city_list = new String[20]; 
			System.out.println("City\n----------------------------");
			while (rs.next()) {
				city_list[county] = rs.getString("city");
				county = county + 1;
				System.out.println(city_list[county - 1]);
			}

			//No cities returned...
			if (county == 0) {
				add_to_database = true;
				landmass_exist = false; 
			}

			System.out.println("\nWhich city/town? ---- ");
			city = in.nextLine();
			Boolean city_exist = false;
		
			//Checks if customer city matches a city in the address book
			for (int i = 0; i < city_list.length; i++) {
				if (city_list[i] == city) {
					add_to_database = false;
					city_exist = true;
				}
			}

			/*
				Adds the foreign customer in the database ONLY IF they are from a planet in our system

				Add_to_database only is true if the customer does not exist in the address book
			*/
			if (add_to_database) {
				q = "insert into address (planet, landmass, city) values ('" + planet + "' ,'" + landmass + "' , '" + city + "')";
				s.executeUpdate(q);
			}
			/*
				End of address update
			*/

			int a_id = 0;

			q = "select a_id from address where planet = '" + planet + "' and landmass = '" + landmass + "' and city = '" + city + "'";
			rs = s.executeQuery(q);
			while(rs.next()) {
				a_id = rs.getInt("a_id");
			}

			/*
				If a_id exists: 
					Update customer with new address
				
				If a_id does not exist:
					Add new address to the address table, update customer after
			*/
			if (a_id != 0) {
				q = "update customer set a_id = '" + a_id + "' where email = '" + email + "'";
				s.executeUpdate(q);
			} else {
				System.out.println("You are not located in our current address book..\n\tAllow us to update it... ");
			}

			System.out.println("\n\n...Update complete...\n\n");
			/*
				Update complete
			*/

			System.out.println("\n\nHere are the nearby service locations\n----------------------------------------------------------------- ");

			/*	
				List the model, planet, landmass, and city ONLY of the model of the car being purchased

				If no such service location exists, then expand the search to the planet...

				If no such service location exists on the planet, then go back to Main Menu to select another car
				or pursure another option
			*/
			String[] models = new String[15];
			String[] planets = new String[15];
			String[] landmasses = new String[15];
			String[] cities = new String[15];
			q = "select model, planet, landmass, city from service_loc natural join address where planet = '" + planet + "' and model = '" + model + "'";
			if (landmass_exist && city_exist) {
				q = "select model, planet, landmass, city from service_loc natural join address where planet = '" + planet + "' and landmass = '" + landmass + "' and city = '" + city + "' and model = '" + model + "'";
		    } else if (landmass_exist && !city_exist) {
			  	q = "select model, planet, landmass, city from service_loc natural join address where planet = '" + planet + "' and landmass = '" + landmass + "' and model = '" + model + "'";
		    }
			System.out.println("Service_ID\t\tModel\t\tPlanet\t\tLandmass\t\tCity\n----------------------------------------------------------------------");

			rs = s.executeQuery(q);
			county = 0;

			rs.next();
			while(rs.next()) {
				county++;
				models[county] = rs.getString("model");
				planets[county] = rs.getString("planet");
				landmasses[county] = rs.getString("landmass");
				cities[county] = rs.getString("city");
				System.out.println("[" + county + "] - \t\t\t\t" + rs.getString("model") + " \t\t" + rs.getString("planet") + "\t\t " + rs.getString("landmass") + "\t\t " + rs.getString("city"));
				System.out.println("\n\n");
			}

			System.out.println("\n\n\n");

			/*
				Start of 'if no service location exists' 

				[1] Expands the search
				[0] Goes to main menu
			*/

			if (county == 0) {
				System.out.println("No service stations found for your model nearby... Would you like to expand the search?\n\t[1] Yes\n\n\t[0] No, go to main menu");
				System.out.print("\t> ");
				int_choose = in.nextInt();
				if (int_choose == 0) {
					mainMenu();
			  } else {
				  	q = "select model, planet, landmass, city from service_loc natural join address where planet = '" + planet + "' and model = '" + model + "'";
			  }
			  	rs = s.executeQuery(q);
				county = 0;
			
			  	System.out.println("Service_ID\t\tModel\t\tPlanet\t\tLandmass\t\tCity\n---------------------------------------------------");
				while (rs.next()) {
					county++;
					models[county] = rs.getString("model");
					planets[county] = rs.getString("planet");
					landmasses[county] = rs.getString("landmass");
					cities[county] = rs.getString("city");
					System.out.println("[" + county + "] - \t\t\t\t" + rs.getString("model") + "\t\t " + rs.getString("planet") + "\t\t " + rs.getString("landmass") + "\t\t " + rs.getString("city"));
					System.out.println("\n\n");
				}

				if (county == 0) {
					System.out.println("\n\nThere are no service locations on your planet for your current model...\n\t...Taking you back to the Main Menu...");
					mainMenu();
				} 
			}
			
			System.out.println("\n\nPlease pick a service location to pick your vehicle up from...\n");
			System.out.print("\t> ");
			int_choose = in.nextInt();
			String final_choice = "";
			Integer c_id = 0; 

			q = "select c_id from customer where f_name = '" + fname + "' and l_name = '" + lname + "' and email = '" + email + "'";

			rs = s.executeQuery(q);
			rs.next();
			c_id = rs.getInt("c_id");


			System.out.println("\n\n\tYou are picking up your vehicle from:\nPlanet: " + planets[int_choose] + "\n\nLandmass: " + landmasses[int_choose] + "\n\nCity: " + cities[int_choose] + "\n\nPrice: " + price);

			System.out.println("\n\nContinue onwards to the payment information?\n\t[Press any key to enter]\n\t[Press 'Q' to go to Main Menu]");
			System.out.print("\n\n\t\t>");
			final_choice = in.nextLine();

			if (final_choice == "Q") 
				mainMenu();
		    else {
				payment(c_id);
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}

	}



	public static void payment(int c_id) {
		Integer check; 
		String cardno = "";
		int cardmonth = 0;
		int cardyear = 0;
		String CVV = "";
		Boolean correct = true;
		Boolean shoot = true;
		String card_check = "";

				Scanner in = new Scanner(System.in);
				System.out.println("\n\n-------What is your credit/debit card number? (Only the first 12 digits, last 4 not needed)\n");
				System.out.print("Card Number \t> ");
				cardno = in.nextLine();
				int temp; 

				while (correct) {
					temp = cardno.length();
					System.out.println(temp + " : " + cardno);
					if (temp != 12) {
						System.out.println("This is an invalid card number. Please try again...\n\n");
						System.out.print("\nCard Number \t> ");
						cardno = in.nextLine();	
					} else {
						correct = false;
					}	
				}

				System.out.println("Card Number: > " + cardno);



				System.out.println("\n\n-------What is your expiration month on the card number?\n");
				System.out.print("\nFormat: XX \t> ");
				cardmonth = in.nextInt();
				correct = true;

				while (correct) {
					if (cardmonth > 0 && cardmonth < 12) {
						correct = false;
					} else {
						System.out.println("This is an invalid month. Please try again...\n");
						System.out.print("\nFormat: XX \t> ");
						cardmonth = in.nextInt();
				} 
				}




				System.out.println("\n\n-------What is your expiration year on the card number?\n");
				System.out.print("\nFormat: XXXX \t> ");
				cardyear = in.nextInt();
				correct = true;

				while (correct) {
					if (cardyear > 2021 && cardyear < 3000) {
						correct = false;
					} else {
						System.out.println("This is an invalid year. Please try again...\n");
						System.out.print("\nFormat: XXXX \t> ");
						cardyear = in.nextInt();
				} 
				}



				System.out.println("\n\n-------What is your CVV on the back?\n");
				System.out.print("\nFormat: XXX \t> ");
				CVV = in.nextLine();
				correct = true;
				shoot = false; 

				while (correct) {
					if (CVV.length() == 3) {
						correct = false;
					} else {
						System.out.println("This is an invalid CVV. Please try again...\n");
						System.out.print("\nFormat: XXX \t> ");
						CVV = in.nextLine();
				} 
				}

				

				System.out.println("\n\n\t\t...Payment information taken... \nPlease wait a moment for our system to verify it...");
				in.close();

				System.out.println("Hello>?");
		
		try (
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", useName, new String(pword));
			Statement s = conn.createStatement();
		) {
			int letpick = 0;
			conn.setAutoCommit(true);
			String q = "insert into payment (c_id, card_no, exp_month, exp_year, cvv) values ('" + c_id + "', '" + cardno + "', '" + cardmonth + "' , '" + cardyear + "', '" + CVV + "')";  
			s.executeUpdate(q);


			q = "select * from ((customer natural join payment) natural join vehicle) natural join modifications where c_id = '" + c_id + "'";
			ResultSet rs;
			rs = s.executeQuery(q);
			while (rs.next()) {
				System.out.println("\t\t " + rs.getString("f_name") + " \t\t" + rs.getString("l_name") + " \t\t" + rs.getString("email") + " \t\t " + rs.getString("model")  +" \t\t " + rs.getString("package") + " \t\t " + rs.getString("year") + "\t\t " + rs.getInt("price") + " \t\t " + rs.getString("card_no") + " \t\t " + rs.getString("planet") + " \t\t " + rs.getString("landmass") + " \t\t " + rs.getString("city") + " ");
			}


			System.out.println("\n\t...Transaction complete... Thank you for your purchase!\n\t[1] Main Menu\n\t[2] View Purchase Details\n\t[3] Exit Alset");
			System.out.print("\n\t>");	
			letpick = in.nextInt();
			switch (letpick) {
				case 1: 
					mainMenu();
					break;
				case 2:
					while (rs.next()) {
						System.out.println("\t\t " + rs.getString("f_name") + " \t\t" + rs.getString("l_name") + " \t\t"
								+ rs.getString("email") + " \t\t " + rs.getString("model") + " \t\t "
								+ rs.getString("package") + " \t\t " + rs.getInt("year") + "\t\t "
								+ rs.getInt("price") + " \t\t " + rs.getString("card_no") + " \t\t "
								+ rs.getInt("cvv") + " \t\t " + rs.getString("planet") + " \t\t "
								+ rs.getString("landmass") + " \t\t " + rs.getString("city") + " ");
					}
					break;
				case 3: 
					System.exit(0);
					break;
				default:
					System.out.println("You did not specify the right key...\n\t ...Going back to main menu...");
					mainMenu();
					break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	public static void serviceCheck() {
		String planet;
		String landmass; 
		int county;


		try (
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", useName, new String(pword));
			Statement s = conn.createStatement();
			Scanner in = new Scanner(System.in);
		) {

		/*
		 * Recording the address information of the customer (planet, landmass, city)
		 */
		String q = "select distinct planet from address";
		ResultSet rs = s.executeQuery(q);

		System.out.println("Planets\n----------------------------");
		while (rs.next()) {
			System.out.println(rs.getString("planet"));
		}

		System.out.print("\nWhich planet are you from? ---- ");
		planet = in.nextLine();

		q = "select distinct landmass from address where planet = '" + planet + "'";
		rs = s.executeQuery(q);
		String q_check = "";
		int q_check2 = 0;
		county = 0;
		System.out.println("Landmass\n----------------------------");
		while (rs.next()) {
			county = county + 1;
			System.out.println(rs.getString("landmass"));
		}
		/*
		 * Block of code for handling if customers do not have any landmass associated
		 * with their planet.
		 * If they don't have a planet in our system, they can't get a car from us...
		 */
		if (county == 0) {
			System.out.println("We're sorry. We're not in your location yet...\n\t");
			System.out.println("Would you like to go to...\n[1] Main Menu\n[2] Exit Alset");
			q_check2 = in.nextInt();
			if (q_check2 == 1) {
				mainMenu();
			} else if (q_check2 == 2) {
				System.out.println("Thank you for visiting Alset's Eccentric Vehicles... Have a nice day!");
				System.exit(1);
			}
		}

		q = "select model, planet, landmass, city from service_loc natural join address where planet = '" + planet + "'";
		System.out.println(
				"Service_ID\t\tModel\t\tPlanet\t\tLandmass\t\tCity\n----------------------------------------------------------------------");

		rs = s.executeQuery(q);
		county = 0;

		rs.next();
		while (rs.next()) {
			county++;
			System.out.println("[" + county + "] - \t\t\t\t" + rs.getString("model") + " \t\t" + rs.getString("planet")
					+ "\t\t " + rs.getString("landmass") + "\t\t " + rs.getString("city"));
			System.out.println("\n\n");
		}


		System.out.println("\n\n\t...Press any key to go to the main menu");

		String input = in.nextLine();

		mainMenu();


		


		} catch (SQLException e) {
			System.out.println("Entered incorrect values for address...\n\n\t...Go to Main Menu...");
			mainMenu();
			e.printStackTrace();
		}

	}

}