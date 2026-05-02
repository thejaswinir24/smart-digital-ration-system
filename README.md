Smart Digital Ration System 🛒
A robust desktop application built in Java designed to digitally manage rationing operations. This system integrates a modern Graphical User Interface (GUI), in-memory data structures, and a persistent database to handle customer details, inventory management, and a live queue-based serving system.

🚀 Key Features
The application features a 10-function dashboard:

Add Customer: Registers new customers with their ID, Name, and Ration Card Number.
Display Customers: Fetches and iterates through the directory of all registered customers.
Add Ration Item: Adds new items (e.g., Rice, Sugar) along with their available quantity into the inventory.
Display Items: Shows the current available stock of ration items.
Join Queue: Allows a verified customer to join the serving queue and automatically generates a unique Token number.
Serve Customer: Pops the next customer off the queue to be served.
Display Queue: Shows the live list of customers currently waiting to be served.
Sort Customers: Uses a custom comparator to sort and display the customer directory alphabetically by name.
Generate Token Report: Displays a sorted report of all tokens generated during the day.
Clear Console & Exit: Utility functions to keep the workspace clean and securely close the application.
🛠️ Tools & Technologies Used
This project was built to demonstrate proficiency in core Java development, specifically focusing on:

Java Core & OOP: Utilizes Object-Oriented principles by separating logic into Customer, RationItem, and Token models.
Java Swing (GUI): Implements a responsive and modern desktop interface using JFrame, BorderLayout, GridLayout, and Event Listeners. Uses UIManager for a native look-and-feel.
Java Collections Framework: Extensively uses in-memory data structures to handle complex data manipulation:
ArrayList: Used to store the master list of customers.
LinkedList: Used as a Queue (FIFO) to manage the line of waiting customers.
HashMap: Used for fast $O(1)$ lookups of inventory items via their Item ID.
TreeMap: Used to automatically keep generated tokens sorted by their Token Number.
Comparator: Used to define custom sorting logic for names.
Iterator: Used to securely traverse collections.
JDBC (Java Database Connectivity): Securely connects to a MySQL backend to ensure data persistence across sessions.
Implements try-with-resources to guarantee memory-safe execution and prevent connection leaks.
Uses PreparedStatement to safely insert user data and prevent SQL injection.
💻 How to Run
Clone the repository.
Import the ration package into your preferred IDE (like Eclipse or IntelliJ).
Ensure you have a local MySQL database named rationdb running on port 3306 with tables for customer and item.
Update the DBConnection.java file with your specific database username and password.
Run RationGUI.java as a Java Application.