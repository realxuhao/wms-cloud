package com.bosch.weight;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import com.bosch.weight.util.PropertiesConstants;
import com.bosch.weight.util.PropertiesUtils;
import com.bosch.weight.util.ReceiveUtil;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.*;

public class UDPListenerWindow {

    private static Logger logger = Logger.getLogger(UDPListenerWindow.class);


    private JFrame frame;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;

    private DatagramSocket socket1, socket2, socket3, socket4;
    private boolean isSocket1Connected = false;
    private boolean isSocket2Connected = false;
    private boolean isSocket3Connected = false;
    private boolean isSocket4Connected = false;


    static {
        File file = new File(PropertiesConstants.CUSTOM_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("创建custom.properties文件错误", e);
            }
        }
    }


    public static void main(String[] args) {
        new UDPListenerWindow();

//        EventQueue.invokeLater(() -> {
//            try {
//                UDPListenerWindow window = new UDPListenerWindow();
//                window.frame.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }

    public UDPListenerWindow() {
        frame = new JFrame();
        frame.setBounds(100, 100, 300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                logger.info("**************************");
            }
        });

        initialize();

        systemTray();
        frame.setVisible(true);

    }

    /**
     * 系统托盘
     *
     * @author xuhao
     * @date 2021/8/13 16:02
     */
    private void systemTray() {

        if (SystemTray.isSupported()) { // 判断系统是否支持托盘功能.
            // 创建托盘右击弹出菜单
            PopupMenu popupMenu = new PopupMenu();

            //邮件菜单
            MenuItem itemShowFrame = new MenuItem("显示主菜单");
            MenuItem itemExit = new MenuItem("退出");
            itemShowFrame.addActionListener(e -> frame.setVisible(true));
            itemExit.addActionListener(e -> {
                logger.info("------------退出----------------");
                System.exit(0);
            });
            popupMenu.add(itemShowFrame);
            popupMenu.add(itemExit);

            //创建托盘图标
            ImageIcon icon = new ImageIcon("./img/logo.png"); // 创建图片对象
            TrayIcon trayIcon = new TrayIcon(icon.getImage(), "文件夹监听",
                    popupMenu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(e -> frame.setVisible(true));

            //把托盘图标添加到系统托盘
            //这个可以点击关闭之后再放到托盘里面，在此是打开程序直接显示托盘图标了
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }
    }



    private void initialize() {


        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 300, 300);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        textField1 = new JTextField();
        textField1.setBounds(20, 34, 86, 30);
        panel.add(textField1);
        textField1.setColumns(10);
        if (PropertiesUtils.getKeyValue(PropertiesConstants.PORT_1, PropertiesConstants.CUSTOM_PATH) != null &&
                !"".equals(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_1, PropertiesConstants.CUSTOM_PATH))) {
            textField1.setText(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_1, PropertiesConstants.CUSTOM_PATH));
        }

        button1 = new JButton("Start");
        button1.setBounds(136, 33, 89, 30);
        panel.add(button1);
        button1.addActionListener(e -> {
            if (StrUtil.isEmpty(textField1.getText())) {
                JOptionPane.showMessageDialog(panel, "请输入端口号", "确认", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isSocket1Connected) {
                int port = Integer.parseInt(textField1.getText());
                PropertiesUtils.updateProperties(PropertiesConstants.PORT_1, textField1.getText(), PropertiesConstants.CUSTOM_PATH);

                try {
                    socket1 = new DatagramSocket(port);
                    isSocket1Connected = true;
                    button1.setText("Stop");
                    new Thread(String.valueOf(port)) {
                        public void run() {
                            byte[] buffer = new byte[1024];
                            while (isSocket1Connected) {
                                try {
                                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                                    socket1.receive(packet);
                                    ReceiveUtil.listenPort(packet, buffer);
                                    System.out.println("Received from port 1: " + new String(packet.getData()));
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    JOptionPane.showMessageDialog(panel, e1.getMessage(), "确认", JOptionPane.WARNING_MESSAGE);

                                }
                            }
                        }
                    }.start();
                } catch (SocketException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "确认", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                PropertiesUtils.updateProperties(PropertiesConstants.PORT_1, textField1.getText(), PropertiesConstants.CUSTOM_PATH);

                isSocket1Connected = false;
                socket1.close();
                button1.setText("Start");
            }
        });

        textField2 = new JTextField();
        textField2.setBounds(20, 75, 86, 30);
        panel.add(textField2);
        textField2.setColumns(10);
        if (PropertiesUtils.getKeyValue(PropertiesConstants.PORT_2, PropertiesConstants.CUSTOM_PATH) != null &&
                !"".equals(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_2, PropertiesConstants.CUSTOM_PATH))) {
            textField2.setText(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_2, PropertiesConstants.CUSTOM_PATH));
        }

        button2 = new JButton("Start");
        button2.setBounds(136, 74, 89, 30);
        panel.add(button2);
        button2.addActionListener(e -> {
            if (StrUtil.isEmpty(textField2.getText())) {
                JOptionPane.showMessageDialog(panel, "请输入端口号", "确认", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isSocket2Connected) {
                int port = Integer.parseInt(textField2.getText());
                PropertiesUtils.updateProperties(PropertiesConstants.PORT_2, textField2.getText(), PropertiesConstants.CUSTOM_PATH);

                try {
                    socket2 = new DatagramSocket(port);
                    isSocket2Connected = true;
                    button2.setText("Stop");
                    new Thread(String.valueOf(port)) {
                        public void run() {
                            byte[] buffer = new byte[1024];
                            while (isSocket2Connected) {
                                try {
                                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                                    socket2.receive(packet);
                                    ReceiveUtil.listenPort(packet, buffer);
                                    System.out.println("Received from port 2: " + new String(packet.getData()));
                                } catch (Exception e1) {
//                                    e1.printStackTrace();
                                    logger.error(e1.getMessage());
                                    JOptionPane.showMessageDialog(panel, e1.getMessage(), "确认", JOptionPane.WARNING_MESSAGE);

                                }
                            }
                        }
                    }.start();
                } catch (SocketException ex) {
//                    ex.printStackTrace();
                    logger.error(ex.getMessage());
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "确认", JOptionPane.WARNING_MESSAGE);

                }
            } else {
                PropertiesUtils.updateProperties(PropertiesConstants.PORT_2, textField2.getText(), PropertiesConstants.CUSTOM_PATH);

                isSocket2Connected = false;
                socket2.close();
                button2.setText("Start");
            }
        });


        textField3 = new JTextField();
        textField3.setBounds(20, 106, 86, 30);
        panel.add(textField3);
        textField3.setColumns(10);
        if (PropertiesUtils.getKeyValue(PropertiesConstants.PORT_3, PropertiesConstants.CUSTOM_PATH) != null &&
                !"".equals(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_3, PropertiesConstants.CUSTOM_PATH))) {
            textField3.setText(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_3, PropertiesConstants.CUSTOM_PATH));
        }

        button3 = new JButton("Start");
        button3.setBounds(136, 105, 89, 30);
        panel.add(button3);
        button3.addActionListener(e -> {
            if (StrUtil.isEmpty(textField3.getText())) {
                JOptionPane.showMessageDialog(panel, "请输入端口号", "确认", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isSocket3Connected) {
                int port = Integer.parseInt(textField3.getText());
                PropertiesUtils.updateProperties(PropertiesConstants.PORT_3, textField3.getText(), PropertiesConstants.CUSTOM_PATH);

                try {
                    socket3 = new DatagramSocket(port);
                    isSocket3Connected = true;
                    button3.setText("Stop");
                    new Thread(String.valueOf(port)) {
                        public void run() {
                            byte[] buffer = new byte[1024];
                            while (isSocket3Connected) {
                                try {
                                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                                    socket3.receive(packet);
                                    ReceiveUtil.listenPort(packet, buffer);
                                    System.out.println("Received from port 3: " + new String(packet.getData()));
                                } catch (Exception e1) {
//                                    e1.printStackTrace();
                                    logger.error(e1.getMessage());
                                    JOptionPane.showMessageDialog(panel, e1.getMessage(), "确认", JOptionPane.WARNING_MESSAGE);

                                }
                            }
                        }
                    }.start();
                } catch (SocketException ex) {
//                    ex.printStackTrace();
                    logger.error(ex.getMessage());
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "确认", JOptionPane.WARNING_MESSAGE);

                }
            } else {
                PropertiesUtils.updateProperties(PropertiesConstants.PORT_3, textField3.getText(), PropertiesConstants.CUSTOM_PATH);

                isSocket3Connected = false;
                socket3.close();
                button3.setText("Start");
            }
        });


        textField4 = new JTextField();
        textField4.setBounds(20, 142, 86, 30);
        panel.add(textField4);
        textField4.setColumns(10);
        if (PropertiesUtils.getKeyValue(PropertiesConstants.PORT_4, PropertiesConstants.CUSTOM_PATH) != null &&
                !"".equals(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_4, PropertiesConstants.CUSTOM_PATH))) {
            textField4.setText(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_4, PropertiesConstants.CUSTOM_PATH));
        }

        button4 = new JButton("Start");
        button4.setBounds(136, 141, 89, 30);
        panel.add(button4);
        button4.addActionListener(e -> {
            if (StrUtil.isEmpty(textField4.getText())) {
                JOptionPane.showMessageDialog(panel, "请输入端口号", "确认", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isSocket4Connected) {
                int port = Integer.parseInt(textField4.getText());
                PropertiesUtils.updateProperties(PropertiesConstants.PORT_4, textField4.getText(), PropertiesConstants.CUSTOM_PATH);

                try {
                    socket4 = new DatagramSocket(port);
                    isSocket4Connected = true;
                    button4.setText("Stop");
                    new Thread(String.valueOf(port)) {
                        public void run() {
                            byte[] buffer = new byte[1024];
                            while (isSocket4Connected) {
                                try {
                                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                                    socket4.receive(packet);
                                    ReceiveUtil.listenPort(packet, buffer);
                                    System.out.println("Received from port 4: " + new String(packet.getData()));
                                } catch (Exception e1) {
//                                    e1.printStackTrace();
                                    logger.error(e1.getMessage());
                                    JOptionPane.showMessageDialog(panel, e1.getMessage(), "确认", JOptionPane.WARNING_MESSAGE);

                                }
                            }
                        }
                    }.start();
                } catch (SocketException ex) {
//                    ex.printStackTrace();
                    logger.error(ex.getMessage());
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "确认", JOptionPane.WARNING_MESSAGE);

                }
            } else {
                PropertiesUtils.updateProperties(PropertiesConstants.PORT_4, textField4.getText(), PropertiesConstants.CUSTOM_PATH);

                isSocket4Connected = false;
                socket4.close();
                button4.setText("Start");
            }
        });


    }
}