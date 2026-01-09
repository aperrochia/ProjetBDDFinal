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
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fr.insa.toto.model.Equipe;
import fr.insa.toto.model.Joueur;
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
@Route(value = "match/:id", layout = MainLayout.class)
@PageTitle("Détail match")
public class MatchDetailView extends VerticalLayout implements SecuredView, HasUrlParameter<Integer> {

    private final TournoiFacade facade = new TournoiFacade();

    @Override
    public void setParameter(BeforeEvent event, Integer matchId) {
        removeAll();
        add(new H2("Match #" + matchId));

        try {
            MatchTournoi m = facade.getMatch(matchId);
            if (m == null) {
                add("Match introuvable.");
                return;
            }

            add("Statut: " + m.getStatut());

            List<Equipe> equipes = facade.getEquipesByMatch(matchId);
            if (equipes.size() == 2) {
                afficherEquipe("Équipe 1", equipes.get(0));
                afficherEquipe("Équipe 2", equipes.get(1));
            } else {
                add("Équipes: " + equipes.size() + " (attendu: 2)");
            }

            IntegerField score1 = new IntegerField("Score équipe 1");
            IntegerField score2 = new IntegerField("Score équipe 2");
            score1.setMin(0);
            score2.setMin(0);

            Button save = new Button("Enregistrer score (admin)", e -> {
                if (!SessionContext.isAdmin()) {
                    Notification.show("Réservé admin.");
                    return;
                }
                if (score1.getValue() == null || score2.getValue() == null) {
                    Notification.show("Saisis les scores.");
                    return;
                }
                try {
                    facade.enregistrerScoreMatch(matchId, score1.getValue(), score2.getValue());
                    Notification.show("Scores enregistrés ✅");
                    this.getUI().ifPresent(ui -> ui.navigate(RondeEtRetourView.class));
                } catch (Exception ex) {
                    Notification.show("Erreur : " + ex.getMessage());
                }
            });

            add(new HorizontalLayout(score1, score2, save));

        } catch (Exception e) {
            Notification.show("Erreur : " + e.getMessage());
        }
    }

    private void afficherEquipe(String titre, Equipe eq) throws Exception {
        add(new H3(titre + " (id=" + eq.getId()
                + ", num=" + eq.getNumeroEquipe()
                + ", score=" + eq.getScore() + ")"));

        List<Integer> ids = facade.getJoueursIdsByEquipe(eq.getId());
        for (Integer idJ : ids) {
            Joueur j = facade.getJoueurById(idJ);
            if (j != null) {
                add(" - " + j.getNom() + " " + j.getPrenom() + " (id=" + j.getId() + ")");
            } else {
                add(" - Joueur id=" + idJ);
            }
        }
    }

    @Route(value = "after-score", layout = MainLayout.class)
    @PageTitle("Score enregistré")
    public static class RondeEtRetourView extends VerticalLayout implements SecuredView {
        public RondeEtRetourView() {
            add("Score enregistré. Utilise le menu pour revenir.");
        }
    }
}
