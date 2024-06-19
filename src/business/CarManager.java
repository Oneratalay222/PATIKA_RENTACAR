package business;

import core.Helper;
import dao.BookDao;
import dao.CarDao;
import dao.ModelDao;
import entity.Book;
import entity.Car;
import entity.Model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CarManager {
    private final CarDao carDao;
    private final BookDao bookDao;

    public CarManager() throws SQLException {
        this.bookDao = new BookDao();
        this.carDao = new CarDao();

    }

    public Car getById(int id) {return this.carDao.getById(id);}

    public ArrayList<Car> findAll() {return this.carDao.findAll();}

    public ArrayList<Object[]> getForTable(int size, ArrayList<Car> cars) {
        ArrayList<Object[]> carList = new ArrayList<>();
        for (Car obj : cars) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = obj.getId();
            rowObject[i++] = obj.getModel().getBrand().getName();
            rowObject[i++] = obj.getModel().getName();
            rowObject[i++] = obj.getPlate();
            rowObject[i++] = obj.getColor();
            rowObject[i++] = obj.getKm();
            rowObject[i++] = obj.getModel().getYear();
            rowObject[i++] = obj.getModel().getType();
            rowObject[i++] = obj.getModel().getFuel();
            rowObject[i++] = obj.getModel().getGear();
            carList.add(rowObject);
        }
        return carList;
    }

    public boolean save(Car car) {
        if (this.getById(car.getId()) != null) {
            Helper.showMsg("error");
            return false;

        }
        return this.carDao.save(car);
    }

    public boolean update(Car car) {
        if (this.getById(car.getId())==null){
            Helper.showMsg(car.getId()+"ID kayıtlı araç bulunamadı...");
            return false;
        }
        return this.carDao.uptade(car);
    }
    public boolean delete(int id) {
        if (this.getById(id) == null) {
            Helper.showMsg(id+ "ID kayıtlı araç bulunamadı...");
            return false;
        }
        return this.carDao.delete(id);

    }
    public ArrayList<Car> searchForBooking(String strt_date , String fnsh_date, Model.Type type, Model.Gear gear,Model.Fuel fuel) {
        String query = "SELECT * FROM public.car as c LEFT JOIN public.model as m on (c.car_model_id = m.model_id)";
        //bURASINI DEĞİŞİK YAZDIM HATA ALIRSAN KONTROL ET


        ArrayList<String> where = new ArrayList<>();
        ArrayList<String> bookOrWhere = new ArrayList<>();

            // İn Date Format = 10/10/2023 dd/MM/yyyy
            // Out Date Format = 2023-10-10 y-m-d
        strt_date = LocalDate.parse(strt_date, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
        fnsh_date = LocalDate.parse(fnsh_date, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();  // Buradam hata aldıgım ıcın yorum satırına aldım çözumu bulunca gerı donecegım




        if (fuel != null) {where.add(" m.model_fuel = '" + fuel.toString() + "'");}
        if (gear != null) {where.add(" m.model_gear = '" + gear.toString() + "'");}
        if (type != null) {where.add(" m.model_type = '" + type.toString() + "'");}

        String  whereStr = String.join(" AND " , where);

        if (whereStr.length()>0){
            query += "WHERE" + whereStr;
        }

        ArrayList<Car> searchedCarList = this.carDao.selectByQuery(query);


        bookOrWhere.add("('" + strt_date + "' BETWEEN book_strt_date AND book_fnsh_date)");
        bookOrWhere.add("('" + fnsh_date + "' BETWEEN book_strt_date AND book_fnsh_date)");
        bookOrWhere.add("(book_strt_date BETWEEN '" + strt_date + "' AND '" + fnsh_date + "')");
        bookOrWhere.add("(book_fnsh_date BETWEEN '" + strt_date + "' AND '" + fnsh_date + "')");

        String bookOrWhereStr = String.join(" OR ", bookOrWhere);
        String bookQuery = "SELECT * FROM  public.book WHERE" + bookOrWhereStr;
        System.out.println(bookQuery);


        ArrayList<Book> bookList = this.bookDao.selectByQuery(bookQuery);
        ArrayList<Integer> busryCarId = new ArrayList<>();
        for (Book book : bookList) {
            busryCarId.add(book.getCar_id());
        }
        searchedCarList.removeIf(car -> busryCarId.contains(car.getId())); // removeIf eger varsa sıl kullanımındaaaaaa


        return searchedCarList;
    }


}
