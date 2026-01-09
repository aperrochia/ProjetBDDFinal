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
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.*;

import fr.insa.toto.model.Tournoi;
import fr.insa.toto.webui.MainLayout;
import fr.insa.toto.webui.utilisateurs.SessionContext;
import fr.insa.toto.webui.security.SecuredView;
import fr.insa.toto.webui.services.TournoiFacade;
/**
 *
 * @author perro
 */

@Route(value = "tournoi/parametres", layout = MainLayout.class)
@PageTitle("Paramètres tournoi")
public class ParametresTournoiView extends VerticalLayout implements SecuredView {

    public ParametresTournoiView() {
        TournoiFacade facade = new TournoiFacade();
        add(new H2("Paramètres du tournoi"));

        if (!SessionContext.isAdmin()) {
            add("Accès refusé : réservé à l’admin.");
            return;
        }

        Integer idTournoi = SessionContext.getTournoiId();
        if (idTournoi == null) {
            add("Aucun tournoi sélectionné.");
            return;
        }

        IntegerField nbTerrains = new IntegerField("Nombre de terrains");
        nbTerrains.setMin(1);

        IntegerField nbJoueursParEquipe = new IntegerField("Joueurs par équipe");
        nbJoueursParEquipe.setMin(1);

        try {
            Tournoi t = facade.getTournoiById(idTournoi);
            if (t != null) {
                nbTerrains.setValue(t.getNbTerrains());
                nbJoueursParEquipe.setValue(t.getNbJoueursParEquipe());
            }
        } catch (Exception e) {
            Notification.show("Erreur : " + e.getMessage());
        }

        Button save = new Button("Enregistrer", e -> {
            if (nbTerrains.getValue() == null || nbJoueursParEquipe.getValue() == null) {
                Notification.show("Valeurs invalides");
                return;
            }
            try {
                facade.updateTournoiParams(idTournoi, nbTerrains.getValue(), nbJoueursParEquipe.getValue());
                Notification.show("Paramètres enregistrés ✅");
            } catch (Exception ex) {
                Notification.show("Erreur : " + ex.getMessage());
            }
        });

        add(nbTerrains, nbJoueursParEquipe, save);
    }
}