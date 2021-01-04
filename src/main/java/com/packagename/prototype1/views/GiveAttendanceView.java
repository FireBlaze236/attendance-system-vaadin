package com.packagename.prototype1.views;

import com.packagename.prototype1.FaceDetector;
import com.packagename.prototype1.VideoComponent;
import com.packagename.prototype1.backend.model.AttendanceData;
import com.packagename.prototype1.backend.model.SessionData;
import com.packagename.prototype1.backend.repository.AttendanceDataRepository;
import com.packagename.prototype1.backend.repository.SessionRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Route("give")
public class GiveAttendanceView extends VerticalLayout {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    AttendanceDataRepository attendanceDataRepository;

    private String current_user = new String();
    private SessionData sessionData = new SessionData();

    private H1 pageHeader = new H1("Give Attendance");
    private TextField idField = new TextField("ID : ");
    private TextField nameField = new TextField("Name : ");
    private TextField codeField = new TextField("Code : ");
    private Button submit_button = new Button("Submit");
    private Button backButton = new Button("Back");
    //private Anchor viewLink = new Anchor("grid","View Attendance");

    AttendanceData attendanceData = new AttendanceData();

    private VideoComponent videoComponent = new VideoComponent();
    private H4 attendanceNotice = new H4(
        "Your Attendance is being recorded. Please keep this tab open."
    );

    private FaceDetector fd = new FaceDetector();

    private CompletableFuture<String> imageURLData = new CompletableFuture<>();
    private Thread worker = new Thread() {
        public void run() {
            while (recordInProgress) {
                boolean verified = false;
                try {
                    verified = fd.detect(imageURLData.get());
                    System.out.println("here");
                }
                catch (Exception e) {
                    System.out.println("llll");
                }
                if (verified) {
                    //attendanceData = attendanceDataRepository.findByUsernameAndSessionData(current_user, sessionData).get();
                    //int cscore = attendanceData.getScore();
                    //attendanceData.setScore(cscore + 5);
                    //attendanceDataRepository.save(attendanceData);
                    System.out.println("score incremented");
                }
            }
        }
    };

    private boolean recordInProgress = false;

    public GiveAttendanceView()
    {
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        submit_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(pageHeader, idField, nameField, codeField, submit_button, backButton);

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
                // Start Recording
                remove(idField, nameField, codeField, submit_button, backButton);
                add(attendanceNotice);

                codeField.setInvalid(false);
                //user who gave the attendance
                current_user = SecurityContextHolder.getContext().getAuthentication().getName();
                //user's ip address
                String current_user_ip = VaadinSession.getCurrent().getBrowser().getAddress();

                // if entry exists, update; otherwise add
                
                Optional<AttendanceData> ad = attendanceDataRepository.findByUsernameAndSessionData(current_user, sessionData);

                if(ad.isEmpty())
                {
                    // add
                    attendanceData.setStudentId(studentId);
                    attendanceData.setStudentName(studentName);
                    attendanceData.setSessionData(sessionData);
    
                    attendanceData.setUsername(current_user);
                    attendanceData.setUserIp(current_user_ip);

                    attendanceData.setScore(0);
                }
                else {
                    // update
                    attendanceData = ad.get();
                }
                
                /*
                if(attendanceDataRepository.findByUsernameAndSessionData(current_user, sessionData).isEmpty())
                {
                    codeField.setInvalid(true);
                    codeField.setErrorMessage("You have already given your attendance in this session.");
                    return;
                }
                */

                recordInProgress = true;
                attendanceDataRepository.save(attendanceData);

                add(videoComponent);

                videoComponent.addSnapshotListener(ev -> {
                    imageURLData = videoComponent.getElement().getChild(1).executeJs(
                        "var context = this.getContext('2d');" +
                        "context.drawImage($0, 0, 0, this.width, this.height);" +
                        "return this.toDataURL();",
                        videoComponent.getElement().getChild(0)
                    ).toCompletableFuture(String.class);
                });

                videoComponent.startIntervalSnap(2000);
                worker.start();

                //Notfiy the user
                Notification notification = new Notification("Attendance recorded!", 1500, Notification.Position.TOP_CENTER);
                notification.open();
                /*
                notification.addOpenedChangeListener(openedChangeEvent->
                {
                    UI.getCurrent().navigate("");
                });
                */
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
