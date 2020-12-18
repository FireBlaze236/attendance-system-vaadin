package com.packagename.prototype1.views;

import com.packagename.prototype1.backend.model.AttendanceData;
import com.packagename.prototype1.backend.model.SessionData;
import com.packagename.prototype1.backend.repository.AttendanceDataRepository;
import com.packagename.prototype1.backend.repository.SessionRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Route("give")
public class GiveAttendanceView extends VerticalLayout {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    AttendanceDataRepository attendanceDataRepository;

    private TextField idField = new TextField("ID : ");
    private TextField nameField = new TextField("Name : ");
    private TextField codeField = new TextField("Code : ");
    private Button submit_button = new Button("Submit");
    private Button backButton = new Button("Back");
    //private Anchor viewLink = new Anchor("grid","View Attendance");

    public GiveAttendanceView()
    {
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        submit_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(new H1("Give Attendance"), idField, nameField, codeField, submit_button, backButton);

        submit_button.addClickListener(clicked ->
        {
            if(idField.isEmpty() || nameField.isEmpty() || codeField.isEmpty())
            {
                return;
            }
            SessionData sessionData = new SessionData();
            String studentId = idField.getValue();
            String studentName = nameField.getValue();
            String sessionCode = codeField.getValue();
            if(sessionRepository.findBySessionCode(sessionCode).isPresent())
            {
                sessionData = sessionRepository.findBySessionCode(sessionCode).get();
            }
            else
            {
                codeField.setErrorMessage("Session not found.");
                codeField.setInvalid(true);
                return;
            }
            if(LocalDateTime.now().isBefore(sessionData.getSessionEndTime().toLocalDateTime()) && LocalDateTime.now().isAfter(sessionData.getSessionStartTime().toLocalDateTime()))
            {

                codeField.setInvalid(false);
                AttendanceData attendanceData = new AttendanceData();
                attendanceData.setStudentId(studentId);
                attendanceData.setStudentName(studentName);
                attendanceData.setSessionData(sessionData);

                String current_user = SecurityContextHolder.getContext().getAuthentication().getName();
                attendanceData.setUsername(current_user);

                if(attendanceDataRepository.findByUsername(current_user).isPresent())
                {
                    codeField.setInvalid(true);
                    codeField.setErrorMessage("You have already given your attendance in this session.");
                    return;
                }

                attendanceDataRepository.save(attendanceData);
                //Notfiy the user
                Notification notification = new Notification("Attendance recorded!", 2000, Notification.Position.TOP_CENTER);
                notification.open();
                notification.addOpenedChangeListener(openedChangeEvent->
                {
                    UI.getCurrent().navigate("");
                });
            }
            else
            {
                codeField.setInvalid(true);
                codeField.setErrorMessage("The session attendance time expired or the session attendance time has not started yet.");
            }
        });

        backButton.addClickListener(c->
        {
            UI.getCurrent().navigate("");
        });

    }
}
