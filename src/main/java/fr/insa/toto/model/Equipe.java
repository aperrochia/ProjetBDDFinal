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

public class Equipe {

    private int id;
    private int idMatch;
    private int numeroEquipe;  // 1 ou 2
    private int score;

    public Equipe() {
    }

    public Equipe(int id, int idMatch, int numeroEquipe, int score) {
        this.id = id;
        this.idMatch = idMatch;
        this.numeroEquipe = numeroEquipe;
        this.score = score;
    }

    public Equipe(int idMatch, int numeroEquipe, int score) {
        this(0, idMatch, numeroEquipe, score);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public int getNumeroEquipe() {
        return numeroEquipe;
    }

    public void setNumeroEquipe(int numeroEquipe) {
        this.numeroEquipe = numeroEquipe;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Equipe{" + "id=" + id + ", idMatch=" + idMatch 
                + ", numeroEquipe=" + numeroEquipe 
                + ", score=" + score + '}';
    }
}

