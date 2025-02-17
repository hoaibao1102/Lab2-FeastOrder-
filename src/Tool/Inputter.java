package Tool;


import Model.Customer;
import Model.OrderFeast;
import java.util.Scanner;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MSI PC
 */
public class Inputter {

    private Scanner ndl;

    public Inputter() {
        ndl = new Scanner(System.in);
    }

    public String getString(String mess) {
        System.out.print(mess);
        return ndl.nextLine();
    }

    public int getInt(String mess) {
        int num = 0;
        String tmp = getString(mess);
        if (Acceptable.isValid(tmp, Acceptable.INTEGER_VALID)) {
            num = Integer.parseInt(tmp);
        }
        return num;
    }

    public double getDouble(String mess) {
        double num = 0;
        String temp = getString(mess);
        if (Acceptable.isValid(temp, Acceptable.DOUBLE_VALID)) {
            num = Double.parseDouble(mess);
        }
        return num;
    }

    public String inputAndLoot(String mess, String pattern) {
        String result = "";
        boolean more = true;
        do {
            result = getString(mess);
            more = !Acceptable.isValid(result, pattern);
            if (more) {
                System.out.println("Date is invalid!. Re-Enter.....");
            }
        } while (more);    
        return result;
    }

    public Customer inputCustomerInfo(boolean isUpdate) {
        Customer x = new Customer();
        // khi không update(false) mới đi vào lệnh if dưới đây
        if(!isUpdate){
            x.setCode(inputAndLoot("Customer Code: ", Acceptable.CUSTOMER_ID_VALID));
        }
        //Cho nhập thông tin khách hàng
        
        x.setName(inputAndLoot("Customer Name: ", Acceptable.NAME_VALID));
        x.setPhone(inputAndLoot("Phone[10 digits]: ", Acceptable.PHONE_VALID));
        x.setEmail(inputAndLoot("Email: ", Acceptable.EMAIL_VALID));
        
        return x;       
    }
    
    public OrderFeast inputOrderFeast(boolean isUpdate){
        OrderFeast x  = new OrderFeast();
        if(!isUpdate){
             x.setCustomeCode(inputAndLoot("Enter customer code: ", Acceptable.CUSTOMER_ID_VALID));
        }
        x.setMenuCode(inputAndLoot("Enter SetMenu code: ", Acceptable.MENU_ID_VALID));
        x.setNumTable(inputAndLoot("Enter number of tables: ", Acceptable.INTEGER_VALID));
        x.setEventDate(inputAndLoot("Enter event date (dd/MM/yyyy): ",Acceptable.DATE_VALID));
        
        return x;
    }
}


