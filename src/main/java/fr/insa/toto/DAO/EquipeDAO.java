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

import fr.insa.toto.model.Equipe;
import java.sql.*;
import java.util.*;

public class EquipeDAO {

    public static Equipe insert(Connection con, Equipe e) throws SQLException {
        String sql = "insert into equipe (idMatch,numeroEquipe,score) values (?,?,?)";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, e.getIdMatch());
            st.setInt(2, e.getNumeroEquipe());
            st.setInt(3, e.getScore());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getInt(1));
            }
        }
        return e;
    }

    public static List<Equipe> findAllByMatch(Connection con, int idMatch) throws SQLException {
        List<Equipe> res = new ArrayList<>();
        String sql = "select * from equipe where idMatch=? order by numeroEquipe";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idMatch);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    res.add(new Equipe(
                        rs.getInt("id"), rs.getInt("idMatch"),
                        rs.getInt("numeroEquipe"), rs.getInt("score")
                    ));
                }
            }
        }
        return res;
    }

    public static void updateScore(Connection con, int idEquipe, int score) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("update equipe set score=? where id=?")) {
            st.setInt(1, score);
            st.setInt(2, idEquipe);
            st.executeUpdate();
        }
    }
}

