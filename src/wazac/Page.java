/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac;


import java.util.logging.Level;
import java.util.logging.Logger;
import wazac.controller.AuthController;
/**
 *
 * @author mardets
 */
public class Page {
    
    private static final Logger LOG  = Logger.getLogger(Page.class.getName()); 
    
    public static Page newPage() {
        return new Page();
    }
    
    public void getPage(Wazac wazac, WazacController controller, String pageName, String title) {
        try {
           // WazacController page = (WazacController) Config.setContentDialog(wazac, controller, pageName, title);
           // page.setWazacInstance(wazac);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
}
