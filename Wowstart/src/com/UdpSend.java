package com;

import java.io.IOException;
import java.net.*;

public class UdpSend {

    private InetAddress ip;
    private DatagramSocket socket;
    private final int port = 9876;

    public UdpSend(InetAddress ip) {
        this.ip = ip;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        byte[] sendData;
        InetAddress address = ip;
        sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
