package com.zhukai.print.dao;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * sqlite�����ֱ࣬�Ӵ�������ʵ������������Ӧ�Ľӿڼ��ɶ�sqlite���ݿ���в���
 */
@Slf4j
public class SqliteHelper {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String dbFilePath;

    /**
     * ���캯��
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public SqliteHelper() {
        this("db/print.db");
    }

    /**
     * ���캯��
     *
     * @param dbFilePath sqlite db �ļ�·��
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public SqliteHelper(String dbFilePath) {
        this.dbFilePath = dbFilePath;
        connection = getConnection(dbFilePath);
    }

    /**
     * ��ȡ���ݿ�����
     *
     * @param dbFilePath db�ļ�·��
     * @return ���ݿ�����
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection(String dbFilePath) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    /**
     * ִ��sql��ѯ
     *
     * @param sql sql select ���
     * @param rse ��������������
     * @return ��ѯ���
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public <T> T executeQuery(String sql, ResultSetExtractor<T> rse) {
        try {
            resultSet = getStatement().executeQuery(sql);
            T rs = rse.extractData(resultSet);
            return rs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            destroyed();
        }
    }

    /**
     * ִ��select��ѯ�����ؽ���б�
     *
     * @param sql sql select ���
     * @param rm  ������������ݴ��������
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public <T> List<T> executeQuery(String sql, RowMapper<T> rm) {
        List<T> rsList = new ArrayList<T>();
        try {
            resultSet = getStatement().executeQuery(sql);
            while (resultSet.next()) {
                rsList.add(rm.mapRow(resultSet, resultSet.getRow()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            destroyed();
        }
        return rsList;
    }

    /**
     * ִ�����ݿ����sql���
     *
     * @param sql
     * @return ��������
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int executeUpdate(String sql) {
        try {
            int c = getStatement().executeUpdate(sql);
            return c;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            destroyed();
        }

    }

    /**
     * ִ�ж��sql�������
     *
     * @param sqls
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void executeUpdate(String... sqls) {
        try {
            if (sqls != null) {
                for (String sql : sqls) {
                    getStatement().executeUpdate(sql);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            destroyed();
        }
    }

    /**
     * ִ�����ݿ���� sql List
     *
     * @param sqls sql�б�
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void executeUpdate(List<String> sqls) {
        try {
            if (sqls != null) {
                for (String sql : sqls) {
                    getStatement().executeUpdate(sql);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            destroyed();
        }
    }

    private Connection getConnection() {
        if (null == connection) {
            connection = getConnection(dbFilePath);
        }
        return connection;
    }

    private Statement getStatement() {
        if (null == statement) {
            try {
                statement = getConnection().createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return statement;
    }

    /**
     * ���ݿ���Դ�رպ��ͷ�
     */
    public void destroyed() {
        try {
            if (null != connection) {
                connection.close();
                connection = null;
            }

            if (null != statement) {
                statement.close();
                statement = null;
            }

            if (null != resultSet) {
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException e) {
            log.error("Sqlite���ݿ�ر�ʱ�쳣", e);
        }
    }
}
