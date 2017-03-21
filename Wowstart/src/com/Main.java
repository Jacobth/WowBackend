package com;

public class Main {

    public static Receiver receiver;

    public static void main(String[] args) {
	    //new Receiver().run(5006);
        new TcpServer().start();
    }
}
