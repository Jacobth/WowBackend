package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Receiver {

    private StartThread logonThread;
    private StartThread worldThread;

    public void run(int port) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            byte[] receiveData = new byte[8];

            System.out.printf("Listening on udp:%s:%d%n",
                    InetAddress.getLocalHost().getHostAddress(), port);
            DatagramPacket receivePacket = new DatagramPacket(receiveData,
                    receiveData.length);

            while(true)
            {
                serverSocket.receive(receivePacket);
                String sentence = new String( receivePacket.getData(), 0,
                        receivePacket.getLength() );
                System.out.println("RECEIVED: " + sentence);
                System.out.println(sentence.length());

                if(sentence.equals("start")) {
                    List<String> logon = new ArrayList<String>();
                    logon.add("cmd.exe");
                    logon.add("/C");
                    logon.add("logon.exe");

                    List<String> world = new ArrayList<String>();
                    world.add("cmd.exe");
                    world.add("/C");
                    world.add("world.exe");

                   // worldThread = new StartThread(world);
                   // worldThread.start();

                   // logonThread = new StartThread(logon);
                   // logonThread.start();
                }

                else if(sentence.equals("stop")) {
                    worldThread.setStop();
                    logonThread.setStop();
                }

                else if(sentence.equals("level")) {
                    DatabaseAction databaseAction = new DatabaseAction();
                    //databaseAction.setLevel(30);
                }
                else if(sentence.equals("login")) {

                }

                // now send acknowledgement packet back to sender
                InetAddress IPAddress = receivePacket.getAddress();
                String sendString = "polo";
                byte[] sendData = sendString.getBytes("UTF-8");
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                        IPAddress, receivePacket.getPort());
                serverSocket.send(sendPacket);

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

