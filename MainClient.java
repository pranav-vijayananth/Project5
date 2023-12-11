import java.awt.event.*;
import java.io.*;
import java.net.Socket;

import javax.swing.*;
import java.awt.*;


public class MainClient extends JComponent implements Runnable {

    JFrame frame = new JFrame();
    Container content = frame.getContentPane();
    int num3 = 160;
    int num4 = 40;
    Dimension buttonSize = new Dimension(num3, num4);
    JButton newAccountButton;
    JButton existingLoginButton;
    JButton customerButton;
    JButton sellerButton;
    JButton existingAccountButton;

    JButton createNewAccountButton;
    JPasswordField passwordField;
    JPasswordField confirmPasswordField;
    JTextField emailField;
    JButton editButton;
    JButton deleteButton;

    JButton sendMsg;
    JButton viewNewMsg;
    JButton viewAllMsgs;
    JButton viewCustomers;
    JButton editMsg;
    JButton deleteMsg;
    JButton downloadMsgs;
    JButton blockCust;
    JButton manageStores;

    JButton exit;

    JTextArea messageToSend;

    JButton sendMsgCust;
    JButton viewNewMsgCust;
    JButton viewAllMsgsCust;
    JButton viewStores;
    JButton viewSellers;
    JButton editMsgCust;
    JButton deleteMsgCust;
    JButton downloadMsgsCust;
    JButton blockSeller;


    JTextArea newMessage;

    JLabel newMessageAlert = new JLabel();

    JButton mainMenuButton;

    int sellerOrCustomer;
    static MainClient client;

    String listOfCustomers;
    JLabel msgFromFileOrText;
    JButton enterTextMsg;
    JButton importTextFileMsg;
    JButton sendMsgSendButton;
    JButton newStoreNameConfirmButton;


    JTextField editAccountEmailField;

    JTextField newStoreNameField;
    JComboBox usersStoresToClose;
    JButton confirmCloseStoreButton;
    JPasswordField editAccountOldPasswordField;
    int editAccountOldPassword;
    JPasswordField editAccountNewPasswordField;
    int editAccountNewPassword;
    JLabel welcomeLabel;
    JPasswordField editAccountNewPasswordConfirmField;
    JButton changePasswordButton;
    JButton editAccountLoginButton;
    int editAccountLogin;

    JTextField deleteAccountEmailField;
    JPasswordField deleteAccountPasswordField;
    JButton deleteAccountButton;
    JButton randomButton;
    JButton blockButtonCust;

    int num1 = 200;
    int num2 = 30;
    Dimension textField = new Dimension(num1, num2);
    JTextField editedMsgField;
    JButton editMessageButton;
    JComboBox messageDropDown;
    JComboBox DropDown;

    JButton deleteMessageButton;
    JButton downloadMessagesButton;
    JTextField usernameToDownloadField;
    JButton openNewStore;
    JComboBox blockCustomerDropDown;
    JButton closeStore;
    JTextField filenameToDownloadField;
    JButton blockButton;
    JComboBox blockSellersDropDown;

    JTextField fileNameImport;
    JButton fileNameImportButton;
    String listOfStores;


    public static void main(String[] args) {

        for (int i = 1; i <= 5; i++) {
            System.out.println(i);
        }

        SwingUtilities.invokeLater(new MainClient());

    }

    public MainClient() {

    }

    Socket socket;
    PrintWriter writer;
    String listOfSellers;
    BufferedReader reader;

    {
        int breakpoint = 0;
        try {
            breakpoint = 0;
            socket = new Socket("localhost", 9000);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            breakpoint++;
        } catch (IOException e) {
            breakpoint++;
            throw new RuntimeException(e);
        }
    }

    ActionListener actionListener = new ActionListener() {


        String credentials = null;
        String customerOrSeller;

        @Override
        public void actionPerformed(ActionEvent e) {

            try {

                int breakpoint = 0;

                if (e.getSource() == sellerButton) {
                    sellerOrCustomer = 1;
                    writer.write("1");
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    String inputCheck = reader.readLine();
                    if (inputCheck.equals("input assigned")) {
                        breakpoint++;
                        newOrExistingWindow();
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error connecting to server");
                    }
                }

                if (e.getSource() == customerButton) {
                    breakpoint++;
                    sellerOrCustomer = 2;
                    writer.write("2");
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    String inputCheck = reader.readLine();
                    if (inputCheck.equals("input assigned")) {
                        breakpoint++;
                        newOrExistingWindow();
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error connecting to server");
                    }
                }
                // Existing vs New vs Edit vs Delete Account
                if (e.getSource() == newAccountButton) {
                    breakpoint++;
                    writer.write("2");
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    newAccountLoginWindow();
                }
                if (e.getSource() == existingAccountButton) {
                    breakpoint++;
                    writer.write("1");
                    writer.println();
                    writer.flush();
                    existingLoginWindow();

                }
                if (e.getSource() == editButton) {
                    breakpoint++;
                    writer.write("3");
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    editAccountWindow();
                }
                if (e.getSource() == editAccountLoginButton) {
                    breakpoint++;
                    credentials = updateEditAccountCredentials();
                    breakpoint++;
                    writer.write(credentials);
                    writer.println();
                    breakpoint++;
                    writer.flush();
                    String credentialsCheck = reader.readLine();
                    if (credentialsCheck.equals("credentials verified")) {
                        breakpoint++;
                        editAccountNewPasswordWindow();
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error logging into account");
                    }
                }
                if (e.getSource() == changePasswordButton) {
                    if (editAccountNewPasswordsMatch()) {
                        breakpoint++;
                        writer.write(retrieveNewPasswords());
                        writer.println();
                        writer.flush();
                        newMessageAlert.setText(reader.readLine());
                        mainMenuWindow();
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Your passwords did not match ");
                    }
                }
                if (e.getSource() == deleteButton) {
                    breakpoint++;
                    writer.write("4");
                    writer.println();
                    writer.flush();
                    deleteAccountWindow();
                }

                if (e.getSource() == randomButton) {

                }

                if (e.getSource() == deleteAccountButton) {
                    breakpoint++;
                    int yesOrNo = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    breakpoint++;
                    if (yesOrNo == JOptionPane.YES_OPTION) {
                        breakpoint++;
                        writer.write(updateDeleteAccountCredentials());
                        breakpoint++;
                        writer.println();
                        writer.flush();
                        int tempItem = 0;

                        String deleteCheck = reader.readLine();

                        if (deleteCheck.equals("deleted")) {
                            breakpoint++;
                            JOptionPane.showMessageDialog(null, "Account Deleted!");
                            frame.dispose();
                            SwingUtilities.invokeLater(new MainClient());

                        } else {
                            breakpoint++;
                            JOptionPane.showMessageDialog(null, deleteCheck);

                        }
                    } else {
                        breakpoint++;
                        frame.dispose();
                        SwingUtilities.invokeLater(new MainClient());
                    }
                }

                //LOGIN
                if (e.getSource() == existingLoginButton) {
                    breakpoint++;

                    int i = 0;
                    if (i == 0) {
                        credentials = updateCredentials();
                        writer.write(credentials);
                        writer.println();
                        writer.flush();
                    }

                    String credentialsCheck = reader.readLine();
                    int creds = 10;
                    if (credentialsCheck.equals("valid credentials") && creds == 10) {

                        breakpoint++;
                        mainMenuWindow();
                        newMessageAlert.setText(reader.readLine());
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Your Email or Password may be incorrect");
                        writer.write("1");
                        breakpoint++;
                        writer.println();
                        writer.flush();
                    }
                }
                if (e.getSource() == createNewAccountButton) {
                    breakpoint++;
                    if (passwordsMatch()) {
                        breakpoint++;
                        credentials = updateCredentials();
                        breakpoint++;
                        writer.write(credentials);
                        breakpoint++;
                        writer.println();
                        writer.flush();
                        String newUserCheck = reader.readLine();
                        if (newUserCheck.equals("User Created!")) {
                            breakpoint++;
                            mainMenuWindow();
                        }
                        
                        newMessageAlert.setText(reader.readLine());
                        
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error: passwords do not match or email is already in use");
                    }
                }

                //seller
                if (e.getSource() == sendMsg) {
                    breakpoint++;
                    writer.write("1");
                    breakpoint++;
                    writer.println();
                    breakpoint++;
                    writer.flush();
                    String recipientEmail = JOptionPane.showInputDialog("Please enter the email of the recipient of your message");
                    breakpoint++;
                    if (recipientEmail == null) {
                        breakpoint++;
                        writer.write("cancel");
                        writer.println();
                        writer.flush();
                        mainMenuWindow();
                        newMessageAlert.setText(reader.readLine());
                    } else {
                        breakpoint++;
                        writer.write(recipientEmail);
                        writer.println();
                        writer.flush();
                        String recipientEmailCheck = reader.readLine();
                        breakpoint++;

                        if (recipientEmailCheck.equals("Message can not be sent!") || recipientEmailCheck.equals("We could not find your recipient. Look through the list of Customers.")) {
                            breakpoint++;
                            JOptionPane.showMessageDialog(null, "Error: user may not exist or have blocked you");
                        } else {
                            breakpoint++;
                            sendMsgFromFileOrText();
                        }
                    }
                }
                if (e.getSource() == enterTextMsg) {
                    breakpoint++;
                    writer.write("1");
                    writer.println();
                    writer.flush();
                    sendMsgFromTextWindow();

                }
                if (e.getSource() == sendMsgSendButton) {
                    breakpoint++;
                    String msgText = messageToSend.getText();
                    writer.write(msgText);
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    String msgConfirmation = reader.readLine();
                    if (msgConfirmation.equals("Message Sent Successfully!")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Message Sent Successfully!");
                        mainMenuWindow();
                        newMessageAlert.setText(reader.readLine());
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "There was an error sending your message");
                        mainMenuWindow();
                        newMessageAlert.setText(reader.readLine());
                        breakpoint++;
                    }
                }
                if (e.getSource() == importTextFileMsg) {
                    breakpoint++;
                    writer.write("2");
                    writer.println();
                    breakpoint++;
                    writer.flush();
                    sendMsgFromTextFileWindow();
                    breakpoint++;
                }
                if (e.getSource() == fileNameImportButton) {
                    breakpoint++;
                    writer.write(fileNameImport.getText());
                    breakpoint++;
                    writer.println();
                    writer.flush();

                    String msgCheck = reader.readLine();
                    if (msgCheck.equals("Message Sent Successfully!")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, msgCheck);
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error Locating your text file");
                    }
                    mainMenuWindow();
                    breakpoint++;
                    newMessageAlert.setText(reader.readLine());
                }

                if (e.getSource() == viewNewMsg) {
                    breakpoint++;
                    if (newMessageAlert.getText().equals("You have no new messages!")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error: No new Messages");
                	} else {
                        breakpoint++;
                        writer.write("2");
                		writer.println();
                		writer.flush();
                		String censoredWordsCheck = reader.readLine();
                		if (!censoredWordsCheck.equals("There are no censored words as of now!")) {

                            breakpoint++;
                            JOptionPane.showMessageDialog(null, "Current Censored Words: " + censoredWordsCheck.replace("~", ", "));
                		} else {
                            breakpoint++;
                            JOptionPane.showMessageDialog(null, censoredWordsCheck);
                		}

                        int confirmCensor = JOptionPane.showConfirmDialog(null, "Would you like to censor new words?", "Censor Words",JOptionPane.YES_NO_OPTION);

                        if (confirmCensor == JOptionPane.YES_OPTION) {
                            breakpoint++;
                            writer.write("1");
                            writer.println();
                            writer.flush();

                            int numOfWords = 0;
                            while (true) {
                                breakpoint++;
                                try {
                                    numOfWords = Integer.parseInt(JOptionPane.showInputDialog(null, "How many words would you like to censor?"));
                                    breakpoint += numOfWords;

                                    break;
                                } catch (Exception npe) {
                                    JOptionPane.showMessageDialog(null, "Enter a valid integer!");
                                }
                            }

                            writer.write("" + numOfWords);
                            breakpoint++;
                            writer.println();
                            writer.flush();

                            for (int i = 0; i < numOfWords; i++) {
                                breakpoint += i;
                                String inputCensor = JOptionPane.showInputDialog(null, (i+1) + ") Enter the word you would like to censor.");
                                breakpoint++;
                                String inputReplacement = JOptionPane.showInputDialog(null, (i+1) + ") Enter the replacement word(Leave blank for auto replace key).");
                                if (inputReplacement.equals(null)) {
                                    breakpoint++;
                                    inputReplacement = "***";
                                }

                                int tempNum = 0;

                                if (tempNum == 0) {
                                    writer.write(inputCensor);
                                    writer.println();
                                    writer.flush();
                                    writer.write(inputReplacement);
                                    writer.write(inputCensor);
                                    breakpoint++;
                                    writer.println();
                                    breakpoint++;
                                    writer.flush();
                                }

                            }

                            String[] censoredPrint = reader.readLine().split("~");
                            newMessage = new JTextArea();
                            breakpoint++;
                            newMessage.setText(String.join("\n", censoredPrint));
                            breakpoint++;
                            viewNewMsgWindow();

                        } else if (confirmCensor == JOptionPane.NO_OPTION) {
                            breakpoint++;
                            writer.write("2");
                            writer.println();
                            writer.flush();
                            breakpoint++;
                            String labelText = reader.readLine();
                            breakpoint++;
                            newMessage = new JTextArea();
                            newMessage.setText(labelText);
                            viewNewMsgWindow();
                        }


                	}

                }


                if (e.getSource() == viewAllMsgs) {
                    breakpoint++;
                    writer.write("3");
                		writer.println();
                		writer.flush();
                		String censoredWordsCheck = reader.readLine();
                		if (!censoredWordsCheck.equals("There are no censored words as of now!")) {
                            breakpoint++;
                            JOptionPane.showMessageDialog(null, "Current Censored Words: " + censoredWordsCheck.replace("~", ", "));
                		} else {
                            breakpoint++;
                            JOptionPane.showMessageDialog(null, censoredWordsCheck);
                		}

                    int confirmCensor = JOptionPane.showConfirmDialog(null, "Would you like to censor new words?", "Censor Words",JOptionPane.YES_NO_OPTION);
                    breakpoint++;
                    if (confirmCensor == JOptionPane.YES_OPTION) {
                        breakpoint++;
                        writer.write("1");
                        writer.println();
                        writer.flush();

                        int numOfWords = 0;
                        breakpoint++;
                        while (true) {
                            breakpoint++;
                            try {
                                numOfWords = Integer.parseInt(JOptionPane.showInputDialog(null, "How many words would you like to censor?"));
                                breakpoint += numOfWords;
                                break;
                            } catch (Exception npe) {
                                JOptionPane.showMessageDialog(null, "Enter a valid integer!");
                            }
                        }

                        writer.write("" + numOfWords);
                        writer.println();
                        writer.flush();

                        for (int i = 0; i < numOfWords; i++) {
                            breakpoint += i;
                            String inputCensor = JOptionPane.showInputDialog(null, (i+1) + ") Enter the word you would like to censor.");
                            breakpoint++;
                            String inputReplacement = JOptionPane.showInputDialog(null, (i+1) + ") Enter the replacement word(Leave blank for auto replace key).");
                            if (inputReplacement.equals(null)) {
                                breakpoint++;
                                inputReplacement = "***";
                            }

                            writer.write(inputCensor);
                            breakpoint++;
                            writer.println();
                            writer.flush();
                            breakpoint++;
                            writer.write(inputReplacement);
                            breakpoint++;
                            writer.println();
                            writer.flush();

                        }

                        String[] censoredPrint = reader.readLine().split("~");
                        breakpoint++;
                        newMessage = new JTextArea();
                        newMessage.setText(String.join("\n", censoredPrint));
                        breakpoint++;
                        viewNewMsgWindow();

                    } else if (confirmCensor == JOptionPane.NO_OPTION) {
                        breakpoint++;
                        writer.write("2");
                        writer.println();
                        writer.flush();
                        String labelText = reader.readLine();
                        breakpoint++;
                        String[] labelTextArray = labelText.split("~");

                        for (String label : labelTextArray) {
                            System.out.println(label);
                        }

                        newMessage = new JTextArea();
                        newMessage.setText(String.join("\n", labelTextArray));
                        viewNewMsgWindow();
                    }

                }
                if (e.getSource() == viewCustomers) {
                    breakpoint++;
                    writer.write("4");
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    listOfCustomers = reader.readLine();
                    breakpoint++;
                    viewCustomersWindow();

                }
                if (e.getSource() == editMsg) {
                    breakpoint++;
                    writer.write("5");
                    writer.println();
                    writer.flush();
                    editMsgWindow();
                }
                if (e.getSource() == editMessageButton) {
                    breakpoint++;
                    int editOption = messageDropDown.getSelectedIndex();
                    writer.write(String.valueOf(editOption));
                    writer.println();
                    breakpoint++;
                    writer.flush();

                    String editInput = editedMsgField.getText();
                    writer.write(editInput);
                    writer.println();
                    writer.flush();
                    breakpoint++;

                    String editCheck = reader.readLine();
                    if (editCheck.equals("Message successfully edited")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, editCheck);
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error: There was a problem editing the message");
                    }
                    mainMenuWindow();
                    breakpoint++;
                    newMessageAlert.setText(reader.readLine());

                }
                if (e.getSource() == deleteMsg) {
                    breakpoint++;
                    writer.write("6");
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    deleteMsgWindow();

                }
                if (e.getSource() == deleteMessageButton) {
                    breakpoint++;
                    int editOption = messageDropDown.getSelectedIndex();
                    writer.write(String.valueOf(editOption));
                    writer.println();
                    writer.flush();
                    breakpoint++;
                    String deleteCheck = reader.readLine();
                    if (deleteCheck.equals("MessageSuccessfully deleted")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, deleteCheck);
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, deleteCheck);
                    }
                    mainMenuWindow();
                    newMessageAlert.setText(reader.readLine());
                }
                if (e.getSource() == downloadMsgs) {
                    breakpoint++;
                    writer.write("7");
                    writer.println();
                    writer.flush();
                    downloadMsgsWindow();
                }
                if (e.getSource() == downloadMessagesButton) {
                    breakpoint++;
                    writer.write(usernameToDownloadField.getText());
                    writer.println();
                    writer.flush();
                    breakpoint++;
                    String usernameCheck = reader.readLine();
                    if (usernameCheck.equals("The user couldn't be found")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error: The user couldn't be found");
                        mainMenuWindow();
                        newMessageAlert.setText(reader.readLine());
                    } else {
                        breakpoint++;
                        String fileName = filenameToDownloadField.getText();
                        writer.write(fileName);
                        writer.println();
                        writer.flush();
                        breakpoint++;
                        File f = new File(fileName);

                        String fileSize = reader.readLine();

                        if (!f.exists()) {
                            breakpoint++;
                            f.createNewFile();
                        }
                        try (FileWriter fw = new FileWriter(f); BufferedWriter bw = new BufferedWriter(fw)) {
                            breakpoint++;
                            for (int i = 0; i < Integer.parseInt(fileSize); i++) {
                                breakpoint += i;
                                bw.write(reader.readLine());
                                    bw.newLine();
                            }
                            bw.flush();
                        } catch (IOException ioe) {
                            breakpoint++;
                            ioe.printStackTrace();
                        }


                    }
                    mainMenuWindow();
                    breakpoint++;
                    newMessageAlert.setText(reader.readLine());
                }
                if (e.getSource() == blockCust) {
                    breakpoint++;
                    writer.write("8");
                    writer.println();
                    writer.flush();
                    String[] customers = reader.readLine().split("~");
                    breakpoint++;
                    blockCustomerDropDown = new JComboBox(customers);
                    blockCustWindow();
                }
                if (e.getSource() == blockButton) {
                    breakpoint++;
                    writer.write(blockCustomerDropDown.getSelectedItem().toString());
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    String blockCheck = reader.readLine();
                    if (blockCheck.equals("User blocked! They will no longer be able to message you")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "User Blocked");
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error blocking user");
                    }
                    mainMenuWindow();
                    newMessageAlert.setText(reader.readLine());
                }
                if (e.getSource() == blockButtonCust) {
                    breakpoint++;
                    writer.write(blockSellersDropDown.getSelectedItem().toString());
                    writer.println();
                    writer.flush();
                    breakpoint++;
                    String blockCheck = reader.readLine();
                    if (blockCheck.equals("User blocked! They will no longer be able to message you")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "User Blocked");
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error blocking user");
                    }
                    mainMenuWindow();
                    newMessageAlert.setText(reader.readLine());
                }
                if (e.getSource() == manageStores) {
                    breakpoint++;
                    writer.write("9");
                    writer.println();
                    writer.flush();
                    manageStoresWindow();
                }
                if (e.getSource() == openNewStore) {
                    breakpoint++;
                    writer.write("1");
                    writer.println();
                    writer.flush();
                    //TODO window for naming store
                    createNewStoreWindow();
                }
                if (e.getSource() == newStoreNameConfirmButton) {
                    breakpoint++;
                    writer.write(newStoreNameField.getText());
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    String storeCreateCheck = reader.readLine();
                    if (storeCreateCheck.equals("Store added successfully")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, storeCreateCheck);
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error: A problem occurred while trying to add the store");
                    }
                    mainMenuWindow();
                    newMessageAlert.setText(reader.readLine());
                }
                if (e.getSource() == closeStore) {
                    breakpoint++;
                    writer.write("2");
                    writer.println();
                    writer.flush();
                    String usersStores = reader.readLine();
                    if (usersStores.equals("[]")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error: You have no stores");
                        newMessageAlert.setText(reader.readLine());
                        mainMenuWindow();
                    } else {
                        breakpoint++;
                        usersStores = usersStores.substring(1, usersStores.length() - 1);
                        String[] stores = usersStores.split(", ");
                        usersStoresToClose = new JComboBox(stores);
                        closeExistingStoreWindow();
                    }

                }
                if (e.getSource() == confirmCloseStoreButton) {
                    breakpoint++;
                    writer.write(usersStoresToClose.getSelectedItem().toString());
                    writer.println();
                    writer.flush();
                    breakpoint++;
                    String closeStoreCheck = reader.readLine();
                    if (closeStoreCheck.equals("Store closed successfully")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, closeStoreCheck);
                        mainMenuWindow();
                    } else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error: A problem occurred while trying to close the store");
                        mainMenuWindow();
                    }
                    newMessageAlert.setText(reader.readLine());
                }


                if (e.getSource() == exit) {
                    breakpoint++;
                    int selection = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out", "Logout", JOptionPane.YES_NO_OPTION);
                    breakpoint++;
                    if (selection == JOptionPane.YES_OPTION) {
                        breakpoint++;
                        int tempNum = 1;
                        if (true) {
                            writer.write("10");
                            writer.println();
                            writer.flush();
                            frame.dispose();
                            SwingUtilities.invokeLater(new MainClient());
                        }

                    }
                }


                if (e.getSource() == mainMenuButton) {
                    breakpoint++;
                    writer.write("menu");
                    writer.println();
                    writer.flush();
                    breakpoint++;
                    String text = reader.readLine();
                    newMessageAlert.setText(text);
                    mainMenuWindow();
                }
                // customer
                if (e.getSource() == sendMsgCust) {
                    writer.write("1");
                    breakpoint++;
                    writer.println();
                    writer.flush();
                    String recipientEmail = JOptionPane.showInputDialog("Please enter the email of the recipient of your message");
                    if (recipientEmail == null) {
                        breakpoint++;
                        writer.write("cancel");
                        writer.println();
                        writer.flush();
                        mainMenuWindow();
                        newMessageAlert.setText(reader.readLine());
                    } else {
                        breakpoint++;
                        if (true) {
                            writer.write(recipientEmail);
                            writer.println();
                            writer.flush();
                        }
                        String recipientEmailCheck = reader.readLine();
                        if (recipientEmailCheck.equals("Message can not be sent!") || recipientEmailCheck.equals("We could not find your recipient. Look through the list of Customers.")) {
                            breakpoint++;
                            JOptionPane.showMessageDialog(null, "Error: user may not exist or have blocked you");
                        } else {
                            breakpoint++;
                            sendMsgFromFileOrText();
                        }
                    }

                }
                if (e.getSource() == viewNewMsgCust) {
                    breakpoint++;
                    if (newMessageAlert.getText().equals("You have no new messages!")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Error: No new Messages");
                        breakpoint++;
                    } else {
                        breakpoint++;
                        writer.write("2");
                        writer.println();
                        writer.flush();
                        String censoredWordsCheck = reader.readLine();
                        if (!censoredWordsCheck.equals("There are no censored words as of now!")) {
                            breakpoint++;
                            JOptionPane.showMessageDialog(null, "Current Censored Words: " + censoredWordsCheck.replace("~", ", "));
                        } else {
                            breakpoint++;
                            JOptionPane.showMessageDialog(null, censoredWordsCheck);
                        }

                        int confirmCensor = JOptionPane.showConfirmDialog(null, "Would you like to censor new words?", "Censor Words",JOptionPane.YES_NO_OPTION);
                        breakpoint++;
                        if (confirmCensor == JOptionPane.YES_OPTION) {
                            breakpoint++;
                            writer.write("1");
                            writer.println();
                            writer.flush();
                            breakpoint++;
                            int numOfWords = 0;
                            while (true) {
                                try {
                                    breakpoint++;
                                    numOfWords = Integer.parseInt(JOptionPane.showInputDialog(null, "How many words would you like to censor?"));
                                    break;
                                } catch (Exception npe) {
                                    breakpoint++;
                                    JOptionPane.showMessageDialog(null, "Enter a valid integer!");
                                }
                            }

                            writer.write("" + numOfWords);
                            writer.println();
                            breakpoint++;
                            writer.flush();

                            for (int i = 0; i < numOfWords; i++) {
                                breakpoint += i;
                                String inputCensor = JOptionPane.showInputDialog(null, (i+1) + ") Enter the word you would like to censor.");
                                String inputReplacement = JOptionPane.showInputDialog(null, (i+1) + ") Enter the replacement word(Leave blank for auto replace key).");
                                if (inputReplacement.equals(null)) {
                                    breakpoint++;
                                    inputReplacement = "***";
                                }

                                writer.write(inputCensor);
                                writer.println();
                                breakpoint++;
                                writer.flush();
                                writer.write(inputReplacement);
                                breakpoint++;
                                writer.println();
                                writer.flush();

                            }

                            String[] censoredPrint = reader.readLine().split("~");
                            newMessage = new JTextArea();
                            breakpoint++;
                            newMessage.setText(String.join("\n", censoredPrint));
                            breakpoint++;
                            viewNewMsgWindow();
                        } else if (confirmCensor == JOptionPane.NO_OPTION) {
                            breakpoint++;
                            writer.write("2");
                            writer.println();
                            writer.flush();
                            breakpoint++;
                            String labelText = reader.readLine();
                            String[] labelTextArray = labelText.split("~");
                            breakpoint++;
                            newMessage = new JTextArea();
                            breakpoint++;
                            newMessage.setText(String.join("\n", labelTextArray));
                            viewNewMsgWindow();
                        }

                    }
                }

                if (e.getSource() == viewAllMsgsCust) {
                    writer.write("3");
                    breakpoint++;
                    writer.println();
            		writer.flush();
            		String censoredWordsCheck = reader.readLine();
                    breakpoint++;
                    if (!censoredWordsCheck.equals("There are no censored words as of now!")) {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, "Current Censored Words: " + censoredWordsCheck.replace("~", ", "));
            		} else {
                        breakpoint++;
                        JOptionPane.showMessageDialog(null, censoredWordsCheck);
            		}

                    int confirmCensor = JOptionPane.showConfirmDialog(null, "Would you like to censor new words?", "Censor Words",JOptionPane.YES_NO_OPTION);
                    breakpoint += confirmCensor;
                    if (confirmCensor == JOptionPane.YES_OPTION) {

                        writer.write("1");
                        writer.println();
                        writer.flush();
                        breakpoint++;
                        int numOfWords = 0;
                        while (true) {
                            try {
                                breakpoint++;
                                numOfWords = Integer.parseInt(JOptionPane.showInputDialog(null, "How many words would you like to censor?"));
                                breakpoint++;
                                break;
                            } catch (Exception npe) {
                                JOptionPane.showMessageDialog(null, "Enter a valid integer!");
                            }
                        }

                        writer.write("" + numOfWords);
                        writer.println();
                        breakpoint++;
                        writer.flush();

                        for (int i = 0; i < numOfWords; i++) {
                            breakpoint += i;
                            String inputCensor = JOptionPane.showInputDialog(null, (i+1) + ") Enter the word you would like to censor.");
                            breakpoint++;
                            String inputReplacement = JOptionPane.showInputDialog(null, (i+1) + ") Enter the replacement word(Leave blank for auto replace key).");
                            if (inputReplacement.equals(null)) {
                                breakpoint++;
                                inputReplacement = "***";
                            }

                            writer.write(inputCensor);
                            writer.println();
                            writer.flush();
                            breakpoint++;
                            writer.write(inputReplacement);
                            writer.println();
                            writer.flush();

                        }

                        String[] censoredPrint = reader.readLine().split("~");
                        newMessage = new JTextArea();
                        breakpoint++;
                        newMessage.setText(String.join("\n", censoredPrint));
                        breakpoint++;
                        viewNewMsgWindow();

                    } else if (confirmCensor == JOptionPane.NO_OPTION) {
                        breakpoint++;
                        writer.write("2");
                        writer.println();
                        writer.flush();
                        breakpoint++;
                        String labelText = reader.readLine();
                        String[] labelTextArray = labelText.split("~");
                        newMessage = new JTextArea();
                        newMessage.setText(String.join("\n", labelTextArray));
                        viewNewMsgWindow();
                    }
                }
                if (e.getSource() == viewStores) {
                    breakpoint++;
                    writer.write("4");
                    writer.println();
                    writer.flush();
                    listOfStores = reader.readLine();
                    breakpoint++;
                    viewStoresWindow();

                }
                if (e.getSource() == viewSellers) {
                    breakpoint++;
                    writer.write("5");
                    writer.println();
                    writer.flush();
                    breakpoint++;
                    listOfSellers = reader.readLine();
                    viewSellersWindow();
                }
                if (e.getSource() == editMsgCust) {
                    breakpoint++;
                    writer.write("6");
                    writer.println();
                    writer.flush();
                    editMsgWindow();
                }
                if (e.getSource() == deleteMsgCust) {
                    breakpoint++;
                    writer.write("7");
                    writer.println();
                    writer.flush();
                    deleteMsgWindow();

                }
                if (e.getSource() == downloadMsgsCust) {
                    breakpoint++;
                    writer.write("8");
                    writer.println();
                    writer.flush();
                    downloadMsgsWindow();

                }
                if (e.getSource() == blockSeller) {
                    breakpoint++;
                    writer.write("9");
                    writer.println();
                    writer.flush();
                    breakpoint++;
                    String[] sellers = reader.readLine().split("~");
                    blockSellersDropDown = new JComboBox(sellers);
                    blockSellerWindow();

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    };

    public void clearWindow() {
        int breakpoint = 0;
        content.removeAll();
        breakpoint ++;
        content.revalidate();
        content.repaint();
    }

    public void newOrExistingWindow() {
        clearWindow();
        int breakpoint = 0;
        Dimension loginPanelButtonSize = new Dimension(320, 40);
        breakpoint++;
        int tempButton = 0;

        if (tempButton == 0) {
            existingAccountButton = new JButton("Login to Existing Account");
            newAccountButton = new JButton("Create a new Account");
            editButton = new JButton("Edit Account");
            deleteButton = new JButton("Delete Account");
            editButton.setPreferredSize(loginPanelButtonSize);
        }

        breakpoint ++;
        deleteButton.setPreferredSize(loginPanelButtonSize);
        newAccountButton.setPreferredSize(loginPanelButtonSize);
        existingAccountButton.setPreferredSize(loginPanelButtonSize);
        breakpoint ++;
        JPanel loginPanel = new JPanel();
        loginPanel.add(existingAccountButton);
        breakpoint ++;
        loginPanel.add(newAccountButton);
        loginPanel.add(editButton);
        loginPanel.add(deleteButton);
        breakpoint ++;
        newAccountButton.addActionListener(actionListener);
        editButton.addActionListener(actionListener);
        deleteButton.addActionListener(actionListener);
        breakpoint ++;
        existingAccountButton.addActionListener(actionListener);
        content.add(loginPanel, BorderLayout.CENTER);
        breakpoint ++;
    }

    public void existingLoginWindow() {
        clearWindow();
        int breakpoint = 0;
        JPanel panel = new JPanel();
        breakpoint++;
        JLabel email = new JLabel("Email Address");
        panel.add(email);
        breakpoint ++;
        emailField = new JTextField();
        emailField.setPreferredSize(textField);
        panel.add(emailField);
        breakpoint++;
        JLabel password = new JLabel("Password");
        password.setBounds(100, 55, 70, 20);
        panel.add(password);
        breakpoint++;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(textField);
        panel.add(passwordField);
        breakpoint++;

        int tempButton = 0;
        if (tempButton == 0) {
            existingLoginButton = new JButton("Login");
            breakpoint++;
            existingLoginButton.setPreferredSize(buttonSize);
            breakpoint++;
            existingLoginButton.addActionListener(actionListener);
            panel.add(existingLoginButton);
            breakpoint++;
            content.add(panel);
        }


    }

    public String updateCredentials() {
        String password = new String(passwordField.getPassword());
        return emailField.getText() + "~" + password;
    }

    public void newAccountLoginWindow() {
        clearWindow();
        int breakpoint = 0;
        Dimension textField = new Dimension(300, 30);
        Dimension label = new Dimension(200, 20);
        breakpoint++;
        JPanel panel = new JPanel();
        breakpoint++;
        JLabel email = new JLabel("Email Address");
        panel.add(email, BorderLayout.NORTH);
        breakpoint++;
        emailField = new JTextField();
        emailField.setPreferredSize(textField);
        panel.add(emailField, BorderLayout.NORTH);
        breakpoint++;
        JLabel password = new JLabel("Password");
        password.setPreferredSize(label);
        panel.add(password, BorderLayout.CENTER);
        breakpoint++;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(textField);
        panel.add(passwordField, BorderLayout.CENTER);
        breakpoint++;
        JLabel confirmPassword = new JLabel("Please Confirm Password");
        confirmPassword.setPreferredSize(label);
        panel.add(confirmPassword, BorderLayout.SOUTH);
        breakpoint++;
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(textField);
        panel.add(confirmPasswordField, BorderLayout.SOUTH);
        breakpoint++;
        JPanel exitPanel = new JPanel();
        createNewAccountButton = new JButton("Create new Account");
        createNewAccountButton.addActionListener(actionListener);
        exitPanel.add(createNewAccountButton);
        breakpoint++;
        content.add(panel, BorderLayout.CENTER);
        content.add(exitPanel, BorderLayout.SOUTH);
    }

    public boolean passwordsMatch() {
        int breakpoint = 0;
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        if (password.equals(confirmPassword)) {
            breakpoint++;
            return true;
        } else {
            breakpoint++;
            return false;
        }
    }

    public void editAccountWindow() {
        int breakpoint = 0;
        clearWindow();
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        JPanel bottom = new JPanel();
        breakpoint++;
        JLabel editAccountPrompt = new JLabel("Login with your existing account details in order to edit your account\n");
        JLabel email = new JLabel("Email");
        JLabel oldPassword = new JLabel("Old Password");
        breakpoint++;
        editAccountEmailField = new JTextField();
        editAccountEmailField.setPreferredSize(textField);
        breakpoint++;
        editAccountOldPasswordField = new JPasswordField();
        editAccountOldPasswordField.setPreferredSize(textField);
        editAccountLoginButton = new JButton("Login");
        breakpoint++;
        editAccountLoginButton.setPreferredSize(buttonSize);
        editAccountLoginButton.addActionListener(actionListener);
        top.add(editAccountPrompt, BorderLayout.CENTER);
        middle.add(email, BorderLayout.WEST);
        breakpoint++;
        if (true) {
            middle.add(editAccountEmailField, BorderLayout.CENTER);
            middle.add(oldPassword, BorderLayout.WEST);
            middle.add(editAccountOldPasswordField, BorderLayout.CENTER);
            bottom.add(editAccountLoginButton, BorderLayout.CENTER);
        }
        breakpoint++;
        content.add(top, BorderLayout.NORTH);
        content.add(middle, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

    }

    public String updateEditAccountCredentials() {
        int breakpoint = 0;
        String password = new String(editAccountOldPasswordField.getPassword());
        breakpoint++;
        return editAccountEmailField.getText() + "~" + password;
    }

    public boolean editAccountNewPasswordsMatch() {
        int breakpoint = 0;
        String password = new String(editAccountNewPasswordField.getPassword());
        String password1 = new String(editAccountNewPasswordConfirmField.getPassword());
        if (password.equals(password1)) {
            breakpoint++;
            return true;
        } else {
            breakpoint++;
            return false;
        }
    }

    public String retrieveNewPasswords() {
        int breakpoint = 0;
        String password = new String(editAccountNewPasswordField.getPassword());
        breakpoint++;
        String password1 = new String(editAccountNewPasswordConfirmField.getPassword());
        if (password.equals(password1)) {
            breakpoint++;
            return password;
        } else {
            breakpoint++;
            return null;
        }
    }

    public void editAccountNewPasswordWindow() {
        clearWindow();
        int breakpoint = 0;
        JLabel newPasswordLabel = new JLabel("Enter your new password");
        JLabel passwordLAbel = new JLabel("new password");
        JLabel confirmPassword = new JLabel("Confirm password");
        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        breakpoint++;
        editAccountNewPasswordField = new JPasswordField();
        editAccountNewPasswordConfirmField = new JPasswordField();
        changePasswordButton = new JButton("Change Password");
        changePasswordButton.setPreferredSize(buttonSize);
        breakpoint++;
        changePasswordButton.addActionListener(actionListener);
        editAccountNewPasswordField.setPreferredSize(textField);
        editAccountNewPasswordConfirmField.setPreferredSize(textField);
        breakpoint++;
        topPanel.add(newPasswordLabel);
        middlePanel.add(passwordLAbel);
        middlePanel.add(editAccountNewPasswordField);
        middlePanel.add(confirmPassword);
        middlePanel.add(editAccountNewPasswordConfirmField);
        bottomPanel.add(changePasswordButton);
        breakpoint++;
        content.add(topPanel, BorderLayout.NORTH);
        content.add(middlePanel, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);


    }

    public void deleteAccountWindow() {
        int breakpoint = 0;
        clearWindow();
        JLabel prompt = new JLabel("Enter your email and password");
        JLabel email = new JLabel("Email");
        JLabel password = new JLabel("Password");
        breakpoint++;
        deleteAccountEmailField = new JTextField();
        deleteAccountPasswordField = new JPasswordField();
        deleteAccountPasswordField.setPreferredSize(textField);
        breakpoint++;
        deleteAccountEmailField.setPreferredSize(textField);
        deleteAccountButton = new JButton("Delete");
        deleteAccountButton.addActionListener(actionListener);
        deleteAccountButton.setPreferredSize(buttonSize);
        JPanel top = new JPanel();
        breakpoint++;
        top.add(prompt, BorderLayout.CENTER);
        JPanel middle = new JPanel();
        middle.add(email);
        breakpoint++;
        middle.add(deleteAccountEmailField);
        middle.add(password);
        middle.add(deleteAccountPasswordField);
        JPanel bottom = new JPanel();
        breakpoint++;
        bottom.add(deleteAccountButton, BorderLayout.CENTER);
        content.add(top, BorderLayout.NORTH);
        content.add(middle, BorderLayout.CENTER);
        breakpoint++;
        content.add(bottom, BorderLayout.SOUTH);

    }

    public String updateDeleteAccountCredentials() {
        String password = new String(deleteAccountPasswordField.getPassword());
        return deleteAccountEmailField.getText() + "~" + password;
    }

    // Main Menu
    public void mainMenuWindow() {
        int breakpoint = 0;
        clearWindow();
        if (sellerOrCustomer == 1) {
            JPanel topPanel = new JPanel();
            breakpoint++;
            JLabel mainMenu = new JLabel("Main Menu");
            topPanel.add(mainMenu);
            frame.setTitle("Seller");
            content.setLayout(new BorderLayout());
            client = new MainClient();
            int num1 = 600;
            int num2 = 400;
            frame.setSize(num1, num2);
            frame.setLocationRelativeTo(null);
            breakpoint++;
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            breakpoint++;
            JPanel panelUpper = new JPanel();
            sendMsg = new JButton("Send a Message");
            breakpoint++;
            sendMsg.setPreferredSize(buttonSize);
            breakpoint++;
            sendMsg.addActionListener(actionListener);
            breakpoint++;
            panelUpper.add(sendMsg);
            breakpoint++;
            viewNewMsg = new JButton("View new messages");
            viewNewMsg.setPreferredSize(buttonSize);
            viewNewMsg.addActionListener(actionListener);
            panelUpper.add(viewNewMsg);
            breakpoint++;
            viewAllMsgs = new JButton("View all messages");
            viewAllMsgs.setPreferredSize(buttonSize);
            viewAllMsgs.addActionListener(actionListener);
            panelUpper.add(viewAllMsgs);
            breakpoint++;
            viewCustomers = new JButton("View all customers");
            viewCustomers.setPreferredSize(buttonSize);
            viewCustomers.addActionListener(actionListener);
            panelUpper.add(viewCustomers);
            breakpoint++;
            editMsg = new JButton("Edit a message");
            editMsg.setPreferredSize(buttonSize);
            editMsg.addActionListener(actionListener);
            panelUpper.add(editMsg);
            breakpoint++;
            deleteMsg = new JButton("Delete a message");
            deleteMsg.setPreferredSize(buttonSize);
            deleteMsg.addActionListener(actionListener);
            panelUpper.add(deleteMsg);
            breakpoint++;
            downloadMsgs = new JButton("Download a message");
            downloadMsgs.setPreferredSize(buttonSize);
            downloadMsgs.addActionListener(actionListener);
            panelUpper.add(downloadMsgs);
            breakpoint++;
            blockCust = new JButton("Block a customer");
            blockCust.setPreferredSize(buttonSize);
            blockCust.addActionListener(actionListener);
            panelUpper.add(blockCust);
            breakpoint++;
            manageStores = new JButton("Manage Stores");
            manageStores.setPreferredSize(buttonSize);
            manageStores.addActionListener(actionListener);
            panelUpper.add(manageStores);
            breakpoint++;
            exit = new JButton("Logout");
            exit.setPreferredSize(buttonSize);
            exit.addActionListener(actionListener);
            JPanel panelLower = new JPanel();
            breakpoint++;
            panelLower.add(exit);
            panelLower.add(newMessageAlert);
            if (true) {
                content.add(topPanel, BorderLayout.NORTH);
                content.add(panelUpper, BorderLayout.CENTER);
                content.add(panelLower, BorderLayout.SOUTH);
            }

            breakpoint++;


        }
        else if (sellerOrCustomer == 2) {
            breakpoint++;
            JPanel topPanel = new JPanel();
            JLabel mainMenu = new JLabel("Main Menu");
            topPanel.add(mainMenu);
            frame.setTitle("Customer");
            content.setLayout(new BorderLayout());
            client = new MainClient();
            int num1 = 600;
            int num2 = 400;
            frame.setSize(num1, num2);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            breakpoint++;
            JPanel panelUpper = new JPanel();
            sendMsgCust = new JButton("Send a Message");
            sendMsgCust.setPreferredSize(buttonSize);
            sendMsgCust.addActionListener(actionListener);
            panelUpper.add(sendMsgCust);
            breakpoint++;
            viewNewMsgCust = new JButton("View new messages");
            viewNewMsgCust.setPreferredSize(buttonSize);
            viewNewMsgCust.addActionListener(actionListener);
            panelUpper.add(viewNewMsgCust);
            breakpoint++;
            viewAllMsgsCust = new JButton("View all messages");
            viewAllMsgsCust.setPreferredSize(buttonSize);
            viewAllMsgsCust.addActionListener(actionListener);
            panelUpper.add(viewAllMsgsCust);
            breakpoint++;
            viewStores = new JButton("View all stores");
            viewStores.setPreferredSize(buttonSize);
            viewStores.addActionListener(actionListener);
            panelUpper.add(viewStores);
            breakpoint++;
            viewSellers = new JButton("View all sellers");
            viewSellers.setPreferredSize(buttonSize);
            viewSellers.addActionListener(actionListener);
            panelUpper.add(viewSellers);
            breakpoint++;
            editMsgCust = new JButton("Edit message");
            editMsgCust.setPreferredSize(buttonSize);
            editMsgCust.addActionListener(actionListener);
            panelUpper.add(editMsgCust);
            breakpoint++;
            deleteMsgCust = new JButton("Delete a message");
            deleteMsgCust.setPreferredSize(buttonSize);
            deleteMsgCust.addActionListener(actionListener);
            panelUpper.add(deleteMsgCust);
            breakpoint++;
            downloadMsgsCust = new JButton("Download a message");
            downloadMsgsCust.setPreferredSize(buttonSize);
            downloadMsgsCust.addActionListener(actionListener);
            panelUpper.add(downloadMsgsCust);
            breakpoint++;
            blockSeller = new JButton("Block a seller");
            blockSeller.setPreferredSize(buttonSize);
            blockSeller.addActionListener(actionListener);
            panelUpper.add(blockSeller);
            breakpoint++;
            exit = new JButton("Logout");
            exit.setPreferredSize(buttonSize);
            breakpoint++;
            exit.addActionListener(actionListener);
            breakpoint++;
            JPanel panelLower = new JPanel();
            panelLower.add(newMessageAlert);
            panelLower.add(exit);
            breakpoint++;
            content.add(topPanel, BorderLayout.NORTH);
            content.add(panelUpper, BorderLayout.CENTER);
            content.add(panelLower, BorderLayout.SOUTH);

        }
    }

    // Seller UI Methods
    public void sendMsgFromTextWindow() {
        int breakpoint = 0;
        clearWindow();
        messageToSend = new JTextArea(15, 45);
        messageToSend.setLineWrap(true);
        messageToSend.setEditable(true);
        breakpoint++;
        sendMsgSendButton = new JButton("Send");
        sendMsgSendButton.setPreferredSize(buttonSize);
        sendMsgSendButton.addActionListener(actionListener);
        breakpoint++;
        JPanel sendMessageMiddle = new JPanel();
        JPanel sendMessageBottom = new JPanel();
        sendMessageBottom.add(sendMsgSendButton, BorderLayout.CENTER);
        sendMessageMiddle.add(messageToSend);
        breakpoint++;
        content.add(sendMessageMiddle, BorderLayout.CENTER);
        content.add(sendMessageBottom, BorderLayout.SOUTH);
    }

    public void sendMsgFromFileOrText() {
        clearWindow();
        int breakpoint = 0;
        msgFromFileOrText = new JLabel("Would you like to Enter a message or Import a text file for the message");
        enterTextMsg = new JButton("Enter Message");
        breakpoint++;
        importTextFileMsg = new JButton("Import Text File");
        enterTextMsg.setPreferredSize(buttonSize);
        importTextFileMsg.setPreferredSize(buttonSize);
        breakpoint++;
        enterTextMsg.addActionListener(actionListener);
        importTextFileMsg.addActionListener(actionListener);
        JPanel bottomPanel = new JPanel();
        breakpoint++;
        JPanel panel = new JPanel();
        panel.add(msgFromFileOrText);
        bottomPanel.add(enterTextMsg);
        breakpoint++;
        bottomPanel.add(importTextFileMsg);
        content.add(bottomPanel, BorderLayout.SOUTH);
        content.add(panel, BorderLayout.CENTER);
    }

    public void sendMsgFromTextFileWindow() {
        clearWindow();
        int breakpoint = 0;
        JLabel fileName = new JLabel("Enter your file name");
        fileNameImport = new JTextField(10);
        breakpoint++;
        fileNameImportButton = new JButton();
        fileNameImportButton.addActionListener(actionListener);
        fileNameImportButton.setPreferredSize(buttonSize);
        breakpoint++;
        JPanel top = new JPanel();
        JPanel bottom = new JPanel();
        top.add(fileName);
        breakpoint++;
        top.add(fileNameImport);
        bottom.add(fileNameImportButton, BorderLayout.CENTER);
        content.add(top, BorderLayout.NORTH);
        breakpoint++;
        content.add(bottom, BorderLayout.SOUTH);
    }

    public void viewNewMsgWindow() {
        clearWindow();
        int breakpoint = 0;
        JPanel panel = new JPanel();
        JPanel bottom = new JPanel();
        breakpoint++;
        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(actionListener);
        breakpoint++;
        mainMenuButton.setPreferredSize(buttonSize);
        bottom.add(mainMenuButton);
        breakpoint++;
        newMessage.setColumns(45);
        newMessage.setRows(10);
        breakpoint++;
        newMessage.setLineWrap(true);
        newMessage.setEditable(false);
        panel.add(new JScrollPane(newMessage));
        breakpoint++;
        content.add(panel, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);


    }


    public void viewCustomersWindow() {
        clearWindow();
        int breakpoint = 0;
        JTextArea textArea = new JTextArea(30, 5);
        breakpoint++;
        String[] arrayOfCustomers = listOfCustomers.split("~");
        textArea.setText(String.join("\n", arrayOfCustomers));
        textArea.setEditable(false);
        breakpoint++;
        JScrollPane customerScroll = new JScrollPane(textArea);
        customerScroll.setPreferredSize(new Dimension(450, 110));
        mainMenuButton = new JButton("Okay");
        breakpoint++;
        mainMenuButton.addActionListener(actionListener);
        mainMenuButton.setPreferredSize(buttonSize);
        JPanel panel = new JPanel();
        JPanel bottom = new JPanel();
        breakpoint++;
        bottom.add(mainMenuButton);
        panel.add(customerScroll);
        breakpoint++;
        content.add(panel);
        content.add(bottom, BorderLayout.SOUTH);
    }

    public void editMsgWindow() {
        clearWindow();
        int breakpoint = 0;
        JLabel text = new JLabel("Select the message you would like to edit");
        JLabel text2 = new JLabel("Enter your edited message: ");
        editMessageButton = new JButton("Edit");
        breakpoint++;
        editMessageButton.addActionListener(actionListener);
        editMessageButton.setPreferredSize(buttonSize);
        breakpoint++;
        editedMsgField = new JTextField();
        editedMsgField.setPreferredSize(textField);
        String[] messages = null;
        breakpoint++;
        try {
            messages = reader.readLine().split("~");
            breakpoint++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageDropDown = new JComboBox(messages);
        breakpoint++;
        content.add(messageDropDown, BorderLayout.CENTER);
        breakpoint++;
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        topPanel.add(text);
        breakpoint++;
        bottomPanel.add(text2);
        bottomPanel.add(editedMsgField);
        breakpoint++;
        bottomPanel.add(editMessageButton);
        content.add(topPanel, BorderLayout.NORTH);
        breakpoint++;
        content.add(bottomPanel, BorderLayout.SOUTH);

    }


    public void deleteMsgWindow() {

        clearWindow();
        int breakpoint = 0;
        JLabel text = new JLabel("Select the message you would like to delete");
        deleteMessageButton = new JButton("Delete");
        deleteMessageButton.addActionListener(actionListener);
        deleteMessageButton.setPreferredSize(buttonSize);
        breakpoint++;
        String[] messages = null;
        try {
            messages = reader.readLine().split("~");
            breakpoint++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageDropDown = new JComboBox(messages);
        JPanel middle = new JPanel();
        breakpoint++;
        middle.add(messageDropDown, BorderLayout.CENTER);
        breakpoint++;
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        topPanel.add(text);
        breakpoint++;
        bottomPanel.add(deleteMessageButton);
        content.add(topPanel, BorderLayout.NORTH);
        content.add(middle, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);
        breakpoint++;
        content.add(bottomPanel, BorderLayout.SOUTH);


    }


    public void downloadMsgsWindow() {
        clearWindow();
        JLabel textTop;
        int breakpoint = 0;
        JLabel textBottom;
        downloadMessagesButton = new JButton("Download");
        downloadMessagesButton.addActionListener(actionListener);
        breakpoint++;
        downloadMessagesButton.setPreferredSize(buttonSize);
        breakpoint++;
        textTop = new JLabel("Enter a user to download your conversation with them:");
        breakpoint++;
        usernameToDownloadField = new JTextField(10);
        breakpoint++;
        textBottom = new JLabel("Enter desired file name:");
        filenameToDownloadField = new JTextField(10);
        breakpoint++;
        JPanel Top = new JPanel();
        JPanel Bottom = new JPanel();
        Top.add(textTop);
        Top.add(usernameToDownloadField);
        Bottom.add(textBottom);
        breakpoint++;
        Bottom.add(filenameToDownloadField);
        Bottom.add(downloadMessagesButton);
        content.add(Top, BorderLayout.NORTH);
        content.add(Bottom, BorderLayout.SOUTH);

    }


    public void blockCustWindow() {
        clearWindow();
        int breakpoint = 0;
        JLabel text = new JLabel("Select the email of the customer you'd like to block:");
        blockButtonCust = new JButton("Block");
        breakpoint++;
        blockButtonCust.addActionListener(actionListener);
        blockButtonCust.setPreferredSize(buttonSize);
        JPanel Top = new JPanel();
        breakpoint++;
        JPanel Bottom = new JPanel();
        Top.add(text);
        breakpoint++;
        Top.add(blockCustomerDropDown);
        Bottom.add(blockButtonCust);
        breakpoint++;
        content.add(Top, BorderLayout.NORTH);
        content.add(Bottom, BorderLayout.SOUTH);
    }


    public void manageStoresWindow() {
        int breakpoint = 0;
        clearWindow();
        String y = "Would you like to:\n Open a new store or\n Close an existing store";
        JLabel text = new JLabel(y);
        breakpoint++;
        JPanel Top = new JPanel();
        JPanel Bottom = new JPanel();
        breakpoint++;
        openNewStore = new JButton("New Store");
        closeStore = new JButton("Close Existing Store");
        openNewStore.addActionListener(actionListener);
        breakpoint++;
        closeStore.addActionListener(actionListener);
        openNewStore.setPreferredSize(buttonSize);
        closeStore.setPreferredSize(buttonSize);
        breakpoint++;
        Top.add(text);
        Bottom.add(openNewStore);
        Bottom.add(closeStore);
        breakpoint++;
        content.add(Top, BorderLayout.CENTER);
        content.add(Bottom, BorderLayout.SOUTH);

    }

    public void createNewStoreWindow() {
        int breakpoint = 0;
        clearWindow();
        Dimension textField = new Dimension(200, 30);
        breakpoint++;
        JLabel createNewStoreLabel = new JLabel("Enter the name of the store you would like to create");
        newStoreNameField = new JTextField();
        newStoreNameField.setPreferredSize(textField);
        newStoreNameConfirmButton = new JButton("Create");
        newStoreNameConfirmButton.setPreferredSize(buttonSize);
        newStoreNameConfirmButton.addActionListener(actionListener);
        breakpoint++;
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        JPanel bottom = new JPanel();
        breakpoint++;
        top.add(createNewStoreLabel);
        breakpoint++;
        middle.add(newStoreNameField);
        bottom.add(newStoreNameConfirmButton);
        breakpoint++;
        content.add(top, BorderLayout.NORTH);
        breakpoint++;
        content.add(middle, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);
    }

    public void closeExistingStoreWindow() {
        clearWindow();
        int breakpoint = 0;
        confirmCloseStoreButton = new JButton("Close Store");
        confirmCloseStoreButton.addActionListener(actionListener);
        confirmCloseStoreButton.setPreferredSize(buttonSize);
        breakpoint++;
        JLabel selectStoreLabel = new JLabel("Select the name of the store you would like to close");
        JPanel top = new JPanel();
        JPanel mid = new JPanel();
        JPanel bottom = new JPanel();
        top.add(selectStoreLabel);
        breakpoint++;
        mid.add(usersStoresToClose);
        bottom.add(confirmCloseStoreButton, BorderLayout.CENTER);
        content.add(top, BorderLayout.NORTH);
        breakpoint++;
        content.add(mid, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);
    }
    //Customer UI Methods

    public void viewStoresWindow() {
        clearWindow();
        int breakpoint = 0;
        JTextArea textArea = new JTextArea(30, 5);
        textArea.setEditable(false);
        breakpoint++;
        String[] arrayOfStores = listOfStores.split("~");
        textArea.setText(String.join("\n", arrayOfStores));
        JScrollPane storeScroll = new JScrollPane(textArea);
        breakpoint++;
        storeScroll.setPreferredSize(new Dimension(450, 110));
        mainMenuButton = new JButton("Okay");
        mainMenuButton.addActionListener(actionListener);
        breakpoint++;
        mainMenuButton.setPreferredSize(buttonSize);
        JPanel panel = new JPanel();
        JPanel bottom = new JPanel();
        breakpoint++;
        panel.add(storeScroll);
        bottom.add(mainMenuButton);
        breakpoint++;
        content.add(panel);
        content.add(bottom, BorderLayout.SOUTH);
    }

    public void viewSellersWindow() {
        clearWindow();
        int breakpoint = 0;
        JTextArea textArea = new JTextArea(30, 5);
        textArea.setEditable(false);
        breakpoint++;
        String[] arrayOfStores = listOfStores.split("~");
        textArea.setText(String.join("\n", arrayOfStores));
        JScrollPane storeScroll = new JScrollPane(textArea);
        breakpoint++;
        storeScroll.setPreferredSize(new Dimension(450, 110));
        mainMenuButton = new JButton("Okay");
        mainMenuButton.addActionListener(actionListener);
        mainMenuButton.setPreferredSize(buttonSize);
        breakpoint++;
        JPanel panel = new JPanel();
        JPanel bottom = new JPanel();
        panel.add(storeScroll);
        breakpoint++;
        bottom.add(mainMenuButton);
        content.add(panel);
        content.add(bottom, BorderLayout.SOUTH);
    }

    public void blockSellerWindow() {
        int breakpoint = 0;
        clearWindow();
        JLabel text = new JLabel("Select the email of the seller you'd like to block:");
        blockButtonCust = new JButton("Block");
        breakpoint++;
        blockButtonCust.addActionListener(actionListener);
        blockButtonCust.setPreferredSize(buttonSize);
        JPanel Top = new JPanel();
        breakpoint++;
        JPanel Bottom = new JPanel();
        Top.add(text);
        breakpoint++;
        Top.add(blockSellersDropDown);
        Bottom.add(blockButtonCust);
        breakpoint++;
        content.add(Top, BorderLayout.NORTH);
        content.add(Bottom, BorderLayout.SOUTH);
    }

    @Override
    public void run() {

        content.setLayout(new BorderLayout());
        int breakpoint = 0;
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.toFront();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        breakpoint++;
        welcomeLabel = new JLabel("Marketplace Messenger", SwingConstants.CENTER);
        breakpoint++;
        int num1 = 175;
        welcomeLabel.setBounds(num1, 100, 300, 75);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        content.add(welcomeLabel);
        JPanel welcomePanel = new JPanel();
        breakpoint++;
        customerButton = new JButton("Customer");
        welcomePanel.add(customerButton);
        customerButton.setPreferredSize(buttonSize);
        customerButton.addActionListener(actionListener);
        breakpoint++;
        sellerButton = new JButton("Seller");
        welcomePanel.add(sellerButton);
        sellerButton.setPreferredSize(buttonSize);
        sellerButton.addActionListener(actionListener);
        breakpoint++;
        content.add(welcomeLabel, BorderLayout.NORTH);
        content.add(welcomePanel, BorderLayout.SOUTH);
        breakpoint++;
        frame.setVisible(true);
    }
}
