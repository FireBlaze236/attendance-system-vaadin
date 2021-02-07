package com.packagename.prototype1.views;

import com.packagename.prototype1.backend.model.SessionData;
import com.packagename.prototype1.backend.repository.SessionRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Vaadin view for creating attendance sessions
 */
@Route("createsession")
public class SessionCreationView extends VerticalLayout {
    @Autowired
    SessionRepository sessionRepository;

    private TextField sessionNameField = new TextField("Session Name : ");
    private TimePicker startTimePicker = new TimePicker("Session Start Time : ");
    private TimePicker endTimePicker = new TimePicker("Session End Time");
    private DatePicker startDatePicker = new DatePicker("Session Start Date : ");
    private DatePicker endDatePicker = new DatePicker("Session End Date :" );
    private Button createSessionButton = new Button("Create");
    private Button backButton = new Button("Back");

    private H2 title = new H2("Create a new session");
    private H4 sessionCodeText = new H4("Created Session");

    public SessionCreationView()
    {
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        //Timers set up duration of 15 minutes increment
        startTimePicker.setStep(Duration.ofMinutes(15));
        //start will be minimum the current time
        startTimePicker.setMinTime(LocalTime.now());
        //end timer also has interval of 15
        endTimePicker.setStep(Duration.ofMinutes(15));
        //end will be at least 15 minutes from current time
        endTimePicker.setMinTime(LocalTime.now().plusMinutes(15));
        //default values
        startTimePicker.setValue(LocalTime.now());
        endTimePicker.setValue(LocalTime.now().plusMinutes(30));

        //default dates
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());

        //the confirmation text set up
        sessionCodeText.setVisible(false);

        createSessionButton.addClickListener(createSession ->
        {
            String current_user = SecurityContextHolder.getContext().getAuthentication().getName();
            if(sessionNameField.isEmpty())
                return;
            if(sessionRepository.findByOwnerUser(current_user).size() > 5)
            {
                sessionCodeText.setText("Session limit reached");
                sessionCodeText.setVisible(true);
                return;
            }
            LocalDateTime startDateTime = LocalDateTime.of(startDatePicker.getValue(), startTimePicker.getValue());
            Timestamp startTimestamp = Timestamp.valueOf(startDateTime);

            LocalDateTime endDateTime = LocalDateTime.of(endDatePicker.getValue(), endTimePicker.getValue());
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);


            String sessionName = sessionNameField.getValue();

            SessionData sessionData = new SessionData();

            sessionData.setOwnerUser(current_user);
            sessionData.setSessionName(sessionName);
            sessionData.setSessionStartTime(startTimestamp);
            sessionData.setSessionCode(sessionName);
            sessionData.setSessionEndTime(endTimestamp);

            sessionRepository.save(sessionData);

            sessionCodeText.setText("Created Session with code : " + sessionData.getSessionCode());
            sessionCodeText.setVisible(true);
        });

        backButton.addClickListener(c->
        {
            UI.getCurrent().navigate("");
        });


        add(title,
                sessionNameField, startDatePicker, startTimePicker, endDatePicker, endTimePicker,
                sessionCodeText, createSessionButton,
                backButton);
    }
}
