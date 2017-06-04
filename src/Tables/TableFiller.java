/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import static Tables.CashFlowInfo.Categories;
import db.Control.ModelControl;
import java.sql.SQLException;

/**
 *
 * @author f98877a
 */
public class TableFiller {

    private CategoryTable catTable;

    public TableFiller() {
    }

    public void categoryFiller() {

        catTable = new CategoryTable();
        for (String[] Categorie : Categories) {
            catTable.setCategory((String) Categorie[0]);
            catTable.setType((String) Categorie[1]);
            catTable.setColor("white");

            try {
                ModelControl.save(catTable);
            } catch (SQLException ex) {
                //ignores error
            }
        }

    }

}
