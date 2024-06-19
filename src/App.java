import business.UserManager;
import core.Db;
import core.Helper;
import view.AdminView;
import view.LoginView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //Dao-- Data Acces Object
        //entity -- Veritabanın çektıgımız verılerı modellere aktarıyoruz
        //Business -- veriyabanı işlemıerını dao üzerinden yapacak
        //View  -- ara yüz yazılacak
        //core
        Helper.setTheme();//Nimbus yapısını çağırıyor
        LoginView loginView = new LoginView();

    }
}

