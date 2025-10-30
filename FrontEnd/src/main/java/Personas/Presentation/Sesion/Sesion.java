package Personas.Presentation.Sesion;

import Personas.Logic.Trabajador;

public class Sesion {
    private static Trabajador userLogged;

    public static Trabajador getUserLogged() {
        return userLogged;
    }

    public static void setUserLogged(Trabajador userLogged) {
        Sesion.userLogged = userLogged;
    }

    public static void logout() {
        userLogged = null;
    }

    public static boolean isLoggedIn() {
        return userLogged != null;
    }
}
