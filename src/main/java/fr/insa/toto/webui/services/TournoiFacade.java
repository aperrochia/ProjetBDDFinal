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
package fr.insa.toto.webui.services;

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import fr.insa.toto.DAO.ClubDAO;
import fr.insa.toto.DAO.TournoiDAO;
import fr.insa.toto.model.Club;
import fr.insa.toto.model.Joueur;
import fr.insa.toto.model.Ronde;
import fr.insa.toto.model.Tournoi;
import fr.insa.toto.service.TournoiService;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author perro
 */
public class TournoiFacade {

    public Tournoi getOrCreateDefaultTournoi() throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            List<Tournoi> tournois = TournoiDAO.findAll(con);
            if (tournois.isEmpty()) {
                Club club = ClubDAO.getOrCreateDefaultClub(con);
                Tournoi t = new Tournoi(0, "Tournoi Tennis Double", 3, 2, club.getId());
                return TournoiDAO.insert(con, t);
            }
            return tournois.get(0);
        }
    }

    public List<Tournoi> getTournois() throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return TournoiDAO.findAll(con);
        }
    }

    public Tournoi getTournoiById(int idTournoi) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return TournoiDAO.findById(con, idTournoi);
        }
    }

    public void updateTournoiParams(int idTournoi, int nbTerrains, int nbJoueursParEquipe) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            TournoiDAO.updateParams(con, idTournoi, nbTerrains, nbJoueursParEquipe);
        }
    }

    public List<Joueur> getJoueurs(int idTournoi) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return new TournoiService(con).getJoueurs(idTournoi);
        }
    }

    public Joueur ajouterJoueur(int idTournoi, String nom, String prenom, String sexe, Date naissance) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return new TournoiService(con).ajouterJoueur(idTournoi, nom, prenom, sexe, naissance);
        }
    }

    public Ronde creerRondeEtGenererMatchs(int idTournoi, Tournoi tournoi) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            TournoiService service = new TournoiService(con);
            Ronde r = service.creerNouvelleRonde(idTournoi);
            service.genererMatchsPourRonde(r, tournoi);
            return r;
        }
    }

    public void enregistrerScoreMatch(int idMatch, int scoreEq1, int scoreEq2) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            new TournoiService(con).enregistrerScoreMatch(idMatch, scoreEq1, scoreEq2);
        }
    }

    public List<Joueur> afficherClassement(int idTournoi) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return new TournoiService(con).afficherClassement(idTournoi);
        }
    }

    // --- Rondes / Matchs : ces méthodes supposent que tu as ces DAO (très probable dans ton projet)
    public List<Ronde> getRondes(int idTournoi) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return fr.insa.toto.DAO.RondeDAO.findAllByTournoi(con, idTournoi);
        }
    }

    public Ronde getRonde(int idRonde) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return fr.insa.toto.DAO.RondeDAO.findById(con, idRonde);
        }
    }

    public java.util.List<fr.insa.toto.model.MatchTournoi> getMatchsByRonde(int idRonde) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return fr.insa.toto.DAO.MatchDAO.findAllByRonde(con, idRonde);
        }
    }

    public fr.insa.toto.model.MatchTournoi getMatch(int idMatch) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return fr.insa.toto.DAO.MatchDAO.findById(con, idMatch);
        }
    }

    public java.util.List<fr.insa.toto.model.Equipe> getEquipesByMatch(int idMatch) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return fr.insa.toto.DAO.EquipeDAO.findAllByMatch(con, idMatch);
        }
    }

    public java.util.List<Integer> getJoueursIdsByEquipe(int idEquipe) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return fr.insa.toto.DAO.JoueurEquipeDAO.findJoueursByEquipe(con, idEquipe);
        }
    }

    public fr.insa.toto.model.Joueur getJoueurById(int idJoueur) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return fr.insa.toto.DAO.JoueurDAO.findById(con, idJoueur);
        }
    }

    public boolean verifierRondeTerminee(int idRonde) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            return new TournoiService(con).verifierRondeTerminee(idRonde);
        }
    }

    public void cloturerRonde(int idRonde) throws Exception {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            new TournoiService(con).cloturerRonde(idRonde);
        }
    }
}
