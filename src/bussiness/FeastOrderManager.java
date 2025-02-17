/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bussiness;

import Model.Customer;
import Model.FeastMenu;
import Model.OrderFeast;
import Tool.Acceptable;
import Tool.Inputter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSI PC
 */
public class FeastOrderManager extends ArrayList<OrderFeast>{
    private Customers customerList;
    private FeastMenuManager menuList;
    private String pathFile;
    private boolean save;
    private Inputter ndl = new Inputter();
    private final String HEADER_TABLE = ("-------------------------------------------------------------------------------------------\n"
                                       + "|ID     | Event date | Customer ID | Set Menu | Price         | Tables | Total Cost       |\n"
                                       + "-------------------------------------------------------------------------------------------"),
                         FOOTER_TABLE = ("-------------------------------------------------------------------------------------------\n");

    
   
    
    public FeastOrderManager() {
        super();
        this.pathFile = "./feast_order_service.dat";
        this.save = true;
        this.customerList = new Customers();
        customerList.readFromFile();
        this.menuList = new FeastMenuManager();
        menuList.readMenuFromFile();
        readOrderFromFile();

        
    }

 
    
    /***
     * ktra khách hàng đã tồn tại trong list hay chưa
     * kiểm tra menu id có tồn tại hay không nếu tồn tại lấy ra giá tiền của mã menu đó
     * kiểm tra số lượng bàn có hợp lí chưa
     * ----
     * sau đó kiểm tra đơn hàng đã tồn tại hay chưa nếu rồi thì tbao còn chưa thì tạo mã đơn hàng
     * tính tổng cost 
     * => tạo ra một orderFeast rồi đưa nó vào list Order FEAST ĐỂ CÓ THỂ LƯU LẠI
     */
    public void placeOrder(OrderFeast Order){
        //kiểm tra xem khách hàng đã tồn tại trong list chưa
        String customerCode = Order.getCustomeCode();
        Customer newCustomer = customerList.searchByCode(customerCode);
        if (newCustomer == null) {
            System.out.println("Customer not found...");
            return;
        }

        
        // NHẬP CODE CỦA MENU CHỌN
        String menuCode = Order.getMenuCode();
        menuCode.trim();
        FeastMenu newM = menuList.searchMenuByCode(menuCode); // tìm kiếm menu bằng mã menu
        if(newM == null){
            System.out.println("SetMenu code not found....");
            return;
        }
        
        // lấy ra giá tiền của mỗi codeMenu
        double price = newM.getPrice();
        
        //kiểm tra số lượng bàn khách hàng đặt có thõa mãn hay chưa
        String numTableStr = Order.getNumTable();
        int numTable2 = Integer.parseInt(numTableStr);
        if(numTable2 <= 0){
            System.out.println("Invalid number of tables...");
            return;
        }
        
        String eventDateStr = Order.getEventDate();
        eventDateStr.trim();
        Date eventDate = parseDate(eventDateStr);
        
        //kiểm tra có bị trùng lặp đơn hàng không
        if(isDupliOrder(customerCode,menuCode,eventDate)){
            System.out.println("Duplicate data!");
            return;
        }
        
        // sau khi kiểm tra toàn bộ xong ta sẽ tạo mã đơn hàng
        String orderId = generateOrderId9();
        
        // tính total code
        double totalCost = numTable2 * newM.getPrice();
        
        // Tạo đơn hàng mới và thêm vào danh sách
        OrderFeast newOrder = new OrderFeast(orderId,customerCode,menuCode, numTableStr, totalCost, eventDateStr, price);
        this.add(newOrder);

        // Hiển thị thông tin đơn hàng đã đặt thành công
        displayOrderInfo(newOrder, newM,newCustomer);
    }
    
    //hàm ép kiểu về ngày
    private Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Chỉ cho phép ngày hợp lệ
        try {
            return sdf.parse(dateStr);
        } catch (ParseException ex) {
           return null;
        }
    }

    // kiểm tra đơn hàng có bị trùng lặp hay không
    private boolean isDupliOrder(String customerCode, String menuCode, Date date) {
        for (OrderFeast i : this) {
            if(i.getCustomeCode().toLowerCase().equalsIgnoreCase(customerCode.toLowerCase())
               && i.getMenuCode().toLowerCase().equalsIgnoreCase(menuCode.toLowerCase())
               && i.getEventDate().equals(date)){
                return true;
            }
        }
        return false;
    }
    
    // TẠO MÃ ĐƠN HÀNG
    private String generateOrderId9() {
        return String.valueOf(this.size()+1);
    }

    private void displayOrderInfo(OrderFeast newOrder, FeastMenu newM,Customer customer) {
        System.out.println("----------------------------------------------------------------");
        System.out.println("Customer order information [Order ID: " + newOrder.getOrderCode() + "]");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Code : " + customer.getCode());
        System.out.println("Customer name : " + customer.getName());
        System.out.println("Phone number : " + customer.getPhone());
        System.out.println("Email : " + customer.getEmail());
        System.out.println("----------------------------------------------------------------");
        System.out.println("Code of Set Menu: " + newM.getCodeMenu());
        System.out.println("Set menu name : " + newM.getNameParty());
        System.out.println("Event date : " + newOrder.getEventDate());
        System.out.println("Number of tables: " + newOrder.getNumTable());
        System.out.println("Price : " + String.format("%,.0f", newM.getPrice()) + " Vnd");
        System.out.println("Ingredients:\n" + newM.getIngredients());
        System.out.println("----------------------------------------------------------------");
        System.out.println("Total cost : " + String.format("%,.0f", newOrder.getTotalCost()) + " Vnd");
        System.out.println("----------------------------------------------------------------");
        System.out.println();
    }
    
    //ĐỌC FILE
    public void readOrderFromFile() {
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
                OrderFeast c = (OrderFeast) ois.readObject();
                this.add(c);
            }
            ois.close();
            this.save = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FeastOrderManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FeastOrderManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FeastOrderManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //LƯU FILE
    public void saveToFileOrder() {
        FileOutputStream fos = null;
        try {

            // tạo file để thu thập dữ liệu đki
            File f = new File(pathFile);

            // mở luồn file output
            fos = new FileOutputStream(f);

            // tạo đối tường outputstream để tuần tự hóa dữ liệu
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            //ghi dữ liệu tuần tự hóa vào file
            for (OrderFeast thi : this) {
                oos.writeObject(thi);
            }
            oos.close();
            this.save = true;
            System.out.println("Registration data has been successfully saved to `feast_order_service.dat`");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FeastOrderManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FeastOrderManager.class.getName()).log(Level.SEVERE, null, ex);
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
    
    //display bảng khách hàng đã đặt bàn
    public void displayOrderList() {
        System.out.println(HEADER_TABLE);
        for (OrderFeast thi : this) {
            System.out.println(thi);
            System.out.print(FOOTER_TABLE);
        }
    }
    
    // tìm kiếm khách hàng bằng mã order
    public OrderFeast searchByID(String orderCode) {
        if (this.isEmpty()) {
            System.out.println("Order list is empty.");
            return null;
        }

        for (OrderFeast i : this) {
            if (i.getOrderCode().trim().equalsIgnoreCase(orderCode.trim())) {
                return i;
            }
        }
        System.out.println("Order not found with code: " + orderCode);
        return null;
    }
    
    /***
     * tìm kiếm thông tin 
     * 
     */
    public void updateInfoOrder(OrderFeast newOrder) {
        OrderFeast oldOrder = searchByID(newOrder.getOrderCode());
        if (oldOrder == null) {
            System.out.println("This Order does not exist");
            return;
        }

        if (newOrder.getMenuCode().length() > 0) {
            oldOrder.setMenuCode(newOrder.getMenuCode());
        }
        String searchCode = newOrder.getMenuCode();
        FeastMenu newM = menuList.searchMenuByCode(searchCode);
        if(newM == null){
            System.out.println("SetMenu code not found....");
            return;
        }
        oldOrder.setCustomeCode(oldOrder.getCustomeCode());
        oldOrder.setPrice(newM.getPrice());
        // cập nhật số lượng bàn mới
        if(!newOrder.getNumTable().isEmpty()){
            oldOrder.setNumTable(newOrder.getNumTable());
            int numTables = Integer.parseInt(newOrder.getNumTable());
            oldOrder.setTotalCost(numTables * oldOrder.getPrice());
        }
        if (!newOrder.getEventDate().isEmpty()
            && parseDate(newOrder.getEventDate()).after(new Date())) {
            oldOrder.setEventDate(newOrder.getEventDate());
        } 

        System.out.println("Order updated successfully.");
        this.save = false;

    }
}
