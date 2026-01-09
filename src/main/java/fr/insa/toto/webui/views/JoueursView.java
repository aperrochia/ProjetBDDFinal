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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fr.insa.toto.model.Joueur;
import fr.insa.toto.webui.MainLayout;
import fr.insa.toto.webui.security.SecuredView;
import fr.insa.toto.webui.services.TournoiFacade;
import fr.insa.toto.webui.utilisateurs.SessionContext;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author perro
 */
@Route(value = "joueurs", layout = MainLayout.class)
@PageTitle("Joueurs")
public class JoueursView extends VerticalLayout implements SecuredView {

    public JoueursView() {
        TournoiFacade facade = new TournoiFacade();

        add(new H2("Joueurs"));

        Integer idTournoi = SessionContext.getTournoiId();
        if (idTournoi == null) {
            add("Aucun tournoi sélectionné.");
            return;
        }

        Grid<Joueur> grid = new Grid<>(Joueur.class, false);
        grid.addColumn(Joueur::getId).setHeader("ID");
        grid.addColumn(Joueur::getNom).setHeader("Nom");
        grid.addColumn(Joueur::getPrenom).setHeader("Prénom");
        grid.addColumn(Joueur::getSexe).setHeader("Sexe");
        grid.addColumn(Joueur::getScoreTotal).setHeader("Score total");
        add(grid);

        // Formulaire admin
        TextField nom = new TextField("Nom");
        TextField prenom = new TextField("Prénom");
        TextField sexe = new TextField("Sexe (M/F)");
        DatePicker naissance = new DatePicker("Naissance");

        Button addBtn = new Button("Ajouter (admin)", e -> {
            if (!SessionContext.isAdmin()) {
                Notification.show("Accès refusé : réservé admin.");
                return;
            }
            try {
                LocalDate ld = naissance.getValue();
                if (ld == null) {
                    Notification.show("Date manquante");
                    return;
                }

                facade.ajouterJoueur(
                        idTournoi,
                        nom.getValue(),
                        prenom.getValue(),
                        sexe.getValue(),
                        Date.valueOf(ld)
                );

                refresh(grid, facade, idTournoi);
                Notification.show("Joueur ajouté ✅");

            } catch (Exception ex) {
                Notification.show("Erreur : " + ex.getMessage());
            }
        });

        add(new HorizontalLayout(nom, prenom, sexe, naissance, addBtn));

        try {
            refresh(grid, facade, idTournoi);
        } catch (Exception e) {
            Notification.show("Erreur : " + e.getMessage());
        }
    }

    private void refresh(Grid<Joueur> grid, TournoiFacade facade, int idTournoi) throws Exception {
        List<Joueur> joueurs = facade.getJoueurs(idTournoi);
        grid.setItems(joueurs);
    }
}