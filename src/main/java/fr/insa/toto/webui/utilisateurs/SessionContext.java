/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.webui.utilisateurs;

import com.vaadin.flow.server.VaadinSession;

/**
 *
 * @author perro
 */
public class SessionContext {

    private static final String KEY_LOGGED = "logged";
    private static final String KEY_IS_ADMIN = "isAdmin";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_TOURNOI_ID = "tournoiId";

    public static boolean isLoggedIn() {
        Object v = VaadinSession.getCurrent().getAttribute(KEY_LOGGED);
        return v instanceof Boolean && (Boolean) v;
    }

    public static void setLoggedIn(boolean logged) {
        VaadinSession.getCurrent().setAttribute(KEY_LOGGED, logged);
    }

    public static boolean isAdmin() {
        Object v = VaadinSession.getCurrent().getAttribute(KEY_IS_ADMIN);
        return v instanceof Boolean && (Boolean) v;
    }

    public static void setAdmin(boolean admin) {
        VaadinSession.getCurrent().setAttribute(KEY_IS_ADMIN, admin);
    }

    public static String getLogin() {
        Object v = VaadinSession.getCurrent().getAttribute(KEY_LOGIN);
        return v instanceof String ? (String) v : null;
    }

    public static void setLogin(String login) {
        VaadinSession.getCurrent().setAttribute(KEY_LOGIN, login);
    }

    public static Integer getTournoiId() {
        Object v = VaadinSession.getCurrent().getAttribute(KEY_TOURNOI_ID);
        return v instanceof Integer ? (Integer) v : null;
    }

    public static void setTournoiId(int id) {
        VaadinSession.getCurrent().setAttribute(KEY_TOURNOI_ID, id);
    }

    public static void logout() {
        setLoggedIn(false);
        setAdmin(false);
        setLogin(null);
        VaadinSession.getCurrent().setAttribute(KEY_TOURNOI_ID, null);
    }
}

