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

    public String inputAndLoop(String mess, String pattern) {
        String result = "";
        boolean more = true;
        do {
            result = getString(mess);
            //nếu mẫu kiểm tra là Date valid thì dùng futurDate
            if(pattern.equals(Acceptable.DATE_VALID)){
                more = !Acceptable.isFutureDate(result, pattern);
            }else
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
            x.setCode(inputAndLoop("Customer Code (C, G, K followed by 4 digits): ", Acceptable.CUSTOMER_ID_VALID));
        }
        //Cho nhập thông tin khách hàng
        
        x.setName(inputAndLoop("Customer Name: ", Acceptable.NAME_VALID));
        x.setPhone(inputAndLoop("Phone[10 digits]: ", Acceptable.PHONE_VALID));
        x.setEmail(inputAndLoop("Email: ", Acceptable.EMAIL_VALID));
        
        return x;       
    }
    
    public OrderFeast inputOrderFeast(boolean isUpdate){
        OrderFeast x  = new OrderFeast();
        if(!isUpdate){
             x.setCustomeCode(inputAndLoop("Enter customer code (C, G, K followed by 4 digits): ", Acceptable.CUSTOMER_ID_VALID));
        }
        x.setMenuCode(inputAndLoop("Enter SetMenu code(PW00 + 1 digit ):  ", Acceptable.MENU_ID_VALID));
        x.setNumTable(inputAndLoop("Enter number of tables: ", Acceptable.INTEGER_VALID));
        x.setEventDate(inputAndLoop("Enter event date (dd/MM/yyyy): ",Acceptable.DATE_VALID));
        
        return x;
    }
}


