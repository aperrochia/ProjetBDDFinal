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
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fr.insa.toto.model.Ronde;
import fr.insa.toto.model.Tournoi;
import fr.insa.toto.webui.MainLayout;
import fr.insa.toto.webui.security.SecuredView;
import fr.insa.toto.webui.services.TournoiFacade;
import fr.insa.toto.webui.utilisateurs.SessionContext;

import java.util.List;

/**
 *
 * @author perro
 */

@Route(value = "rondes", layout = MainLayout.class)
@PageTitle("Rondes")
public class RondesView extends VerticalLayout implements SecuredView {

    public RondesView() {
        TournoiFacade facade = new TournoiFacade();
        add(new H2("Rondes"));

        Integer idTournoi = SessionContext.getTournoiId();
        if (idTournoi == null) {
            add("Aucun tournoi sélectionné.");
            return;
        }

        Grid<Ronde> grid = new Grid<>(Ronde.class, false);
        grid.addColumn(Ronde::getId).setHeader("ID");
        grid.addColumn(Ronde::getNumero).setHeader("Numéro");
        grid.addColumn(Ronde::getStatut).setHeader("Statut");

        grid.addItemClickListener(eventClick -> {
            Ronde r = eventClick.getItem();
            this.getUI().ifPresent(ui -> ui.navigate(RondeDetailView.class, r.getId()));
        });

        Button creer = new Button("Créer une nouvelle ronde (admin)", e -> {
            if (!SessionContext.isAdmin()) {
                Notification.show("Accès refusé : réservé admin.");
                return;
            }
            try {
                Tournoi t = facade.getTournoiById(idTournoi);
                if (t == null) {
                    Notification.show("Tournoi introuvable");
                    return;
                }
                facade.creerRondeEtGenererMatchs(idTournoi, t);
                refresh(grid, facade, idTournoi);
                Notification.show("Ronde créée ✅");
            } catch (Exception ex) {
                Notification.show("Erreur : " + ex.getMessage());
            }
        });

        add(creer, grid);

        try {
            refresh(grid, facade, idTournoi);
        } catch (Exception e) {
            Notification.show("Erreur : " + e.getMessage());
        }
    }

    private void refresh(Grid<Ronde> grid, TournoiFacade facade, int idTournoi) throws Exception {
        List<Ronde> rondes = facade.getRondes(idTournoi);
        grid.setItems(rondes);
    }
}
