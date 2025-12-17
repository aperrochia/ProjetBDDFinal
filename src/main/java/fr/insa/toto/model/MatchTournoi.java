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

public class MatchTournoi {

    private int id;
    private int idRonde;
    private Integer idTerrain; // peut être null
    private String statut;     // EN_COURS / CLOSE

    public MatchTournoi() {
    }

    public MatchTournoi(int id, int idRonde, Integer idTerrain, String statut) {
        this.id = id;
        this.idRonde = idRonde;
        this.idTerrain = idTerrain;
        this.statut = statut;
    }

    public MatchTournoi(int idRonde, Integer idTerrain, String statut) {
        this(0, idRonde, idTerrain, statut);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRonde() {
        return idRonde;
    }

    public void setIdRonde(int idRonde) {
        this.idRonde = idRonde;
    }

    public Integer getIdTerrain() {
        return idTerrain;
    }

    public void setIdTerrain(Integer idTerrain) {
        this.idTerrain = idTerrain;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "MatchTournoi{" + "id=" + id + ", idRonde=" + idRonde 
                + ", statut=" + statut + '}';
    }
}

