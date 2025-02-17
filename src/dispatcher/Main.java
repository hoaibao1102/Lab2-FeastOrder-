/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dispatcher;

import Model.Customer;
import Model.OrderFeast;
import Tool.Inputter;
import bussiness.Customers;
import bussiness.FeastMenuManager;
import bussiness.FeastOrderManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSI PC
 */
public class Main {
    public static void main(String[] args) {
        
        Customers ctmList = new Customers();
        FeastMenuManager menuList = new FeastMenuManager();
        FeastOrderManager orderList = new FeastOrderManager();
        Inputter ndl = new Inputter();
        int choice = 0;
        do {
            choice = ndl.getInt("===== Traditional Feast Management System =====\n"+
                        "1. Register customers.\n" +
                        "2. Update customer information.\n" +
                        "3. Search for customer information by name.\n" +
                        "4. Display feast menus.\n" +
                        "5. Place a feast order.\n" +
                        "6. Update order information.\n" +
                        "7. Save data to file.\n" +
                        "8. Display Customer or Order lists.\n" +
                        "9. Exit the Program\n"+
                        "Enter your choice:   ");
            switch (choice) {
                case 1:
                    //Nhap dl khach hang  
                    Customer newC = ndl.inputCustomerInfo(false);
                    ctmList.addNew(newC);
                    break;
                case 2:
                    //nhập code customer muốn tìm trước vào.
                    String codeUpdateC = ndl.getString("Enter customer code you want to update: ");
                    // tạo ra 1 customer trống để ktra trong ctmList
                    Customer existC = ctmList.searchByCode(codeUpdateC);
                    if (existC == null) {
                        System.out.println("This customer does not exist.");
                        break;
                    }
                    Customer updateC = ndl.inputCustomerInfo(true);
                    updateC.setCode(codeUpdateC);
                    ctmList.updateInfoCutomer(updateC);
                    System.out.println("Update successful!");
                    break;
                case 3:
                    //nhập tên muốn tìm 
                    String nameCusToSearch = ndl.getString("Enter customer name you want to search: ");
                    // tìm tên đó trong ctmList
                    ctmList.searchByName(nameCusToSearch);
                    break;
                case 4:
                    FeastMenuManager menu = new FeastMenuManager();
                    menu.displayMenu();
                    break;
                case 5:
                    OrderFeast newO = ndl.inputOrderFeast(false);
                    orderList.placeOrder(newO);
                    break;
                case 6:
                    //nhập id order 
                    String orderCode = ndl.getString("Enter your Order ID: ");         
                    OrderFeast existO = orderList.searchByID(orderCode);
                    if (existO == null) {
                        System.out.println("This Order does not exist ");
                        break;
                    }
                    OrderFeast updateO = ndl.inputOrderFeast(true);
                    updateO.setOrderCode(orderCode);
                    orderList.updateInfoOrder(updateO);
                    break;
                case 7:
                    ctmList.saveToFile();
                    orderList.saveToFileOrder();
                    break;
                case 8:
                    int choiceS = ndl.getInt("1. List Containing Customers Data \n"
                            + "2. List Containing Orders Data   \n"
                            + "Enter your choice : ");
                    if (choiceS == 1) {
                        ctmList.displayAll();
                    } else {
                        orderList.displayOrderList();
                    }
                    break;
                case 9:
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 9);
        
        
    }
}
