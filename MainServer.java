import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.net.Socket;

public class MainServer {

    public static void main(String[] args) {

        ServerSocket serverSocket = null;

        try {

            for (int i = 0; i < 5; i++) {
                // Your loop operations here
                System.out.println("Iteration: " + i);
                // Additional operations within the loop
            }

            int breakpoint = 0;

            serverSocket = new ServerSocket(9000);
            serverSocket.setReuseAddress(true);

            breakpoint++;

            while (true) {
                breakpoint++;
                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket);
                new Thread(thread).start();
                breakpoint++;
            }


        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static class ServerThread implements Runnable {
        private final Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            int breakpoint = 0;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

                boolean sellerFlag = false;
                boolean customerFlag = false;
                boolean sellerMessageFlag = false;
                boolean customerMessageFlag = false;
                String userEmail = "";

                while (true) {
                    breakpoint++;
                    String input = reader.readLine();
                    if (input.equals("1")) {
                        breakpoint++;
                        sellerFlag = true;
                        writer.write("input assigned");
                        writer.println();
                        writer.flush();
                        break;
                    }
                    if (input.equals("2")) {
                        breakpoint++;
                        customerFlag = true;
                        writer.write("input assigned");
                        writer.println();
                        writer.flush();
                        break;
                    } else {
                        break;
                    }
                }

                // Seller loop
                while (sellerFlag) {
                    breakpoint++;
                    String accountInput = reader.readLine();
                    //login to existing account
                    if (accountInput.equals("1")) {
                        breakpoint++;
                        String credentials = reader.readLine();
                        String email = credentials.substring(0, credentials.indexOf("~"));
                        String password = credentials.substring(credentials.indexOf("~") + 1);
                        if (Seller.verifyCredentials(email, password)) {
                            breakpoint++;
                            Seller seller = new Seller(email, password);
                            userEmail = email;
                            sellerMessageFlag = true;
                            sellerFlag = false;
                            writer.write("valid credentials");
                            writer.println();
                            writer.flush();
                        } else {
                            breakpoint++;
                            writer.write("User Input was Invalid");
                            writer.println();
                            writer.flush();
                        }

                    }
                    //Create new account
                    if (accountInput.equals("2")) {
                        breakpoint++;
                        String credentials = reader.readLine();
                        String email = credentials.substring(0, credentials.indexOf("~"));
                        String password = credentials.substring(credentials.indexOf("~") + 1);

                        Seller newSeller = new Seller(email, password);
                        if (!newSeller.userExists()) {
                            breakpoint++;
                            newSeller.writeCredentials();
                            writer.write("User Created!");
                            writer.println();
                            writer.flush();
                            sellerMessageFlag = true;
                            userEmail = email;
                            sellerFlag = false;
                            break;
                        } else {
                            breakpoint++;
                            writer.write("Email is already in use");
                            writer.println();
                            writer.flush();
                            break;
                        }


                    }

                    // edit account
                    if (accountInput.equals("3")) {
                        breakpoint++;
                        while (true) {
                            breakpoint++;
                            String credentials = reader.readLine();
                            String email = credentials.substring(0, credentials.indexOf("~"));
                            String oldPassword = credentials.substring(credentials.indexOf("~") + 1);
                             if (Seller.verifyCredentials(email, oldPassword)) {
                                 breakpoint++;
                                 writer.write("credentials verified");
                                 breakpoint++;
                                 writer.println();
                                writer.flush();
                                 breakpoint++;
                                 String newPassword = reader.readLine();
                                Seller newSeller = new Seller(email, newPassword);
                                if (true) {
                                    newSeller.updateCredentials(email, oldPassword, newPassword);
                                    breakpoint++;
                                }
                                userEmail = email;
                                sellerMessageFlag = true;
                                sellerFlag = false;
                                break;

                            } else {
                                continue;
                            }
                        }
                    }
                    // delete account
                    if (accountInput.equals("4")) {
                        breakpoint++;
                        while (true) {
                            breakpoint++;
                            String credentials = reader.readLine();
                            String email = credentials.substring(0, credentials.indexOf("~"));
                            String password = credentials.substring(credentials.indexOf("~") + 1);
                            if (Seller.verifyCredentials(email, password)) {
                                breakpoint++;
                                Seller deletedSeller = new Seller(email, password);
                                deletedSeller.deleteUser(email, password);
                                sellerFlag = false;
                                writer.write("deleted");
                                writer.println();
                                writer.flush();
                                break;
                            } else {
                                breakpoint++;
                                writer.write("Error deleting account");
                                writer.println();
                                writer.flush();
                            }
                        }
                    }
                    //exit
                    if (accountInput.equals("5")) {
                        breakpoint++;
                        sellerFlag = false;
                        break;
                    }
                }

                // Customer loop
                while (customerFlag) {
                    String accountInput = reader.readLine();
                    breakpoint++;
                    if (accountInput.equals("1")) {
                        breakpoint++;
                        String credentials = reader.readLine();
                        String email = credentials.substring(0, credentials.indexOf("~"));
                        String password = credentials.substring(credentials.indexOf("~") + 1);
                        if (Customer.verifyCredentials(email, password)) {
                            breakpoint++;
                            Customer customer = new Customer(email, password);
                            userEmail = email;
                            customerFlag = false;
                            customerMessageFlag = true;
                            breakpoint++;
                            writer.write("valid credentials");
                            writer.println();
                            writer.flush();
                        } else {
                            breakpoint++;
                            writer.write("User Input was Invalid");
                            writer.println();
                            writer.flush();
                        }

                    }

                    if (accountInput.equals("2")) {
                        breakpoint++;
                        while (true) {
                            String credentials = reader.readLine();
                            String email = credentials.substring(0, credentials.indexOf("~"));
                            String password = credentials.substring(credentials.indexOf("~") + 1);
                            Customer newCustomer = new Customer(email, password);
                            System.out.println(credentials);
                            if (!newCustomer.userExists()) {
                                breakpoint++;
                                newCustomer.writeCredentials();
                                writer.write("User Created!");
                                writer.println();
                                writer.flush();
                                customerMessageFlag = true;
                                userEmail = email;
                                customerFlag = false;
                                break;
                            } else {
                                breakpoint++;
                                writer.write("Email is already in use");
                                writer.println();
                                writer.flush();
                                break;
                            }
                        }
                    }
                    // edit account
                    if (accountInput.equals("3")) {
                        breakpoint++;
                        while (true) {
                            breakpoint++;
                            String credentials = reader.readLine();
                            String email = credentials.substring(0, credentials.indexOf("~"));
                            String oldPassword = credentials.substring(credentials.indexOf("~") + 1);
                            if (Customer.verifyCredentials(email, oldPassword)) {
                                breakpoint++;
                                writer.write("credentials verified");
                                writer.println();
                                writer.flush();
                                String newPassword = reader.readLine();
                                breakpoint++;
                                Customer newCustomer = new Customer(email, newPassword);
                                newCustomer.updateCredentials(email, oldPassword, newPassword);
                                userEmail = email;
                                customerMessageFlag = true;
                                customerFlag = false;
                                break;

                            } else {
                                continue;
                            }
                        }
                    }
                    // delete user
                    if (accountInput.equals("4")) {
                        breakpoint++;
                        String credentials = reader.readLine();
                        String email = credentials.substring(0, credentials.indexOf("~"));
                        String password = credentials.substring(credentials.indexOf("~") + 1);
                        if (Customer.verifyCredentials(email, password)) {
                            breakpoint++;
                            Customer deletedCustomer = new Customer(email, password);
                            deletedCustomer.deleteUser(email, password);
                            customerFlag = false;
                            writer.write("deleted");
                            writer.println();
                            writer.flush();
                            break;
                        } else {
                            breakpoint++;
                            writer.write("Error deleting account");
                            writer.println();
                            writer.flush();
                        }
                    }
                    if (accountInput.equals("5")) {
                        breakpoint++;
                        customerFlag = false;
                        break;
                    }
                }


                while (sellerMessageFlag) {
                    breakpoint++;
                    Seller mainSeller = Seller.findSeller(userEmail);
                    if (mainSeller.alertUserOfNewMessage()) {
                        breakpoint++;
                        writer.write("You have new messages!");
                        writer.println();
                        writer.flush();
                    } else {
                        breakpoint++;
                        writer.write("You have no new messages!");
                        writer.println();
                        writer.flush();
                    }


                    File customers = new File("customers.txt");
                    breakpoint++;
                    String inboxInput = reader.readLine();

                    if (inboxInput.equals("1")) {
                        breakpoint++;
                        File blockedFile = new File("blockedCustomers.txt");
                        if (!blockedFile.exists()) {
                            breakpoint++;
                            blockedFile.createNewFile();
                        }

                        while (true) {
                            breakpoint++;
                            String recipient = reader.readLine();
                            if(recipient.equals("cancel")) {
                                breakpoint++;
                                break;
                            }
                            Customer recipientCustomer = Customer.findCustomer(recipient);
                            boolean blockedUser = true;
                            breakpoint++;
                            ArrayList<String> blockedCustomers = mainSeller.blockedCustomersFileRead();
                            for (String blocked : blockedCustomers) {
                                breakpoint++;
                                String sellerName = recipientCustomer.getEmail();
                                if (blocked.equals(sellerName)) {
                                    breakpoint++;
                                    writer.write("Message can not be sent!");
                                    writer.println();
                                    writer.flush();
                                    blockedUser = false;
                                    break;
                                }
                            }
                            if (blockedUser == false) {
                                continue;
                            }
                            if (blockedUser == true) {
                                if (recipientCustomer == null) {
                                    breakpoint++;
                                    writer.write("We could not find your recipient. Look through the list of Customers.");
                                    writer.println();
                                    writer.flush();
                                    continue;
                                } else {
                                    breakpoint++;
                                    writer.write("Can send message");
                                    writer.println();
                                    writer.flush();
                                    breakpoint++;
                                    String messageOption = reader.readLine();
                                    Message message = null;
                                    if (messageOption.equals("1")) {
                                        breakpoint++;
                                        String messageInput = reader.readLine();
                                        message = mainSeller.createMessage(recipientCustomer, messageInput);
                                    } else if (messageOption.equals("2")) {
                                        breakpoint++;
                                        String messageInput = reader.readLine();
                                        try {
                                            message = mainSeller.createMessageFromFile(recipientCustomer, messageInput);
                                        } catch (FileNotFoundException e) {
                                            writer.write("The file couldn't be found.");
                                            writer.println();
                                            writer.flush();
                                            continue;
                                        }
                                    }
                                    mainSeller.writeFile(message);
                                    recipientCustomer.writeFile(message);
                                    breakpoint++;
                                    writer.write("Message Sent Successfully!");
                                    writer.println();
                                    writer.flush();
                                    break;
                                }
                            }
                        }
                        continue;
                    }

                    if (inboxInput.equals("2")) {
                        breakpoint++;
                        File censoredFile = new File("censors.txt");
                        if (!censoredFile.exists()) {
                            breakpoint++;
                            censoredFile.createNewFile();
                        }

                    	ArrayList<String> allMessages = null;
                        breakpoint++;
                        ArrayList<String> allCensored = null;


                        try {

                            breakpoint++;

                            allMessages = mainSeller.getNewMessages();

                            // Getting list of censored words

                            allCensored = mainSeller.getCensoredWords();

                            // checking if there is censored words

                            if (allCensored == null) {
                                breakpoint++;
                                writer.write("There are no censored words as of now!");
                            } else {
                                breakpoint++;
                                writer.write(String.join("~", allCensored));
                            }
                            writer.println();
                            breakpoint++;
                            writer.flush();

                            String censorOrNot = reader.readLine();

                    		if (censorOrNot.equals("1")) {
                                breakpoint++;

                                // How many words to censor
                    			
                    			int numberOfCensoredWords = Integer.parseInt(reader.readLine());
                                breakpoint++;
                                ArrayList<String> newCensoredWords = new ArrayList<String>();
                                breakpoint++;
                                ArrayList<String> replacementWords = new ArrayList<String>();
                    			
                    			for (int i = 0; i < numberOfCensoredWords; i++) {
                                    breakpoint++;
                                    newCensoredWords.add(reader.readLine());
                    				replacementWords.add(reader.readLine());
                    			}
                    			
                    			for (int i = 0; i < numberOfCensoredWords; i++) {
                                    breakpoint++;
                                    mainSeller.saveCensor(newCensoredWords.get(i), replacementWords.get(i));
                    			}
                    			
                    			String censoredPrint = "";
                    			for (String message : allMessages) {
                                    breakpoint++;
                                    censoredPrint += mainSeller.censorTextMessage(message) + "~";
                    			}

                                if (true) {
                                    writer.write(censoredPrint);
                                    breakpoint++;
                                    writer.println();
                                    writer.flush();
                                }

                    			
                    		} else if (censorOrNot.equals("2")) {
                                breakpoint++;
                    			
                    			String censoredPrint = "";
                    			for (String message : allMessages) {
                                    breakpoint++;
                                    censoredPrint += mainSeller.censorTextMessage(message) + "~";
                    			}
                    			if (true) {
                                    writer.write(censoredPrint);
                                    writer.println();
                                    breakpoint++;
                                    writer.flush();
                                }

                    		}


                        } catch (Exception e) {
                            breakpoint++;
                            e.printStackTrace();
                            writer.write("You have no messages");
                            breakpoint++;
                            writer.println();
                            writer.flush();
                        }
                        String test = reader.readLine();
                    }

                    if (inboxInput.equals("3")) {
                        breakpoint++;

                        File censoredFile = new File("censors.txt");
                        if (!censoredFile.exists()) {
                            breakpoint++;
                            censoredFile.createNewFile();
                        }

                    	ArrayList<String> allMessages = null;
                        breakpoint++;
                        ArrayList<String> allCensored = null;


                        try {

                            // Getting all messages

                            allMessages = mainSeller.getAllMessages();

                            // Getting list of censored words

                            allCensored = mainSeller.getCensoredWords();

                            // checking if there is censored words

                            if (allCensored == null) {
                                breakpoint++;
                                writer.write("There are no censored words as of now!");
                            } else {
                                breakpoint++;
                                writer.write(String.join("~", allCensored));
                            }
                            writer.println();
                            writer.flush();

                            // Getting input on whether or not the user wants to add more

                            String censorOrNot = reader.readLine();

                            if (censorOrNot.equals("1")) {
                                breakpoint++;

                                // How many words to censor

                                int numberOfCensoredWords = Integer.parseInt(reader.readLine());
                                breakpoint++;
                                ArrayList<String> newCensoredWords = new ArrayList<String>();
                                breakpoint++;
                                ArrayList<String> replacementWords = new ArrayList<String>();
                                breakpoint += numberOfCensoredWords;

                                for (int i = 0; i < numberOfCensoredWords; i++) {
                                    breakpoint++;
                                    newCensoredWords.add(reader.readLine());
                                    replacementWords.add(reader.readLine());
                                }

                                for (int i = 0; i < numberOfCensoredWords; i++) {
                                    breakpoint++;
                                    mainSeller.saveCensor(newCensoredWords.get(i), replacementWords.get(i));
                                }

                                String censoredPrint = "";
                                for (String message : allMessages) {
                                    breakpoint++;
                                    censoredPrint += mainSeller.censorTextMessage(message) + "~";
                                }

                                writer.write(censoredPrint);
                                writer.println();
                                writer.flush();

                            } else if (censorOrNot.equals("2")) { // If the user does not want to censor

                                String censoredPrint = "";
                                for (String message : allMessages) {
                                    breakpoint++;
                                    censoredPrint += mainSeller.censorTextMessage(message) + "~";
                                }

                                writer.write(censoredPrint);
                                breakpoint++;
                                writer.println();
                                writer.flush();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            breakpoint++;
                            writer.write("You have no messages");
                            writer.println();
                            writer.flush();
                        }
                        String test = reader.readLine();
                    }

                    if (inboxInput.equals("4")) {
                        breakpoint++;
                        ArrayList<String> allCustomers = Customer.getCustomers();
                        writer.write(String.join("~", allCustomers));
                        writer.println();
                        writer.flush();
                        String mainMenuCheck = reader.readLine();
                        if(mainMenuCheck.equals("menu")) {
                            breakpoint++;
                            continue;
                        }
                    }

                    if (inboxInput.equals("5")) {
                        breakpoint++;

                        ArrayList<String> allMessages = mainSeller.getAllMessages();
                        for (int i = 0; i < allMessages.size(); i++) {
                            breakpoint++;
                            writer.write(allMessages.get(i) + "~");
                        }
                        writer.println();
                        writer.flush();

                        String editOption = reader.readLine();
                        //"Enter the message to replace it with:");
                        String editInput = reader.readLine();

                        int editNum;

                        editNum = Integer.parseInt(editOption);
                        breakpoint += editNum;

                        if (editNum >= 0 && editNum < allMessages.size()) {
                            breakpoint++;
                            if (mainSeller.editMessage(allMessages.get(editNum), editInput)) {
                                breakpoint++;
                                writer.write("Message successfully edited");
                                writer.println();
                                writer.flush();
                                continue;
                            } else {
                                breakpoint++;
                                writer.write("There was a problem editing the message");
                                writer.println();
                                writer.flush();
                                continue;
                            }
                        } else {
                            breakpoint++;
                            writer.write("You didn't enter a valid number");
                            writer.println();
                            breakpoint++;
                            writer.flush();
                            continue;
                        }
                    }

                    if (inboxInput.equals("6")) {
                        breakpoint++;

                        ArrayList<String> allMessages = mainSeller.getAllMessages();
                        for (int i = 0; i < allMessages.size(); i++) {
                            breakpoint++;
                            writer.write(allMessages.get(i) + "~");
                        }
                        writer.println();
                        writer.flush();

                        String editOption = reader.readLine();
                        int editNum;
                        editNum = Integer.parseInt(editOption);

                        breakpoint += editNum;

                        if (editNum >= 0 && editNum < allMessages.size()) {
                            breakpoint++;
                            if (mainSeller.deleteMessage(allMessages.get(editNum))) {
                                breakpoint++;
                                writer.write("Message successfully deleted");
                                writer.println();
                                writer.flush();
                                continue;
                            } else {
                                breakpoint++;
                                writer.write("There was a problem deleting the message");
                                writer.println();
                                writer.flush();
                                continue;
                            }
                        } else {
                            breakpoint++;
                            writer.write("You didn't enter a valid number");
                            writer.println();
                            writer.flush();
                            continue;
                        }
                    }

                    if (inboxInput.equals("7")) {
                        breakpoint++;

                        String username = reader.readLine();
                        Customer participant = Customer.findCustomer(username);
                        if (participant == null) {
                            breakpoint++;
                            writer.write("The user couldn't be found");
                            writer.println();
                            writer.flush();
                            continue;
                        }
                        writer.write("user found");
                        writer.println();
                        breakpoint++;
                        writer.flush();
                        String filename = reader.readLine();
                        mainSeller.downloadMessages(participant, filename);
                        breakpoint++;

                        File f = new File(filename);
                        breakpoint++;

                        ArrayList<String> file = new ArrayList<String>();
                        breakpoint++;

                        BufferedReader br = null;

                        try {
                            breakpoint++;

                            br = new BufferedReader(new FileReader(f));
                            breakpoint++;
                            String placeholderLine = br.readLine();
                            while (placeholderLine != null) {
                                breakpoint++;
                                file.add(placeholderLine);
                                placeholderLine = br.readLine();
                            }

                        } catch (FileNotFoundException e) {
                            writer.write("Something went wrong");
                            writer.println();
                            writer.flush();

                        } catch (IOException e) {
                        	writer.write("Something went wrong");
                        	writer.println();
                            writer.flush();

                        } finally {

                            if (br != null) {
                                breakpoint++;
                                try {
                                    br.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        
                        writer.write("" + file.size());
                        writer.println();
                        writer.flush();
                        
                        for (int i = 0; i < file.size(); i++) {
                            breakpoint++;
                            writer.write(file.get(i));
                        	writer.println();
                        	writer.flush();
                        }

                        continue;
                    }

                    if (inboxInput.equals("8")) {
                        breakpoint++;
                        ArrayList<String> blockedCustomers = new ArrayList<String>();
                        ArrayList<String> allCustomers = Customer.getCustomers();
                        writer.write(String.join("~", allCustomers));
                        writer.println();
                        writer.flush();
                        breakpoint++;
                        String blocked = reader.readLine();
                        blockedCustomers.add(blocked);
                        mainSeller.blockedCustomersFileWrite(blockedCustomers, true);
                        writer.write("User blocked! They will no longer be able to message you");
                        writer.println();
                        writer.flush();

                    }

                    if (inboxInput.equals("9")) {
                        breakpoint++;
                        ArrayList<String> userStores = mainSeller.getStoresFromFile();


                        String storeInput = reader.readLine();
                        if (storeInput.equals("1")) {
                            breakpoint++;
                            String storeName = reader.readLine();
                            if (mainSeller.addStore(storeName)) {
                                breakpoint++;
                                writer.write("Store added successfully");
                            } else {
                                writer.write("A problem occurred while trying to add the store");
                            }
                            writer.println();
                            writer.flush();

                        } else if (storeInput.equals("2")) {
                            breakpoint++;
                            writer.write(mainSeller.getStoresFromFile().toString());
                            writer.println();
                            writer.flush();
                            if (mainSeller.getStoresFromFile().toString().equals("[]")) {
                                breakpoint++;
                                continue;
                            }
                            String storeName = reader.readLine();

                            if (mainSeller.closeStore(storeName)) {
                                breakpoint++;
                                writer.write("Store closed successfully");
                                writer.println();
                                writer.flush();

                            } else {
                                breakpoint++;
                                writer.write("A problem occurred while trying to close the store\nYou may have spelled the store name incorrectly");
                                writer.println();
                                writer.flush();
                            }
                        }
                        continue;
                    }

                    if (inboxInput.equals("10")) {
                        breakpoint++;
                        break;
                    }
                }

                while (customerMessageFlag) {
                    breakpoint++;
                    Customer mainCustomer = Customer.findCustomer(userEmail);
                    if (mainCustomer.alertUserOfNewMessage()) {
                        breakpoint++;
                        writer.write("You have new messages!");
                        writer.println();
                        breakpoint++;
                        writer.flush();
                    } else {
                        breakpoint++;
                        writer.write("You have no new messages!");
                        writer.println();
                        writer.flush();
                    }
                    File sellers = new File("sellers.txt");
                    breakpoint++;

                    String inboxInput = reader.readLine();

                    if (inboxInput.equals("1")) {
                        breakpoint++;
                        File blockedFile = new File("blockedSellers.txt");
                        if (!blockedFile.exists()) {
                            breakpoint++;
                            blockedFile.createNewFile();
                        }

                        while (true) {
                            String recipient = reader.readLine();
                            Seller recipientSeller = null;
                            if (Seller.findSeller(recipient) == null) {
                                breakpoint++;
                                recipientSeller = Seller.findSeller(Seller.getOwner(recipient));
                            } else {
                                breakpoint++;
                                recipientSeller = Seller.findSeller(recipient);
                            }
                            if(recipient.equals("cancel")) {
                                breakpoint++;
                                break;
                            }
                            boolean blockedUser = true;
                            ArrayList<String> blockedCustomers = mainCustomer.blockedSellersFileRead();
                            for (String blocked : blockedCustomers) {
                                breakpoint++;
                                String sellerName = recipientSeller.getEmail();
                                if (blocked.equals(sellerName)) {
                                    breakpoint++;
                                    writer.write("Message can not be sent!");
                                    writer.println();
                                    writer.flush();
                                    blockedUser = false;
                                    break;
                                }
                            }
                            if (blockedUser == false) {
                                continue;
                            }
                            if (blockedUser == true) {
                                breakpoint++;
                                if (recipientSeller == null) {
                                    breakpoint++;
                                    writer.write("We could not find your recipient. Look through the list of Customers.");
                                    writer.println();
                                    writer.flush();
                                    continue;
                                } else {
                                    breakpoint++;
                                    writer.write("Can send message");
                                    writer.println();
                                    writer.flush();
                                    // "Would you like to:\n1. Enter a message\n2. Import a text file for the message"
                                    String messageOption = reader.readLine();
                                    Message message = null;
                                    if (messageOption.equals("1")) {
                                        breakpoint++;
                                        String messageInput = reader.readLine();
                                        message = mainCustomer.createMessage(recipientSeller, messageInput);
                                    } else if (messageOption.equals("2")) {
                                        breakpoint++;
                                        String messageInput = reader.readLine();
                                        try {
                                            message = mainCustomer.createMessageFromFile(recipientSeller, messageInput);
                                        } catch (FileNotFoundException e) {
                                            writer.write("The file couldn't be found.");
                                            writer.println();
                                            writer.flush();
                                            continue;
                                        }
                                    }
                                    mainCustomer.writeFile(message);
                                    recipientSeller.writeFile(message);
                                    breakpoint++;
                                    writer.write("Message Sent Successfully!");
                                    writer.println();
                                    writer.flush();
                                    break;
                                }
                            }
                        }
                        continue;
                    }


                    if (inboxInput.equals("2")) {
                        breakpoint++;

                        File censoredFile = new File("censors.txt");
                        if (!censoredFile.exists()) {
                            breakpoint++;
                            censoredFile.createNewFile();
                        }

                        ArrayList<String> allMessages = null;
                        ArrayList<String> allCensored = null;


                        try {

                            // Getting all messages

                            allMessages = mainCustomer.getNewMessages();

                            // Getting list of censored words

                            allCensored = mainCustomer.getCensoredWords();

                            // checking if there is censored words

                            if (allCensored == null) {
                                breakpoint++;
                                writer.write("There are no censored words as of now!");
                            } else {
                                breakpoint++;
                                writer.write(String.join("~", allCensored));
                            }
                            writer.println();
                            writer.flush();

                            // Getting input on whether or not the user wants to add more

                            String censorOrNot = reader.readLine();

                            if (censorOrNot.equals("1")) {

                                breakpoint++;

                                int numberOfCensoredWords = Integer.parseInt(reader.readLine());

                                ArrayList<String> newCensoredWords = new ArrayList<String>();
                                ArrayList<String> replacementWords = new ArrayList<String>();

                                for (int i = 0; i < numberOfCensoredWords; i++) {
                                    breakpoint++;
                                    newCensoredWords.add(reader.readLine());
                                    replacementWords.add(reader.readLine());
                                }

                                for (int i = 0; i < numberOfCensoredWords; i++) {
                                    breakpoint++;
                                    mainCustomer.saveCensor(newCensoredWords.get(i), replacementWords.get(i));
                                }

                                String censoredPrint = "";
                                for (String message : allMessages) {
                                    breakpoint++;
                                    censoredPrint += mainCustomer.censorTextMessage(message) + "~";
                                }

                                if (true) {
                                    writer.write(censoredPrint);
                                    breakpoint++;
                                    writer.println();
                                    writer.flush();
                                }



                            } else if (censorOrNot.equals("2")) {
                                breakpoint++;

                                String censoredPrint = "";
                                for (String message : allMessages) {
                                    breakpoint++;
                                    censoredPrint += mainCustomer.censorTextMessage(message) + "~";
                                }

                                writer.write(censoredPrint);
                                writer.println();
                                writer.flush();
                            }


                        } catch (Exception e) {
                            breakpoint++;
                            e.printStackTrace();
                            writer.write("You have no messages");
                            writer.println();
                            writer.flush();
                        }
                        String test = reader.readLine();
                    }


                    if (inboxInput.equals("3")) {
                        breakpoint++;

                        File censoredFile = new File("censors.txt");
                        if (!censoredFile.exists()) {
                            breakpoint++;
                            censoredFile.createNewFile();
                        }

                        ArrayList<String> allMessages = null;
                        ArrayList<String> allCensored = null;


                        try {

                            breakpoint++;

                            allMessages = mainCustomer.getAllMessages();

                            breakpoint++;
                            allCensored = mainCustomer.getCensoredWords();

                            breakpoint++;

                            if (allCensored == null) {
                                breakpoint++;
                                writer.write("There are no censored words as of now!");
                            } else {
                                writer.write(String.join("~", allCensored));
                            }
                            writer.println();
                            writer.flush();

                            breakpoint++;

                            String censorOrNot = reader.readLine();

                    		if (censorOrNot.equals("1")) {
                                breakpoint++;


                    			int numberOfCensoredWords = Integer.parseInt(reader.readLine());

                    			ArrayList<String> newCensoredWords = new ArrayList<String>();
                                breakpoint++;
                                ArrayList<String> replacementWords = new ArrayList<String>();

                    			for (int i = 0; i < numberOfCensoredWords; i++) {
                                    breakpoint++;
                                    newCensoredWords.add(reader.readLine());
                    				replacementWords.add(reader.readLine());
                    			}

                    			for (int i = 0; i < numberOfCensoredWords; i++) {
                                    breakpoint++;
                                    mainCustomer.saveCensor(newCensoredWords.get(i), replacementWords.get(i));
                    			}

                    			String censoredPrint = "";
                    			for (String message : allMessages) {
                                    breakpoint++;
                                    censoredPrint += mainCustomer.censorTextMessage(message) + "~";
                    			}

                    			writer.write(censoredPrint);
                    			writer.println();
                    			writer.flush();

                    		} else if (censorOrNot.equals("2")) { // If the user does not want to censor

                    			String censoredPrint = "";
                    			for (String message : allMessages) {
                                    breakpoint++;
                                    censoredPrint += mainCustomer.censorTextMessage(message) + "~";
                    			}

                    			writer.write(censoredPrint);
                    			writer.println();
                    			writer.flush();
                    		}


                        } catch (Exception e) {
                            e.printStackTrace();
                            writer.write("You have no messages");
                            breakpoint++;
                            writer.println();
                            writer.flush();
                        }
                        String test = reader.readLine();
                    }

                    if (inboxInput.equals("4")) {
                        breakpoint++;
                        ArrayList<String> allStores = Seller.getAllStores();
                        for (String message : allStores) {
                            breakpoint++;
                            writer.write(message + "~");
                        }
                        writer.println();
                        writer.flush();
                        breakpoint++;
                        String mainMenuCheck = reader.readLine();
                        if(mainMenuCheck.equals("menu")) {
                            breakpoint++;
                            continue;
                        }
                    }

                    if (inboxInput.equals("5")) {
                        breakpoint++;
                        ArrayList<String> allSellers = Seller.getSellers();
                        writer.write(String.join("~", allSellers));
                        writer.println();
                        writer.flush();
                        String mainMenuCheck = reader.readLine();
                        if(mainMenuCheck.equals("menu")) {
                            breakpoint++;
                            continue;
                        }
                    }

                    if (inboxInput.equals("6")) {
                        breakpoint++;

                        ArrayList<String> allMessages = mainCustomer.getAllMessages();
                        for (int i = 0; i < allMessages.size(); i++) {
                            breakpoint++;
                            writer.write(i + ") " + allMessages.get(i) + "~");
                        }
                        writer.println();
                        writer.flush();

                        String editOption = reader.readLine();
                        breakpoint++;
                        String editInput = reader.readLine();

                        int editNum;

                        editNum = Integer.parseInt(editOption);
                        breakpoint += editNum;

                        if (editNum >= 0 && editNum < allMessages.size()) {
                            breakpoint++;
                            if (mainCustomer.editMessage(allMessages.get(editNum), editInput)) {
                                breakpoint++;
                                writer.write("Message successfully edited");
                                writer.println();
                                writer.flush();
                                continue;
                            } else {
                                breakpoint++;
                                writer.write("There was a problem editing the message");
                                writer.println();
                                writer.flush();
                                continue;
                            }
                        } else {
                            breakpoint++;
                            writer.write("You didn't enter a valid number");
                            writer.println();
                            writer.flush();
                            continue;
                        }
                    }

                    if (inboxInput.equals("7")) {
                        breakpoint++;

                        ArrayList<String> allMessages = mainCustomer.getAllMessages();
                        for (int i = 0; i < allMessages.size(); i++) {
                            breakpoint++;
                            writer.write(i + ") " + allMessages.get(i) + "~");
                        }
                        writer.println();
                        writer.flush();

                        String editOption = reader.readLine();
                        int editNum;
                        editNum = Integer.parseInt(editOption);
                        breakpoint += editNum;
                        breakpoint++;
                        if (editNum >= 0 && editNum < allMessages.size()) {
                            breakpoint++;
                            if (mainCustomer.deleteMessage(allMessages.get(editNum))) {
                                breakpoint++;
                                writer.write("Message successfully deleted");
                                writer.println();
                                breakpoint++;
                                writer.flush();
                                continue;
                            } else {
                                breakpoint++;
                                writer.write("There was a problem deleting the message");
                                writer.println();
                                breakpoint++;
                                writer.flush();
                                continue;
                            }
                        } else {
                            breakpoint++;
                            writer.write("You didn't enter a valid number");
                            writer.println();
                            breakpoint++;
                            writer.flush();
                            continue;
                        }
                    }


                    if (inboxInput.equals("8")) {
                        breakpoint++;

                        String username = reader.readLine();
                        Seller participant = Seller.findSeller(username);
                        if (participant == null) {
                            breakpoint++;
                            writer.write("The user couldn't be found");
                            writer.println();
                            writer.flush();
                            continue;
                        }
                        writer.write("user found");
                        writer.println();
                        breakpoint++;
                        writer.flush();
                        String filename = reader.readLine();
                        breakpoint++;
                        mainCustomer.downloadMessages(participant, filename);
                        breakpoint++;
                        File f = new File(filename);
                        breakpoint++;
                        ArrayList<String> file = new ArrayList<String>();
                        breakpoint++;
                        BufferedReader br = null;

                        try {

                            br = new BufferedReader(new FileReader(f));

                            String placeholderLine = br.readLine();
                            while (placeholderLine != null) {
                                breakpoint++;
                                file.add(placeholderLine);
                                placeholderLine = br.readLine();
                            }

                        } catch (FileNotFoundException e) {
                            writer.write("Something went wrong");
                            writer.println();
                            breakpoint++;
                            writer.flush();

                        } catch (IOException e) {
                        	writer.write("Something went wrong");
                        	writer.println();
                            writer.flush();

                        } finally {

                            if (br != null) {
                                breakpoint++;
                                try {
                                    br.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        
                        writer.write("" + file.size());
                        writer.println();
                        writer.flush();
                        
                        for (int i = 0; i < file.size(); i++) {
                            breakpoint++;
                            writer.write(file.get(i));
                        	writer.println();
                        	writer.flush();
                        }
                        
                    }
                    if (inboxInput.equals("9")) {
                        breakpoint++;
                        ArrayList<String> blockedSellers = new ArrayList<String>();
                        breakpoint++;
                        ArrayList<String> allSellers = Seller.getSellers();
                        writer.write(String.join("~", allSellers));
                        breakpoint++;
                        writer.println();
                        writer.flush();
                        breakpoint++;
                        String blocked = reader.readLine();
                        blockedSellers.add(blocked);
                        breakpoint++;
                        mainCustomer.blockedSellersFileWrite(blockedSellers, true);
                        breakpoint++;
                        writer.write("User blocked! They will no longer be able to message you");
                        writer.println();
                        breakpoint++;
                        writer.flush();
                    }
                    if (inboxInput.equals("10")) {
                        breakpoint++;
                        break;
                    }
                }
            } catch (IOException e) {
                breakpoint++;
                return;
            } catch (NullPointerException e) {
                breakpoint++;
                return;
            }
        }
    }
}