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
package fr.insa.toto.DAO;

import java.sql.*;
import java.util.*;

public class JoueurEquipeDAO {

    public static void insert(Connection con, int idEquipe, int idJoueur) throws SQLException {
        String sql = "insert into joueur_equipe (idEquipe,idJoueur) values (?,?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idEquipe);
            st.setInt(2, idJoueur);
            st.executeUpdate();
        }
    }

    public static List<Integer> findJoueursByEquipe(Connection con, int idEquipe) throws SQLException {
        List<Integer> res = new ArrayList<>();
        String sql = "select idJoueur from joueur_equipe where idEquipe=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idEquipe);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) res.add(rs.getInt("idJoueur"));
            }
        }
        return res;
    }
}

