/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.Control;

import Tables.CategoryTable;
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
public class CategoryTableControl implements TableControl {

    public static class Config {

        protected static final String CATEGORY_TABLE = "CASHFLOWDB.CATEGORY_TABLE";

        protected static final String SQL_CREATE = " create table " + CATEGORY_TABLE + " ( "
                + "  CAT_OID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),  " //-- object identification
                + "  CAT_CATEGORY VARCHAR(256) NOT NULL,  "
                + "  CAT_TYPE VARCHAR(256) NOT NULL,  "
                + "  CAT_COLOR VARCHAR(256) NOT NULL,  "
                + "  CAT_GOAL_VAL DOUBLE NOT NULL,  "
                + "  CAT_PERCENTAGE DOUBLE NOT NULL,  "
                + "  CAT_HASH_ID VARCHAR(256) NOT NULL, "
                + "  CONSTRAINT CAT_PK_OID PRIMARY KEY (CAT_OID),  "
                + "  CONSTRAINT CAT_UC_HASH_ID UNIQUE (CAT_HASH_ID)) ";

        protected static final String SQL_DROP = "drop table " + CATEGORY_TABLE;

        protected static final String SQL_INSERT
                = " insert into " + CATEGORY_TABLE + " ( "
                + "   CAT_CATEGORY, "
                + "   CAT_TYPE, "
                + "   CAT_COLOR, "
                + "   CAT_GOAL_VAL, "
                + "   CAT_PERCENTAGE, "
                + "   CAT_HASH_ID ) "
                + "  values (?, ?, ?, ?, ?, ?) ";

        protected static final String SQL_DELETE = "delete from " + CATEGORY_TABLE + " where CAT_OID = ?";

        protected static final String SQL_LOAD
                = " select "
                + "  t.CAT_CATEGORY as CAT_CATEGORY, "
                + "  t.CAT_TYPE as CAT_TYPE, "
                + "  t.CAT_COLOR as CAT_COLOR, "
                + "  t.CAT_GOAL_VAL as CAT_GOAL_VAL, "
                + "  t.CAT_PERCENTAGE as CAT_PERCENTAGE, "
                + "  t.CAT_HASH_ID as CAT_HASH_ID "
                + " from " + CATEGORY_TABLE + " as t "
                + " where t.CAT_OID=? ";

        protected static final String SQL_SELECT
                = " select "
                + "  t.CAT_OID as CAT_OID, "
                + "  t.CAT_CATEGORY as CAT_CATEGORY, "
                + "  t.CAT_TYPE as CAT_TYPE, "
                + "  t.CAT_COLOR as CAT_COLOR, "
                + "  t.CAT_GOAL_VAL as CAT_GOAL_VAL, "
                + "  t.CAT_PERCENTAGE as CAT_PERCENTAGE, "
                + "  t.CAT_HASH_ID as CAT_HASH_ID "
                + " from " + CATEGORY_TABLE + " as t";

        protected static final String SQL_UPDATE
                = " update " + CATEGORY_TABLE + " as t "
                + "  set "
                + "   t.CAT_CATEGORY = ?, "
                + "   t.CAT_TYPE = ?, "
                + "   t.CAT_COLOR = ?, "
                + "   t.CAT_GOAL_VAL = ?, "
                + "   t.CAT_PERCENTAGE = ?, "
                + "   t.CAT_HASH_ID = ? "
                + "  where t.CAT_OID = ? ";

    }

    protected DBHelper conn;
    protected PreparedStatement pstn_insert;
    protected PreparedStatement pstn_delete;
    protected PreparedStatement pstn_load;
    protected PreparedStatement pstn_select;
    protected PreparedStatement pstn_update;

    public CategoryTableControl() {
    }

    public void setConnection(DBHelper conn) {
        this.conn = conn;
    }

    public DBHelper getConnection() {
        return conn;
    }

    public void createStatements() throws SQLException {
        pstn_insert = conn.prepareStatement(CategoryTableControl.Config.SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        pstn_delete = conn.prepareStatement(CategoryTableControl.Config.SQL_DELETE);
        pstn_load = conn.prepareStatement(CategoryTableControl.Config.SQL_LOAD);
        pstn_select = conn.prepareStatement(CategoryTableControl.Config.SQL_SELECT);
        pstn_update = conn.prepareStatement(CategoryTableControl.Config.SQL_UPDATE);
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
            if (tableModel instanceof CategoryTable) {

                CategoryTable catTable = (CategoryTable) tableModel;

                catTable.computeHash();

                pstn_insert.setString(1, catTable.getCategory());
                pstn_insert.setString(2, catTable.getType());
                pstn_insert.setString(3, catTable.getColor());
                pstn_insert.setDouble(4, catTable.getGoalValue());
                pstn_insert.setDouble(5, catTable.getPercentage());
                pstn_insert.setString(6, catTable.getHashId());

                pstn_insert.executeUpdate();

                resultSet = pstn_insert.getGeneratedKeys();

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    catTable.setOid(id);
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

            if (tableModel instanceof CategoryTable) {
                CategoryTable catTable = (CategoryTable) tableModel;

                pstn_delete.setInt(1, catTable.getOid());
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
        CategoryTable category = null;
        ResultSet resultSet = null;
        try {

            if (!(id instanceof Integer)) {
                throw new java.lang.IllegalArgumentException("Argument must be instanceof by Integer class");
            }

            pstn_load.setInt(1, (Integer) id);

            resultSet = pstn_load.executeQuery();

            if (resultSet.next()) {
                category = new CategoryTable();

                category.setOid((Integer) id);
                category.setCategory(resultSet.getString("CAT_CATEGORY"));
                category.setType(resultSet.getString("CAT_TYPE"));
                category.setColor(resultSet.getString("CAT_COLOR"));
                category.setGoalValue(resultSet.getDouble("CAT_GOAL_VAL"));
                category.setPercentage(resultSet.getDouble("CAT_PERCENTAGE"));
                category.setHashId(resultSet.getString("CAT_HASH_ID"));
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return (TableModel) category;
    }

    @Override
    public List<TableModel> select() throws SQLException {
        ResultSet resultSet = null;
        List<TableModel> list = new ArrayList();

        try {

            resultSet = pstn_select.executeQuery();

            while (resultSet.next()) {

                CategoryTable catTable = new CategoryTable();

                catTable.setOid(resultSet.getInt("CAT_OID"));
                catTable.setCategory(resultSet.getString("CAT_CATEGORY"));
                catTable.setType(resultSet.getString("CAT_TYPE"));
                catTable.setColor(resultSet.getString("CAT_COLOR"));
                catTable.setGoalValue(resultSet.getDouble("CAT_GOAL_VAL"));
                catTable.setPercentage(resultSet.getDouble("CAT_PERCENTAGE"));
                catTable.setHashId(resultSet.getString("CAT_HASH_ID"));

                list.add((TableModel) catTable);
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
