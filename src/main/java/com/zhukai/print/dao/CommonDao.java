package com.zhukai.print.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhukai
 * @date 2019/1/30
 */
public class CommonDao {

    /**
     * ��ȡȫ������
     */
    public static Map<String, String> getSysConfig() {
        SqliteHelper sh = new SqliteHelper();
        Map<String, String> map = sh.executeQuery("select * from sys_config", (rs) -> {
            Map<String, String> res = new HashMap<>();
            while (rs.next()) {
                String sysCode = rs.getString("sys_code");
                String sysValue = rs.getString("sys_value");
                res.put(sysCode, sysValue);
            }
            return res;
        });
        return map;
    }

    /**
     * �޸�����, ɾ��֮��������
     */
    public static void updateSysConfig(String sysCode, String sysValue) {
        List<String> sqlList = new ArrayList<>();
        sqlList.add("delete from sys_config where sys_code = '" + sysCode + "'");
        sqlList.add("insert into sys_config values ('" + sysCode + "', '" + sysValue + "')");
        new SqliteHelper().executeUpdate(sqlList);
    }

    public static void deleteSysConfig(String sysCode) {
        new SqliteHelper().executeUpdate("delete from sys_config where sys_code = '" + sysCode + "'");
    }
}
