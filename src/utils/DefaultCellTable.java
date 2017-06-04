/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author F98877A
 */
public class DefaultCellTable implements Cloneable {

    protected Object cellValue;

    public DefaultCellTable() {
    }

    public Object getCellValue() {
        return cellValue;
    }

    public void setCellValue(Object cellValue) {
        this.cellValue = cellValue;
    }

    public void clone(DefaultCellTable other) {
        this.cellValue = other.getCellValue();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
    }

    @Override
    public String toString() {
        return cellValue == null ? "" : cellValue.toString();
    }

}
