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

        DatagramSocket socket3 = new DatagramSocket(6668);

        DatagramSocket socket4 = new DatagramSocket(6669);


        new Thread(() -> {
            try {
                send(socket1, 7777);
                Thread.sleep(300);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();

            }
        }).start();
        new Thread(() -> {
            try {
                send(socket2, 7778);
                Thread.sleep(300);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();

            }
        }).start();
        new Thread(() -> {
            try {
                send(socket3, 7779);
                Thread.sleep(300);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();

            }
        }).start();
        new Thread(() -> {
            try {
                send(socket4, 7780);
                Thread.sleep(300);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();

            }
        }).start();

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

    public static byte[] convertStringArrayToHexByteArray(String[] stringArray) {
        int length = stringArray.length;
        byte[] byteArray = new byte[length];
        for (int i = 0; i < length; i++) {
            byteArray[i] = (byte) Integer.parseInt(stringArray[i], 16);
        }
        return byteArray;
    }

    private static void send(DatagramSocket socket, Integer port) throws IOException, InterruptedException {
        while (true) {
            Thread.sleep(1000);
            String[] strArray = new String[]{"53", "54", "41", "54", "45", "3A", "20", "32", "33", "2D", "30", "35", "2D", "30", "38", "20", "31", "36", "3A", "34", "31", "3A", "30", "33", "00", "8E", "04", "01", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "24", "00", "00", "00", "8D", "7B", "34", "44", "D2", "02", "00", "00", "00", "00", "00", "00", "D2", "02", "00", "00", "02", "66", "96", "5F", "45", "02", "00", "D8", "97", "45", "02", "00", "80", "18", "45", "02", "00", "10", "E1", "44", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "0E", "77"};
            byte[] buffer = convertStringArrayToHexByteArray(strArray);

            // 2.创建一个数据包对象封装数据
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), port);

            // 3.发送数据
            socket.send(packet);
        }
    }
}
