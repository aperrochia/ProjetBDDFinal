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

import fr.insa.toto.model.Tournoi;
import java.sql.*;
import java.util.*;

public class TournoiDAO {

    public static Tournoi insert(Connection con, Tournoi t) throws SQLException {
        String sql = "insert into tournoi (nom,nbTerrains,nbJoueursParEquipe,idClub) values (?,?,?,?)";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, t.getNom());
            st.setInt(2, t.getNbTerrains());
            st.setInt(3, t.getNbJoueursParEquipe());
            st.setInt(4, t.getIdClub());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) t.setId(rs.getInt(1));
            }
        }
        return t;
    }

    public static List<Tournoi> findAll(Connection con) throws SQLException {
        List<Tournoi> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("select * from tournoi")) {
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    res.add(new Tournoi(
                        rs.getInt("id"), rs.getString("nom"),
                        rs.getInt("nbTerrains"),
                        rs.getInt("nbJoueursParEquipe"),
                        rs.getInt("idClub")
                    ));
                }
            }
        }
        return res;
    }

    public static Tournoi findById(Connection con, int id) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("select * from tournoi where id=?")) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Tournoi(
                        rs.getInt("id"), rs.getString("nom"),
                        rs.getInt("nbTerrains"),
                        rs.getInt("nbJoueursParEquipe"),
                        rs.getInt("idClub")
                    );
                }
                return null;
            }
        }
    }
    public static void updateParams(java.sql.Connection con, int idTournoi, int nbTerrains, int nbJoueursParEquipe)
        throws java.sql.SQLException {

    String sql = "UPDATE tournoi SET nbTerrains = ?, nbJoueursParEquipe = ? WHERE id = ?";
    try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, nbTerrains);
        pst.setInt(2, nbJoueursParEquipe);
        pst.setInt(3, idTournoi);
        pst.executeUpdate();
    }
}


}



