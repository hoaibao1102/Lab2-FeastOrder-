/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bussiness;

import Model.FeastMenu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSI PC
 */
public class FeastMenuManager {

    private String pathFile;
    private ArrayList<FeastMenu> menuList;
    private final String HEADER_TABLE = ("--------------------------------------------------------------------------\n"
                                       + "List of Set Menus for ordering party:                                     \n"
                                       + "--------------------------------------------------------------------------"),
                         FOOTER_TABLE = ("--------------------------------------------------------------------------\n");

    public FeastMenuManager(){
        this.pathFile = "./FeastMenu.csv";
        this.menuList = new ArrayList<>();  
        readMenuFromFile();
    }
    
    //đọc file
    public void readMenuFromFile() {
        FileReader fr = null;

        try {
            File f = new File(pathFile);
            //kiểm tra sự tồn tại của file
            if (!f.exists()) {
                System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            }
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String temp = "";

            // Bỏ qua dòng đầu tiên nếu đó là tiêu đề
            br.readLine(); // Đọc và bỏ qua dòng đầu tiên
            
            // Đọc dữ liệu từ file
            while ((temp = br.readLine()) != null) {
                // tạo đối tượng i để add vào list Menu
                FeastMenu i = dataToObject(temp);
                if (i != null) {
                    menuList.add(i);// Thêm thực đơn vào danh sách
                }
            }

            //Sắp xếp danh sách menu với giá tăng dần
            menuList.sort(Comparator.comparingDouble(FeastMenu::getPrice));
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FeastMenuManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FeastMenuManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fr != null) { // Kiểm tra fr trước khi đóng
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(FeastMenuManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private FeastMenu dataToObject(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null; // Trả về null nếu chuỗi trống
        }
        //tách chuỗi text thành 1 mảng để xử lí
        String[] parts = text.split(",");
        if (parts.length < 4) {
            // Kiểm tra nếu số lượng thành phần không phải là 4
            System.out.println("Invalid data format: " + text);
            return null;
        }
        // Loại bỏ khoảng trắng dư thừa của từng phần tử
        String menuCode = parts[0].trim();
        String nameParty = parts[1].trim();
        double price = Double.parseDouble(parts[2]);
        //kiểm ra ingredients rồi thay thế các dấu# thành xuống hàng
        String ingre = parts.length > 3 ? parts[3].trim().replace("#", "\n")  : "";

        // trả về một MENU được khởi tạo từ các thành phần trên
        return new FeastMenu(menuCode, nameParty, price, ingre);
    }
    
    public void displayMenu() {
        System.out.println(HEADER_TABLE);
        for (FeastMenu feastMenu : menuList) {
            System.out.println(feastMenu);
            System.out.println(FOOTER_TABLE);
        }
    }
        
    //TÌM KIẾM MENU BẰNG CODE 
    public FeastMenu searchMenuByCode(String menuCode) {
        for (FeastMenu i : menuList) {
            if (i.getCodeMenu().toLowerCase().equalsIgnoreCase(menuCode.toLowerCase())) {
                return i;
            }
        }
        return null;
    }
}
