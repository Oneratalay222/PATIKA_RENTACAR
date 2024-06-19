package core;

import javax.swing.*;
import java.awt.*;

public class Helper {
    public static void setTheme() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }//Nimbus yapısı oluşturgudumuz swing yapısına bıraz daha koyuluk katabılıyor
                break;

            }
        }
    }

    public static void showMsg(String str) {
        optionPanelTR();
        String msg;
        String title;
     switch (str) {
         case "fill" -> {
             msg = "Lütfen Tüm alanları doldurunuz !";
             title = "Hata";
         }
         case "done" ->{
             msg = "İşlem Başarılı!";
             title = "Sonuç";
         }
         case "notFound" ->{
             msg = "Kayıt Bulunamadı!";
             title = "Bulunamadı";
         }
         case "error" ->{
             msg = "Hatalı işlem yaptınız!";
             title = "Hata";
         }

         default -> {
             msg = str;
             title = "Mesaj";
         }

     }


        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
        //JOption satırı kullanıcı adı gırılmedıgı zaman ekrana uyarı verır.

    }
    public static boolean confirm(String str) {
        optionPanelTR();
        String msg;
        if (str.equals("sure")) {
            msg = " Bu işlemi yapmak ıstedıgıne emın mısın";
        }else{
            msg=str;
        }
        return JOptionPane.showConfirmDialog(null, msg,"Emin misin ? ",JOptionPane.YES_NO_OPTION) == 0;
    }
    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }
    public static boolean isFieldListEMpty(JTextField[] fieldList) {
        for (JTextField field : fieldList){
            if (isFieldEmpty(field)) {
                return true;
            }
        }
        return false;
    }

    public static int getLocationPoint(String type , Dimension size){
        //Swingi ortalayan kod blogu bunu loginde çalıştırıyoruz
        return switch (type) {
            case "x" -> (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
            case "y" -> (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
            default -> 0;
        };
    }
public static void optionPanelTR(){
        UIManager.put("OptionPane.okButtonText","Tamam" );
        UIManager.put("OptionPane.yesButtonText","Evet" );
        UIManager.put("OptionPane.noButtonText","Hayır" );
}

}
