# Downloading + Compliling

1. Clone the Project 5 Repo from https://github.com/pranav-vijayananth/Project5
2. Compile MainClient.java and MainServer.java
3. Open up your terminal and run `javac MainServer.java`
4. After that, run `java MainServer`
5. Open up a new terminal tab to run the client-side
6. Run `javac MainClient.java`
7. After that, run `java MainClient`
8. Congrats! The GUI should pop up and you could use the application

# Customer.java

The Customer class extends User and represents users with specific attributes like unadded, invisible, and blocked. It manages a list of customers, handles file operations to interact with customer data, including reading and writing from various text files like customers.txt, blockedSellers.txt, and unaddedSellers.txt. Methods include functionality to check and modify customer visibility, block status, and obtaining customer-related data. Additionally, it provides functions to manage blocked sellers, write and read data related to these sellers in files, and find customers based on their email within the stored data. Overall, it facilitates the management and manipulation of customer data and interactions within a system.

# MainClient.java

The MainClient class with several instance variables such as sellerOrCustomer, client, buttonSize, textField, listOfCustomers, and listOfSellers. The main method initiates a Swing-based application. The class sets up a socket connection and handles various ActionListener events triggered by buttons and input actions. These actions include user authentication, account management (creation, deletion, and editing), and messaging functionalities for different user types (sellers/customers). The code implements communication with a server, likely through sockets, to exchange messages and perform operations based on user interactions within the GUI.

# MainServer.java 

The MainServer class that manages a server-side application. It includes a main method that sets up a ServerSocket to accept incoming connections from clients. For each client connection, it launches a new ServerThread in a separate thread to handle client interactions.

The ServerThread class, nested within MainServer, implements the Runnable interface and contains logic to handle client interactions within its run method. This logic involves reading inputs from clients, determining user statuses (seller or customer), and performing actions accordingly. It includes functionalities for seller and customer accounts, such as user authentication, creating new accounts, editing account details, and deleting accounts.

The server-side code follows a structure where it awaits input from connected clients, processes requests based on the received inputs, and communicates back with clients via the BufferedReader and PrintWriter.

# Message.java

The Message class represents a message entity with specific attributes such as a recipient, sender, message contents, and a timestamp. It includes constructors to create a message object with or without an existing timestamp.

The class provides getter and setter methods for each attribute, allowing access to and modification of the message's recipient, sender, message contents, and timestamp. Additionally, it includes a toString method to generate a string representation of the message object containing details like the timestamp, sender, recipient, and message contents concatenated together for easy readability.

This class encapsulates the essential components of a message, facilitating the creation, retrieval, and manipulation of message data between users within a messaging system.

# Seller.java 

This Seller class is an extension of the User class, representing sellers within a system. It contains various methods to handle seller-specific functionalities such as managing stores, retrieving seller information, adding and removing stores, blocking customers, and more.

The class utilizes file I/O operations to read from and write to files that store seller-related data, such as seller information, stores, and blocked customers. It includes methods to interact with these files, manage seller stores, identify sellers, and handle blocked customers.


It's essential to ensure proper error handling and edge case consideration in file operations, especially when dealing with I/O exceptions or unexpected file conditions, to maintain the integrity of the data.

# User.java

The User class encapsulates the properties and behaviors related to a user within the system. It contains various methods for managing user credentials, messages, and handling interactions with other users. The User class serves as a fundamental entity within the system, responsible for managing user information, messages, and interactions. Its potential interactions with other classes are crucial for providing a comprehensive system for user management, messaging, and security.

### Functionalities:

User Authentication:
Manages user credentials (email and password).
Provides methods to create, update, delete user credentials, and verify user login credentials.

Message Handling:
Facilitates the creation, storage, retrieval, and manipulation of messages between users.
Includes methods to write, read, download, delete, and edit messages.

File Operations:
Manages user-specific files (.txt files) to store messages.
Creates, reads, and updates files related to user messages and credentials.

Censoring:
Allows censorship of text messages by replacing censored words with specified replacements.
Provides functionality to manage censored and replacement words for a user.

