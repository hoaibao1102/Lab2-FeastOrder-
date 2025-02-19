/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bussiness;

import Model.Customer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSI PC
 */
public class Customers extends ArrayList<Customer>{
    private String pathFile;
    private boolean save;
    private final String HEADER_TABLE = ("------------------------------------------------------------------------\n"
                                       + "|Customer ID | Customer Name      | Phone      |Email                  |\n"
                                       + "------------------------------------------------------------------------"),
                         FOOTER_TABLE = ("------------------------------------------------------------------------\n");

    public Customers(){
        super();
        this.pathFile = "./customers.dat";  
        this.save= true;
        readFromFile();
    }
    
    public boolean isSave() {
        return save;
    }
    
    /***
     * kiểm tra khách hàng đã đki chưa nếu có thì thông báo
     * còn chưa tồn tại thì thêm khách hàng vào list
     */
    public void addNew(Customer x) {
        if (!checkExist(x)) {
            this.add(x);
            save = false;
            saveToFile();
        }
        else System.out.println("The customer already exists in the Register List");
        System.out.println();
    }
    
    /**
     *kiểm tra khách hàng đã đki chưa nếu chưa thì thông báo 
     *nếu có rồi thì tiến hành update cho khách hàng(không update id khách hàng)
     */
    public void updateInfoCutomer(Customer newCus){
        Customer oldCus = searchByCode(newCus.getCode());
        if(oldCus == null){
            System.out.println("Customers are not registered yet.");
        }else {
            oldCus.setName(newCus.getName().length() > 0 ? newCus.getName() : oldCus.getName());
            oldCus.setPhone(newCus.getPhone().length() > 0 ? newCus.getPhone() : oldCus.getPhone());
            oldCus.setEmail(newCus.getEmail().length() > 0 ? newCus.getEmail() : oldCus.getEmail());
            System.out.println("Customer updated successfully");
            this.save = false;
        }
    }
    
    public void displayAll(){
        displayAll(this);
    }
    //display bảng khách hàng đã đăng kí
    public void displayAll(List<Customer>cs){
        System.out.println(HEADER_TABLE);
        for (Customer i : cs) {
            System.out.println(i);
            System.out.print(FOOTER_TABLE);
        }
    }
    
    /**
     * tại sẵn 1 list mới để chưa danh sách kiếm được
     * tìm kiếm khách hàng bằng tên 
     * nếu tồn tại thêm vào danh sách vừa tạo 
     * sau đó trình chiếu danh sách khách hàng có cùng tên tìm kiếm
     * CHÚ Ý : nên dùng hàm contains khi tìm kiếm
     */
    public void searchByName(String name) {
        List<Customer> matchingName = new ArrayList<>();
        for (Customer i : this) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) {
                matchingName.add(i);
            }
        }
        if(matchingName.isEmpty()){
            System.out.println("No one matches the search criteria!");
        }else{
            System.out.println(HEADER_TABLE);
            for (Customer customer : matchingName) {
                System.out.println(customer);
                System.out.print(FOOTER_TABLE);
            }
            
        }
    }
    
    // đọc file 
    public void readFromFile() {
        FileInputStream fis = null;
        try {
            File f = new File(pathFile);

            if (!f.exists()) {
                System.out.println("file not found...");
                return;
            }
            //tạo đối tượng đọc dữ liệu
            fis = new FileInputStream(f);
            //tạo ois để đọc dữ liệu từ file
            ObjectInputStream ois = new ObjectInputStream(fis);
            //tạo vòng lặp để đọc dữ liệu
            while (fis.available() > 0) {
                Customer c = (Customer)ois.readObject();
                this.add(c);
            }
            ois.close();
            this.save = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                 if (fis != null) fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // lưu file
    public void saveToFile() {
        FileOutputStream fos = null; 
        try {
            // 1. Thu thập dữ liệu đăng ký hiện tại
            File f = new File(this.pathFile);

            // 2. Mở luồng file output
            fos = new FileOutputStream(f);

            // 3. Tạo object output stream để tuần tự hóa dữ liệu
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // 4. Ghi dữ liệu tuần tự hóa vào file
            for (Customer i : this) {
                oos.writeObject(i);
            }
            oos.close();
            this.save = true;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                // Đóng tất cả luồng để giải phóng tài nguyên
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // tìm kiếm khách hàng bằng code
    public Customer searchByCode(String Code) {
        for (Customer i : this) {
            if (i.getCode().toLowerCase().trim().equalsIgnoreCase(Code.toLowerCase().trim())) {
                return i;
            }
        }
        return null;
    }
    
    //kiểm tra xem khách hàng đã tồn tại trong list hay chưa
    private boolean checkExist(Customer x) {
        for (Customer i : this) {
            if(i.getCode().toLowerCase().equalsIgnoreCase(x.getCode().toLowerCase())
               &&i.getName().toLowerCase().contains(x.getName().toLowerCase())
               && i.getPhone().equals(x.getPhone())){
                return true;
            }
        }
        return false;
      
    }
}
