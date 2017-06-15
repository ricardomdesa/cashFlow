/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.Control;

import Tables.AccountTable;
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
 * @author F98877A
 */
public class AccountTableControl implements TableControl {

    protected static class Config {

        protected static final String ACCOUNT_TABLE = "CASHFLOWDB.ACCOUNT_TABLE";

        protected static final String SQL_CREATE = " create table " + ACCOUNT_TABLE + " ( "
                + "  ACC_OID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),  " //-- object identification
                + "  ACC_VALUE DOUBLE NOT NULL,  "
                + "  ACC_TOTAL_EXPENSE DOUBLE NOT NULL,  "
                + "  ACC_TOTAL_INCOME DOUBLE NOT NULL,  "
                + "  ACC_NAME VARCHAR(256) NOT NULL,   " //Itau, carteira, etc
                + "  ACC_STS VARCHAR(128) NOT NULL,  "
                + "  ACC_TYPE VARCHAR(256) NOT NULL,  " // Poupanca, corrente, salario
                + "  ACC_DAY INT NOT NULL,  "
                + "  ACC_MONTH INT NOT NULL,  "
                + "  ACC_YEAR INT NOT NULL,  "
                + "  ACC_HASH_ID VARCHAR(256) NOT NULL, "// -- hash ('CA_NAME'+'CA_VARIANT') //Ex. ITAU + Corrente
                + "  CONSTRAINT ACC_PK_OID PRIMARY KEY (ACC_OID),  "
                + "  CONSTRAINT ACC_UC_HASH_ID UNIQUE (ACC_HASH_ID)) ";

        protected static final String SQL_DROP = "drop table " + ACCOUNT_TABLE;

        protected static final String SQL_INSERT
                = " insert into " + ACCOUNT_TABLE + " ( "
                + "   ACC_VALUE, "
                + "   ACC_TOTAL_EXPENSE, "
                + "   ACC_TOTAL_INCOME, "
                + "   ACC_NAME, "
                + "   ACC_STS, "
                + "   ACC_TYPE,"
                + "   ACC_DAY,"
                + "   ACC_MONTH,"
                + "   ACC_YEAR,"
                + "   ACC_HASH_ID ) "
                + "  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        protected static final String SQL_DELETE = "delete from " + ACCOUNT_TABLE + " where ACC_OID = ?";

        protected static final String SQL_LOAD
                = " select "
                + "  t.ACC_VALUE as ACC_VALUE, "
                + "  t.ACC_TOTAL_EXPENSE as ACC_TOTAL_EXPENSE, "
                + "  t.ACC_TOTAL_INCOME as ACC_TOTAL_INCOME, "
                + "  t.ACC_NAME as ACC_NAME, "
                + "  t.ACC_STS as ACC_STS, "
                + "  t.ACC_TYPE as ACC_TYPE, "
                + "  t.ACC_DAY as ACC_DAY, "
                + "  t.ACC_MONTH as ACC_MONTH, "
                + "  t.ACC_YEAR as ACC_YEAR, "
                + "  t.ACC_HASH_ID as ACC_HASH_ID "
                + " from " + ACCOUNT_TABLE + " as t "
                + " where t.ACC_OID=? ";

        protected static final String SQL_SELECT
                = " select "
                + "  t.ACC_OID as ACC_OID, "
                + "  t.ACC_VALUE as ACC_VALUE, "
                + "  t.ACC_TOTAL_EXPENSE as ACC_TOTAL_EXPENSE, "
                + "  t.ACC_TOTAL_INCOME as ACC_TOTAL_INCOME, "
                + "  t.ACC_NAME as ACC_NAME, "
                + "  t.ACC_STS as ACC_STS, "
                + "  t.ACC_TYPE as ACC_TYPE, "
                + "  t.ACC_DAY as ACC_DAY, "
                + "  t.ACC_MONTH as ACC_MONTH, "
                + "  t.ACC_YEAR as ACC_YEAR, "
                + "  t.ACC_HASH_ID as ACC_HASH_ID "
                + " from " + ACCOUNT_TABLE + " as t";

        protected static final String SQL_UPDATE
                = " update " + ACCOUNT_TABLE + " as t "
                + "  set "
                + "   t.ACC_VALUE = ?, "
                + "   t.ACC_TOTAL_EXPENSE = ?, "
                + "   t.ACC_TOTAL_INCOME = ?, "
                + "   t.ACC_NAME = ?, "
                + "   t.ACC_STS = ?, "
                + "   t.ACC_TYPE = ?, "
                + "   t.ACC_DAY = ?, "
                + "   t.ACC_MONTH = ?, "
                + "   t.ACC_YEAR = ?, "
                + "   t.ACC_HASH_ID = ? "
                + "  where t.ACC_OID = ? ";

    }

    protected DBHelper conn;
    protected PreparedStatement pstn_insert;
    protected PreparedStatement pstn_delete;
    protected PreparedStatement pstn_load;
    protected PreparedStatement pstn_select;
    protected PreparedStatement pstn_update;

    public AccountTableControl() {
    }

    public void setConnection(DBHelper conn) {
        this.conn = conn;
    }

    public DBHelper getConnection() {
        return conn;
    }

    public void createStatements() throws SQLException {
        pstn_insert = conn.prepareStatement(Config.SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        pstn_delete = conn.prepareStatement(Config.SQL_DELETE);
        pstn_load = conn.prepareStatement(Config.SQL_LOAD);
        pstn_select = conn.prepareStatement(Config.SQL_SELECT);
        pstn_update = conn.prepareStatement(Config.SQL_UPDATE);
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
            if (tableModel instanceof AccountTable) {

                AccountTable accTable = (AccountTable) tableModel;

                accTable.computeHash();

                pstn_insert.setDouble(1, accTable.getValue());
                pstn_insert.setDouble(2, accTable.getTotalExpense());
                pstn_insert.setDouble(3, accTable.getTotalIncome());
                pstn_insert.setString(4, accTable.getName());
                pstn_insert.setString(5, accTable.getStatus());
                pstn_insert.setString(6, accTable.getType());
                pstn_insert.setInt(7, accTable.getDay());
                pstn_insert.setInt(8, accTable.getMonth());
                pstn_insert.setInt(9, accTable.getYear());
                pstn_insert.setString(10, accTable.getHashId());

                pstn_insert.executeUpdate();

                resultSet = pstn_insert.getGeneratedKeys();

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    accTable.setOid(id);
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

            if (tableModel instanceof AccountTable) {
                AccountTable tableIdentification = (AccountTable) tableModel;

                pstn_delete.setInt(1, tableIdentification.getOid());

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
        try {

            if (tableModel instanceof AccountTable) {
                AccountTable accountTable = (AccountTable) tableModel;

                accountTable.computeHash();

                pstn_update.setDouble(1, accountTable.getValue());
                pstn_update.setDouble(2, accountTable.getTotalExpense());
                pstn_update.setDouble(3, accountTable.getTotalIncome());
                pstn_update.setString(4, accountTable.getName());
                pstn_update.setString(5, accountTable.getStatus());
                pstn_update.setString(6, accountTable.getType());
                pstn_update.setInt(7, accountTable.getDay());
                pstn_update.setInt(8, accountTable.getMonth());
                pstn_update.setInt(9, accountTable.getYear());
                pstn_update.setString(10, accountTable.getHashId());

                pstn_update.setInt(11, accountTable.getOid());

                pstn_update.executeUpdate();

            }

        } catch (SQLException e) {
            throw e;
        } finally {
            conn.commit();
        }
    }

    @Override
    public TableModel load(Object id) throws SQLException {
        AccountTable account = null;
        ResultSet resultSet = null;
        try {

            if (!(id instanceof Integer)) {
                throw new java.lang.IllegalArgumentException("Argument must be instanceof by Integer class");
            }

            pstn_load.setInt(1, (Integer) id);

            resultSet = pstn_load.executeQuery();

            if (resultSet.next()) {
                account = new AccountTable();

                account.setOid((Integer) id);
                account.setName(resultSet.getString("ACC_NAME"));
                account.setStatus(resultSet.getString("ACC_STS"));
                account.setType(resultSet.getString("ACC_TYPE"));
                account.setDay(resultSet.getInt("ACC_DAY"));
                account.setMonth(resultSet.getInt("ACC_MONTH"));
                account.setYear(resultSet.getInt("ACC_YEAR"));
                account.setValue(resultSet.getDouble("ACC_VALUE"));
                account.setTotalExpense(resultSet.getInt("ACC_TOTAL_EXPENSE"));
                account.setTotalIncome(resultSet.getInt("ACC_TOTAL_INCOME"));
                account.setHashId(resultSet.getString("ACC_HASH_ID"));
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return (TableModel) account;
    }

    @Override
    public List<TableModel> select() throws SQLException {
        ResultSet resultSet = null;
        List<TableModel> list = new ArrayList();

        try {

            resultSet = pstn_select.executeQuery();

            while (resultSet.next()) {

                AccountTable account = new AccountTable();

                account.setOid(resultSet.getInt("ACC_OID"));
                account.setName(resultSet.getString("ACC_NAME"));
                account.setStatus(resultSet.getString("ACC_STS"));
                account.setType(resultSet.getString("ACC_TYPE"));
                account.setDay(resultSet.getInt("ACC_DAY"));
                account.setMonth(resultSet.getInt("ACC_MONTH"));
                account.setYear(resultSet.getInt("ACC_YEAR"));
                account.setValue(resultSet.getDouble("ACC_VALUE"));
                account.setTotalExpense(resultSet.getInt("ACC_TOTAL_EXPENSE"));
                account.setTotalIncome(resultSet.getInt("ACC_TOTAL_INCOME"));
                account.setHashId(resultSet.getString("ACC_HASH_ID"));

                list.add((TableModel) account);
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return list;
    }

}
