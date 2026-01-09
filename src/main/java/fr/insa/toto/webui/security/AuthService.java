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
package fr.insa.toto.webui.security;

import java.util.Map;

/**
 *
 * @author perro
 */
public class AuthService {

    // Copié de ta console
    private static final Map<String, String> ADMINS = Map.of(
            "adminLeo", "L34pass!",
            "adminSophie", "SoAdmin22",
            "adminMax", "MaxPwd99",
            "adminInsa", "InsaAdmin7"
    );

    private static final Map<String, String> USERS = Map.of(
            "userTom", "tom123",
            "userAnna", "anna2025",
            "userHugo", "hugoPwd",
            "userNina", "nina007"
    );

    public enum Role { ADMIN, USER, NONE }

    public static Role authenticate(String login, String password) {
        if (login == null || password == null) return Role.NONE;
        String l = login.trim();
        String p = password.trim();

        if (ADMINS.containsKey(l) && ADMINS.get(l).equals(p)) return Role.ADMIN;
        if (USERS.containsKey(l) && USERS.get(l).equals(p)) return Role.USER;
        return Role.NONE;
    }
}
