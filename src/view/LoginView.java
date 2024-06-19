package view;

import business.UserManager;
import core.Helper;
import entity.User;

import javax.swing.*;
import java.sql.SQLException;

public class LoginView extends Layout {

    private JPanel container;
    private JPanel w_tab;
    private JLabel lbl_welcome;
    private JLabel lbl_welcome2;
    private JPanel w_button;
    private JTextField fld_username;
    private JPasswordField fld_pass;
    private JButton btn_login;
    private JLabel lbl_username;
    private JLabel lbl_pass;
    private final UserManager userManager;

    public LoginView() throws SQLException {
        this.userManager = new UserManager();
        this.add(container);
        this.guiInitilaze(400, 400);
        btn_login.addActionListener(e -> {
            JTextField[] checkFieldList = {this.fld_username, this.fld_pass};
            if (Helper.isFieldListEMpty(checkFieldList)) {
                Helper.showMsg("fill");
            } else {
                User loginUser = this.userManager.findByLogin(this.fld_username.getText(), this.fld_pass.getText());
                if (loginUser == null) {
                    Helper.showMsg("notFound");
                } else {
                    try {
                        AdminView adminView = new AdminView(loginUser);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    dispose();
                }
            }
        });
    }
}
