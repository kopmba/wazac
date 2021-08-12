/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author mardets
 */
public interface WazacController extends Initializable {
    
    public abstract void setWazacInstance(Wazac wazac);
    
    public abstract void create(ActionEvent event);
    
    public abstract void cancelCreate(ActionEvent event);
    
    public abstract void setDialog(Dialog dialog);
    
}
