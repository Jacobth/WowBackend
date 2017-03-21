package com;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class TcpServer {

    private ArrayList<String> info;
    private Socket socket;
    private StartThread logonThread;
    private StartThread worldThread;
    private final int PORT = 9999;
    private InetAddress ip;

    public void start() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                ArrayList<String> getList = new ArrayList<>();
                ArrayList<String> sendList = new ArrayList<>();

                String message= "";
                socket = server.accept();

                try {
                    ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

                    try {
                        Object object = objectInput.readObject();
                        getList = (ArrayList<String>) object;
                        message = getList.get(0);
                        System.out.println(message);
                        ip = socket.getInetAddress();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (message) {
                    case "login":
                        System.out.println("Username: " + getList.get(1));
                        String username = getList.get(1);

                        DatabaseAction da = new DatabaseAction();
                        ArrayList<String> accountList = da.getAccount(username);

                        sendList = da.getZones();

                        String password = accountList.get(0);
                        String acct = accountList.get(1);

                        System.out.println(sendList.get(0));

                        sendList.add(password);
                        sendList.add(acct);
                        writeObject(sendList);
                        break;

                    case "start":
                        System.out.println("Starting server");
                        startThread();
                        break;

                    case "characters":
                        System.out.println("Select characters");
                        String acctc = getList.get(1);
                        DatabaseAction dc = new DatabaseAction();
                        ArrayList<String> charList = dc.getCharacters(acctc);
                        System.out.println(charList.size());
                        writeObject(charList);
                        break;

                    case "save":
                        String name = getList.get(1);
                        String newName = getList.get(2);
                        String level = getList.get(3);
                        String gold = getList.get(4);
                        String gender = getList.get(5);
                        DatabaseAction ds = new DatabaseAction();
                        ds.updateCharacter(name, newName, level, gold, gender);

                        if(getList.get(6).equals("move")) {
                            String x = getList.get(7);
                            String y = getList.get(8);
                            String z = getList.get(9);
                            String orientation = getList.get(10);
                            String mapId = getList.get(11);
                            String zoneId = getList.get(12);
                            new DatabaseAction().updatePosition(x, y, z, orientation, mapId, zoneId, newName);
                        }

                        ArrayList<String> response = new ArrayList<>();
                        response.add("Saved");
                        writeObject(response);
                        break;

                    case "weather":
                        String zoneId = getList.get(1);
                        String type = getList.get(2);
                        new DatabaseAction().changeWeather(zoneId, type);

                        ArrayList<String> res = new ArrayList<>();
                        res.add("Saved");
                        writeObject(res);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeObject(Object o) {
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startThread() {
      /*  List<String> logon = new ArrayList<>();
        logon.add("cmd.exe");
        logon.add("/C");
        logon.add("logon.exe");
*/
        List<String> world = new ArrayList<>();
        world.add("cmd.exe");
        world.add("/C");
        world.add("world.exe");

        worldThread = new StartThread(world, ip);
        worldThread.start();

  //      logonThread = new StartThread(logon, ip);
    //    logonThread.start();


    }
}
