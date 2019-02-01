package com.zhukai.print.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ����sqlite�ļ�, ������ʱ����� The database disk image is malformed , Ӧ���Ǹ���ʱ�����ļ�
 * ���԰�line:36�л������1024 * 1024, һ���Ը�����ʱ����������
 *
 * @author zhukai
 * @date 2019/2/1
 */
public class DbConfigUtil {

    private static final String BASE_DIR = "db";
    private static final String CONFIG_FILE = "/print.db";

    public static void createEmptyFiles() throws Exception {
        File file = new File(BASE_DIR);
        if (!file.exists()) {
            file.mkdir();
        }
        File uiConfigFile = new File(BASE_DIR + CONFIG_FILE);
        if (!uiConfigFile.exists()) {
            createDbFile(uiConfigFile);
        }
    }

    private static void createDbFile(File uiConfigFile) throws IOException {
        InputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(BASE_DIR + CONFIG_FILE);
            fos = new FileOutputStream(uiConfigFile);
            byte[] buffer = new byte[1024 * 1024];
            int byteread = 0;
            while ((byteread = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, byteread);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }

    }
}
