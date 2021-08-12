package wazac.entity;

import java.util.HashMap;
import java.util.Map;

public class User {
    
    private String id;
    private String username;
    private String email;
    private String password;
    private Map<String, String> roles;
    
    public User(String id) {
        this.id = id;
        roles = new HashMap<>();
    }

    public User() {
        roles = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, String> roles) {
        this.roles = roles;
    }
    
    
    
}