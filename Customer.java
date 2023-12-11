import java.io.*;
import java.util.ArrayList;


public class Customer extends User{
	private boolean invisible;
    private static final String sellersFile = "sellers.txt";

    private boolean blocked;
    private boolean unseen;
    private static final String customersFile = "customers.txt";
    private static ArrayList<Customer> listOfCustomers = new ArrayList<>();
    private static ArrayList<String> listofUsers = new ArrayList<>();

    private static final String blockedSellersFile = "blockedSellers.txt";

    private static final String unblockedSellersFile = blockedSellersFile + ".txt";


    private static final Object blockSync = new Object();


    public Customer (String email, String password) {
        super(email, password);
        listOfCustomers.add(this);
        this.invisible = false;
        this.blocked = this.unseen = false;

    }

    public boolean isInvisible() {
        System.out.println("done");
        return invisible;
    }

    public void setBlocked(boolean blocked) {
        System.out.println("done");
        this.blocked = blocked;
    }

    public static String getCustomersFile() {
        return customersFile;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isBlocked() {
        return blocked;
    }



    // Returns arraylist of customer emails
    public static ArrayList<String> getCustomers() {
        int breakpoint = 0;
        File file = new File(customersFile);
        if (!file.exists()) {
            createFile(customersFile);
            breakpoint++;
        }
        ArrayList<String> customers = new ArrayList<>();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            breakpoint++;
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                customers.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            breakpoint++;
            return null;
        }
        return customers;
    }
    
    public static Customer findCustomer(String email) {
        int breakpoint = 0;
        Customer customer = null;
        boolean flag = false;
        File f = new File("customers.txt");
        breakpoint++;
        // ensuring that the email that is passed through the method is a seller
        try {
            breakpoint++;
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                if (line.equals(email)) {
                    breakpoint++;
                    flag = true;
                }
                line = br.readLine();
                breakpoint++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag) {
            File usernames = new File("UsernameAndPasswords.txt");
            try {
                breakpoint++;
                BufferedReader br = new BufferedReader(new FileReader(usernames));
                String line = br.readLine();
                while (line != null) {
                    breakpoint++;
                    if (line.substring(0, line.indexOf("~")).equals(email)) {
                        breakpoint++;
                        customer = new Customer(email, line.substring(line.indexOf("~")) + 1);
                    }
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                breakpoint++;
                e.printStackTrace();
            }
        }
        return customer;
    }


    public void blockedSellersFileWrite(ArrayList<String> blockedSellers, Boolean notAlreadyWritten) {
        int breakpoint = 0;
        synchronized (blockSync) {
            File file = new File(blockedSellersFile);
            if (!file.exists()) {
                breakpoint++;
                createFile(blockedSellersFile);
            }
            try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
                breakpoint++;
                for (String line : blockedSellers) {
                    breakpoint++;
                    bw.write(line);
                    bw.newLine();
                }
                bw.flush();
            } catch (IOException e) {
                breakpoint++;
                e.printStackTrace();
            }
        }

        if (notAlreadyWritten) {
            breakpoint++;
            ArrayList<String> transfer = Seller.findSeller(blockedSellers.get(blockedSellers.size() - 1)).blockedCustomersFileRead();
            transfer.add(this.getEmail());
            breakpoint++;
            Seller.findSeller(blockedSellers.get(blockedSellers.size() - 1)).blockedCustomersFileWrite(transfer, false);
        }
    }

    public ArrayList<String> blockedSellersFileRead() {
        int breakpoint = 0;
        ArrayList<String> sellersBlocked = new ArrayList<>();
        breakpoint++;
        File f = new File(blockedSellersFile);
        if (!f.exists()) {
            breakpoint++;
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                sellersBlocked.add(line);
                breakpoint++;
                line = br.readLine();
                breakpoint++;
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sellersBlocked;
    }
}
