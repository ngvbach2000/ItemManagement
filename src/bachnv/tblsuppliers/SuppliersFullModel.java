/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachnv.tblsuppliers;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ngvba
 */
public class SuppliersFullModel extends AbstractTableModel{
    SuppliersDAO suppliers = null;

    public SuppliersFullModel(SuppliersDAO suppliers) {
        this.suppliers = suppliers;
    }

    public SuppliersDAO getSuppliers() {
        return suppliers;
    }

    @Override
    public int getRowCount() {
        return suppliers.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column){
        String columnName = "";
        switch (column){
            case 0: columnName = "Code"; break;
            case 1: columnName = "Name"; break;
            case 2: columnName = "Address"; break;
        }
        return columnName;
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        SuppliersDTO supplier = suppliers.get(row);
        Object obj = null;
        switch (column){
            case 0: obj = supplier.getSupCode(); break;
            case 1: obj = supplier.getSupName(); break;
            case 2: obj = supplier.getAddress(); break;
        }
        return obj;
    }
}
