/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import db.tableInterfaces.TableModel;
import java.util.Objects;

/**
 *
 * @author f98877a
 */
public class ExpenseTable implements TableModel {

    private int oid;
    private int accOid;
    private String description;
    private int value;
    private int category;
    private int day;
    private int month;
    private int year;
    private boolean payed;
    private boolean repeat;
    private String hashId;

    public ExpenseTable() {
    }

    public ExpenseTable(int id, String description, int value, int category, int day, int month, int year, boolean payed, boolean repeat, String hashId) {
        this.oid = id;
        this.description = description;
        this.value = value;
        this.category = category;
        this.day = day;
        this.month = month;
        this.year = year;
        this.payed = payed;
        this.repeat = repeat;
        this.hashId = hashId;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int id) {
        this.oid = id;
    }

    public int getAccOid() {
        return accOid;
    }

    public void setAccOid(int id) {
        this.accOid = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public void computeHash() {
        String nameTmp = description;
        int variantTmp = category;

        StringBuilder builder = new StringBuilder();
        builder.append(nameTmp).append(variantTmp);

        String hash = builder.toString();

        hashId = Integer.toHexString(Objects.hashCode(hash)).toUpperCase();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

}
