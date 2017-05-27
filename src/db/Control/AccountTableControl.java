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
                + "  ACC_VALUE INTEGER NOT NULL,  "
                + "  ACC_TOTAL_EXPENSE INTEGER NOT NULL,  "
                + "  ACC_TOTAL_INCOME INTEGER NOT NULL,  "
                + "  ACC_NAME VARCHAR(256) NOT NULL,   " //Itau, carteira, etc
                + "  ACC_STS VARCHAR(128) NOT NULL,  "
                + "  ACC_TYPE VARCHAR(256) NOT NULL,  " // Poupanca, corrente, salario
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
                + "   ACC_HASH_ID ) "
                + "  values (?, ?, ?, ?, ?, ?, ?) ";

        protected static final String SQL_DELETE = "delete from " + ACCOUNT_TABLE + " where ACC_OID = ?";

        protected static final String SQL_LOAD
                = " select "
                + "  t.ACC_VALUE as ACC_VALUE, "
                + "  t.ACC_TOTAL_EXPENSE as ACC_TOTAL_EXPENSE, "
                + "  t.ACC_TOTAL_INCOME as ACC_TOTAL_INCOME, "
                + "  t.ACC_NAME as ACC_NAME, "
                + "  t.ACC_STS as ACC_STS, "
                + "  t.ACC_TYPE as ACC_TYPE, "
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

                AccountTable testeTable = (AccountTable) tableModel;

                testeTable.computeHash();

                pstn_insert.setInt(1, testeTable.getValue());
                pstn_insert.setInt(2, testeTable.getTotalExpense());
                pstn_insert.setInt(3, testeTable.getTotalIncome());
                pstn_insert.setString(4, testeTable.getName());
                pstn_insert.setString(5, testeTable.getStatus());
                pstn_insert.setString(6, testeTable.getType());
                pstn_insert.setString(7, testeTable.getHashId());

                pstn_insert.executeUpdate();

                resultSet = pstn_insert.getGeneratedKeys();

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    testeTable.setOid(id);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                account.setValue(resultSet.getInt("ACC_VALUE"));
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}