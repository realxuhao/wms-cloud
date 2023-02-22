package com.bosch.weight.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @program: broadcasttest
 * @description:
 * @author: xuhao
 * @create: 2023-02-14 10:28
 **/
public class Send {
    public static void main(String[] args) throws IOException, InterruptedException {
/**
 * 发送端
 */

        System.out.println("=============发送端启动===========");
        // 1.创建发送端对象
        DatagramSocket socket1 = new DatagramSocket(6666);
        DatagramSocket socket2 = new DatagramSocket(6667);
//
//        DatagramSocket socket3 = new DatagramSocket(6668);
//
//        DatagramSocket socket4 = new DatagramSocket(6669);

        send(socket1,7777);
        send(socket2,6667);

//        new Thread(()->{
//            try {
//                send(socket1,7777);
//                Thread.sleep(2000);
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//
//            }
//        }).start();

//        FlexibleThreadPool.submitTask(() -> {
//            try {
//                Thread.sleep(2000);
//                send(socket1,7777);
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        FlexibleThreadPool.submitTask(() -> {
//            try {
//                send(socket2,7778);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        FlexibleThreadPool.submitTask(() -> {
//            try {
//                send(socket3,7779);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        FlexibleThreadPool.submitTask(() -> {
//            try {
//                send(socket4,7780);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }


    private static void send(DatagramSocket socket,Integer port) throws IOException, InterruptedException {
        while (true) {
            Thread.sleep(1000);
            String msg = "53 54 41 54 45 3A 20 31 39 2D 30 39 2D 32 33 20 32 32 3A 30 38 3A 31 36 00 8E 01 01 00 00 00 00 00 00 00 00 00 00 00 00 24 00 00 00 3F DC 6E 45 EE 0E 00 00 00 00 00 00 EE 0E 00 00 02 00 B3 E4 47 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0B 8E";
            System.out.println("111111");

            // 2.创建一个数据包对象封装数据
            byte[] buffer = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 7777);
            DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 7778);

            // 3.发送数据
            socket.send(packet);
            socket.send(packet2);


        }
    }
}
