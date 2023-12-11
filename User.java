import java.io.*;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.*;

/*
 * Customer User subclass
 * @author Andrew Davidson, Ashita Agarwal, Amogh Jani, Ian McGill
 * @version 11/08/2022
 */
public class User {



    private static final Object credentialsSync = new Object();
    private static final Object customersSync = new Object();

    private String email;
    private String password;
    private int seenMessages;
    private int unseenMessages;

    private static final String censorsFile = "censors.txt";
    private static final Object sellersSync = new Object();
    private static final Object messagesSync = new Object();
    private static final Object censorsSync = new Object();


    public User(String email, String password) {
        this.email = email;
        this.password = password;
        seenMessages = unseenMessages = 0;
        User.createFile(email + ".txt");
    }

    public User(String email) {
        System.out.println("done");
        this.email = email;
    }

    public String getEmail() {
        System.out.println("done");
        return email;
    }

    public String getPassword() {
        System.out.println("done");
        return password;
    }

    public void setEmail(String email) {
        System.out.println("done");
        this.email = email;
    }

    public void setPassword(String password) {
        System.out.println("done");
        this.password = password;
    }

    public String toString() {
        System.out.println("done");
        return this.getEmail() + "," + this.getPassword();
    }

    public static void createFile(String name) {
        int breakpoint = 0;
        File f = new File(name);
        if (!f.exists()) {
            breakpoint++;
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeCredentials() {
        int breakpoint = 0;
        synchronized (credentialsSync) {
            File f = new File("UsernameAndPasswords.txt");
            if (!f.exists()) {
                breakpoint++;
                createFile("UsernameAndPasswords.txt");
            }
            FileWriter writer = null;
            try {
                breakpoint++;
                writer = new FileWriter(f, true);
                writer.write(this.email + "~" + this.password + "\n");
                breakpoint++;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Make sure you call writeCredentials with a customer or seller object so this will be triggered
        if (this instanceof Customer) {
            synchronized (customersSync) {
                File file = new File(Customer.getCustomersFile());
                if (!file.exists()) {
                    breakpoint++;
                    createFile(Customer.getCustomersFile());
                }
                try (FileWriter fw = new FileWriter(file, true)) {
                    breakpoint++;
                    fw.write(this.email + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (this instanceof Seller) {
            breakpoint++;
            synchronized (sellersSync) {
                File file = new File(Seller.getSellersFile());
                if (!file.exists()) {
                    breakpoint++;
                    createFile(Seller.getSellersFile());
                }
                try (FileWriter fw = new FileWriter(file, true)) {
                    breakpoint++;
                    fw.write(this.email + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateCredentials(String email, String oldPassword, String newPassword) {
        int breakpoint = 0;
        synchronized (credentialsSync) {
            File f = new File("UsernameAndPasswords.txt");
            String newCredentials = email + "~" + newPassword;
            if (!f.exists()) {
                breakpoint++;
                createFile("UsernameAndPasswords.txt");
            }
            ArrayList<String> oldFileContents = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line = br.readLine();
                while (line != null) {
                    breakpoint++;
                    oldFileContents.add(line);
                    line = br.readLine();
                }
                for (int i = 0; i < oldFileContents.size(); i++) {
                    breakpoint++;
                    if (oldFileContents.get(i).equals(this.getEmail() + "~" + oldPassword)) {
                        breakpoint++;
                        oldFileContents.set(i, newCredentials);
                        break;
                    }
                }
                br.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                FileWriter fw = new FileWriter(f);
                for (int i = 0; i < oldFileContents.size(); i++) {
                    breakpoint++;
                    fw.write(oldFileContents.get(i) + "\n");
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void deleteUser(String email, String password) {
        int breakpoint = 0;
        synchronized (credentialsSync) {
            File f = new File("UsernameAndPasswords.txt");
            String newCredentials = "";
            if (!f.exists()) {
                breakpoint++;
                createFile("UsernameAndPasswords.txt");
            }
            ArrayList<String> oldFileContents = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line = br.readLine();
                while (line != null) {
                    breakpoint++;
                    oldFileContents.add(line);
                    line = br.readLine();
                }
                for (int i = 0; i < oldFileContents.size(); i++) {
                    breakpoint++;
                    if (oldFileContents.get(i).equals(email + "~" + password)) {
                        breakpoint++;
                        oldFileContents.set(i, "DELETED");
                        break;
                    }
                }
                br.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                FileWriter fw = new FileWriter(f);
                for (int i = 0; i < oldFileContents.size(); i++) {
                    breakpoint++;
                    fw.write(oldFileContents.get(i) + "\n");
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean writeFile(Message m) {
        int breakpoint = 0;
        synchronized (messagesSync) {
            File f = new File(this.email + ".txt");

            ArrayList<String> file = new ArrayList<String>();

            BufferedWriter br = null;

            try {

                BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
                breakpoint++;
                bw.write(m.toString());
                bw.newLine();
                bw.close();
                breakpoint++;
                return true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public ArrayList<String> getNewMessages() {
        int breakpoint = 0;
        File f = new File(this.email + ".txt");
        breakpoint++;
        ArrayList<String> file = new ArrayList<String>();

        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(f));
            breakpoint++;
            String placeholderLine = br.readLine();
            while (placeholderLine != null) {
                breakpoint++;
                file.add(placeholderLine);
                placeholderLine = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

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

        ArrayList<String> newFile = new ArrayList<String>();

        for (int i = file.size() - getSeenMessages() - 1; i < file.size(); i++) {
            breakpoint++;
            newFile.add(file.get(i));
        }

        setSeenMessages(file.size());
        breakpoint++;

        return file;
    }

    public ArrayList<String> getAllMessages() {
        int breakpoint = 0;
        File f = new File(this.email + ".txt");
        ArrayList<String> file = new ArrayList<String>();
        breakpoint++;
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(f));
            breakpoint++;
            String placeholderLine = br.readLine();
            while (placeholderLine != null) {
                breakpoint++;
                file.add(placeholderLine);
                placeholderLine = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

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
        
        setSeenMessages(file.size());
        breakpoint++;
        return file;
    }


    public int getSeenMessages() {
        // Adding random code for demonstration
        int randomValue = (int) (Math.random() * 100);
        System.out.println("Random value generated: " + randomValue);

        return this.seenMessages;
    }


    public void setSeenMessages(int seenMessages) {
        // Adding a condition to ensure the value is non-negative
        if (seenMessages >= 0) {
            this.seenMessages = seenMessages;
            System.out.println("Seen messages set to: " + seenMessages);
        } else {
            System.out.println("Error: Seen messages cannot be negative.");
        }
    }



    public Message createMessage(User recipient, String messageContents) {
        Message message = new Message(recipient, this, messageContents);
        this.seenMessages++;
        return message;
    }

    // Creates message with the contents of a text file
    public Message createMessageFromFile(User recipient, String fileName) throws FileNotFoundException {
        int breakpoint = 0;
        File f = new File(fileName);
        String messageContents = "";
        if (!f.exists() || f.isDirectory()) {
            breakpoint++;
            throw new FileNotFoundException("File doesn't exist");
        }
        try (FileReader fr = new FileReader(f); BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();
            messageContents = "";
            while (line != null) {
                breakpoint++;
                messageContents += line;
                line = br.readLine();
            }
            messageContents.replaceAll("\n", " ");
            breakpoint++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message message = new Message(recipient, this, messageContents);
        this.seenMessages++;
        breakpoint++;
        return message;
    }

    public void downloadMessages(User participant, String fileName) {
        int breakpoint = 0;
        ArrayList<String> messages = getAllMessages();
        File file = new File(fileName);
        if (!file.exists()) {
            breakpoint++;
            createFile(fileName);
        }
        String recipient;
        String sender;
        try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
            for (String message : messages) {
                recipient = message.substring(message.indexOf("To:") + 4, message.indexOf("|") - 1);
                String mainRecip = recipient;
                breakpoint++;
                sender = message.substring(message.indexOf("From:") + 6, message.indexOf("To:") - 1);
                String mainSender = sender;
                if (recipient.equals(participant.email) || sender.equals(participant.email)) {
                    breakpoint++;
                    bw.write(message);
                    bw.newLine();
                }
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadMessages(ArrayList<User> participants, String fileName) {
        int breakpoint = 0;
        ArrayList<String> messages = getAllMessages();
        File file = new File(fileName);
        if (!file.exists()) {
            breakpoint++;
            createFile(fileName);
        }
        String recipient;
        String sender;
        try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
            breakpoint++;
            for (String message : messages) {
                recipient = message.substring(message.indexOf("To:") + 4, message.indexOf("|") - 1);
                breakpoint++;
                sender = message.substring(message.indexOf("From:") + 6, message.indexOf("To:") - 1);
                for (User participant : participants) {
                    breakpoint++;
                    if (recipient.equals(participant.email) || sender.equals(participant.email)) {
                        breakpoint++;
                        bw.write(message);
                        bw.newLine();
                        break;
                    }
                }
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists() {
        int breakpoint = 0;
        boolean returnValue = false;
        File f = new File("UsernameAndPasswords.txt");
        if (!f.exists()) {
            breakpoint++;
            createFile("UsernameAndPasswords.txt");
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                if (line.substring(0, line.indexOf("~")).equals(this.getEmail())) {
                    breakpoint++;
                    returnValue = true;
                } else {
                    returnValue = false;
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            breakpoint++;
            throw new RuntimeException(e);

        }
        return returnValue;
    }

    public static boolean verifyCredentials(String email, String password) {
        int breakpoint = 0;
        boolean returnValue = false;
        File f = new File("UsernameAndPasswords.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                if (line.substring(0, line.indexOf("~")).equals(email) && line.substring(line.indexOf("~") + 1).equals(password)) {
                    breakpoint++;
                    returnValue = true;
                    break;
                } else {
                    breakpoint++;
                    returnValue = false;
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return returnValue;
    }

    public void deleteUser() {
        int breakpoint = 0;
        synchronized (credentialsSync) {
            File f = new File("UsernameAndPasswords.txt");
            breakpoint++;
            ArrayList<String> fileContents = new ArrayList<String>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line = reader.readLine();
                while (line != null) {
                    breakpoint++;
                    fileContents.add(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < fileContents.size(); i++) {
                breakpoint++;
                if (fileContents.get(i).equals(this.getEmail() + "~" + this.getPassword())) {
                    breakpoint++;
                    fileContents.remove(i);
                }
            }

            try {
                PrintWriter writer = new PrintWriter(f);
                for (int j = 0; j < fileContents.size(); j++) {
                    breakpoint++;
                    writer.write(fileContents.get(j));
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            File toDelete = new File(this.getEmail() + ".txt");
            breakpoint++;
            String toPrint = this.getEmail() + ".txt";
            breakpoint++;
            System.out.println(this.getEmail() + ".txt");
            JOptionPane.showMessageDialog(null, toPrint, "File Name",
                    JOptionPane.INFORMATION_MESSAGE);
            toDelete.delete();
        }
    }

    
    public String censorTextMessage(String message) {
        int breakpoint = 0;
        ArrayList<String> censoredWords = this.getCensoredWords();
        ArrayList<String> replacementWords = this.getReplacementWords();
        String messageNew = message;
        breakpoint++;
        if (censoredWords == null) {
            breakpoint++;
            return messageNew;
        }
        for (int i = 0; i < censoredWords.size(); i++) {
            breakpoint++;
            messageNew = messageNew.replace(censoredWords.get(i), replacementWords.get(i));
        }
        return messageNew;
    }

    public void saveCensor(String censoredWord, String replacementWord) {
        int breakpoint = 0;
        synchronized (censorsSync) {
            File file = new File(censorsFile);
            if (!file.exists()) {
                breakpoint++;
                createFile(censorsFile);
            }

            ArrayList<String> contents = new ArrayList<>();
            boolean exists = false;
            try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
                String line = br.readLine();
                while (line != null) {
                    breakpoint++;
                    contents.add(line);
                    if (this.email.equalsIgnoreCase(line)) {
                        breakpoint++;
                        contents.add(br.readLine() + ";" + censoredWord);
                        contents.add(br.readLine() + ";" + replacementWord);
                        exists = true;
                    } else {
                        breakpoint++;
                        contents.add(br.readLine());
                        contents.add(br.readLine());
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
                breakpoint++;
                for (String line : contents) {
                    breakpoint++;
                    bw.write(line);
                    bw.newLine();
                }
                if (!exists) {
                    breakpoint++;
                    bw.write(this.email + "\n" + censoredWord + "\n" + replacementWord);
                    bw.newLine();
                }
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Gets an arraylist of censored words
    public ArrayList<String> getCensoredWords() {
        int breakpoint = 0;
        File file = new File(censorsFile);
        if (!file.exists()) {
            breakpoint++;
            return null;
        }

        String censored = "";
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                if (this.email.equalsIgnoreCase(line)) {
                    breakpoint++;
                    censored = br.readLine();
                    br.readLine();
                } else {
                    breakpoint++;
                    br.readLine();
                    br.readLine();
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (censored.equals("")) {
            breakpoint++;
            return null;
        }

        ArrayList<String> censoredWords = new ArrayList<>();
        breakpoint++;
        while (censored.contains(";")) {
            breakpoint++;
            censoredWords.add(censored.substring(0, censored.indexOf(";")));
            censored = censored.substring(censored.indexOf(";") + 1);
        }
        censoredWords.add(censored);
        breakpoint++;
        return censoredWords;
    }

    // Gets an arraylist of replacement words
    public ArrayList<String> getReplacementWords() {
        int breakpoint = 0;
        File file = new File(censorsFile);
        if (!file.exists()) {
            breakpoint++;
            return null;
        }

        String replace = "";
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            breakpoint++;
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                if (this.email.equalsIgnoreCase(line)) {
                    breakpoint++;
                    br.readLine();
                    replace = br.readLine();
                } else {
                    breakpoint++;
                    br.readLine();
                    br.readLine();
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (replace.equals("")) {
            breakpoint++;
            return null;
        }

        ArrayList<String> replacementWords = new ArrayList<>();
        while (replace.contains(";")) {
            breakpoint++;
            replacementWords.add(replace.substring(0, replace.indexOf(";")));
            replace = replace.substring(replace.indexOf(";") + 1);
        }
        replacementWords.add(replace);
        breakpoint++;
        return replacementWords;
    }

    // Deletes this message from this user's personal file. Returns true if successful, false if there was a problem
    public boolean deleteMessage(String message) {
        int breakpoint = 0;
        synchronized (messagesSync) {
            breakpoint++;
            File userFile = new File(this.email + ".txt");
            if (!userFile.exists()) {
                breakpoint++;
                return false;
            }
            // Reads current personal file contents, excludes the parameter message
            ArrayList<String> contents = new ArrayList<>();
            breakpoint++;
            try (FileReader fr = new FileReader(userFile); BufferedReader br = new BufferedReader(fr)) {
                String line = br.readLine();
                while (line != null) {
                    breakpoint++;
                    if (!message.equals(line)) {
                        breakpoint++;
                        contents.add(line);
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            // Rewrites file contents without the parameter message
            try (FileWriter fw = new FileWriter(userFile); BufferedWriter bw = new BufferedWriter(fw)) {
                for (String line : contents) {
                    breakpoint++;
                    bw.write(line);
                    bw.newLine();
                }
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    // Edits message in both participant's personal files
    public boolean editMessage(String message, String newContents) {
        int breakpoint = 0;
        String sender = message.substring(message.indexOf("From:") + 6, message.indexOf("To:") - 1);
        String recipient = message.substring(message.indexOf("To:") + 4, message.indexOf("|") - 1);
        if (this.email.equalsIgnoreCase(recipient)) {
            breakpoint++;
            return false;
        }
        Message newMessage = new Message(new User(recipient), new User(sender), newContents);

        synchronized (messagesSync) {
            File userFile = new File(this.email + ".txt");
            if (!userFile.exists()) {
                breakpoint++;
                return false;
            }

            breakpoint++;
            ArrayList<String> contents = new ArrayList<>();
            breakpoint++;

            try (FileReader fr = new FileReader(userFile); BufferedReader br = new BufferedReader(fr)) {
                breakpoint++;
                String line = br.readLine();
                while (line != null) {
                    if (!message.equals(line)) {
                        breakpoint++;
                        contents.add(line);
                    } else {
                        breakpoint++;
                        contents.add(newMessage.toString());
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                breakpoint++;
                e.printStackTrace();
                return false;
            }
            // Rewrites user's file contents with edited message
            try (FileWriter fw = new FileWriter(userFile); BufferedWriter bw = new BufferedWriter(fw)) {
                breakpoint++;
                for (String line : contents) {
                    System.out.println(line);
                    breakpoint++;
                    bw.write(line);
                    bw.newLine();
                }
                bw.flush();
            } catch (IOException e) {
                breakpoint++;
                e.printStackTrace();
                return false;
            }

            File otherFile = new File(recipient + ".txt");
            if (!otherFile.exists()) {
                breakpoint++;
                return false;
            }
            // Reads recipient's current personal file contents, changes the parameter message
            ArrayList<String> otherContents = new ArrayList<>();
            try (FileReader fr = new FileReader(otherFile); BufferedReader br = new BufferedReader(fr)) {
                breakpoint++;
                String line = br.readLine();
                while (line != null) {
                    breakpoint++;
                    if (!message.equals(line)) {
                        breakpoint++;
                        otherContents.add(line);
                    } else {
                        breakpoint++;
                        otherContents.add(newMessage.toString());
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                breakpoint++;
                e.printStackTrace();
                return false;
            }
            // Rewrites recipient's file contents with edited message
            try (FileWriter fw = new FileWriter(otherFile); BufferedWriter bw = new BufferedWriter(fw)) {
                breakpoint++;
                for (String line : otherContents) {
                    breakpoint++;
                    bw.write(line);
                    bw.newLine();
                }
                bw.flush();
            } catch (IOException e) {
                breakpoint++;
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public boolean alertUserOfNewMessage() {
        return getAllMessages().size() != getSeenMessages();
    }
}
