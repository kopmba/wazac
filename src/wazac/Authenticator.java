/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import wazac.controller.AuthController;
import wazac.controller.DashboardController;
import wazac.entity.User;
import wazac.service.SessionService;
import wazac.service.UserService;
import wazac.util._FileRW;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class Authenticator {
    private static String loggedUser;
    
    public static List<User> findUsers() throws IOException, NoSuchFieldException {
        //String jsonText = FileRW.read("collections/users.txt");
        //Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        List<User> users = UserService.find();
        return users;
    }
    
    public static boolean validate(String username, String password) throws IOException, NoSuchFieldException{
        for (User u : findUsers()) {
            return u.getUsername().equals(username) && u.getPassword().equals(password);
        }
        return false;
        
    }
    
    public static boolean userLogging(Wazac wazac, String username, String password) throws Exception{
        if (Authenticator.validate(username, password)) {
            loggedUser = UserService.findByUsername(username).getUsername();
            SessionService.save(loggedUser);
            String title = "Bonjour, " + username + " et bienvenue sur votre Dashboard";
            wazac.setPane(wazac.getStage(), DashboardController.getInstance(), "view/dashboard.fxml", title);
            return true;
        } else {
            return false;
        }
    }
}
