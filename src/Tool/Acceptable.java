package Tool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MSI PC
 */
public interface Acceptable {
    public final String CUSTOMER_ID_VALID = "^[CcGgKk]\\d{4}$";
    public final String NAME_VALID = "^.{2,20}$";
    public final String DOUBLE_VALID = "^\\d+.?\\d*$";
    public final String INTEGER_VALID = "^[1-9]\\d*$";
    public final String PHONE_VALID = "^(0[3|5|7|8|9])+([0-9]{8})$";
    public final String EMAIL_VALID = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public final String MENU_ID_VALID = "^[Pp][Ww]00[1-6]$";
    public final String DATE_VALID = "^(0[1-9]|[1|2][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";
    
    /***
     * kiểm tra xem định dạng ngày có đúng chưa (sai thì trả về false)
     * sau đó ép kiểu về LocalDate theo formart dd/mm/yyyy để so với ngày hiện tại
     * nếu ngày đki trong quá khứ(isBefore) thì trả về false 
     * còn không thì trả về true
     */
    static boolean isFutureDate(String dateStr, String pattern){
        // kiểm tra xem định dạnh ngày có hợp lệ không 
        if(!isValid(dateStr, pattern)){
            System.out.println("định dạng ngày không hợp lệ");
            return false;
        }
        // Kiểm tra xem ngày có phải trong tương lai không
        try {
            LocalDate eventDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (eventDate.isBefore(LocalDate.now().plusDays(1))) {
                System.out.println("Ngày tiệc phải là ngày trong tương lai.");
                return false;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Định dạng ngày không hợp lệ.");
            return false;
        }

        return true;
    }
    /**
     * Kiểm tra dữ liệu có trong data có phù hợp với mẫu pattern theo yêu cầu
     * không
     */
    // Static method to validate data against a pattern
    static boolean isValid(String data, String pattern) {
        return data.matches(pattern);
    }
    
}
