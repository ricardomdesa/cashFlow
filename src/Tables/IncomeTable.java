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
 * @author f98877a
 */
public class IncomeTable implements TableModel {

    private int oid;
    private int accOid;
    private String description;
    private double value;
    private int category;
    private int day;
    private int month;
    private int year;
    private boolean received;
    private boolean repeat;
    private String hashId;

    public IncomeTable() {
    }

    public IncomeTable(int id, String description, double value, int category, int day, int month, int year, boolean received, boolean repeat, String hashId) {
        this.oid = id;
        this.description = description;
        this.value = value;
        this.category = category;
        this.day = day;
        this.month = month;
        this.year = year;
        this.received = received;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
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
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.oid);
        hash = 37 * hash + Objects.hashCode(this.accOid);
        hash = 37 * hash + Objects.hashCode(this.category);
        hash = 37 * hash + Objects.hashCode(this.description);
        hash = 37 * hash + Objects.hashCode(this.day);
        hash = 37 * hash + Objects.hashCode(LocalTime.now().toString());

        hashId = Integer.toHexString(Objects.hashCode(hash)).toUpperCase();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.oid);
        hash = 37 * hash + Objects.hashCode(this.category);
        hash = 37 * hash + Objects.hashCode(this.description);
        hash = 37 * hash + Objects.hashCode(this.day);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IncomeTable other = (IncomeTable) obj;
        if (this.oid != other.oid) {
            return false;
        }
        if (this.category != other.category) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

}
