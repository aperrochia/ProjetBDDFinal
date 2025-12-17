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

import java.sql.Timestamp;

public class Ronde {

    private int id;
    private int numero;
    private Timestamp timestampDebut;
    private String statut;     // "EN_COURS" ou "CLOSE"
    private int idTournoi;

    public Ronde() {
    }

    public Ronde(int id, int numero, Timestamp timestampDebut, String statut, int idTournoi) {
        this.id = id;
        this.numero = numero;
        this.timestampDebut = timestampDebut;
        this.statut = statut;
        this.idTournoi = idTournoi;
    }

    public Ronde(int numero, Timestamp timestampDebut, String statut, int idTournoi) {
        this(0, numero, timestampDebut, statut, idTournoi);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Timestamp getTimestampDebut() {
        return timestampDebut;
    }

    public void setTimestampDebut(Timestamp timestampDebut) {
        this.timestampDebut = timestampDebut;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getIdTournoi() {
        return idTournoi;
    }

    public void setIdTournoi(int idTournoi) {
        this.idTournoi = idTournoi;
    }

    @Override
    public String toString() {
        return "Ronde{" + "id=" + id + ", numero=" + numero 
                + ", statut=" + statut + '}';
    }
}

