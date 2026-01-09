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

import fr.insa.toto.model.Joueur;
import java.sql.*;
import java.util.*;

public class JoueurDAO {

    public static Joueur insert(Connection con, Joueur j) throws SQLException {
        String sql = "insert into joueur (nom,prenom,sexe,dateN,scoreTotal,idTournoi) values (?,?,?,?,?,?)";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, j.getNom());
            st.setString(2, j.getPrenom());
            st.setString(3, j.getSexe());
            st.setDate(4, j.getDateN());
            st.setInt(5, j.getScoreTotal());
            st.setInt(6, j.getIdTournoi());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) j.setId(rs.getInt(1));
            }
        }
        return j;
    }

    public static List<Joueur> findAllByTournoi(Connection con, int idTournoi) throws SQLException {
        List<Joueur> res = new ArrayList<>();
        String sql = "select * from joueur where idTournoi=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idTournoi);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    res.add(new Joueur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getDate("dateN"),
                        rs.getInt("scoreTotal"),
                        rs.getInt("idTournoi")
                    ));
                }
            }
        }
        return res;
    }

    public static void updateScore(Connection con, int idJoueur, int delta) throws SQLException {
        String sql = "update joueur set scoreTotal = scoreTotal + ? where id=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, delta);
            st.setInt(2, idJoueur);
            st.executeUpdate();
        }
    }

    public static List<Joueur> findAllOrderByScore(Connection con, int idTournoi) throws SQLException {
        List<Joueur> res = new ArrayList<>();
        String sql = "select * from joueur where idTournoi=? order by scoreTotal desc";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idTournoi);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    res.add(new Joueur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getDate("dateN"),
                        rs.getInt("scoreTotal"),
                        rs.getInt("idTournoi")
                    ));
                }
            }
        }
        return res;
    }
    
   public static Joueur findById(Connection con, int id) throws SQLException {
    String sql = "select * from joueur where id=?";
    try (PreparedStatement st = con.prepareStatement(sql)) {
        st.setInt(1, id);
        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return new Joueur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getDate("dateN"),
                        rs.getInt("scoreTotal"),
                        rs.getInt("idTournoi")
                );
            }
        }
    }
    return null;
} 
}

