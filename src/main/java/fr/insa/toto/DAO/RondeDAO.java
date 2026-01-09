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

import fr.insa.toto.model.Ronde;
import java.sql.*;
import java.util.*;

public class RondeDAO {

    public static Ronde insert(Connection con, Ronde r) throws SQLException {
        String sql = "insert into ronde (numero,timestampDebut,statut,idTournoi) values (?,?,?,?)";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, r.getNumero());
            st.setTimestamp(2, r.getTimestampDebut());
            st.setString(3, r.getStatut());
            st.setInt(4, r.getIdTournoi());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getInt(1));
            }
        }
        return r;
    }

    public static int getNextNumeroRonde(Connection con, int idTournoi) throws SQLException {
        String sql = "select max(numero) from ronde where idTournoi=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idTournoi);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next() && !rs.wasNull()) return rs.getInt(1) + 1;
                return 1;
            }
        }
    }

    public static List<Ronde> findAllByTournoi(Connection con, int idTournoi) throws SQLException {
        List<Ronde> res = new ArrayList<>();
        String sql = "select * from ronde where idTournoi=? order by numero";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idTournoi);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    res.add(new Ronde(
                        rs.getInt("id"), rs.getInt("numero"),
                        rs.getTimestamp("timestampDebut"),
                        rs.getString("statut"),
                        rs.getInt("idTournoi")
                    ));
                }
            }
        }
        return res;
    }

    public static void updateStatut(Connection con, int idRonde, String statut) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("update ronde set statut=? where id=?")) {
            st.setString(1, statut);
            st.setInt(2, idRonde);
            st.executeUpdate();
        }
    }
    
    public static Ronde findById(Connection con, int id) throws SQLException {
        String sql = "select * from ronde where id=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Ronde(
                            rs.getInt("id"),
                            rs.getInt("numero"),
                            rs.getTimestamp("timestampDebut"),
                            rs.getString("statut"),
                            rs.getInt("idTournoi")
                    );
                }
            }
        }
        return null;
    }

}
