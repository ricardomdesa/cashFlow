/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import static org.junit.Assert.*;

/**
 *
 * @author Rogério Lecarião Leite
 */
public class DBHelper implements Connection {

    private static DBHelper connInstance;
    private Object driverInstance;
    private Properties props;
    private Connection conn;
    private List<Statement> statements;

    static {

    }

    protected DBHelper() {
        statements = new ArrayList();
        props = new Properties();
    }

    public static DBHelper getConnection() throws SQLException {
        if (connInstance == null) {
            connInstance = new DBHelper();
            connInstance.config();
            connInstance.startup();
            connInstance.onShutdown();
            connInstance.open();
        }

        return connInstance;
    }

    protected static DBHelper getConnection(Properties input) throws SQLException {
        if (connInstance == null) {
            connInstance = new DBHelper();
            connInstance.config(input);
            connInstance.startup();
            connInstance.onShutdown();
            connInstance.open();
        }

        return connInstance;
    }

    protected void config() throws SQLException {

        InputStream input = null;

        try {
            input = new FileInputStream("db\\config.properties");
            props.load(input);
        } catch (IOException ex) {
            try {
                File file = new File("db");
                if (!file.exists() || !file.isDirectory()) {
                    file.mkdir();
                }
                PropApp.store();
                input = new FileInputStream("db\\config.properties");
                props.load(input);
            } catch (IOException ex1) {
                throw new SQLException(ex);
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    throw new SQLException(ex);
                }
            }
        }
    }

    protected void config(Properties input) throws SQLException {
        props = input;
    }

    protected void startup() throws SQLException {
        String driver = props.getProperty("database.driver");

        try {
            driverInstance = Class.forName(driver).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            throw new SQLException(ex);
        }
        System.out.println("Loaded the appropriate driver " + driverInstance.toString());
    }

    protected void onShutdown() {
        final String framework = props.getProperty("database.framework");
        final String protocol = props.getProperty("database.protocol");

        Runtime.getRuntime().addShutdownHook(
                new Thread() {

            @Override
            public void run() {
                WeakReference ref = new WeakReference(driverInstance);

                try {
                    if (isConnected()) {
                        closeStatements();
                        close();
                    }
                } catch (SQLException ex) {
                }

                if (framework.equals("embedded")) {
                    try {

                        DriverManager.getConnection(protocol + ";shutdown=true");

                    } catch (SQLException ex) {
                        if (((ex.getErrorCode() == 50000) && ("XJ015".equals(ex.getSQLState())))) {
                            System.out.println("Derby shut down normally");
                        } else {
                            System.err.println("Derby did not shut down normally");
                            ex.printStackTrace(System.out);
                        }
                    }
                }

                driverInstance = null;

                Runtime.getRuntime().gc();

                assertTrue(ref.get() == null);
            }
        }
        );
    }

    protected void open() throws SQLException {
        Properties prop = new Properties();
        String username = props.getProperty("database.user.name");
        String userpass = props.getProperty("database.user.pass");
        String protocol = props.getProperty("database.protocol");
        String database = props.getProperty("database.dbname");
        String derbyhome = props.getProperty("derby.system.home");

        String url = protocol + derbyhome + database;

        prop.setProperty("user", username);
        prop.setProperty("password", userpass);
        prop.setProperty("create", "true");
        prop.setProperty("derby.system.home", derbyhome);

        try {
            conn = DriverManager.getConnection(url, prop);
            System.out.println("Connected");
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            throw ex;
        }

        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            stmt.execute(
                    "create table " + database + ".bug_create_true" + "("
                    + "tp_oid INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                    + "CONSTRAINT tp_pkoid PRIMARY KEY (tp_oid)"
                    + ")");

            stmt.execute("drop table " + database + ".bug_create_true");

        } finally {
            conn.commit();
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public void closeStatements() throws SQLException {
        try {

            while (!statements.isEmpty()) {
                Statement st = statements.remove(0);
                if (st != null) {
                    st.close();
                    st = null;
                }
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            System.out.println("Statements Closed");
        }
    }

    public void closeStatement(Statement st) throws SQLException {
        try {

            if (statements.remove(st)) {
                st.close();
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            System.out.println("Statement Closed");
        }
    }

    @Override
    public void close() throws SQLException {
        try {

            while (!statements.isEmpty()) {
                Statement st = statements.remove(0);
                try {
                    if (st != null) {
                        st.close();
                        st = null;
                    }
                } catch (SQLException ex) {
                    throw ex;
                }
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            conn.close();
            conn = null;
            System.out.println("Disconnected");
        }
    }

    @Override
    public void commit() throws SQLException {
        conn.commit();
    }

    public boolean isConnected() throws SQLException {
        return (conn != null) && (!conn.isClosed());
    }

    @Override
    public Statement createStatement() throws SQLException {
        Statement st = conn.createStatement();
        statements.add(st);
        return st;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement st = conn.prepareStatement(sql);
        statements.add(st);
        return st;
    }

    public boolean checkTable(String sql) {
        Statement st = null;
        boolean returns = false;

        try {
            st = conn.createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            switch (ex.getSQLState()) {
                case "42X05":
                    System.out.println("Table does not exist");
                    returns = false;
                case "42X14":
                case "42821":
                    System.out.println("Incorrect table definition. Drop table PERSONS_LIST and rerun this program");
                    returns = true;
                    break;
                default:
                    System.out.println("Unhandled SQLException");
                    returns = true;
                    break;
            }
        }
        System.out.println("Table already exist");
        return returns;
    }

    private static void reportFailure(String message) {
        System.err.println("\nData verification failed:");
        System.err.println('\t' + message);
    }

    public static void printSQLException(SQLException e) {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null) {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }

    @Override
    public CallableStatement prepareCall(String string) throws SQLException {
        return conn.prepareCall(string);
    }

    @Override
    public String nativeSQL(String string) throws SQLException {
        return conn.nativeSQL(string);
    }

    @Override
    public void setAutoCommit(boolean bln) throws SQLException {
        conn.setAutoCommit(bln);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return conn.getAutoCommit();
    }

    @Override
    public void rollback() throws SQLException {
        conn.rollback();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return conn.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    @Override
    public void setReadOnly(boolean bln) throws SQLException {
        conn.setReadOnly(bln);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    @Override
    public void setCatalog(String string) throws SQLException {
        conn.setCatalog(string);
    }

    @Override
    public String getCatalog() throws SQLException {
        return conn.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int i) throws SQLException {
        conn.setTransactionIsolation(i);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return conn.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        conn.clearWarnings();
    }

    @Override
    public Statement createStatement(int i, int i1) throws SQLException {
        return conn.createStatement(i, i1);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int i, int i1) throws SQLException {
        return conn.prepareStatement(string, i, i1);
    }

    @Override
    public CallableStatement prepareCall(String string, int i, int i1) throws SQLException {
        return conn.prepareCall(string, i, i1);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return conn.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        conn.setTypeMap(map);
    }

    @Override
    public void setHoldability(int i) throws SQLException {
        conn.setHoldability(i);
    }

    @Override
    public int getHoldability() throws SQLException {
        return conn.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return conn.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String string) throws SQLException {
        return conn.setSavepoint(string);
    }

    @Override
    public void rollback(Savepoint svpnt) throws SQLException {
        conn.rollback(svpnt);
    }

    @Override
    public void releaseSavepoint(Savepoint svpnt) throws SQLException {
        conn.releaseSavepoint(svpnt);
    }

    @Override
    public Statement createStatement(int i, int i1, int i2) throws SQLException {
        return conn.createStatement(i, i1, i2);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int i, int i1, int i2) throws SQLException {
        return conn.prepareStatement(string, i, i1, i2);
    }

    @Override
    public CallableStatement prepareCall(String string, int i, int i1, int i2) throws SQLException {
        return conn.prepareCall(string, i, i1, i2);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int i) throws SQLException {
        return conn.prepareStatement(string, i);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int[] ints) throws SQLException {
        return conn.prepareStatement(string, ints);
    }

    @Override
    public PreparedStatement prepareStatement(String string, String[] strings) throws SQLException {
        return conn.prepareStatement(string, strings);
    }

    @Override
    public Clob createClob() throws SQLException {
        return conn.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return conn.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return conn.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return conn.createSQLXML();
    }

    @Override
    public boolean isValid(int i) throws SQLException {
        return conn.isValid(i);
    }

    @Override
    public void setClientInfo(String string, String string1) throws SQLClientInfoException {
        conn.setClientInfo(string, string1);
    }

    @Override
    public void setClientInfo(Properties prprts) throws SQLClientInfoException {
        conn.setClientInfo(prprts);
    }

    @Override
    public String getClientInfo(String string) throws SQLException {
        return conn.getClientInfo(string);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return conn.getClientInfo();
    }

    @Override
    public Array createArrayOf(String string, Object[] os) throws SQLException {
        return conn.createArrayOf(string, os);
    }

    @Override
    public Struct createStruct(String string, Object[] os) throws SQLException {
        return conn.createStruct(string, os);
    }

    @Override
    public void setSchema(String string) throws SQLException {
        conn.setSchema(string);
    }

    @Override
    public String getSchema() throws SQLException {
        return conn.getSchema();
    }

    @Override
    public void abort(Executor exctr) throws SQLException {
        conn.abort(exctr);
    }

    @Override
    public void setNetworkTimeout(Executor exctr, int i) throws SQLException {
        conn.setNetworkTimeout(exctr, i);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return conn.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> type) throws SQLException {
        return conn.unwrap(type);
    }

    @Override
    public boolean isWrapperFor(Class<?> type) throws SQLException {
        return conn.isWrapperFor(type);
    }

    public static void main(String[] args) {
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        Statement s = null;
        ResultSet rs = null;

        try {
            DBHelper conn = getConnection();

            s = conn.createStatement();

            s.execute("create table location(num int, addr varchar(40))");

            psInsert = conn.prepareStatement(
                    "insert into location values (?, ?)");
            psUpdate = conn.prepareStatement(
                    "update location set num=?, addr=? where num=?");

            System.out.println("Created table location");

            psInsert.setInt(1, 1956);
            psInsert.setString(2, "Webster St.");
            psInsert.executeUpdate();
            System.out.println("Inserted 1956 Webster");

            psInsert.setInt(1, 1910);
            psInsert.setString(2, "Union St.");
            psInsert.executeUpdate();
            System.out.println("Inserted 1910 Union");

            psUpdate.setInt(1, 180);
            psUpdate.setString(2, "Grand Ave.");
            psUpdate.setInt(3, 1956);
            psUpdate.executeUpdate();
            System.out.println("Updated 1956 Webster to 180 Grand");

            psUpdate.setInt(1, 300);
            psUpdate.setString(2, "Lakeshore Ave.");
            psUpdate.setInt(3, 180);
            psUpdate.executeUpdate();
            System.out.println("Updated 180 Grand to 300 Lakeshore");

            rs = s.executeQuery("SELECT num, addr FROM location ORDER BY num");

            int number; // street number retrieved from the database
            boolean failure = false;
            if (!rs.next()) {
                failure = true;
                reportFailure("No rows in ResultSet");
            }

            if ((number = rs.getInt(1)) != 300) {
                failure = true;
                reportFailure("Wrong row returned, expected num=300, got " + number);
            }

            if (!rs.next()) {
                failure = true;
                reportFailure("Too few rows");
            }

            if ((number = rs.getInt(1)) != 1910) {
                failure = true;
                reportFailure("Wrong row returned, expected num=1910, got " + number);
            }

            if (rs.next()) {
                failure = true;
                reportFailure("Too many rows");
            }

            if (!failure) {
                System.out.println("Verified the rows");
            }

            // delete the table
            s.execute("drop table location");
            System.out.println("Dropped table location");

            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            conn.commit();
            System.out.println("Committed the transaction");

        } catch (SQLException ex) {
            printSQLException(ex);
        }
    }
}
