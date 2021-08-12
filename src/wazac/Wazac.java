/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import wazac.entity.User;
import wazac.controller.AuthController;
import wazac.controller.DashboardController;
import wazac.controller.handler.ArticleHandler;
import wazac.controller.handler.CategoryHandler;
import wazac.controller.handler.SettingsHandler;
import wazac.controller.handler.SubCategoryHandler;
import wazac.entity.Activity;
import wazac.entity.Article;
import wazac.entity.Category;
import wazac.entity.Profile;
import wazac.entity.SubCategory;
import wazac.service.ActivityService;
import wazac.service.ProfileService;
import wazac.service.SessionService;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class Wazac extends Application {
    
    private static final Logger LOG  = Logger.getLogger(Page.class.getName()); 
    private Stage stage;
    private User loggedUser;
    
    @Override
    public void start(Stage pStage) {
        setPane(pStage, IndexController.getInstance(), "index.fxml", "Welcome to wazac!");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    public Stage getStage() {
        return stage;
    }
    
    public Wazac createInstance() {
        return new Wazac();
    }
    
    public void setPane(Stage pStage, WazacController controller, String fxml, String title) {
       try {
            stage = pStage;
            stage.setMinWidth(1280);
            stage.setMinHeight(800);
            goToPage(controller, fxml, title);
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void setPane(Stage pStage, String pageRedirect) throws Exception {
        
        replaceSceneContent(IndexController.getInstance(), "index.fxml", "Redirect");
    }
    
    private void goToIndex() throws Exception {
        goToPage(IndexController.getInstance(), "index.fxml", "Welcome to wazac!");
    }
    
    public void goToPage(WazacController controller, String fxml, String title) throws Exception {
        try {
            WazacController page = (WazacController) replaceSceneContent(controller, fxml, title);
            page.setWazacInstance(this);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public void setContentDialog(String fxml, String title, WazacController controller) throws IOException {
        Parent root;
        root = FXMLLoader.load(Wazac.class.getResource(fxml));
        Dialog dialog = new Dialog();
        dialog.setTitle(title);
        dialog.getDialogPane().setContent(root);
        
        dialog.show();
        controller.setDialog(dialog);
        
        //return dialog;
    }
    
    public void viewpane(Wazac wazac, String html) throws URISyntaxException, IOException {
        
        
        WebView webView = new WebView();
        String htmlView = contentString(html);
        WebEngine eng = webView.getEngine();
        Config config = new Config(eng);
        JSObject window = (JSObject) eng.executeScript("window");
        window.setMember("wazac", config);
        eng.load(getClass().getResource(html).toExternalForm());
        webView.setPrefSize(1200, 800);
        eng.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                        if(newValue == Worker.State.SUCCEEDED) {
                            Document doc = eng.getDocument();
                            
                            SettingsHandler settingsHandler = SettingsHandler.getInstance();
                        
                            /************ Dashboard Element Binding **********/
                            Element dTag = doc.getElementById("dashboard-link");

                            /************ Settings Elements Binding *********/
                            Element upTag = doc.getElementById("edit-profile");
                            
                            /************ Articles Elements Binding *********/
                            Element laTag = doc.getElementById("product-list");
                            Element uarTag = doc.getElementById("edit-product");
                            Element raTag = doc.getElementById("remove-article");
                            
                            /************ Categories Elements Binding *********/
                            Element ucTag = doc.getElementById("edit-category");
                            Element lcTag = doc.getElementById("category-list");
                            Element rcTag = doc.getElementById("remove-category");
                            
                            /************ SubCategories Elements Binding *********/
                            Element uscTag = doc.getElementById("edit-subcategory");
                            Element lscTag = doc.getElementById("subcategory-list");
                            Element rscTag = doc.getElementById("remove-subcategory");
                            Element sscTag = doc.getElementById("search-subcategory");
                            
                            if(html.contains("settings-profile.html")) {
                                try {
                                    settingsHandler.initFormData(eng, doc, stage);
                                    ((EventTarget) upTag).addEventListener("click", settingsHandler.updateProfile(eng, doc, wazac, new Profile(), new Activity()), false);
                                    ((EventTarget) lcTag).addEventListener("click", CategoryHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) laTag).addEventListener("click", ArticleHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) lscTag).addEventListener("click", SubCategoryHandler.getInstance().find(eng, doc, wazac), false);
                                } catch (URISyntaxException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            
                            
                            if(html.contains("product-create.html")) {
                                try {
                                    Config.fillFormSelect(doc, eng);
                                    ((EventTarget) uarTag).addEventListener("click", ArticleHandler.getInstance().update(eng, doc, wazac, new Article() , uarTag.getAttribute("id")), false);
                                    ((EventTarget) lcTag).addEventListener("click", CategoryHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) lscTag).addEventListener("click", SubCategoryHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) laTag).addEventListener("click", ArticleHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) upTag).addEventListener("click", settingsHandler.findById(eng, doc, wazac), false);
                                } catch (IOException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } 
                            if(html.contains("product-list-table.html")) {
                                
                                if(doc.getElementById("table-view") != null) {
                                    ArticleHandler.getInstance().tableListView(eng, doc, wazac);
                                }
                                
                                for(Element e : Config.findElementByParam(doc, "item")) {
                                    if(e.getAttribute("id").contains("view")) {
                                        ((EventTarget) e).addEventListener("click", ArticleHandler.getInstance().findById(eng, doc, wazac, e.getAttribute("id")), false);
                                    } else if(e.getAttribute("id").contains("edit")) {
                                        ((EventTarget) e).addEventListener("click", ArticleHandler.getInstance().update(eng, doc, wazac, new Article(), e.getAttribute("id")), false);
                                    } else {
                                        ((EventTarget) e).addEventListener("click", ArticleHandler.getInstance().delete(eng, doc, wazac, e.getAttribute("id")), false);
                                    }
                                }
                                
                                ((EventTarget) uarTag).addEventListener("click", ArticleHandler.getInstance().createView(eng, doc, wazac), false);
                                ((EventTarget) lscTag).addEventListener("click", SubCategoryHandler.getInstance().find(eng, doc, wazac), false);
                                ((EventTarget) lcTag).addEventListener("click", CategoryHandler.getInstance().find(eng, doc, wazac), false);
                                ((EventTarget) upTag).addEventListener("click", settingsHandler.findById(eng, doc, wazac), false);
                            
                            }
                            if(html.contains("product.html")) {
                                try {
                                    ArticleHandler.getInstance().viewData(eng, doc, wazac, SessionService.getParam().get("param").toString());
                                    for(Element e : Config.findElementByParam(doc, "item")) {
                                        if(e.getAttribute("id").contains("edit")) {
                                            ((EventTarget) e).addEventListener("click", ArticleHandler.getInstance().update(eng, doc, wazac, new Article(), e.getAttribute("id")), false);
                                        } else {
                                            ((EventTarget) e).addEventListener("click", ArticleHandler.getInstance().delete(eng, doc, wazac, e.getAttribute("id")), false);
                                        }
                                    }
                                    
                                    ((EventTarget) laTag).addEventListener("click", ArticleHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) lscTag).addEventListener("click", SubCategoryHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) upTag).addEventListener("click", settingsHandler.findById(eng, doc, wazac), false);
                                    //((EventTarget) raTag).addEventListener("click", ArticleHandler.getInstance().delete(eng, doc, wazac, raTag.getAttribute("id")), false);
                                } catch (NoSuchFieldException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (URISyntaxException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            
                            
                            if(html.contains("category-create.html") && !html.contains("subcategory-create.html")) {
                                try {
                                    Config.fillFormSelect(doc, eng);
                                    ((EventTarget) ucTag).addEventListener("click", CategoryHandler.getInstance().create(eng, doc, wazac), false);
                                    ((EventTarget) laTag).addEventListener("click", ArticleHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) lscTag).addEventListener("click", SubCategoryHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) upTag).addEventListener("click", settingsHandler.findById(eng, doc, wazac), false);
                                } catch (IOException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } 
                            if(html.contains("category-list.html") && !html.contains("subcategory-list.html")) {
                                if(doc.getElementById("table-view") != null) {
                                    CategoryHandler.getInstance().tableListView(eng, doc, wazac);
                                }
                                //In pagination case, extract the current page and add in findPaginationElement method
                                //extract by calling the e.getAttribute("id")
                                for(Element e : Config.findElementByParam(doc, "item")) {
                                    if(e.getAttribute("id").contains("view")) {
                                        ((EventTarget) e).addEventListener("click", CategoryHandler.getInstance().findById(eng, doc, wazac, e.getAttribute("id")), false);
                                    } else if(e.getAttribute("id").contains("edit")) {
                                        ((EventTarget) e).addEventListener("click", CategoryHandler.getInstance().update(eng, doc, wazac, new Category(), e.getAttribute("id")), false);
                                    } else {
                                        ((EventTarget) e).addEventListener("click", CategoryHandler.getInstance().delete(eng, doc, wazac, e.getAttribute("id")), false);
                                    }
                                }
                                
                                ((EventTarget) ucTag).addEventListener("click", CategoryHandler.getInstance().createView(eng, doc, wazac), false);
                                ((EventTarget) laTag).addEventListener("click", ArticleHandler.getInstance().find(eng, doc, wazac), false);
                                ((EventTarget) lscTag).addEventListener("click", SubCategoryHandler.getInstance().find(eng, doc, wazac), false);
                                ((EventTarget) upTag).addEventListener("click", settingsHandler.findById(eng, doc, wazac), false);
                                /*for(Element e : Config.findElementByParam(doc, "category")) {
                                ((EventTarget) e).addEventListener("click", CategoryHandler.getInstance().findById(eng, doc, stage, e.getAttribute("id")), false);
                                ((EventTarget) rcTag).addEventListener("click", CategoryHandler.getInstance().delete(eng, doc, stage, rcTag.getAttribute("id")), false);
                                }*/
                            }
                            if(html.contains("category.html") && !html.contains("subcategory.html")) {
                                try {
                                    ArticleHandler.getInstance().viewData(eng, doc, wazac, SessionService.getParam().get("param").toString());
                                    /* for(Element e : Config.findElementByParam(doc, "item")) {
                                    if(e.getAttribute("id").contains("edit")) {
                                    ((EventTarget) e).addEventListener("click", CategoryHandler.getInstance().update(eng, doc, wazac, new Category(), e.getAttribute("id")), false);
                                    } else {
                                    ((EventTarget) e).addEventListener("click", CategoryHandler.getInstance().delete(eng, doc, wazac, e.getAttribute("id")), false);
                                    }
                                    }*/
                                    
                                    ((EventTarget) laTag).addEventListener("click", ArticleHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) lscTag).addEventListener("click", SubCategoryHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) upTag).addEventListener("click", settingsHandler.findById(eng, doc, wazac), false);
                                    //((EventTarget) rcTag).addEventListener("click", CategoryHandler.getInstance().delete(eng, doc, wazac, rcTag.getAttribute("id")), false);
                                } catch (IOException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (NoSuchFieldException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (URISyntaxException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            
                            
                            if(html.contains("subcategory-create.html")) {
                                try {
                                    Config.fillFormSelect(doc, eng);
                                    ((EventTarget) uscTag).addEventListener("click", SubCategoryHandler.getInstance().create(eng, doc, wazac), false);
                                    ((EventTarget) laTag).addEventListener("click", ArticleHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) lcTag).addEventListener("click", CategoryHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) upTag).addEventListener("click", settingsHandler.findById(eng, doc, wazac), false);
                                } catch (IOException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } 
                            if(html.contains("subcategory-list.html")) {
                                if(doc.getElementById("table-view") != null) {
                                    SubCategoryHandler.getInstance().tableListView(eng, doc, wazac);
                                }
                                
                                for(Element e : Config.findElementByParam(doc, "item")) {
                                    if(e.getAttribute("id").contains("view")) {
                                        ((EventTarget) e).addEventListener("click", SubCategoryHandler.getInstance().findById(eng, doc, wazac, e.getAttribute("id")), false);
                                    } else if(e.getAttribute("id").contains("edit")) {
                                        ((EventTarget) e).addEventListener("click", SubCategoryHandler.getInstance().update(eng, doc, wazac, new SubCategory(), e.getAttribute("id")), false);
                                    } else {
                                        ((EventTarget) e).addEventListener("click", SubCategoryHandler.getInstance().delete(eng, doc, wazac, e.getAttribute("id")), false);
                                    }
                                }
                                
                                ((EventTarget) uscTag).addEventListener("click", SubCategoryHandler.getInstance().createView(eng, doc, wazac), false);
                                ((EventTarget) laTag).addEventListener("click", ArticleHandler.getInstance().find(eng, doc, wazac), false);
                                ((EventTarget) lcTag).addEventListener("click", CategoryHandler.getInstance().find(eng, doc, wazac), false);
                                ((EventTarget) upTag).addEventListener("click", settingsHandler.findById(eng, doc, wazac), false);
                            
                            }
                            if(html.contains("subcategory.html")) {
                                try {
                                    ArticleHandler.getInstance().viewData(eng, doc, wazac, SessionService.getParam().get("param").toString());
                                    /*for(Element e : Config.findElementByParam(doc, "item")) {
                                    if(e.getAttribute("id").contains("edit")) {
                                    ((EventTarget) e).addEventListener("click", SubCategoryHandler.getInstance().update(eng, doc, wazac, new SubCategory(), e.getAttribute("id")), false);
                                    } else {
                                    ((EventTarget) e).addEventListener("click", SubCategoryHandler.getInstance().delete(eng, doc, wazac, e.getAttribute("id")), false);
                                    }
                                    }*/
                                    //SubCategoryHandler.getInstance().viewData(eng, doc, wazac, "item");
                                    ((EventTarget) laTag).addEventListener("click", ArticleHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) lcTag).addEventListener("click", CategoryHandler.getInstance().find(eng, doc, wazac), false);
                                    ((EventTarget) upTag).addEventListener("click", settingsHandler.findById(eng, doc, wazac), false);
                                    //((EventTarget) rscTag).addEventListener("click", SubCategoryHandler.getInstance().delete(eng, doc, wazac, rscTag.getAttribute("id")), false);
                                } catch (IOException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (NoSuchFieldException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (URISyntaxException ex) {
                                    Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            
                            EventListener listener = new EventListener() {
                                public void handleEvent(Event ev) {
                                    try {
                                        //Bug to fix later : verify data to use the adapted fxml dashboard
                                        replaceSceneContent(DashboardController.getInstance(), "view/dashboard.fxml", "Welcome back to your Dashboard");
                                    } catch (Exception ex) {
                                        Logger.getLogger(Wazac.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            };

                            if(dTag != null) {
                                ((EventTarget) dTag).addEventListener("click", listener, false);
                            }
                        }
                    }
                    
                }
        );
        
        wazac.getStage().setScene(new Scene(webView));
        wazac.getStage().show();
        wazac.getStage().getScene().getWindow().setWidth(1200);
        wazac.getStage().getScene().getWindow().setHeight(800);
        wazac.getStage().getScene().getWindow().sizeToScene();
        
    }
    
    public String contentString(String html) throws URISyntaxException, IOException {
        InputStream is = Wazac.class.getResourceAsStream(html);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        } in.close();
        System.out.println(response.toString());
        return response.toString();
    }   
    
    private Initializable replaceSceneContent(WazacController controller, String fxml, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Wazac.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Wazac.class.getResource(fxml));
        AnchorPane page;
        BorderPane borderPage;
        Scene scene = null;
        if(fxml.contains("index") || fxml.contains("login") || fxml.contains("dashboard") || fxml.contains("upload")) {
           try {
            page = (AnchorPane) loader.load(in);
           } finally {
                in.close();
           }  
           scene = new Scene(page, 1200, 800);
        } else {
           try {
            borderPage = (BorderPane) loader.load(in);
           } finally {
                in.close();
           } 
           scene = new Scene(borderPage, 1200, 800);
        }
        //scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
    
}
