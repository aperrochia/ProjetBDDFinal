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
package fr.insa.toto.webui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import fr.insa.toto.webui.utilisateurs.SessionContext;
import fr.insa.toto.webui.views.LoginView;



/**
 * 
 * @author perro
 */

public class MainLayout extends AppLayout {

    public MainLayout() {
        H2 title = new H2("Tournoi");
        title.getStyle().set("margin", "0");

        Button logout = new Button("Déconnexion", e -> {
            SessionContext.logout();
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });

        HorizontalLayout header = new HorizontalLayout(title, logout);
        header.setWidthFull();
        header.expand(title);
        header.setPadding(true);

        addToNavbar(header);
        addToDrawer(new MainMenu());
    }
}
