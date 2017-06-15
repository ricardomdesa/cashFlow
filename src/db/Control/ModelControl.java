/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.Control;

import Tables.AccountTable;
import Tables.CategoryTable;
import Tables.ExpenseTable;
import Tables.IncomeTable;
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
    private static IncomeTableControl incomeTblCtrl;
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
        incomeTblCtrl = new IncomeTableControl();
        categoryTblCtrl = new CategoryTableControl();

        accountTblCtrl.setConnection(DBHelper.getConnection());
        expenseTblCtrl.setConnection(DBHelper.getConnection());
        incomeTblCtrl.setConnection(DBHelper.getConnection());
        categoryTblCtrl.setConnection(DBHelper.getConnection());
    }

    public static void createTables() throws SQLException {
        accountTblCtrl.createTable();
        expenseTblCtrl.createTable();
        incomeTblCtrl.createTable();
        categoryTblCtrl.createTable();
        accountTblCtrl.createStatements();
        expenseTblCtrl.createStatements();
        incomeTblCtrl.createStatements();
        categoryTblCtrl.createStatements();
    }

    public static void dropTables() throws SQLException {
        accountTblCtrl.dropTable();
        expenseTblCtrl.dropTable();
        incomeTblCtrl.dropTable();
        categoryTblCtrl.dropTable();
    }

    public static void update(TableModel tableModel) throws SQLException {

        if (tableModel instanceof AccountTable) {
            accountTblCtrl.update(tableModel);
        } else if (tableModel instanceof ExpenseTable) {
            expenseTblCtrl.update(tableModel);
        } else if (tableModel instanceof IncomeTable) {
            incomeTblCtrl.update(tableModel);
        } else if (tableModel instanceof CategoryTable) {
            categoryTblCtrl.update(tableModel);
        } else {
            throw new SQLException("Unknow object " + tableModel.getClass());
        }
    }

    public static void save(TableModel tableModel) throws SQLException {

        if (tableModel instanceof AccountTable) {
            accountTblCtrl.save(tableModel);
        } else if (tableModel instanceof ExpenseTable) {
            expenseTblCtrl.save(tableModel);
        } else if (tableModel instanceof IncomeTable) {
            incomeTblCtrl.save(tableModel);
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
        } else if (classObject instanceof ExpenseTable) {
            tableModel = expenseTblCtrl.load(id);
        } else if (classObject instanceof IncomeTable) {
            tableModel = incomeTblCtrl.load(id);
        } else if (classObject instanceof CategoryTable) {
            tableModel = categoryTblCtrl.load(id);
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
        } else if (classObject instanceof ExpenseTable) {
            tableModelList = expenseTblCtrl.select();
        } else if (classObject instanceof AccountTable) {
            tableModelList = accountTblCtrl.select();
        } else {
            throw new SQLException("Unknow object " + clazz);
        }

        return tableModelList;
    }

    public static List<TableModel> selectWithParam(Class clazz, Object id, Object accOid) throws SQLException {
        List<TableModel> tableModelList = new ArrayList();
        Object classObject = null;
        try {
            classObject = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SQLException("Object class instance requires default constructor or static inner class", ex);
        }

        if (classObject instanceof ExpenseTable) {
            tableModelList = expenseTblCtrl.selectByMonth((Integer) id, (Integer) accOid);
        } else if (classObject instanceof IncomeTable) {
            tableModelList = incomeTblCtrl.selectByMonth((Integer) id, (Integer) accOid);
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
