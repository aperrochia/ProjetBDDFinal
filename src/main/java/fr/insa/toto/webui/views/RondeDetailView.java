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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fr.insa.toto.model.MatchTournoi;
import fr.insa.toto.webui.MainLayout;
import fr.insa.toto.webui.security.SecuredView;
import fr.insa.toto.webui.services.TournoiFacade;
import fr.insa.toto.webui.utilisateurs.SessionContext;

import java.util.List;

/**
 *
 * @author perro
 */
@Route(value = "ronde/:id", layout = MainLayout.class)
@PageTitle("Détail ronde")
public class RondeDetailView extends VerticalLayout implements SecuredView, HasUrlParameter<Integer> {

    private final TournoiFacade facade = new TournoiFacade();

    @Override
    public void setParameter(BeforeEvent event, Integer rondeId) {
        removeAll();

        add(new H2("Ronde #" + rondeId));

        Grid<MatchTournoi> grid = new Grid<>(MatchTournoi.class, false);
        grid.addColumn(MatchTournoi::getId).setHeader("Match ID");
        grid.addColumn(MatchTournoi::getStatut).setHeader("Statut");

        grid.addItemClickListener(eventClick -> {
            MatchTournoi match = eventClick.getItem();
            this.getUI().ifPresent(ui -> ui.navigate(MatchDetailView.class, match.getId()));
        });

        Button cloturer = new Button("Clôturer la ronde (admin)", e -> {
            if (!SessionContext.isAdmin()) {
                Notification.show("Réservé admin.");
                return;
            }
            try {
                boolean terminee = facade.verifierRondeTerminee(rondeId);
                if (!terminee) {
                    Notification.show("Tous les matchs ne sont pas terminés/annulés.");
                    return;
                }
                facade.cloturerRonde(rondeId);
                Notification.show("Ronde clôturée ✅");
            } catch (Exception ex) {
                Notification.show("Erreur : " + ex.getMessage());
            }
        });

        add(cloturer, grid);

        try {
            List<MatchTournoi> matchs = facade.getMatchsByRonde(rondeId);
            grid.setItems(matchs);
        } catch (Exception e) {
            Notification.show("Erreur : " + e.getMessage());
        }
    }
}