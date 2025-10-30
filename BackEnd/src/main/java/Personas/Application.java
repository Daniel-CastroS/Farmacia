package Personas;

import Personas.Logic.Server;

public class Application {
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
