//package com.xuhao.test;
//
//import cn.hutool.core.util.StrUtil;
//import com.xuhao.test.util.ReceiveUtil;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.IOException;
//import java.net.DatagramSocket;
//import java.net.SocketException;
//
///**
// * @program: WeightTool
// * @description:
// * @author: xuhao
// * @create: 2023-02-15 10:33
// **/
//public class Main {
//
//    private JFrame jFrame = new JFrame("称重监听");
//    private Container container = jFrame.getContentPane();
//    private JTextField port1Text = new JTextField("7777");
//    private JTextField port2Text = new JTextField("7778");
//    private JTextField port3Text = new JTextField();
//    private JTextField port4Text = new JTextField();
//
//    private JButton port1Button = new JButton("开始");
//    private JButton port2Button = new JButton("开始");
//
//    private JButton port3Button = new JButton("开始");
//
//    private JButton port4Button = new JButton("开始");
//
//    private static Integer port1ButtonFlag = 0;
//    private static Integer port2ButtonFlag = 0;
//    private static Integer port3ButtonFlag = 0;
//    private static Integer port4ButtonFlag = 0;
//
//    private static DatagramSocket port1Socket = null;
//    private static DatagramSocket port2Socket = null;
//    private static DatagramSocket port3Socket = null;
//    private static DatagramSocket port4Socket = null;
//
//    private static Thread port1Thread = null;
//    private static Thread port2Thread = null;
//
//
//
//
//    public Main() {
//        jFrame.setBounds(600, 200, 330, 220);
//        //布局管理器
//        container.setLayout(new BorderLayout());
//        //设置不可以改变窗体大小
//        jFrame.setResizable(false);
//        //窗口显示在屏幕中间
//        jFrame.setLocationRelativeTo(null);
//        // 添加关闭按钮事件，关闭时候实质是把窗体隐藏
//        jFrame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                jFrame.setVisible(false);
//            }
//        });
//
//
//        //初始化窗体
//        init();
//
//
//        jFrame.setVisible(true);
//
//
//    }
//
//
//    public void init() {
//        JPanel fieldPanel = new JPanel();
//        fieldPanel.setLayout(null);
//
//
//        port1Text.setBounds(30, 25, 60, 35);
//        port2Text.setBounds(105, 25, 60, 35);
//        port3Text.setBounds(175, 25, 60, 35);
//        port4Text.setBounds(235, 25, 60, 35);
//
//        port1Button.setBounds(30, 80, 60, 35);
//        Font f = new Font("宋体", Font.BOLD, 14);
//        port1Button.setFont(f);
//        port1Button.setFocusPainted(false);
//        port1Button.setBackground(Color.WHITE);
//        port1Button.addActionListener(e -> {
//            runTask(port1Text, port1Button, port1ButtonFlag, port1Socket, port1Thread);
//
//        });
//
//        port2Button.setBounds(105, 80, 60, 35);
//        port2Button.setFont(f);
//        port2Button.setFocusPainted(false);
//        port2Button.setBackground(Color.WHITE);
//
//
//        port2Button.addActionListener(e -> {
//            runTask(port2Text, port2Button, port2ButtonFlag, port2Socket, port2Thread);
//        });
//
//        port3Button.setBounds(175, 80, 60, 35);
//        port3Button.setFont(f);
//        port3Button.setFocusPainted(false);
//        port3Button.setBackground(Color.WHITE);
//
//        port4Button.setBounds(235, 80, 60, 35);
//        port4Button.setFont(f);
//        port4Button.setFocusPainted(false);
//        port4Button.setBackground(Color.WHITE);
//
//        fieldPanel.add(port1Text);
//        fieldPanel.add(port2Text);
//        fieldPanel.add(port3Text);
//        fieldPanel.add(port4Text);
//
//        fieldPanel.add(port1Button);
//        fieldPanel.add(port2Button);
//        fieldPanel.add(port3Button);
//        fieldPanel.add(port4Button);
//
//
//        container.add(fieldPanel, "Center");
//
//    }
//
//
//    private void runTask(JTextField portText, JButton button, Integer buttonFlag, DatagramSocket socket, Thread thread) {
//        DatagramSocket finalSocket = socket;
//
//        if (StrUtil.isEmpty(portText.getText())) {
//            JOptionPane.showMessageDialog(container, "请输入端口号", "确认", JOptionPane.WARNING_MESSAGE);
//        } else {
//            if (buttonFlag == 0) {
//                buttonFlag = 1;
//                portText.setEnabled(false);
//                button.setText("停止");
//                button.setBackground(Color.red);
//
//                try {
//                    finalSocket = new DatagramSocket(Integer.parseInt(portText.getText()));
//                } catch (SocketException socketException) {
//                    socketException.printStackTrace();
//                }
//                thread = new Thread(() -> {
//                    try {
//
//                        ReceiveUtil.listenPort(finalSocket);
//                    } catch (IOException ioException) {
////                            ioException.printStackTrace();
////                            JOptionPane.showMessageDialog(container, "端口号错误", "确认", JOptionPane.WARNING_MESSAGE);
//                    }
//                });
//                thread.start();
//
//            } else {
//
//                buttonFlag = 0;
//                button.setText("开始");
//                button.setBackground(Color.white);
//                portText.setEnabled(true);
//
//                finalSocket.close();
//                thread.interrupt();
//                socket.close();
//
//
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        new Main();
//    }
//}
