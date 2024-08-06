package com.bosch.weight;

import com.bosch.weight.util.ReceiveUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinuxMain {
    public static void main(String[] args) {
//        int[] ports = new int[4];
//        ports[0] = 5555;
//        ports[1] = 6666;
//        ports[2] = 7777;
//        ports[3] = 8888;
//
//
//        Thread[] threads = new Thread[4];
//        for (int i = 0; i < 4; i++) {
//            int port = ports[i];
//            Thread thread = new Thread(new PortListenerThread(port));
//            thread.start();
//            threads[i] = thread;
//        }
//
//        // 主程序等待所有线程完成
//        for (Thread thread : threads) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println("所有线程已完成。主程序退出。");

        int[] ports = {5555, 6666, 7777, 8888}; // 定义要监听的端口号数组
        ExecutorService executor = Executors.newFixedThreadPool(ports.length);

        for (int port : ports) {
            executor.submit(new PortListener(port));
        }
    }
}

class PortListenerThread implements Runnable {
    private int port;

    public PortListenerThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        // 在这里实现监听指定端口的逻辑，你需要根据你的具体需求来编写
        // 这里只是一个示例，需要根据实际情况编写端口监听的逻辑

        // 在这里可以调用相关方法来监听指定端口
        System.out.println("监听端口 " + port);
        try {
            DatagramSocket socket1 = new DatagramSocket(port);
            byte[] buffer = new byte[1024];

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) {
                socket1.receive(packet);
                ReceiveUtil.listenPort(packet, buffer);
            }
        } catch (Exception e1) {

        }
    }

}

class PortListener implements Runnable {

    private static Logger logger = Logger.getLogger(PortListener.class);

    private int port;
    private DatagramSocket socket;

    public PortListener(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(port);
            System.out.println("Server started on port: " + port);
            logger.info("Server started on port:" + port);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (!Thread.interrupted()) {
                try {
                    socket.receive(packet);
                    logger.info("Received packet on port " + port + " from " + packet.getAddress());

                    // 在这里处理接收到的数据包
                    //String receivedData = new String(packet.getData(), 0, packet.getLength());
                    //System.out.println("Data: " + receivedData);
                    ReceiveUtil.listenPort(packet, buffer);

                } catch (IOException e) {
                    System.err.println("Error receiving packet on port " + port + ": " + e.getMessage());
                    logger.error("Error receiving packet on port " + port + ": " + e.getMessage());
                } catch (InterruptedException e) {
                    System.err.println("Error1 receiving packet on port " + port + ": " + e.getMessage());
                    logger.error("Error1 receiving packet on port " + port + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error opening socket on port " + port + ": " + e.getMessage());
            logger.error("Error opening socket on port " + port + ": " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}