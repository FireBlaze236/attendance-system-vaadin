package com.packagename.prototype1.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("")
public class MainView extends VerticalLayout {
    private static UI currentUI = UI.getCurrent();

    private Button giveAttendanceButton = new Button("Give Attendance");
    private Button createAttendanceSessionButton = new Button("Create Attendance Session");
    private Button manageAttendanceSessionButton = new Button("Manage Attendance Session");
    private Button logoutButton = new Button("Logout");

    public MainView()
    {
        addClassName("main_view_class");
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.CENTER);

        giveAttendanceButton.addClickListener(c ->
                {
                        currentUI.navigate("give");
                }
        );
        giveAttendanceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        createAttendanceSessionButton.addClickListener(c ->
                {
                    currentUI.navigate("createsession");
                }
        );
        createAttendanceSessionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        manageAttendanceSessionButton.addClickListener(c ->
                {
                    currentUI.navigate("sessions");
                }
        );
        manageAttendanceSessionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        logoutButton.addClickListener(c ->
                {
                    SecurityContextHolder.clearContext();
                    currentUI.getSession().close();
                    currentUI.navigate("login");
                }
        );
        logoutButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        add(new H2("Welcome, " + SecurityContextHolder.getContext().getAuthentication().getName()),
                giveAttendanceButton, createAttendanceSessionButton, manageAttendanceSessionButton, logoutButton);
    }
}
