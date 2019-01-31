package com.zhukai.print;

import com.zhukai.print.netty.HttpServer;
import com.zhukai.print.netty.ServerHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;

@Slf4j
public class Main extends Application {

    private int httpPort = 8086;

    private String title = "��ӡ��������";

    private TrayIcon trayIcon;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // ��С������
        this.enableTray(primaryStage);

        URL resource = Thread.currentThread().getContextClassLoader().getResource("fxml/main.fxml");
        Parent root = FXMLLoader.load(resource);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle(title);
        // ���ܸı䴰���С
        primaryStage.setResizable(false);
        // ����ͼ��G
        primaryStage.getIcons().add(new Image("/icon/print.png"));
        // �ر�ʱ�����¼�
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Platform.runLater(() -> primaryStage.hide());
        });
        primaryStage.show();

        // ����һ��Http����
        this.startHttpServer();
    }

    private void startHttpServer() {
        new Thread(() -> {
            System.setProperty("io.netty.noUnsafe", "true");
            // ��Ӻ��Ե�ַ
            ServerHandler.addIgnoreUrl("/favicon.ico");
            // ��������
            new HttpServer().bind(httpPort);
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }


    // ���½�, ��С��
    private void enableTray(final Stage stage) {
        PopupMenu popupMenu = new PopupMenu();
        java.awt.MenuItem openItem = new java.awt.MenuItem("��ʾ");
        java.awt.MenuItem hideItem = new java.awt.MenuItem("��С��");
        java.awt.MenuItem quitItem = new java.awt.MenuItem("�˳�");

        Platform.setImplicitExit(false); // ���ʹ����ʾ����������false

        ActionListener acl = e -> {
            java.awt.MenuItem item = (java.awt.MenuItem) e.getSource();
            if (item.getLabel().equals("�˳�")) {
                SystemTray.getSystemTray().remove(trayIcon);
                Platform.exit();
                System.exit(0);
                return;
            }
            if (item.getLabel().equals("��ʾ")) {
                Platform.runLater(() -> stage.show());
            }
            if (item.getLabel().equals("��С��")) {
                Platform.runLater(() -> stage.hide());
            }
        };

        // ˫���¼�����
        MouseListener sj = new MouseListener() {

            public void mouseReleased(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {}

            public void mouseExited(MouseEvent e) {}

            public void mouseEntered(MouseEvent e) {}

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (stage.isShowing()) {
                        Platform.runLater(() -> stage.hide());
                    } else {
                        Platform.runLater(() -> stage.show());
                    }
                }
            }
        };


        openItem.addActionListener(acl);
        quitItem.addActionListener(acl);
        hideItem.addActionListener(acl);

        popupMenu.add(openItem);
        popupMenu.add(hideItem);
        popupMenu.add(quitItem);

        try {
            SystemTray tray = SystemTray.getSystemTray();
            BufferedImage image = ImageIO.read(Main.class.getResourceAsStream("/icon/print16.png"));
            trayIcon = new TrayIcon(image, title, popupMenu);
            trayIcon.setToolTip(title);
            tray.add(trayIcon);
            trayIcon.addMouseListener(sj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
