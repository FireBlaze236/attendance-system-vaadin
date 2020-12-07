package com.packagename.prototype1.views;

import com.packagename.prototype1.backend.DataModel;
import com.packagename.prototype1.backend.DataService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

@Route("")
public class MainView extends VerticalLayout {

    private DataService dataService;
    private TextField idField = new TextField("ID : ");
    private TextField nameField = new TextField("Name : ");
    private TextField codeField = new TextField("Code : ");
    private Button submit_button = new Button("Submit");
    private Anchor viewLink = new Anchor("grid","View Attendance");
    /**
     * The MainView constructor is passed the dataService parameter which is Autowired through spring boot.
     * @param dataService
     */
    @Autowired
    public MainView( DataService dataService)
    {

        this.dataService = dataService;

        //Button set up
        submit_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submit_button.addClickListener(clicked ->
        {
            int id = Integer.valueOf(idField.getValue());
            String name = nameField.getValue();
            int code = Integer.valueOf(codeField.getValue());
            DataModel new_data = new DataModel(id, name, code);
            dataService.save(new_data);
        });


        add(idField, nameField, codeField, submit_button, viewLink);


    }
}
