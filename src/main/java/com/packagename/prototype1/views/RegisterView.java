package com.packagename.prototype1.views;

import com.packagename.prototype1.backend.model.UserData;
import com.packagename.prototype1.backend.repository.UserRepository;
import com.packagename.prototype1.backend.security.SecurityUtils;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Registration form
 */
@Route("register")
public class RegisterView extends VerticalLayout {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private TextField usernameField = new TextField("New Username: ");
    private PasswordField passwordField = new PasswordField("New Password: ");
    private PasswordField confirmPasswordField = new PasswordField("New Password: ");
    private EmailField emailField = new EmailField("Email: ");
    private Button submitButton = new Button("Create Account");
    private Notification passwordNoMatchNotif = new Notification("Passwords don't match!", 2000);
    
    public RegisterView()
    {
        addClassName("register-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        add(new H1("Register for Online Attendance System"), usernameField, emailField, passwordField, confirmPasswordField,  submitButton);

        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClickShortcut(Key.ENTER);
        submitButton.addClickListener(submitClicked ->
        {
            usernameField.setInvalid(false);
            //Multiple users cannot have the same username
            if(userRepository.findByUsername(usernameField.getValue()).isPresent())
            {
                usernameField.setErrorMessage("Username taken");
                usernameField.setInvalid(true);
                return;
            }
            if(passwordField.getValue().equals(confirmPasswordField.getValue())) {
                UserData newUserData = new UserData( userRepository.count()+1);
                newUserData.setUsername(usernameField.getValue());
                String password = passwordEncoder.encode(passwordField.getValue());
                newUserData.setPassword(password);
                newUserData.setActive(true);
                newUserData.setRoles("USER");
                userRepository.save(newUserData);
                UI.getCurrent().navigate("login");
            }
            else
            {
                passwordNoMatchNotif.open();
            }
        });

    }
}
