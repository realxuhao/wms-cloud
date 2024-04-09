package com.bosch.weight;

import com.bosch.weight.util.ReceiveUtil;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class LinuxMain {
    public static void main(String[] args) {
        int[] ports = new int[4];
        ports[0] = 5555;
        ports[1] = 6666;
        ports[2] = 7777;
        ports[3] = 8888;


        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            int port = ports[i];
            Thread thread = new Thread(new PortListenerThread(port));
            thread.start();
            threads[i] = thread;
        }

        // 主程序等待所有线程完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("所有线程已完成。主程序退出。");
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

