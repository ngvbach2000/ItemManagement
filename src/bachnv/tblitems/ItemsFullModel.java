/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachnv.tblitems;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ngvba
 */
public class ItemsFullModel extends AbstractTableModel{
    ItemsDAO items;

    public ItemsFullModel(ItemsDAO items) {
        this.items = items;
    }

    public ItemsDAO getItems() {
        return items;
    }
        
    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    public String getColumnName(int column){
        String columnName = "";
        switch(column) {
            case 0:
                columnName = "Code";
                break;
            case 1:
                columnName = "Name";
                break;
            case 2:
                columnName = "Supplier";
                break;
            case 3:
                columnName = "Unit";
                break;
            case 4:
                columnName = "Price";
                break;
            case 5:
                columnName = "Supply";
                break;
        }
        return columnName;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemsDTO dto = items.get(rowIndex);
        Object obj = null;
        String supplier = dto.getSuppiler().getSupCode() + "-" + dto.getSuppiler().getSupName();
        switch(columnIndex){
            case 0:
                obj = dto.getItemCode();
                break;
            case 1:
                obj = dto.getItemName();
                break;
            case 2:
                obj = supplier;
                break;
            case 3:
                obj = dto.getUnit();
                break;
            case 4:
                obj = dto.getPrice();
                break;
            case 5:
                obj = dto.isSupplying();
                break;
        }
        return obj;
    }
    
}
