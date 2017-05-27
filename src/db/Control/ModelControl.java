/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.Control;

import Tables.AccountTable;
import Tables.CategoryTable;
import Tables.ExpenseTable;
import Tables.TableFiller;
import db.tableInterfaces.TableModel;
import db.util.DBHelper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author F98877A
 */
public class ModelControl {

    private static AccountTableControl accountTblCtrl;
    private static ExpenseTableControl expenseTblCtrl;
    private static CategoryTableControl categoryTblCtrl;

    static {
        try {
            start();
            createTables();
            fillTables();
        } catch (SQLException ex) {
            Logger.getLogger(ModelControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void start() throws SQLException {
        accountTblCtrl = new AccountTableControl();
        expenseTblCtrl = new ExpenseTableControl();
        categoryTblCtrl = new CategoryTableControl();

        accountTblCtrl.setConnection(DBHelper.getConnection());
        expenseTblCtrl.setConnection(DBHelper.getConnection());
        categoryTblCtrl.setConnection(DBHelper.getConnection());
    }

    public static void createTables() throws SQLException {
        accountTblCtrl.createTable();
        expenseTblCtrl.createTable();
        categoryTblCtrl.createTable();
        accountTblCtrl.createStatements();
        expenseTblCtrl.createStatements();
        categoryTblCtrl.createStatements();
    }

    public static void dropTables() throws SQLException {
        accountTblCtrl.dropTable();
        expenseTblCtrl.dropTable();
        categoryTblCtrl.dropTable();
    }

    public static void save(TableModel tableModel) throws SQLException {

        if (tableModel instanceof AccountTable) {
            accountTblCtrl.save(tableModel);
        } else if (tableModel instanceof ExpenseTable) {
            expenseTblCtrl.save(tableModel);
        } else if (tableModel instanceof CategoryTable) {
            categoryTblCtrl.save(tableModel);
        } else {
            throw new SQLException("Unknow object " + tableModel.getClass());
        }
    }

    public static TableModel load(Class clazz, Object id) throws SQLException {
        TableModel tableModel = null;
        Object classObject = null;
        try {
            classObject = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SQLException("Object class instance requires default constructor or static inner class", ex);
        }

        if (classObject instanceof AccountTable) {
            tableModel = accountTblCtrl.load(id);
        } else {
            throw new SQLException("Unknow object " + clazz);
        }

        return tableModel;
    }

    public static List<TableModel> select(Class clazz) throws SQLException {
        List<TableModel> tableModelList = new ArrayList();
        Object classObject = null;
        try {
            classObject = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SQLException("Object class instance requires default constructor or static inner class", ex);
        }

        if (classObject instanceof CategoryTable) {
            tableModelList = categoryTblCtrl.select();
        } else {
            throw new SQLException("Unknow object " + clazz);
        }

        return tableModelList;
    }

    private static void fillTables() {
        TableFiller filler = new TableFiller();
        filler.categoryFiller();
    }

}
