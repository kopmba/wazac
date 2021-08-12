package wazac.entity;

import javafx.beans.property.SimpleStringProperty;

public class Category {
    
    private String id;
    private String name; 
    private String description;
    private String created;
    private String edited;
    
    private SimpleStringProperty pId = new SimpleStringProperty("");
    private SimpleStringProperty pName = new SimpleStringProperty("");
    private SimpleStringProperty pDescription = new SimpleStringProperty("");
    
    public Category() {
        this("", "", "");
    }
    
    public Category(String id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getpId() {
        return pId.get();
    }

    public void setpId(String pId) {
        this.pId.setValue(id);
    }

    public String getpName() {
        return pName.get();
    }

    public void setpName(String pName) {
        this.pName.setValue(pName);
    }

    public String getpDescription() {
        return pDescription.get();
    }

    public void setpDescription(String pDescription) {
        this.pDescription.setValue(pDescription);
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