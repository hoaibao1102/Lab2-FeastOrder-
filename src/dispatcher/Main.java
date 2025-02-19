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
            choice = ndl.getInt("===== Traditional Feast Management System =====\n"
                    + "1. Register customers.\n"
                    + "2. Update customer information.\n"
                    + "3. Search for customer information by name.\n"
                    + "4. Display feast menus.\n"
                    + "5. Place a feast order.\n"
                    + "6. Update order information.\n"
                    + "7. Save data to file.\n"
                    + "8. Display Customer or Order lists.\n"
                    + "9. Exit the Program\n"
                    + "Enter your choice:   ");
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
                    int choiceS1 = ndl.getInt("Choose which data to save:\n"
                            + " 1. Save customer data.\n"
                            + " 2. Save order data. \n"
                            + " 3. Save both.   \n"
                            + " 4. Do not save.  \n"
                            + "Enter your choice (1, 2, 3, or 4):  ");
                    switch (choiceS1) {
                        case 1:
                            ctmList.saveToFile();
                            System.out.println("Registration data has been successfully saved to `customer.dat`");
                            break;
                        case 2:
                            orderList.saveToFileOrder();
                            System.out.println("Registration data has been successfully saved to `feast_order_service.dat`");
                            break;
                        case 3:
                            ctmList.saveToFile();
                            orderList.saveToFileOrder();
                            System.out.println("Saved both files");
                            break;
                        case 4:
                            System.out.println("No data saved.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                        break;
                case 8:
                    int choiceS2 = ndl.getInt("Which data list you want to see?\n"
                            + "1. List Containing Customers Data \n"
                            + "2. List Containing Orders Data   \n"
                            + "Enter your choice : ");
                    if (choiceS2 == 1) {
                        ctmList.displayAll();
                    } else {
                        orderList.displayOrderList();
                    }
                    break;

                case 9:
                    int choiceS3 = ndl.getInt("Do you want to save all data: \n"
                            + " 1.YES               2.NO\n"
                            + "Enter your choice:   ");
                    if (choiceS3 == 1) {
                        ctmList.saveToFile();
                        orderList.saveToFileOrder();
                    } else {
                        System.out.println("don't save...");
                    }
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 9);

    }
}
