package view;

import business.BrandManager;
import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.Brand;
import entity.Car;
import entity.Model;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdminView extends Layout {
    private JPanel container;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JTabbedPane tab_menü;
    private JButton btn_logout;
    private JPanel pnl_brand;
    private JScrollPane scl_brand;
    private JTable tbl_brand;
    private JScrollPane scrl_model;
    private JTable tbl_model;
    private JTable tbl_car;
    private JLabel lbl_brand;
    private JLabel lbl_type;
    private JLabel lbl_fuel;
    private JLabel lbl_gear;
    private JButton btn_search;
    private JComboBox cmb_s_model_brand;
    private JComboBox cmb_s_model_type;
    private JComboBox cmb_s_model_gear;
    private JComboBox cmb_s_model_fuel;
    private JButton btn_cncl_model;
    private JButton btn_booking_search;
    private JComboBox cmb_booking_gear;
    private JComboBox cmb_booking_fuel;
    private JComboBox cmb_booking_type;
    private JFormattedTextField fld_strt_date;
    private JFormattedTextField fld_fnsh_date;
    private JTable tbl_booking;
    private JButton btn_cncl_booking;
    private User user;
    private DefaultTableModel tmdl_brand = new DefaultTableModel();
    private DefaultTableModel tmdl_car = new DefaultTableModel();
    private DefaultTableModel tmdl_booking = new DefaultTableModel();
    private DefaultTableModel tmdl_model = new DefaultTableModel();
    private BrandManager brandManager;
    private ModelManager modelManager;
    private JPopupMenu brand_Menu;
    private JPopupMenu model_Menu;
    private JPopupMenu car_manu;
    private BrandView brandView;
    private Object[] col_model;
    private JPopupMenu car_menu;
    private CarManager carManager;
    private JPopupMenu booking_menu;
    private Object[] col_car;
    private Object[] col_booking_list;

    public AdminView(User user) throws SQLException {
        this.carManager = new CarManager();
        this.brandManager = new BrandManager();
        this.modelManager = new ModelManager();
        this.add(container);
        this.guiInitilaze(1000, 500);
        this.user = user;

        if (this.user == null) {
            dispose();
        }
        this.lbl_welcome.setText("Hoşgeldiniz : " + this.user.getUsername());
        //General Code
        loadComponent();

        //Brand Tab Menü
        loadBrandTable();
        loadBrandCompanent();
        //Model Tab Menü
        loadModelTable(null);
        loadModelComponent();
        loadModelFilter();
        //Car Tab Menü
        loadCarTable();
        loadCarComponent();

        //Booking Tab Menü
        loadBookingTable(null);
        loadBookingComponent();
        loadBookingFilter();







    }

    private void loadComponent(){
        this.btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    LoginView loginView = new LoginView();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void loadBookingComponent() {
        tableRowSelected(this.tbl_booking);
        booking_menu = new JPopupMenu();
        this.booking_menu.add("Rezervasyon Yap").addActionListener(e -> {
            int selectcarId = this.getTableSelectedRow(this.tbl_booking , 0);
            try {
                BookingView bookingView = new BookingView(
                        this.carManager.getById(selectcarId),
                        this.fld_strt_date.getText(),
                        this.fld_fnsh_date.getText()
                );
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.tbl_booking.setComponentPopupMenu(booking_menu);
        btn_booking_search.addActionListener(e -> {
            ArrayList<Car> carList = this.carManager.searchForBooking(
                    fld_strt_date.getText(),
                    fld_fnsh_date.getText(),
                    (Model.Type) cmb_booking_type.getSelectedItem(),
                    (Model.Gear) cmb_booking_gear.getSelectedItem(),
                    (Model.Fuel) cmb_booking_fuel.getSelectedItem()
            );
            ArrayList<Object[]> carBookingRow = this.carManager.getForTable(this.col_booking_list.length, carList);
            loadBookingTable(carBookingRow);
        });


        btn_cncl_booking.addActionListener(e -> {
            loadBookingFilter();

        });
    }

    public void loadBookingTable(ArrayList<Object[]> bookList) {
        this.col_booking_list = new Object[]{"ID", "Marka", "Model", "Plaka", "Renk", "KM", "Yıl", "Tip", "Yakıt Türü", "Vites"};
        ArrayList<Car> carArrayList = this.carManager.searchForBooking(
                fld_strt_date.getText(),
                fld_fnsh_date.getText(),
                (Model.Type) cmb_booking_type.getSelectedItem(),
                (Model.Gear) cmb_booking_gear.getSelectedItem(),
                (Model.Fuel) cmb_booking_fuel.getSelectedItem()
        );
        ArrayList<Object[]> carBookingRow = this.carManager.getForTable(col_booking_list.length, carArrayList);
        createTable(this.tmdl_booking, this.tbl_booking, col_booking_list, carBookingRow);

    }

    public void loadBookingFilter() {
        this.cmb_booking_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_booking_type.setSelectedItem(null);
        this.cmb_booking_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_booking_gear.setSelectedItem(null);
        this.cmb_booking_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_booking_fuel.setSelectedItem(null);
    }


    private void loadCarComponent() {
        this.car_menu = new JPopupMenu();
        this.car_menu.add("Yeni").addActionListener(e -> {
            CarView carView = null;
            try {
                carView = new CarView(new Car());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                }

            });
        });
        this.car_menu.add("Güncelle").addActionListener(e -> {
            int selectModelId = this.getTableSelectedRow(tbl_car, 0);
            CarView carView = null;
            try {
                carView = new CarView(this.carManager.getById(selectModelId));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    loadCarTable();
                }
            });
        });
        this.car_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectCarId = this.getTableSelectedRow(tbl_car, 0);
                if (this.carManager.delete(selectCarId)) {
                    Helper.showMsg("done");
                    loadCarTable();
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        this.tbl_car.setComponentPopupMenu(car_menu);
    }

    private void loadCarTable() {
        col_car = new Object[]{"ID", "Marka", "Model", "Plaka", "Renk", "Km", "Yıl", "Tip", "Yakıt Türü", "Vites"};
        ArrayList<Object[]> carList = this.carManager.getForTable(col_car.length, this.carManager.findAll());
        createTable(this.tmdl_car, this.tbl_car, col_car, carList);

    }


    private void loadModelComponent() {
        tableRowSelected(this.tbl_model);
        this.tbl_brand.setComponentPopupMenu(brand_Menu);
        this.model_Menu = new JPopupMenu();
        this.model_Menu.add("Yeni").addActionListener(e -> {
            try {
                ModelView modelView = new ModelView(new Model());
                modelView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                modelView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadModelTable(null);
                    }

                });

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.model_Menu.add("Güncelle").addActionListener(e -> {

            try {
                int selectModelId = this.getTableSelectedRow(tbl_model, 0);
                ModelView modelView = new ModelView(new Model(selectModelId));
                modelView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                modelView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadModelTable(null);
                        loadCarTable();
                        loadBookingTable(null);
                    }

                });

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.model_Menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectModelId = this.getTableSelectedRow(tbl_model, 0);
                if (this.modelManager.delete(selectModelId)) {
                    Helper.showMsg("done");
                    loadModelTable(null);

                } else {
                    Helper.showMsg("error");
                }
            }
        });


        this.tbl_model.setComponentPopupMenu(model_Menu);
        this.btn_search.addActionListener(e -> {
            ComboItem selectedBrand = (ComboItem) this.cmb_s_model_brand.getSelectedItem();
            int brandId = 0;
            if (selectedBrand != null) {
                brandId = selectedBrand.getKey();
            }
            ArrayList<Model> modelListBySearch = this.modelManager.searchForTable(
                    brandId,
                    (Model.Fuel) cmb_s_model_fuel.getSelectedItem(),
                    (Model.Gear) cmb_s_model_gear.getSelectedItem(),
                    (Model.Type) cmb_s_model_type.getSelectedItem()
            );
            ArrayList<Object[]> modelRowListBySearch = this.modelManager.getForTable(this.col_model.length, modelListBySearch);
            loadModelTable(modelRowListBySearch);
        });
        this.btn_cncl_model.addActionListener(e -> {
            this.cmb_s_model_type.setSelectedItem(null);
            this.cmb_s_model_gear.setSelectedItem(null);
            this.cmb_s_model_fuel.setSelectedItem(null);
            this.cmb_s_model_brand.setSelectedItem(null);
            loadModelTable(null);
        });
    }

    public void loadModelTable(ArrayList<Object[]> modelList) {
        this.col_model = new Object[]{"Model ID", "Marka", "Model Adı", "Tip", "Yıl", "Yakıt türü", "Vites"};
        if (modelList == null) {
            modelList = this.modelManager.getForTable(this.col_model.length, this.modelManager.findAll());
        }
        createTable(this.tmdl_model, this.tbl_model, col_model, modelList);
    }

    public void loadModelFilter() {
        this.cmb_s_model_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_s_model_type.setSelectedItem(null);
        this.cmb_s_model_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_s_model_gear.setSelectedItem(null);
        this.cmb_s_model_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_s_model_fuel.setSelectedItem(null);
        loadModelFilterBrand();
    }

    public void loadModelFilterBrand() {
        this.cmb_s_model_brand.removeAllItems();
        for (Brand obj : brandManager.findAll()) {
            this.cmb_s_model_brand.addItem(new ComboItem(obj.getId(), obj.getName()));
        }
        this.cmb_s_model_brand.setSelectedItem(null);

    }

    public void loadBrandCompanent() {
        tableRowSelected(this.tbl_brand);

        this.brand_Menu = new JPopupMenu();
        this.brand_Menu.add("Yeni").addActionListener(e -> {
            try {
                brandView = new BrandView(null);
                brandView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                brandView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        loadBrandTable();
                        loadModelTable(null);
                        loadModelFilterBrand();

                    }

                });
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.brand_Menu.add("Güncelle").addActionListener(e -> {
            int selectBrandId = this.getTableSelectedRow(tbl_brand, 0);
            try {
                BrandView brandView = new BrandView(this.brandManager.getById(selectBrandId));
                brandView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        loadBrandTable();
                        loadModelTable(null);
                        loadModelFilterBrand();
                        loadBookingTable(null);

                    }
                });
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        this.brand_Menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectBrandId = this.getTableSelectedRow(tbl_brand, 0);
                if (this.brandManager.delete(selectBrandId)) {
                    Helper.showMsg("done");
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable();
                } else {
                    Helper.showMsg("error");
                }
            }
        });
    }

    public void loadBrandTable() {

        String[] col_brand = {"Marka ID", "Marka Adı"};
        ArrayList<Object[]> brandList = this.brandManager.getForTable(col_brand.length);
        this.createTable(this.tmdl_brand, this.tbl_brand, col_brand, brandList);

    }

    private void createUIComponents() throws ParseException {
        this.fld_strt_date =  new JFormattedTextField(new MaskFormatter("##/##/####"));// Girilen tarih bu formatta yazılması için kod dizimi
        this.fld_strt_date.setText("10/10/2023");
        this.fld_fnsh_date =  new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_fnsh_date.setText("16/10/2023");
    }
}
