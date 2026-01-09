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
package fr.insa.toto.model;

import java.sql.Date;

public class Joueur {

    private int id;
    private String nom;
    private String prenom;
    private String sexe;          // 'M' ou 'F' ou autre
    private Date dateN;
    private int scoreTotal;
    private int idTournoi;

    public Joueur() {
    }

    public Joueur(int id, String nom, String prenom, String sexe, 
                  Date dateN, int scoreTotal, int idTournoi) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateN = dateN;
        this.scoreTotal = scoreTotal;
        this.idTournoi = idTournoi;
    }

    public Joueur(String nom, String prenom, String sexe, Date dateNaissance, int idTournoi) {
        this(0, nom, prenom, sexe, dateNaissance, 0, idTournoi);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Date getDateN() {
        return dateN;
    }

    public void setDateN(Date dateN) {
        this.dateN = dateN;
    }

    public int getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(int scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public int getIdTournoi() {
        return idTournoi;
    }

    public void setIdTournoi(int idTournoi) {
        this.idTournoi = idTournoi;
    }

    @Override
    public String toString() {
        return "Joueur{" + "id=" + id + ", nom=" + nom 
                + ", prenom=" + prenom + ", scoreTotal=" + scoreTotal + '}';
    }
}

