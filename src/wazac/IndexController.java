/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import wazac.controller.AuthController;

/**
 *
 * @author mardets
 */
public class IndexController extends AnchorPane implements WazacController, Initializable {
    
    private static final Logger LOG  = Logger.getLogger(IndexController.class.getName());
    
    @FXML
    Label label;
    
    @FXML
    Button login;
    
    @FXML
    Button register;
    
    private Wazac wazac;
    
    public static IndexController getInstance() {
        return new IndexController();
    }
    
    public void handleButtonActionLogin(ActionEvent event) throws Exception {
        LOG.log(Level.INFO, "Click on Login button");
        this.wazac.goToPage(AuthController.getInstance(), "view/auth/login.fxml", "Login to the application Dashboard");
    }
    
    @FXML
    private void handleButtonActionRegister(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Register");
    }
    
    public void setApp(Wazac wazac){
        this.wazac = wazac;
    }
    
    public void setWazacInstance(Wazac wazac){
        setApp(wazac);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void create(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelCreate(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDialog(Dialog dialog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
