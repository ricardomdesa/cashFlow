/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.Control;

import Tables.ExpenseTable;
import db.tableInterfaces.TableControl;
import db.tableInterfaces.TableModel;
import db.util.DBHelper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author f98877a
 */
public class ExpenseTableControl implements TableControl {

    public static class Config {

        protected static final String EXPENSE_TABLE = "CASHFLOWDB.EXPENSE_TABLE";

        protected static final String SQL_CREATE = " create table " + EXPENSE_TABLE + " ( "
                + "  EXP_OID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),  " //-- object identification
                + "  EXP_ACC_OID INTEGER NOT NULL,  "
                + "  EXP_VALUE DOUBLE NOT NULL,  "
                + "  EXP_PAYED BOOLEAN NOT NULL,  "
                + "  EXP_REPEAT BOOLEAN NOT NULL,  "
                + "  EXP_DESCRIPTION VARCHAR(256) NOT NULL,   "
                + "  EXP_CATEGORY_ID INT NOT NULL,  "
                + "  EXP_DAY INTEGER NOT NULL,  "
                + "  EXP_MONTH INTEGER NOT NULL,  "
                + "  EXP_YEAR INTEGER NOT NULL,  "
                + "  EXP_HASH_ID VARCHAR(256) NOT NULL, "
                + "  CONSTRAINT EXP_PK_OID PRIMARY KEY (EXP_OID),  "
                + "  CONSTRAINT EXP_UC_HASH_ID UNIQUE (EXP_HASH_ID)) ";

        protected static final String SQL_DROP = "drop table " + EXPENSE_TABLE;

        protected static final String SQL_INSERT
                = " insert into " + EXPENSE_TABLE + " ( "
                + "   EXP_ACC_OID, "
                + "   EXP_VALUE, "
                + "   EXP_PAYED, "
                + "   EXP_REPEAT, "
                + "   EXP_DESCRIPTION, "
                + "   EXP_CATEGORY_ID, "
                + "   EXP_DAY,"
                + "   EXP_MONTH,"
                + "   EXP_YEAR,"
                + "   EXP_HASH_ID ) "
                + "  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        protected static final String SQL_DELETE = "delete from " + EXPENSE_TABLE + " where EXP_OID = ?";

        protected static final String SQL_LOAD
                = " select "
                + "  t.EXP_ACC_OID as EXP_ACC_OID, "
                + "  t.EXP_VALUE as EXP_VALUE, "
                + "  t.EXP_PAYED as EXP_PAYED, "
                + "  t.EXP_REPEAT as EXP_REPEAT, "
                + "  t.EXP_DESCRIPTION as EXP_DESCRIPTION, "
                + "  t.EXP_CATEGORY_ID as EXP_CATEGORY_ID, "
                + "  t.EXP_DAY as EXP_DAY, "
                + "  t.EXP_MONTH as EXP_MONTH, "
                + "  t.EXP_YEAR as EXP_YEAR, "
                + "  t.EXP_HASH_ID as EXP_HASH_ID "
                + " from " + EXPENSE_TABLE + " as t "
                + " where t.EXP_OID=? ";

        protected static final String SQL_SELECT
                = " select "
                + "  t.EXP_OID as EXP_OID, "
                + "  t.EXP_ACC_OID as EXP_ACC_OID, "
                + "  t.EXP_VALUE as EXP_VALUE, "
                + "  t.EXP_PAYED as EXP_PAYED, "
                + "  t.EXP_REPEAT as EXP_REPEAT, "
                + "  t.EXP_DESCRIPTION as EXP_DESCRIPTION, "
                + "  t.EXP_CATEGORY_ID as EXP_CATEGORY_ID, "
                + "  t.EXP_DAY as EXP_DAY, "
                + "  t.EXP_MONTH as EXP_MONTH, "
                + "  t.EXP_YEAR as EXP_YEAR, "
                + "  t.EXP_HASH_ID as EXP_HASH_ID "
                + " from " + EXPENSE_TABLE + " as t";

        protected static final String SQL_UPDATE
                = " update " + EXPENSE_TABLE + " as t "
                + "  set "
                + "   t.EXP_ACC_OID = ?, "
                + "   t.EXP_VALUE = ?, "
                + "   t.EXP_PAYED = ?, "
                + "   t.EXP_REPEAT = ?, "
                + "   t.EXP_DESCRIPTION = ?, "
                + "   t.EXP_CATEGORY_ID = ?, "
                + "   t.EXP_DAY = ?, "
                + "   t.EXP_MONTH = ?, "
                + "   t.EXP_YEAR = ?, "
                + "   t.EXP_HASH_ID = ? "
                + "  where t.EXP_OID = ? ";

    }
    protected DBHelper conn;
    protected PreparedStatement pstn_insert;
    protected PreparedStatement pstn_delete;
    protected PreparedStatement pstn_load;
    protected PreparedStatement pstn_select;
    protected PreparedStatement pstn_update;

    public ExpenseTableControl() {
    }

    public void setConnection(DBHelper conn) {
        this.conn = conn;
    }

    public DBHelper getConnection() {
        return conn;
    }

    public void createStatements() throws SQLException {
        pstn_insert = conn.prepareStatement(ExpenseTableControl.Config.SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        pstn_delete = conn.prepareStatement(ExpenseTableControl.Config.SQL_DELETE);
        pstn_load = conn.prepareStatement(ExpenseTableControl.Config.SQL_LOAD);
        pstn_select = conn.prepareStatement(ExpenseTableControl.Config.SQL_SELECT);
        pstn_update = conn.prepareStatement(ExpenseTableControl.Config.SQL_UPDATE);
    }

    @Override
    public void createTable() throws SQLException {
        Statement stmt;

        try {
            stmt = conn.createStatement();
            stmt.execute(Config.SQL_CREATE);
        } catch (SQLException e) {
            if ((e.getErrorCode() != 20000) || (!"X0Y32".equals(e.getSQLState()))) {
                throw e;
            }
        } finally {
            conn.commit();
        }
    }

    @Override
    public void dropTable() throws SQLException {
        Statement stmt;

        try {
            stmt = conn.createStatement();
            stmt.execute(Config.SQL_DROP);

        } catch (SQLException e) {
            if ((e.getErrorCode() != 20000) || (!"42Y55".equals(e.getSQLState()))) {
                throw e;
            }
        } finally {
            conn.commit();
        }
    }

    @Override
    public void save(TableModel tableModel) throws SQLException {
        ResultSet resultSet = null;
        try {
            if (tableModel instanceof ExpenseTable) {

                ExpenseTable expTable = (ExpenseTable) tableModel;

                expTable.computeHash();

                pstn_insert.setInt(1, expTable.getAccOid());
                pstn_insert.setDouble(2, expTable.getValue());
                pstn_insert.setBoolean(3, expTable.isPayed());
                pstn_insert.setBoolean(4, expTable.isRepeat());
                pstn_insert.setString(5, expTable.getDescription());
                pstn_insert.setInt(6, expTable.getCategory());
                pstn_insert.setInt(7, expTable.getDay());
                pstn_insert.setInt(8, expTable.getMonth());
                pstn_insert.setInt(9, expTable.getYear());
                pstn_insert.setString(10, expTable.getHashId());

                pstn_insert.executeUpdate();

                resultSet = pstn_insert.getGeneratedKeys();

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    expTable.setOid(id);
                }
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            conn.commit();
        }
    }

    @Override
    public void remove(TableModel tableModel) throws SQLException {
        try {

            if (tableModel instanceof ExpenseTable) {
                ExpenseTable expTable = (ExpenseTable) tableModel;

                pstn_delete.setInt(1, expTable.getOid());
                pstn_delete.executeUpdate();

            }

        } catch (SQLException e) {
            throw e;
        } finally {
            conn.commit();
        }
    }

    @Override
    public void populate(TableModel tableModel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(TableModel tableModel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TableModel load(Object id) throws SQLException {
        ExpenseTable expense = null;
        ResultSet resultSet = null;
        try {

            if (!(id instanceof Integer)) {
                throw new java.lang.IllegalArgumentException("Argument must be instanceof by Integer class");
            }

            pstn_load.setInt(1, (Integer) id);

            resultSet = pstn_load.executeQuery();

            if (resultSet.next()) {
                expense = new ExpenseTable();

                expense.setOid((Integer) id);
                expense.setDescription(resultSet.getString("EXP_DESCRIPTION"));
                expense.setCategory(resultSet.getInt("EXP_CATEGORY_ID"));
                expense.setDay(resultSet.getInt("EXP_DAY"));
                expense.setMonth(resultSet.getInt("EXP_MONTH"));
                expense.setYear(resultSet.getInt("EXP_YEAR"));
                expense.setValue(resultSet.getDouble("EXP_VALUE"));
                expense.setPayed(resultSet.getBoolean("EXP_PAYED"));
                expense.setRepeat(resultSet.getBoolean("EXP_REPEAT"));
                expense.setHashId(resultSet.getString("EXP_HASH_ID"));
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return (TableModel) expense;
    }

    @Override
    public List<TableModel> select() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
