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
public class CategoryTable implements TableModel {

    private int oid;
    private String category;
    private String type;
    private String color;
    private String hashId;

    public CategoryTable() {
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int id) {
        this.oid = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public void computeHash() {
        String nameTmp = category;

        StringBuilder builder = new StringBuilder();
        builder.append(nameTmp);

        String hash = builder.toString();

        hashId = Integer.toHexString(Objects.hashCode(hash)).toUpperCase();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.oid);
        hash = 37 * hash + Objects.hashCode(this.category);
        hash = 37 * hash + Objects.hashCode(this.color);
        hash = 37 * hash + Objects.hashCode(this.type);
        return hash;
    }

}