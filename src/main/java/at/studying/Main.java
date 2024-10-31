package at.studying;

import at.studying.MtcgApp;
import at.studying.Server;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        Server server = new Server(new MtcgApp());
        server.start();
    }
}
