package business;

import core.Helper;
import dao.BrandDao;
import entity.Brand;
import entity.Model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class BrandManager {
    private final BrandDao brandDao;
    private final ModelManager modelManager;


    public BrandManager() throws SQLException {
        this.brandDao = new BrandDao();
        this.modelManager = new ModelManager();
    }

public ArrayList<Object[]> getForTable(int size)  {
    ArrayList<Object[]> brandRowList = new ArrayList<>();
    for (Brand brand : this.findAll()) {
        Object[] rowObject = new Object[size];
        int i = 0;
        rowObject[i++] = brand.getId();
        rowObject[i++]= brand.getName();
        brandRowList.add(rowObject);
    }
    return brandRowList;
}
    public ArrayList<Brand> findAll(){
        return this.brandDao.findAll();
    }



    public boolean save(Brand brand) throws SQLException {
        if(brand.getId() !=0){
            Helper.showMsg("error");
        }
        return this.brandDao.save(brand);
    }
    public Brand getById(int id){
        //return this.brandDao.getById(id);
        return this.brandDao.getById(id);
    }

    public boolean update(Brand brand) throws SQLException {
        if(this.getById(brand.getId()) == null){
            Helper.showMsg("notFound");
        }
        return this.brandDao.update(brand);
    }

 public boolean delete(int id) {
        if (this.getById(id) == null) {
            Helper.showMsg(id+" ID kayıtlı marka bulunamadı");
            return false;
        }
        for (Model model : this.modelManager.getByListBrandId(id)) {
            this.modelManager.delete(model.getId());
        }

        return this.brandDao.delete(id);
 }
}
