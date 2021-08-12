package wazac.entity;

import javafx.beans.property.SimpleStringProperty;

public class SubCategory {
    
    private SimpleStringProperty id = new SimpleStringProperty("");
    private SimpleStringProperty name = new SimpleStringProperty("") ; 
    private SimpleStringProperty description = new SimpleStringProperty(""); 
    private SimpleStringProperty categoryId = new SimpleStringProperty("");
    
    private String created;
    private String edited;
    
    public SubCategory() {
        this("", "", "", "");
    }
    
    public SubCategory(String id) {
        this(id, "", "", "");
    }
    
    public SubCategory(String id, String name, String description, String categoryId) {
        setId(id);
        setName(name);
        setDescription(description);
        setCategoryId(categoryId);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String value) {
        id.set(value);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }
    
    public String getCategoryId() {
        return categoryId.get();
    }

    public void setCategoryId(String value) {
        categoryId.set(value);
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }
    
    
}