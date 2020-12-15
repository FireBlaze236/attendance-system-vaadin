package com.packagename.prototype1.views;

import com.packagename.prototype1.backend.DataModel;
import com.packagename.prototype1.backend.DataService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Route("grid")
public class GridView extends VerticalLayout {
    private Grid<DataModel> dataModelGrid = new Grid<>(DataModel.class);
    private DataService dataService;
    private Button submitButton = new Button("Search");
    private TextField codeField = new TextField("Code : ");
    private Anchor giveLink = new Anchor("","Give Attendance");

    private int searchCode = 0;

    @Autowired
    public GridView(DataService dataService)
    {
        this.dataService = dataService;
        add(giveLink, codeField, submitButton, dataModelGrid);
        dataModelGrid.setItems(new ArrayList<DataModel>());
        dataModelGrid.removeColumnByKey("code");
        dataModelGrid.removeColumnByKey("id");
        codeField.addValueChangeListener(valueChanged ->
        {
            searchCode = Integer.valueOf(codeField.getValue());

        });

        submitButton.addClickListener(clicked ->
        {
            dataModelGrid.setItems(new ArrayList<DataModel>());
            dataModelGrid.setItems(dataService.findByCode(searchCode));
        });
    }
}
