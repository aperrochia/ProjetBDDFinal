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

import fr.insa.toto.model.Club;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubDAO {

    // =============================
    // INSERT
    // =============================
    public static Club insert(Connection con, Club club) throws SQLException {
        String sql = "INSERT INTO club (nom) VALUES (?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, club.getNom());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    club.setId(rs.getInt(1));
                }
            }
        }
        return club;
    }

    // =============================
    // FIND ALL
    // =============================
    public static List<Club> findAll(Connection con) throws SQLException {
        List<Club> res = new ArrayList<>();

        String sql = "SELECT id, nom FROM club";

        try (PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Club c = new Club(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
                res.add(c);
            }
        }
        return res;
    }

    // =============================
    // GET OR CREATE DEFAULT CLUB
    // =============================
    public static Club getOrCreateDefaultClub(Connection con) throws SQLException {
        List<Club> clubs = findAll(con);

        if (!clubs.isEmpty()) {
            return clubs.get(0);
        }

        Club def = new Club(0, "Club par défaut");
        return insert(con, def);
    }
}




