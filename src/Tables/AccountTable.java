/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import db.tableInterfaces.TableModel;
import java.time.LocalTime;
import java.util.Objects;

/**
 *
 * @author F98877A
 */
public class AccountTable implements TableModel {

    private int oid;
    private double value;
    private double totalExpense;
    private double totalIncome;
    private String name;
    private String status;
    private String type;
    private int day;
    private int month;
    private int year;
    private String hashId;

    public AccountTable(int oid, String name, double value, String status, double totalExpense, double totalIncome, String type, String hashId) {
        this.oid = oid;
        this.name = name;
        this.value = value;
        this.status = status;
        this.totalExpense = totalExpense;
        this.totalIncome = totalIncome;
        this.type = type;
        this.hashId = hashId;
    }

    public AccountTable() {
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHashId() {
        return hashId;
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

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public void computeHash() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.oid);
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.value);
        hash = 37 * hash + Objects.hashCode(this.status);
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + Objects.hashCode(this.day);
        hash = 37 * hash + Objects.hashCode(LocalTime.now().toString());

        hashId = Integer.toHexString(Objects.hashCode(hash)).toUpperCase();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountTable other = (AccountTable) obj;
        if (!Objects.equals(this.oid, other.oid)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.oid);
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.value);
        hash = 37 * hash + Objects.hashCode(this.status);
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + Objects.hashCode(this.day);
        return hash;
    }

}
