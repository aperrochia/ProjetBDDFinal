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
package fr.insa.toto.console;

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import fr.insa.toto.model.*;
import fr.insa.toto.service.TournoiService;
import fr.insa.toto.DAO.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {

    private Connection con;
    private TournoiService service;
    private Tournoi tournoi;
    private Scanner sc = new Scanner(System.in);

    private boolean isAdmin = false;

    // ===== LISTE DES ADMINS =====
    private final Map<String, String> admins = Map.of(
            "adminLeo", "L34pass!",
            "adminSophie", "SoAdmin22",
            "adminMax", "MaxPwd99",
            "adminInsa", "InsaAdmin7"
    );

    // ===== LISTE DES UTILISATEURS =====
    private final Map<String, String> users = Map.of(
            "userTom", "tom123",
            "userAnna", "anna2025",
            "userHugo", "hugoPwd",
            "userNina", "nina007"
    );


    public ConsoleApp() {
        try {
            con = ConnectionSimpleSGBD.defaultCon();
            service = new TournoiService(con);

            // Connexion (admin ou utilisateur)
            boolean ok = choisirMode();
            if (!ok) {
                System.out.println("Programme arrêté.");
                System.exit(0);
            }

            // Charger ou créer un tournoi
            List<Tournoi> tournois = TournoiDAO.findAll(con);

            if (tournois.isEmpty()) {
                Club club = ClubDAO.getOrCreateDefaultClub(con);
                tournoi = new Tournoi(0, "Tournoi Tennis Double", 3, 2, club.getId());
                tournoi = TournoiDAO.insert(con, tournoi);
                System.out.println(">>> Nouveau tournoi créé : " + tournoi.getNom());
            } else {
                tournoi = tournois.get(0);
                System.out.println(">>> Tournoi chargé : " + tournoi.getNom());
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /* ============================================================
     *           CONNEXION ADMIN / UTILISATEUR
     * ============================================================ */
    private boolean choisirMode() {

        System.out.println("===== CONNEXION =====");

        System.out.print("Identifiant : ");
        String login = sc.nextLine().trim();

        System.out.print("Mot de passe : ");
        String mdp = sc.nextLine().trim();

        // Vérifie ADMIN
        if (admins.containsKey(login) && admins.get(login).equals(mdp)) {
            isAdmin = true;
            System.out.println(">>> Connexion ADMIN réussie : " + login);
            return true;
        }

        // Vérifie UTILISATEUR
        if (users.containsKey(login) && users.get(login).equals(mdp)) {
            isAdmin = false;
            System.out.println(">>> Connexion UTILISATEUR réussie : " + login);
            return true;
        }

        // Aucun compte trouvé
        System.out.println("❌ Compte introuvable.");
        return false;
    }


    /* ============================================================
     *                         MENU
     * ============================================================ */
    public void menu() {
        int choix = -1;

        while (choix != 0) {
            System.out.println("\n===== MENU TOURNOI =====");
            System.out.println("1. Liste des joueurs");
            System.out.println("2. Afficher le classement");

            if (isAdmin) {
                System.out.println("3. Ajouter un joueur");
                System.out.println("4. Créer une nouvelle ronde");
                System.out.println("5. Enregistrer le score d’un match");
            }

            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            choix = Integer.parseInt(sc.nextLine());

            try {
                switch (choix) {
                    case 1 -> afficherJoueurs();
                    case 2 -> afficherClassement();
                    case 3 -> {
                        if (isAdmin) ajouterJoueur();
                        else System.out.println("Accès refusé : réservé à l’admin.");
                    }
                    case 4 -> {
                        if (isAdmin) creerRonde();
                        else System.out.println("Accès refusé : réservé à l’admin.");
                    }
                    case 5 -> {
                        if (isAdmin) enregistrerScore();
                        else System.out.println("Accès refusé : réservé à l’admin.");
                    }
                    case 0 -> System.out.println("Au revoir !");
                    default -> System.out.println("Choix invalide !");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }


    /* ============================================================
     *                     AJOUT JOUEUR
     * ============================================================ */
    private void ajouterJoueur() throws SQLException {
        System.out.print("Nom : ");
        String nom = sc.nextLine();

        System.out.print("Prénom : ");
        String prenom = sc.nextLine();

        System.out.print("Sexe (M/F) : ");
        String sexe = sc.nextLine();

        System.out.print("Date de naissance (AAAA-MM-JJ) : ");
        Date date = Date.valueOf(sc.nextLine());

        Joueur j = service.ajouterJoueur(tournoi.getId(), nom, prenom, sexe, date);
        System.out.println(">>> Joueur ajouté : " + j.getNom() + " " + j.getPrenom());
    }

    /* ============================================================
     *                  AFFICHER LES JOUEURS
     * ============================================================ */
    private void afficherJoueurs() throws SQLException {
        List<Joueur> joueurs = service.getJoueurs(tournoi.getId());
        System.out.println("\n===== LISTE DES JOUEURS =====");
        for (Joueur j : joueurs) {
            System.out.println(j.getId() + " - " + j.getNom() + " " + j.getPrenom());
        }
    }

    /* ============================================================
     *                 CREATION D’UNE RONDE
     * ============================================================ */
    private void creerRonde() throws SQLException {
        Ronde r = service.creerNouvelleRonde(tournoi.getId());
        service.genererMatchsPourRonde(r, tournoi);
        System.out.println(">>> Ronde " + r.getNumero() + " créée !");
    }

    /* ============================================================
     *               ENREGISTRER SCORE D'UN MATCH
     * ============================================================ */
    private void enregistrerScore() throws SQLException {
        System.out.print("ID du match : ");
        int idMatch = Integer.parseInt(sc.nextLine());

        System.out.print("Score équipe 1 : ");
        int score1 = Integer.parseInt(sc.nextLine());

        System.out.print("Score équipe 2 : ");
        int score2 = Integer.parseInt(sc.nextLine());

        service.enregistrerScoreMatch(idMatch, score1, score2);
        System.out.println(">>> Scores enregistrés !");
    }

    /* ============================================================
     *                  CLASSEMENT GENERAL
     * ============================================================ */
    private void afficherClassement() throws SQLException {
        List<Joueur> classement = service.afficherClassement(tournoi.getId());

        System.out.println("\n===== CLASSEMENT GENERAL =====");
        int pos = 1;
        for (Joueur j : classement) {
            System.out.println(pos + ". " + j.getNom() + " " + j.getPrenom() +
                    " - " + j.getScoreTotal() + " pts");
            pos++;
        }
    }

    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.menu();
    }
}








