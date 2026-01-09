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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import fr.insa.toto.webui.utilisateurs.SessionContext;
import fr.insa.toto.webui.security.AuthService;
import fr.insa.toto.webui.services.TournoiFacade;
import fr.insa.toto.model.Tournoi;

/**
 *
 * @author perro
 */

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    public LoginView() {
        setWidth("400px");
        setAlignItems(Alignment.STRETCH);

        add(new H2("Connexion"));

        TextField login = new TextField("Identifiant");
        PasswordField mdp = new PasswordField("Mot de passe");

        Button go = new Button("Se connecter", e -> {
            AuthService.Role role = AuthService.authenticate(login.getValue(), mdp.getValue());
            if (role == AuthService.Role.NONE) {
                Notification.show("❌ Compte introuvable.", 3000, Notification.Position.MIDDLE);
                return;
            }

            SessionContext.setLoggedIn(true);
            SessionContext.setLogin(login.getValue().trim());
            SessionContext.setAdmin(role == AuthService.Role.ADMIN);

            // Comme ta console : charger ou créer le tournoi par défaut
            try {
                TournoiFacade facade = new TournoiFacade();
                Tournoi t = facade.getOrCreateDefaultTournoi();
                SessionContext.setTournoiId(t.getId());
            } catch (Exception ex) {
                Notification.show("Erreur DB : " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                return;
            }

            getUI().ifPresent(ui -> ui.navigate(AccueilView.class));
        });

        add(login, mdp, go);
    }
}
