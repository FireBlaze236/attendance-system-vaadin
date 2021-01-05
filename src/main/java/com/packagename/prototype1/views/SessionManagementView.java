package com.packagename.prototype1.views;

import com.helger.commons.csv.CSVWriter;
import com.packagename.prototype1.backend.model.AttendanceData;
import com.packagename.prototype1.backend.model.SessionData;
import com.packagename.prototype1.backend.repository.AttendanceDataRepository;
import com.packagename.prototype1.backend.repository.SessionRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

@Route("sessions")
public class SessionManagementView extends VerticalLayout {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    AttendanceDataRepository attendanceDataRepository;

    //UI
    private Select<String> sessionSelect = new Select<String>();
    private Grid<SessionData> sessionDataGrid = new Grid<>(SessionData.class);
    private Grid<AttendanceData> attendanceDataGrid = new Grid<>(AttendanceData.class);
    private String current_user = SecurityContextHolder.getContext().getAuthentication().getName();
    private Button showButton = new Button("Show All Sessions");
    private H3 attendanceReportTitle = new H3("Attendance Report: ");
    private Button backButton = new Button("Back");
    //Download report functionality
    private String attendanceSessionNameCurrent = new String("AttendanceReport");
    private Anchor downloadReportLink = new Anchor();
    private SessionData reportSessionData = new SessionData();

    public SessionManagementView(){
        sessionDataGrid.removeAllColumns();
        sessionDataGrid.addColumns("sessionName", "sessionCode", "sessionStartTime", "sessionEndTime");
        attendanceDataGrid.removeColumnByKey("sessionData");
        attendanceDataGrid.removeColumnByKey("aId");
        //attendanceDataGrid.removeColumnByKey("score");
        showButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //No report no download
        downloadReportLink.setEnabled(false);

        sessionDataGrid.addComponentColumn(sessionData->
        {
            Button reportButton = new Button("Show report");
            reportButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            reportButton.addClickListener(clicked-> {
                attendanceDataGrid.setItems(attendanceDataRepository.findBySessionData(sessionData));
                attendanceSessionNameCurrent = sessionData.getSessionName();
                reportSessionData = sessionData;
                attendanceReportTitle.setText("Attendance Report for Session : "+ attendanceSessionNameCurrent);
                downloadReportLink.setEnabled(true);
            });
            return reportButton;
        }).setHeader("----");

        //Show the ui components
        add(backButton, showButton, sessionDataGrid, attendanceReportTitle, attendanceDataGrid);
        //back
        backButton.addClickListener(c->
        {
            UI.getCurrent().navigate("");
        });
        //show only the sessions user owns
        showButton.addClickListener(c->{
            sessionDataGrid.setItems(sessionRepository.findByOwnerUser(current_user));
            }
        );

        //Download the report as csv functionality
        downloadReportLink = new Anchor(new StreamResource("attendance.csv", this::getInputStream) , "Export as CSV");
        add(downloadReportLink);

    }

    private InputStream getInputStream()
    {
        try{
            StringWriter stringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(stringWriter);

            csvWriter.writeNext("student_id", "name", "score");
            attendanceDataRepository.findBySessionData(reportSessionData).forEach(c->
            {
                csvWriter.writeNext("" + c.getStudentId(), c.getStudentName());

            });
            return IOUtils.toInputStream(stringWriter.toString(), "UTF-8");
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
