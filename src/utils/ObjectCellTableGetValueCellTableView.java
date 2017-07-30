/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import cashFlow.GUI.CellValueObj;

/**
 *
 * @author F98877A
 */
public class ObjectCellTableGetValueCellTableView extends DefaultCellTable {

    public ObjectCellTableGetValueCellTableView() {
    }

    @Override
    public String toString() {
        String to;
        if (getCellValue() instanceof CellValueObj) {
            to = ((CellValueObj) getCellValue()).getValue();
        } else {
            to = super.toString();
        }
        return to;
    }

    @Override
    public void setCellValue(Object cellValue) {
        if (cellValue instanceof CellValueObj) {
            if (cellValue != (CellValueObj) getCellValue()) {
                super.setCellValue(cellValue);
            }
        } else if (cellValue instanceof String) {
            super.setCellValue(cellValue);
        } else if (cellValue instanceof Double) {
            super.setCellValue(cellValue);
        }
//        super.setCellValue(cellValue); //To change body of generated methods, choose Tools | Templates.
    }

}
