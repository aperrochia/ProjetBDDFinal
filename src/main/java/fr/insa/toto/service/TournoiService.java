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
package fr.insa.toto.service;

import fr.insa.toto.DAO.*;
import fr.insa.toto.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class TournoiService {

    private final Connection con;

    public TournoiService(Connection con) {
        this.con = con;
    }

    /* ============================================================
     *                 AJOUT DE JOUEUR
     * ============================================================ */
    public Joueur ajouterJoueur(int idTournoi, String nom, String prenom, 
                                String sexe, java.sql.Date dateNaissance) throws SQLException {

        Joueur j = new Joueur(0, nom, prenom, sexe, dateNaissance, 0, idTournoi);
        return JoueurDAO.insert(con, j);
    }

    /* ============================================================
     *       RECUPERER TOUS LES JOUEURS D’UN TOURNOI
     * ============================================================ */
    public List<Joueur> getJoueurs(int idTournoi) throws SQLException {
        return JoueurDAO.findAllByTournoi(con, idTournoi);
    }

    /* ============================================================
     *         CREATION D’UNE NOUVELLE RONDE
     * ============================================================ */
    public Ronde creerNouvelleRonde(int idTournoi) throws SQLException {

        int numero = RondeDAO.getNextNumeroRonde(con, idTournoi);

        Ronde r = new Ronde(0, numero, new Timestamp(System.currentTimeMillis()), 
                            "EN_COURS", idTournoi);

        return RondeDAO.insert(con, r);
    }

    /* ============================================================
     *     CREATION DES MATCHS ET EQUIPES POUR UNE RONDE
     * ============================================================ */
    public List<MatchTournoi> genererMatchsPourRonde(Ronde ronde, Tournoi tournoi) throws SQLException {

        int nbTerrains = tournoi.getNbTerrains();
        int joueursParEquipe = tournoi.getNbJoueursParEquipe();   // dans ton cas : 2

        List<Joueur> joueurs = JoueurDAO.findAllByTournoi(con, tournoi.getId());
        Collections.shuffle(joueurs);   // Mélange aléatoire

        int total = joueurs.size();
        int joueursParMatch = joueursParEquipe * 2;   // tennis double : 4 joueurs

        int index = 0;
        List<MatchTournoi> matchs = new ArrayList<>();

        // Un match par terrain
        for (int i = 0; i < nbTerrains; i++) {

            MatchTournoi m = new MatchTournoi(0, ronde.getId(), null, "EN_COURS");
            m = MatchDAO.insert(con, m);
            matchs.add(m);

            // Récupérer joueurs pour ce match
            List<Joueur> selection = new ArrayList<>();
            for (int k = 0; k < joueursParMatch && index < total; k++) {
                selection.add(joueurs.get(index));
                index++;
            }

            if (selection.size() < joueursParMatch) {
                // Pas assez de joueurs → match annulé
                MatchDAO.updateStatut(con, m.getId(), "ANNULE");
                continue;
            }

            // Deux équipes de 2 joueurs
            Equipe eq1 = new Equipe(0, m.getId(), 1, 0);
            eq1 = EquipeDAO.insert(con, eq1);

            Equipe eq2 = new Equipe(0, m.getId(), 2, 0);
            eq2 = EquipeDAO.insert(con, eq2);

            // Affectation
            JoueurEquipeDAO.insert(con, eq1.getId(), selection.get(0).getId());
            JoueurEquipeDAO.insert(con, eq1.getId(), selection.get(1).getId());

            JoueurEquipeDAO.insert(con, eq2.getId(), selection.get(2).getId());
            JoueurEquipeDAO.insert(con, eq2.getId(), selection.get(3).getId());
        }

        return matchs;
    }

    /* ============================================================
     *            ENREGISTRER LE SCORE D’UN MATCH
     * ============================================================ */
    public void enregistrerScoreMatch(int idMatch, int scoreEq1, int scoreEq2) throws SQLException {

        List<Equipe> equipes = EquipeDAO.findAllByMatch(con, idMatch);

        if (equipes.size() != 2) {
            throw new SQLException("Erreur : un match doit avoir 2 équipes !");
        }

        Equipe eq1 = equipes.get(0);
        Equipe eq2 = equipes.get(1);

        EquipeDAO.updateScore(con, eq1.getId(), scoreEq1);
        EquipeDAO.updateScore(con, eq2.getId(), scoreEq2);

        // Ajouter les scores aux joueurs
        for (int idJoueur : JoueurEquipeDAO.findJoueursByEquipe(con, eq1.getId())) {
            JoueurDAO.updateScore(con, idJoueur, scoreEq1);
        }
        for (int idJoueur : JoueurEquipeDAO.findJoueursByEquipe(con, eq2.getId())) {
            JoueurDAO.updateScore(con, idJoueur, scoreEq2);
        }

        MatchDAO.updateStatut(con, idMatch, "TERMINE");
    }

    /* ============================================================
     *      VERIFIER SI UNE RONDE EST TERMINEE
     * ============================================================ */
    public boolean verifierRondeTerminee(int idRonde) throws SQLException {

        List<MatchTournoi> matchs = MatchDAO.findAllByRonde(con, idRonde);

        for (MatchTournoi m : matchs) {
            if (!m.getStatut().equals("TERMINE") 
             && !m.getStatut().equals("ANNULE")) {
                return false;
            }
        }
        return true;
    }

    /* ============================================================
     *                CLÔTURER UNE RONDE
     * ============================================================ */
    public void cloturerRonde(int idRonde) throws SQLException {
        RondeDAO.updateStatut(con, idRonde, "TERMINEE");
    }

    /* ============================================================
     *                 CLASSEMENT GLOBAL
     * ============================================================ */
    public List<Joueur> afficherClassement(int idTournoi) throws SQLException {
        return JoueurDAO.findAllOrderByScore(con, idTournoi);
    }
}

