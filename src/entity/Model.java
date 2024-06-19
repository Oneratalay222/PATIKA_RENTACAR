package entity;


import core.ComboItem;

public class Model {
    public Object getComboItem; // Hata almamak ıcın ben yazdım sılınebılır...!!!!!!!!!!!!!!!!!!!!
    private int id;
    private int brand_id;
    private String name;
    private String year;
    private Type type;
    private Fuel fuel;
    private Gear gear;
    private Brand brand;

    public Model(int selectModelId) {
    }

    public ComboItem getComboItem() {
        return null; //Hata almamak ıcın ben yazdım sılınebılır !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }


    public enum Fuel{
        GASOLINE,
        LPG,
        ELECTRIC,
        DIESEL
    }
    public enum Gear{
        MANUEL,
        AUTO

    }
    public enum Type{
        SEDAN,
        HACBACK

    }
    public Model() {

    }

    public Model(int id, int brand_id, String name, String year, Type type, Fuel fuel, Gear gear, Brand brand) {
        this.id = id;
        this.brand_id = brand_id;
        this.name = name;
        this.year = year;
        this.type = type;
        this.fuel = fuel;
        this.gear = gear;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Gear getGear() {
        return gear;
    }

    public void setGear(Gear gear) {
        this.gear = gear;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public void getBrand(Brand brand) {this.brand = brand;}

    @Override
    public String toString() {
        return "{" +
                "model_id=" + id +
                ", model_brand_id=" + brand_id +
                ", model_name='" + name + '\'' +
                ", model_type=" + type +
                ", model_year='" + year + '\'' +
                ", model_fuel=" + fuel +
                ", model_gear=" + gear +
                ", model_brand=" + brand +
                '}';
    }

}
