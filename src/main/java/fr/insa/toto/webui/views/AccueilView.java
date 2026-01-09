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

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import fr.insa.toto.model.Tournoi;
import fr.insa.toto.webui.MainLayout;
import fr.insa.toto.webui.utilisateurs.SessionContext;
import fr.insa.toto.webui.security.SecuredView;
import fr.insa.toto.webui.services.TournoiFacade;

import java.util.List;
/**
 *
 * @author perro
 */

@Route(value = "", layout = MainLayout.class)
@PageTitle("Accueil")
public class AccueilView extends VerticalLayout implements SecuredView {

    public AccueilView() {
        TournoiFacade facade = new TournoiFacade();

        add(new H2("Accueil"));
        add(new Paragraph("Connecté: " + SessionContext.getLogin()
                + (SessionContext.isAdmin() ? " (ADMIN)" : " (USER)")));

        ComboBox<Tournoi> tournois = new ComboBox<>("Tournoi courant");
        tournois.setItemLabelGenerator(t -> t.getId() + " - " + t.getNom());

        try {
            List<Tournoi> all = facade.getTournois();
            tournois.setItems(all);

            Integer id = SessionContext.getTournoiId();
            Tournoi selected = null;
            if (id != null) selected = all.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
            if (selected == null && !all.isEmpty()) selected = all.get(0);

            if (selected != null) {
                tournois.setValue(selected);
                SessionContext.setTournoiId(selected.getId());
            }
        } catch (Exception e) {
            Notification.show("Erreur chargement tournois : " + e.getMessage());
        }

        tournois.addValueChangeListener(e -> {
            Tournoi t = e.getValue();
            if (t != null) {
                SessionContext.setTournoiId(t.getId());
                Notification.show("Tournoi sélectionné: " + t.getNom());
            }
        });

        add(tournois);
    }
}