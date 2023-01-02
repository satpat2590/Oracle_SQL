Author: Satyam Patel 

Description: 

This directory contains the database creation and modification present in the dbdata.sql file. 
Using this database template, you can query different tables and data within in order to derive specific information. 

Overall, this project models an intergalactic vehicle dealershio, offering different models listed in the 
'Customer UI' portion of this README.md. 

The Customer.java file then uses each of the database tables to register new customer information and then link them
according to the vehicle they wish to purchase. 

We take into account the location of the vehicle dealership with respect to the customers, offering a location 
selector based on the available locations registered within the database. 

Customers are able to select specific models and check if they exist within the nearby dealership, as well as the 
price. 

If a vehicle is not available at a specified dealership, then you will be given a list of all nearby locations selling
the vehicle. 


Usage:  

[1]

javac -cp ./ojdbc8.jar Customer.java -d ./

[2] 

jar cfmv Customer.jar Manifest.txt Customer.class


[3] 

java -jar Customer.jar


Customer UI: 
    1. Begin by selecting whether or not to purchase a vehicle or look at the list of service locations nearby

    2. If selected to purchase a vehicle, then you will first select the Model type
        M - Moon vehicle

        U - Underwater vehicle

        S - Space craft

        K - Ground vehicle

        Then you will select the package for the car, the color, and then input that into the vehicle table

        From there, you will enter the user information (name, email, etc...) and also record their adddresses... 

        We'll use the address to find nearby service locations that can serve your created car

            If no service locations found nearby, prompts you back to main menu
            
        Take in user's payment information and then input that into the payment table along with the c_id from the customer

    3. If selected to find nearby service locations, the program will prompt you to enter a planet name, and then it will print out a list of service locations in that planet. 

        If no planet is found in the system from the input, then it will prompt you back to the main menu...


All relational data was created by me, and from my own interests. 



