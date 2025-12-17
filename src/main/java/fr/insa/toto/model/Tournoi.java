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

public class Tournoi {

    private int id;
    private String nom;
    private int nbTerrains;
    private int nbJoueursParEquipe;
    private int idClub;

    public Tournoi() {}

    public Tournoi(int id, String nom, int nbTerrains, int nbJoueursParEquipe, int idClub) {
        this.id = id;
        this.nom = nom;
        this.nbTerrains = nbTerrains;
        this.nbJoueursParEquipe = nbJoueursParEquipe;
        this.idClub = idClub;
    }

    public Tournoi(String nom, int nbTerrains, int nbJoueursParEquipe, int idClub) {
        this(0, nom, nbTerrains, nbJoueursParEquipe, idClub);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getNbTerrains() { return nbTerrains; }
    public void setNbTerrains(int nbTerrains) { this.nbTerrains = nbTerrains; }

    public int getNbJoueursParEquipe() { return nbJoueursParEquipe; }
    public void setNbJoueursParEquipe(int nbJoueursParEquipe) { this.nbJoueursParEquipe = nbJoueursParEquipe; }

    public int getIdClub() { return idClub; }
    public void setIdClub(int idClub) { this.idClub = idClub; }

    @Override
    public String toString() {
        return "Tournoi{" + "id=" + id + ", nom=" + nom + "}";
    }
}
