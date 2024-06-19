package view;

import business.BrandManager;
import core.Helper;
import entity.Brand;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class BrandView extends Layout {
    private JPanel container;
    private JLabel lbl_brand;
    private JLabel lbl_brand_name;
    private JTextField fld_brand_name;
    private JButton btn_brand_save;
    private Brand brand;
    private BrandManager brandManager;

public BrandView(Brand brand) throws SQLException {
    this.brandManager = new BrandManager();
    this.add(container);
    this.guiInitilaze(300,300);
    this.brand = brand;

    if (brand != null){
        this.fld_brand_name.setText(brand.getName());
    }
    btn_brand_save.addActionListener(e -> {
        if (Helper.isFieldEmpty(this.fld_brand_name)){
            Helper.showMsg("fill");
        }else{
            boolean result;
            if (this.brand == null){
                Brand obj = new Brand((fld_brand_name.getText()));
                try {
                    result = this.brandManager.save(obj);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }else{
                this.brand.setName(fld_brand_name.getText());
                try {
                    result = this.brandManager.update(this.brand);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if(result){
                Helper.showMsg("done");
                dispose();
            }else{
                Helper.showMsg("error");
            }
        }
    });
}
}
