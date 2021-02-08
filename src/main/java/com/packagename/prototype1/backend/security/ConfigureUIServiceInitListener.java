package com.packagename.prototype1.backend.security;

import com.packagename.prototype1.views.LoginView;
import com.packagename.prototype1.views.RegisterView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

/**
 * Configuration class for Vaadin Services
 */
@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    /**
     * Add the before enter listener to each UI view
     * @param event
     */
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    /**
     * Reroutes the user if they're not authorized to access the view.
     *
     * @param event
     *            before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        if(RegisterView.class.equals(event.getNavigationTarget()))
        {
            event.rerouteTo(RegisterView.class);
        }
        else if (!LoginView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }
}