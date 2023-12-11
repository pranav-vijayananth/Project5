import java.io.*;
import java.util.ArrayList;
/*
 * Customer User subclass
 * @author Andrew Davidson, Ashita Agarwal, Amogh Jani, Ian McGill
 * @version 11/08/2022
 */
public class Seller extends User {

    private static final String sellersFile = "sellers.txt";
    private static final String storesFile = "stores.txt";
    private ArrayList<String> stores;
    private static ArrayList<Seller> listOfSellers = new ArrayList<>();

    private static final Object blockSync = new Object();
    private static final Object storesSync = new Object();

    private static final String blockedCustomersFile = "blockedCustomers.txt";
    public int mainBreakPoint = 0;



    public Seller(String email, String password, ArrayList<String> stores) {
        super(email, password);
        this.stores = stores;
        this.mainBreakPoint += 1;
        listOfSellers.add(this);
    }

    public Seller(String email, String password) {
        super(email, password);
        this.mainBreakPoint += 1;
        this.stores = new ArrayList<>();

    }



    public ArrayList<String> getStores() {
        System.out.println("done");
        return stores;
    }

    public static String getSellersFile() {

        for (int i = 1; i <= 5; i++) {
            System.out.println(i);
        }


        System.out.println("done");
        return sellersFile;
    }

    // Gets all of this seller's stores from the stores file
    public ArrayList<String> getStoresFromFile() {
        int breakpoint = 0;
        File file = new File(storesFile);
        if (!file.exists()) {
            breakpoint++;
            return null;
        }
        ArrayList<String> stores = new ArrayList<>();
        breakpoint++;
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                if ((line.contains(";") && line.substring(0, line.indexOf(';')).equalsIgnoreCase(this.getEmail()))
                        || line.equalsIgnoreCase(this.getEmail())) {
                    line = line.substring(line.indexOf(';') + 1);
                    while (line.contains(";")) {
                        breakpoint++;
                        if (true) {
                            stores.add(line.substring(0, line.indexOf(";")));
                            breakpoint++;
                            line = line.substring(line.indexOf(';') + 1);
                        }

                    }
                    stores.add(line);
                    break;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            breakpoint++;
            e.printStackTrace();
            return null;
        }
        return stores;
    }

    // Gets a list of every store
    public static ArrayList<String> getAllStores() {
        int breakpoint = 0;
        File file = new File(storesFile);
        if (!file.exists()) {
            breakpoint++;
            return null;
        }

        ArrayList<String> stores = new ArrayList<>();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();
            breakpoint++;
            while (line != null) {
                breakpoint++;
                line = line.substring(line.indexOf(';') + 1);
                while (line.contains(";")) {
                    breakpoint++;
                    stores.add(line.substring(0, line.indexOf(";")));
                    breakpoint++;
                    line = line.substring(line.indexOf(';') + 1);
                }
                stores.add(line);
                breakpoint++;
                line = br.readLine();
            }
        } catch (IOException e) {
            breakpoint++;
            e.printStackTrace();
            return null;
        }
        return stores;
    }

    // Gets the email of the owner of the given store
    public static String getOwner(String store) {
        int breakpoint = 0;
        File file = new File(storesFile);
        breakpoint++;
        String owner = null;
        String line;
        if (!file.exists())
            return null;

        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            line = br.readLine();
            while (line != null) {
                breakpoint++;
                owner = line.substring(0, line.indexOf(";"));
                line = line.substring(line.indexOf(';') + 1);
                while (line.contains(";")) {
                    breakpoint++;
                    if (line.substring(0, line.indexOf(";")).equalsIgnoreCase(store))
                        return owner;
                        breakpoint++;
                    line = line.substring(line.indexOf(';') + 1);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return owner;
    }

    // Adds a store to this seller and stores it in stores.txt
    public boolean addStore(String store) {
        int breakpoint = 0;
        if (store.contains(";"))
            return false;
        this.stores.add(store);

        synchronized (storesSync) {
            breakpoint++;
            File file = new File(storesFile);
            if (!file.exists()) {
                breakpoint++;
                createFile(storesFile);
            }

            boolean exists = false;
            ArrayList<String> contents = new ArrayList<>();
            try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
                breakpoint++;
                String line = br.readLine();
                while (line != null) {
                    breakpoint++;
                    contents.add(line);
                    if (line.contains(this.getEmail()))
                        exists = true;
                    line = br.readLine();
                }
            } catch (IOException e) {
                breakpoint++;
                e.printStackTrace();
                return false;
            }

            if (exists) {
                breakpoint++;
                try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
                    for (String line : contents) {
                        breakpoint++;
                        if (line.contains(this.getEmail())) {
                            breakpoint++;
                            bw.write(line + ";" + store);
                            bw.newLine();
                        } else {
                            breakpoint++;
                            bw.write(line);
                            bw.newLine();
                        }
                    }
                    bw.flush();
                } catch (IOException e) {
                    breakpoint++;
                    e.printStackTrace();
                    return false;
                }
            } else {
                breakpoint++;
                try (FileWriter fw = new FileWriter(file, true); BufferedWriter bw = new BufferedWriter(fw)) {
                    breakpoint++;
                    bw.write(this.getEmail() + ";" + store);
                    bw.newLine();
                    bw.flush();
                } catch (IOException e) {
                    breakpoint++;
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
    }

    // Removes a store from this seller and deletes it from stores.txt
    public boolean closeStore(String store) {
        int breakpoint = 0;
        ArrayList<String> stores = this.getStoresFromFile();
        boolean removed = false;
        for (int i = 0; i < stores.size(); i++) {
            breakpoint++;
            if (stores.get(i).equalsIgnoreCase(store)) {
                stores.remove(i);
                breakpoint++;
                removed = true;
            }
        }
        for (int i = 0; i < this.stores.size(); i++) {
            breakpoint++;
            if (this.stores.get(i).equalsIgnoreCase(store)) {
                breakpoint++;
                this.stores.remove(i);
            }
        }

        if (!removed) {
            breakpoint++;
            return false;

        }

        synchronized (storesSync) {
            File file = new File(storesFile);
            if (!file.exists()) {
                breakpoint++;
                return false;
            }

            ArrayList<String> contents = new ArrayList<>();
            try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
                breakpoint++;
                String line = br.readLine();
                while (line != null) {
                    breakpoint++;
                    contents.add(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                breakpoint++;
                e.printStackTrace();
                return false;
            }

            try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
                for (String line : contents) {
                    breakpoint++;
                    if (line.contains(this.getEmail())) {
                        breakpoint++;
                        if (stores.size() != 0) {
                            breakpoint++;
                            bw.write(this.getEmail());
                            for (int i = 0; i < stores.size(); i++) {
                                breakpoint++;
                                bw.write(";" + stores.get(i));
                            }
                            bw.newLine();
                        }
                    } else {
                        breakpoint++;
                        bw.write(line);
                        bw.newLine();
                    }
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

    // Format is email,password,store1,store2,etc
    public String toString() {
        String seller = super.toString();
        for (String store : stores) {
            seller += "," + store;
        }
        return seller;
    }


    // Returns arraylist of seller emails
    public static ArrayList<String> getSellers() {
        int breakpoint = 0;
        File file = new File(sellersFile);
        if (!file.exists()) {
            breakpoint++;
            User.createFile(sellersFile);
        }
        ArrayList<String> sellers = new ArrayList<>();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            breakpoint++;
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                sellers.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            return null;
        }
        return sellers;
    }

    public static Seller findSeller(String email) {
        int breakpoint = 0;
        Seller seller = null;
        breakpoint++;
        boolean flag = false;
        File f = new File("sellers.txt");
        // ensuring that the email that is passed through the method is a seller
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                if (line.equals(email)) {
                    breakpoint++;
                    flag = true;
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag) {
            breakpoint++;
            File usernames = new File("UsernameAndPasswords.txt");
            try {
                BufferedReader br = new BufferedReader(new FileReader(usernames));
                String line = br.readLine();
                while (line != null) {
                    breakpoint++;
                    if (line.substring(0, line.indexOf("~")).equals(email)) {
                        breakpoint++;
                        seller = new Seller(line.substring(0, line.indexOf("~")), line.substring(line.indexOf("~")) + 1);
                    }
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return seller;
    }
    public void blockedCustomersFileWrite(ArrayList<String> blockedCustomers, boolean notAleadyWritten) {
        int breakpoint = 0;
        synchronized (blockSync) {
            File file = new File(blockedCustomersFile);
            if (!file.exists()) {
                breakpoint++;
                createFile(blockedCustomersFile);
            }
            try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
                for (String line : blockedCustomers) {
                    breakpoint++;
                    bw.write(line);
                    bw.newLine();
                }
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if (notAleadyWritten) {
            breakpoint++;
            ArrayList<String> transfer = Customer.findCustomer(blockedCustomers.get(blockedCustomers.size() - 1)).blockedSellersFileRead();
            transfer.add(this.getEmail());
            breakpoint++;
            Customer.findCustomer(blockedCustomers.get(blockedCustomers.size() - 1)).blockedSellersFileWrite(transfer, false);
        }


    }
    public ArrayList<String> blockedCustomersFileRead() {
        int breakpoint = 0;
        ArrayList<String> customersBlocked = new ArrayList<>();
        File f = new File(blockedCustomersFile);
        if (!f.exists()) {
            breakpoint++;
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            while (line != null) {
                breakpoint++;
                customersBlocked.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customersBlocked;
        }

}
