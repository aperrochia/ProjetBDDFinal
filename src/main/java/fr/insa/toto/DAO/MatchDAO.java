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

import fr.insa.toto.model.MatchTournoi;
import java.sql.*;
import java.util.*;

public class MatchDAO {

    public static MatchTournoi insert(Connection con, MatchTournoi m) throws SQLException {
        String sql = "insert into match (idRonde,idTerrain,statut) values (?,?,?)";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, m.getIdRonde());
            if (m.getIdTerrain() == null) st.setNull(2, Types.INTEGER);
            else st.setInt(2, m.getIdTerrain());
            st.setString(3, m.getStatut());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getInt(1));
            }
        }
        return m;
    }

    public static List<MatchTournoi> findAllByRonde(Connection con, int idRonde) throws SQLException {
        List<MatchTournoi> res = new ArrayList<>();
        String sql = "select * from match where idRonde=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idRonde);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Integer idTerrain = rs.getObject("idTerrain") == null ? null : rs.getInt("idTerrain");
                    res.add(new MatchTournoi(
                        rs.getInt("id"), rs.getInt("idRonde"),
                        idTerrain, rs.getString("statut")
                    ));
                }
            }
        }
        return res;
    }

    public static void updateStatut(Connection con, int idMatch, String statut) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("update match set statut=? where id=?")) {
            st.setString(1, statut);
            st.setInt(2, idMatch);
            st.executeUpdate();
        }
    }
}

