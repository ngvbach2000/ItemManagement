package bachnv.tblitems;

import bachnv.tblsuppliers.SuppliersDTO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ngvba
 */
public class ItemsDTO {
    String itemCode;
    String itemName;
    SuppliersDTO suppiler;
    String unit;
    float price;
    boolean supplying;

    public ItemsDTO(String itemCode, String itemName, SuppliersDTO suppiler, String unit, float price, boolean supplying) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.suppiler = suppiler;
        this.unit = unit;
        this.price = price;
        this.supplying = supplying;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public SuppliersDTO getSuppiler() {
        return suppiler;
    }

    public void setSuppiler(SuppliersDTO suppiler) {
        this.suppiler = suppiler;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isSupplying() {
        return supplying;
    }

    public void setSupplying(boolean supplying) {
        this.supplying = supplying;
    }

}
