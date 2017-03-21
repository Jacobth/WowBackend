package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacobth on 2016-08-09.
 */
public class StartThread extends Thread{

    private Thread t;
    private String name;
    private List<String> list;
    private boolean isStopped;
    private ArrayList<String> messages;
    private UdpSend udpSend;

    StartThread(List<String> list, InetAddress ip) {
        this.list = list;
        messages = new ArrayList<>();
        udpSend = new UdpSend(ip);
    }

    public void run() {
        try {
            while(!isStopped)
            startCommand(list);
        }
        catch (Exception e) {

        }
    }

    public void start() {
        if(t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    private void startCommand(List<String> list) {
        try {
            ProcessBuilder builder = new ProcessBuilder(list);
            builder.directory(new File("/D:/Arcemu"));
            builder.redirectErrorStream(true);
            Process p = builder.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                if(isStopped) {

                }
                System.out.println(line);
                udpSend.sendMessage(line);

            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void setStop() {
        isStopped = true;

    }

    public ArrayList<String> getMessages() {
        return messages;
    }
}
