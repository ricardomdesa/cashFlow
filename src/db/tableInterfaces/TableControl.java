/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.tableInterfaces;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author F98877A
 */
public interface TableControl {

    public void createTable() throws SQLException;

    public void dropTable() throws SQLException;

    public void save(TableModel tableModel) throws SQLException;

    public void remove(TableModel tableModel) throws SQLException;

    public void populate(TableModel tableModel) throws SQLException;

    public void update(TableModel tableModel) throws SQLException; //, NonInstanceofTableModelException;

    public TableModel load(Object id) throws SQLException;

    public List<TableModel> select() throws SQLException;

}
