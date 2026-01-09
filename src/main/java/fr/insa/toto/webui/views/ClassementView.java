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
package fr.insa.toto.webui.views;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import fr.insa.toto.model.Joueur;
import fr.insa.toto.webui.MainLayout;
import fr.insa.toto.webui.utilisateurs.SessionContext;
import fr.insa.toto.webui.security.SecuredView;
import fr.insa.toto.webui.services.TournoiFacade;

import java.util.List;
/**
 *
 * @author perro
 */
@Route(value = "classement", layout = MainLayout.class)
@PageTitle("Classement")
public class ClassementView extends VerticalLayout implements SecuredView {

    public ClassementView() {
        TournoiFacade facade = new TournoiFacade();
        add(new H2("Classement"));

        Integer idTournoi = SessionContext.getTournoiId();
        if (idTournoi == null) {
            add("Aucun tournoi sélectionné.");
            return;
        }

        Grid<Joueur> grid = new Grid<>(Joueur.class, false);
        grid.addColumn(Joueur::getId).setHeader("ID");
        grid.addColumn(Joueur::getNom).setHeader("Nom");
        grid.addColumn(Joueur::getPrenom).setHeader("Prénom");
        grid.addColumn(Joueur::getScoreTotal).setHeader("Score total");
        add(grid);

        try {
            List<Joueur> classement = facade.afficherClassement(idTournoi);
            grid.setItems(classement);
        } catch (Exception e) {
            Notification.show("Erreur : " + e.getMessage());
        }
    }
}
