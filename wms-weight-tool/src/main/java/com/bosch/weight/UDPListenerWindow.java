package com.bosch.weight;

import cn.hutool.core.util.StrUtil;
import com.bosch.weight.util.PropertiesConstants;
import com.bosch.weight.util.PropertiesUtils;
import com.bosch.weight.util.ReceiveUtil;
import org.apache.log4j.Logger;

import java.awt.EventQueue;
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
        EventQueue.invokeLater(() -> {
            try {
                UDPListenerWindow window = new UDPListenerWindow();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UDPListenerWindow() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 250, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 250, 240);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        textField1 = new JTextField();
        textField1.setBounds(10, 34, 86, 20);
        panel.add(textField1);
        textField1.setColumns(10);
        if (PropertiesUtils.getKeyValue(PropertiesConstants.PORT_1, PropertiesConstants.CUSTOM_PATH) != null &&
                !"".equals(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_1, PropertiesConstants.CUSTOM_PATH))) {
            textField1.setText(PropertiesUtils.getKeyValue(PropertiesConstants.PORT_1, PropertiesConstants.CUSTOM_PATH));
        }

        button1 = new JButton("Start");
        button1.setBounds(106, 33, 89, 23);
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
                isSocket1Connected = false;
                socket1.close();
                button1.setText("Start");
            }
        });

        textField2 = new JTextField();
        textField2.setBounds(10, 75, 86, 20);
        panel.add(textField2);
        textField2.setColumns(10);

        button2 = new JButton("Start");
        button2.setBounds(106, 74, 89, 23);
        panel.add(button2);
        button2.addActionListener(e -> {
            if (StrUtil.isEmpty(textField2.getText())) {
                JOptionPane.showMessageDialog(panel, "请输入端口号", "确认", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isSocket2Connected) {
                int port = Integer.parseInt(textField2.getText());
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
                isSocket2Connected = false;
                socket2.close();
                button2.setText("Start");
            }
        });


        textField3 = new JTextField();
        textField3.setBounds(10, 106, 86, 20);
        panel.add(textField3);
        textField3.setColumns(10);

        button3 = new JButton("Start");
        button3.setBounds(106, 105, 89, 23);
        panel.add(button3);
        button3.addActionListener(e -> {
            if (StrUtil.isEmpty(textField3.getText())) {
                JOptionPane.showMessageDialog(panel, "请输入端口号", "确认", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isSocket3Connected) {
                int port = Integer.parseInt(textField3.getText());
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
                isSocket3Connected = false;
                socket3.close();
                button3.setText("Start");
            }
        });


        textField4 = new JTextField();
        textField4.setBounds(10, 142, 86, 20);
        panel.add(textField4);
        textField4.setColumns(10);

        button4 = new JButton("Start");
        button4.setBounds(106, 141, 89, 23);
        panel.add(button4);
        button4.addActionListener(e -> {
            if (StrUtil.isEmpty(textField4.getText())) {
                JOptionPane.showMessageDialog(panel, "请输入端口号", "确认", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isSocket4Connected) {
                int port = Integer.parseInt(textField4.getText());
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
                isSocket4Connected = false;
                socket4.close();
                button4.setText("Start");
            }
        });


    }
}