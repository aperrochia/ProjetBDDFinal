/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is ecole of CoursBeuvron.

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
/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is ecole of CoursBeuvron.

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
package fr.insa.toto.model;

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class GestionBDD {

    /* =====================================================
     *                  CREATION DU SCHEMA
     * ===================================================== */
    public static void creeSchema(Connection con) throws SQLException {

        try {
            con.setAutoCommit(false);

            try (Statement st = con.createStatement()) {

                /* =============================
                 *          TABLE CLUB
                 * ============================= */
                st.executeUpdate("create table club ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nom varchar(50) not null,"
                        + ")"
                );

                /* =============================
                 *         TABLE TOURNOI
                 * ============================= */
                st.executeUpdate("create table tournoi ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nom varchar(50) not null,"
                        + " nbTerrains integer not null,"
                        + " nbJoueursParEquipe integer not null,"
                        + " idClub integer not null"
                        + ")"
                );

                st.executeUpdate("alter table tournoi "
                        + "add constraint fk_tournoi_club "
                        + "foreign key (idClub) references club(id)"
                );

                /* =============================
                 *      TABLE UTILISATEUR
                 * ============================= */
                st.executeUpdate("create table utilisateur ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " surnom varchar(30) not null unique,"
                        + " pass varchar(20) not null,"
                        + " role integer not null"
                        + ")"
                );

                /* =============================
                 *          TABLE JOUEUR
                 * ============================= */
                st.executeUpdate("create table joueur ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nom varchar(30) not null,"
                        + " prenom varchar(30) not null,"
                        + " sexe char(1),"
                        + " dateNaissance date,"
                        + " scoreTotal integer default 0,"
                        + " idTournoi integer not null"
                        + ")"
                );

                st.executeUpdate("alter table joueur "
                        + "add constraint fk_joueur_tournoi "
                        + "foreign key (idTournoi) references tournoi(id)"
                );

                /* =============================
                 *           TABLE RONDE
                 * ============================= */
                st.executeUpdate("create table ronde ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " numero integer not null,"
                        + " timestampDebut timestamp not null,"
                        + " statut varchar(10) not null,"
                        + " idTournoi integer not null"
                        + ")"
                );

                st.executeUpdate("alter table ronde "
                        + "add constraint fk_ronde_tournoi "
                        + "foreign key (idTournoi) references tournoi(id)"
                );

                /* =============================
                 *          TABLE TERRAIN
                 * ============================= */
                st.executeUpdate("create table terrain ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nom varchar(30),"
                        + " description text"
                        + ")"
                );

                /* =============================
                 *            TABLE MATCH
                 * ============================= */
                st.executeUpdate("create table match ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " idRonde integer not null,"
                        + " idTerrain integer,"
                        + " statut varchar(10) not null"
                        + ")"
                );

                st.executeUpdate("alter table match "
                        + "add constraint fk_match_ronde "
                        + "foreign key (idRonde) references ronde(id)"
                );

                st.executeUpdate("alter table match "
                        + "add constraint fk_match_terrain "
                        + "foreign key (idTerrain) references terrain(id)"
                );

                /* =============================
                 *           TABLE EQUIPE
                 * ============================= */
                st.executeUpdate("create table equipe ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " idMatch integer not null,"
                        + " numeroEquipe integer not null,"
                        + " score integer default 0"
                        + ")"
                );

                st.executeUpdate("alter table equipe "
                        + "add constraint fk_equipe_match "
                        + "foreign key (idMatch) references match(id)"
                );

                /* =============================
                 *      TABLE JOUEUR_EQUIPE
                 * ============================= */
                st.executeUpdate("create table joueur_equipe ("
                        + " idEquipe integer not null,"
                        + " idJoueur integer not null,"
                        + " primary key(idEquipe,idJoueur)"
                        + ")"
                );

                st.executeUpdate("alter table joueur_equipe "
                        + "add constraint fk_je_equipe "
                        + "foreign key (idEquipe) references equipe(id)"
                );

                st.executeUpdate("alter table joueur_equipe "
                        + "add constraint fk_je_joueur "
                        + "foreign key (idJoueur) references joueur(id)"
                );

                // FIN CREATION
                con.commit();

            }

        } catch (SQLException ex) {
            con.rollback();
            throw ex;

        } finally {
            con.setAutoCommit(true);
        }
    }

    /* =====================================================
     *             SUPPRESSION DU SCHEMA
     * ===================================================== */
    public static void deleteSchema(Connection con) throws SQLException {

        try (Statement st = con.createStatement()) {

            try { st.executeUpdate("alter table joueur_equipe drop constraint fk_je_equipe"); } catch (SQLException ex) {}
            try { st.executeUpdate("alter table joueur_equipe drop constraint fk_je_joueur"); } catch (SQLException ex) {}

            try { st.executeUpdate("alter table equipe drop constraint fk_equipe_match"); } catch (SQLException ex) {}

            try { st.executeUpdate("alter table match drop constraint fk_match_ronde"); } catch (SQLException ex) {}
            try { st.executeUpdate("alter table match drop constraint fk_match_terrain"); } catch (SQLException ex) {}

            try { st.executeUpdate("alter table ronde drop constraint fk_ronde_tournoi"); } catch (SQLException ex) {}

            try { st.executeUpdate("alter table joueur drop constraint fk_joueur_tournoi"); } catch (SQLException ex) {}

            try { st.executeUpdate("alter table tournoi drop constraint fk_tournoi_club"); } catch (SQLException ex) {}

            try { st.executeUpdate("drop table joueur_equipe"); } catch (SQLException ex) {}
            try { st.executeUpdate("drop table equipe"); } catch (SQLException ex) {}
            try { st.executeUpdate("drop table match"); } catch (SQLException ex) {}
            try { st.executeUpdate("drop table terrain"); } catch (SQLException ex) {}
            try { st.executeUpdate("drop table ronde"); } catch (SQLException ex) {}
            try { st.executeUpdate("drop table joueur"); } catch (SQLException ex) {}
            try { st.executeUpdate("drop table utilisateur"); } catch (SQLException ex) {}
            try { st.executeUpdate("drop table tournoi"); } catch (SQLException ex) {}
            try { st.executeUpdate("drop table club"); } catch (SQLException ex) {}
        }
    }

    /* =====================================================
     *                 RAZ BDD
     * ===================================================== */
    public static void razBdd(Connection con) throws SQLException {
        deleteSchema(con);
        creeSchema(con);
    }

    /* =====================================================
     *                     MAIN
     * ===================================================== */
    public static void main(String[] args) {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            razBdd(con);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

}







