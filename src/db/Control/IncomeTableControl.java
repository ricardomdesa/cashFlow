/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.Control;

import Tables.IncomeTable;
import db.tableInterfaces.TableControl;
import db.tableInterfaces.TableModel;
import db.util.DBHelper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author f98877a
 */
public class IncomeTableControl implements TableControl {

    public static class Config {

        protected static final String INCOME_TABLE = "CASHFLOWDB.INCOME_TABLE";

        protected static final String SQL_CREATE = " create table " + INCOME_TABLE + " ( "
                + "  INC_OID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),  " //-- object identification
                + "  INC_ACC_OID INTEGER NOT NULL,  "
                + "  INC_VALUE DOUBLE NOT NULL,  "
                + "  INC_RECEIVED BOOLEAN NOT NULL,  "
                + "  INC_REPEAT BOOLEAN NOT NULL,  "
                + "  INC_DESCRIPTION VARCHAR(256) NOT NULL,   "
                + "  INC_CATEGORY_ID INT NOT NULL,  "
                + "  INC_DAY INTEGER NOT NULL,  "
                + "  INC_MONTH INTEGER NOT NULL,  "
                + "  INC_YEAR INTEGER NOT NULL,  "
                + "  INC_HASH_ID VARCHAR(256) NOT NULL, "
                + "  CONSTRAINT INC_PK_OID PRIMARY KEY (INC_OID),  "
                + "  CONSTRAINT INC_UC_HASH_ID UNIQUE (INC_HASH_ID)) ";

        protected static final String SQL_DROP = "drop table " + INCOME_TABLE;

        protected static final String SQL_INSERT
                = " insert into " + INCOME_TABLE + " ( "
                + "   INC_ACC_OID, "
                + "   INC_VALUE, "
                + "   INC_RECEIVED, "
                + "   INC_REPEAT, "
                + "   INC_DESCRIPTION, "
                + "   INC_CATEGORY_ID, "
                + "   INC_DAY,"
                + "   INC_MONTH,"
                + "   INC_YEAR,"
                + "   INC_HASH_ID ) "
                + "  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        protected static final String SQL_DELETE = "delete from " + INCOME_TABLE + " where INC_OID = ?";

        protected static final String SQL_LOAD
                = " select "
                + "  t.INC_ACC_OID as INC_ACC_OID, "
                + "  t.INC_VALUE as INC_VALUE, "
                + "  t.INC_RECEIVED as INC_RECEIVED, "
                + "  t.INC_REPEAT as INC_REPEAT, "
                + "  t.INC_DESCRIPTION as INC_DESCRIPTION, "
                + "  t.INC_CATEGORY_ID as INC_CATEGORY_ID, "
                + "  t.INC_DAY as INC_DAY, "
                + "  t.INC_MONTH as INC_MONTH, "
                + "  t.INC_YEAR as INC_YEAR, "
                + "  t.INC_HASH_ID as INC_HASH_ID "
                + " from " + INCOME_TABLE + " as t "
                + " where t.INC_OID=? ";

        protected static final String SQL_SELECT
                = " select "
                + "  t.INC_OID as INC_OID, "
                + "  t.INC_ACC_OID as INC_ACC_OID, "
                + "  t.INC_VALUE as INC_VALUE, "
                + "  t.INC_RECEIVED as INC_RECEIVED, "
                + "  t.INC_REPEAT as INC_REPEAT, "
                + "  t.INC_DESCRIPTION as INC_DESCRIPTION, "
                + "  t.INC_CATEGORY_ID as INC_CATEGORY_ID, "
                + "  t.INC_DAY as INC_DAY, "
                + "  t.INC_MONTH as INC_MONTH, "
                + "  t.INC_YEAR as INC_YEAR, "
                + "  t.INC_HASH_ID as INC_HASH_ID "
                + " from " + INCOME_TABLE + " as t";

        protected static final String SQL_SELECT_BY_MONTH
                = " select "
                + "  t.INC_OID as INC_OID, "
                + "  t.INC_VALUE as INC_VALUE, "
                + "  t.INC_RECEIVED as INC_RECEIVED, "
                + "  t.INC_REPEAT as INC_REPEAT, "
                + "  t.INC_DESCRIPTION as INC_DESCRIPTION, "
                + "  t.INC_CATEGORY_ID as INC_CATEGORY_ID, "
                + "  t.INC_DAY as INC_DAY, "
                + "  t.INC_YEAR as INC_YEAR, "
                + "  t.INC_HASH_ID as INC_HASH_ID "
                + " from " + INCOME_TABLE + " as t"
                + " where t.INC_MONTH=? and t.INC_ACC_OID=?";

        protected static final String SQL_UPDATE
                = " update " + INCOME_TABLE + " as t "
                + "  set "
                + "   t.INC_ACC_OID = ?, "
                + "   t.INC_VALUE = ?, "
                + "   t.INC_RECEIVED = ?, "
                + "   t.INC_REPEAT = ?, "
                + "   t.INC_DESCRIPTION = ?, "
                + "   t.INC_CATEGORY_ID = ?, "
                + "   t.INC_DAY = ?, "
                + "   t.INC_MONTH = ?, "
                + "   t.INC_YEAR = ?, "
                + "   t.INC_HASH_ID = ? "
                + "  where t.INC_OID = ? ";

    }
    protected DBHelper conn;
    protected PreparedStatement pstn_insert;
    protected PreparedStatement pstn_delete;
    protected PreparedStatement pstn_load;
    protected PreparedStatement pstn_select;
    protected PreparedStatement pstn_selectByMonth;
    protected PreparedStatement pstn_update;

    public IncomeTableControl() {
    }

    public void setConnection(DBHelper conn) {
        this.conn = conn;
    }

    public DBHelper getConnection() {
        return conn;
    }

    public void createStatements() throws SQLException {
        pstn_insert = conn.prepareStatement(IncomeTableControl.Config.SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        pstn_delete = conn.prepareStatement(IncomeTableControl.Config.SQL_DELETE);
        pstn_load = conn.prepareStatement(IncomeTableControl.Config.SQL_LOAD);
        pstn_select = conn.prepareStatement(IncomeTableControl.Config.SQL_SELECT);
        pstn_selectByMonth = conn.prepareStatement(IncomeTableControl.Config.SQL_SELECT_BY_MONTH);
        pstn_update = conn.prepareStatement(IncomeTableControl.Config.SQL_UPDATE);
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
            if (tableModel instanceof IncomeTable) {

                IncomeTable incTable = (IncomeTable) tableModel;

                incTable.computeHash();

                pstn_insert.setInt(1, incTable.getAccOid());
                pstn_insert.setDouble(2, incTable.getValue());
                pstn_insert.setBoolean(3, incTable.isReceived());
                pstn_insert.setBoolean(4, incTable.isRepeat());
                pstn_insert.setString(5, incTable.getDescription());
                pstn_insert.setInt(6, incTable.getCategory());
                pstn_insert.setInt(7, incTable.getDay());
                pstn_insert.setInt(8, incTable.getMonth());
                pstn_insert.setInt(9, incTable.getYear());
                pstn_insert.setString(10, incTable.getHashId());

                pstn_insert.executeUpdate();

                resultSet = pstn_insert.getGeneratedKeys();

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    incTable.setOid(id);
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

            if (tableModel instanceof IncomeTable) {
                IncomeTable incTable = (IncomeTable) tableModel;

                pstn_delete.setInt(1, incTable.getOid());
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
        IncomeTable incTable = null;
        ResultSet resultSet = null;
        try {

            if (!(id instanceof Integer)) {
                throw new java.lang.IllegalArgumentException("Argument must be instanceof by Integer class");
            }

            pstn_load.setInt(1, (Integer) id);

            resultSet = pstn_load.executeQuery();

            if (resultSet.next()) {
                incTable = new IncomeTable();

                incTable.setOid((Integer) id);
                incTable.setDescription(resultSet.getString("INC_DESCRIPTION"));
                incTable.setCategory(resultSet.getInt("INC_CATEGORY_ID"));
                incTable.setDay(resultSet.getInt("INC_DAY"));
                incTable.setMonth(resultSet.getInt("INC_MONTH"));
                incTable.setYear(resultSet.getInt("INC_YEAR"));
                incTable.setValue(resultSet.getDouble("INC_VALUE"));
                incTable.setReceived(resultSet.getBoolean("INC_RECEIVED"));
                incTable.setRepeat(resultSet.getBoolean("INC_REPEAT"));
                incTable.setHashId(resultSet.getString("INC_HASH_ID"));
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return (TableModel) incTable;
    }

    @Override
    public List<TableModel> select() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<TableModel> selectByMonth(int month, int acc) throws SQLException {
        List<TableModel> list = new ArrayList();
        ResultSet resultSet = null;

        try {
            pstn_selectByMonth.setInt(1, month);
            pstn_selectByMonth.setInt(2, acc);
            resultSet = pstn_selectByMonth.executeQuery();

            while (resultSet.next()) {

                IncomeTable table = new IncomeTable();

                table.setOid(resultSet.getInt("INC_OID"));
                table.setDescription(resultSet.getString("INC_DESCRIPTION"));
                table.setAccOid(acc);
                table.setCategory(resultSet.getInt("INC_CATEGORY_ID"));
                table.setDay(resultSet.getInt("INC_DAY"));
                table.setMonth(month);
                table.setYear(resultSet.getInt("INC_YEAR"));
                table.setValue(resultSet.getDouble("INC_VALUE"));
                table.setReceived(resultSet.getBoolean("INC_RECEIVED"));
                table.setRepeat(resultSet.getBoolean("INC_REPEAT"));
                table.setHashId(resultSet.getString("INC_HASH_ID"));

                list.add((TableModel) table);
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }

        return list;
    }

}
