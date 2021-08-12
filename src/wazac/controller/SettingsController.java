package wazac.controller;


import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import wazac.Config;
import wazac.Uploader;
import wazac.Wazac;
import wazac.WazacController;
import wazac.entity.Activity;
import wazac.entity.Article;
import wazac.entity.Profile;
import wazac.entity.User;
import wazac.service.ActivityService;
import wazac.service.ArticleService;
import wazac.service.HistoryService;
import wazac.service.ImageService;
import wazac.service.ProfileService;
import wazac.service.SessionService;
import wazac.service.UserService;
import wazac.util._FileRW;
import wazac.util.JsonObject;
import wazac.util.JsonObject.JsonBuilder;
import wazac.util.Util;

public class SettingsController extends BorderPane implements WazacController, Initializable {

    private static final Logger LOG = Logger.getLogger(SettingsController.class.getName());
    
    private Desktop desktop = Desktop.getDesktop();
    
    private Wazac wazac;
    private Config pageConfig;
    
    @FXML
    TextField firstname;
    @FXML
    TextField lastname;
    @FXML
    TextField phone;
    @FXML
    TextField birthday;
    @FXML
    TextField username;
    @FXML
    TextField email;
    
    @FXML
    Button mSave;
    @FXML
    Button mCancel;
    
    
    
    @FXML
    Button changePhoto;
    @FXML
    Button uploader;
    
    @FXML
    TextField shopname;
    @FXML
    TextField title;
    @FXML
    TextField location;
    @FXML
    TextField city;
    
    @FXML
    Button aSave;
    @FXML
    Button aCancel;
    
    @FXML
    Label fullname;
    @FXML
    Label activity;
    
    @FXML
    ImageView pImage;
    
    public static SettingsController getInstance() {
        return new SettingsController();
    }
    
    @Override
    public void setWazacInstance(Wazac wazac) {
        this.wazac = wazac;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            findUser(SessionService.getSession().get("userId").toString());
        } catch (IOException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToDashboard() {
        LOG.log(Level.INFO, "Go to Dashboard!");
        wazac.setPane(wazac.getStage(), DashboardController.getInstance(), "view/dashboard.fxml", "Contrôlez vos ressources via ce dasboard!");
    }
    
    public void goToArticles() throws URISyntaxException, IOException {
        LOG.log(Level.INFO, "Go to Articles list!");
        _FileRW.remove("collections/images.txt");
        wazac.setPane(wazac.getStage(), ArticleController.getInstance(), "view/article/list.fxml", "List of articles");
    }
    
    public void goToCategories() {
        LOG.log(Level.INFO, "Go to Categories list!");
        wazac.setPane(wazac.getStage(), CategoryController.getInstance(), "view/category/list.fxml", "List of categories");
    }
    
    public void goToSettings() throws URISyntaxException, IOException {
        LOG.log(Level.INFO, "Go to Settings!");
        _FileRW.remove("collections/images.txt");
        wazac.setPane(wazac.getStage(), SettingsController.getInstance(), "view/settings.fxml", "Settings");
    }
    
    public void changePhoto(ActionEvent event) throws FileNotFoundException, URISyntaxException, IOException {
        LOG.log(Level.INFO, "Change photo!");
        _FileRW.remove("collections/images.txt");
        FileChooser fileChooser = new FileChooser();
        Uploader.configureFileChooser(fileChooser, "Image Files", "*.png", "*.jpg", "*.gif");
        Map<String, Object> uploader = Uploader.openFile(event, fileChooser);
        List<File> files = (List<File>) uploader.get("file");
        
        if(ImageService.find(uploader.get("json").toString()) != null) {
            Uploader.adaptView(files.get(0), pImage);
            
        }
    }
    
    public void upload(ActionEvent event) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        if(UserService.findByUsername(SessionService.getSession().get("userId").toString()) != null) {
            updateProfile(event);
        }
    }
    
    public void setUserInfo(Profile p, Activity a) throws IOException {
        LOG.log(Level.INFO, "Set settings");
        if(p != null) {
            fullname.setText(p.getFirstname() + " " + p.getLastname());
        }
        
        
        if(a != null) {
            activity.setText(a.getTitle() + " - " + a.getShopname() + ", " + a.getCity());
        }
        
    }
    
    public void updateProfile(ActionEvent event) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        LOG.log(Level.INFO, "Update profile");
        
        Map obj = SessionService.getSession();
        String userId = obj.get("userId").toString();
        
        Profile p = new Profile();
        if(ProfileService.find() == null) {
            p.setId(String.valueOf(1));
        } else {
            p.setId(String.valueOf(ProfileService.find().size()));
        }
        
        p.setFirstname(firstname.getText());
        p.setLastname(lastname.getText());
        p.setBirthday(birthday.getText());
        p.setPhone(phone.getText());
        p.setUserId(userId);
        if(ImageService.find("") != null) {
            p.setImgUrl(ImageService.find("").get(0).getUrl());
        }
        
        User u = UserService.findByUsername(userId);
        u.setEmail(email.getText());
        u.setUsername(username.getText());
        UserService.update(u, u.getId());
        
        
        ProfileService.update(p, p.getId());
        HistoryService.save("Update profile", Date.valueOf(LocalDate.now()));
        setUserInfo(p, null);
    }
    
    public void cancelProfile(ActionEvent event) {
        firstname.setText("");
        lastname.setText("");
        birthday.setText("");
        phone.setText("");
    }
    
    public void updateActivity(ActionEvent event) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        LOG.log(Level.INFO, "Update activity");
        Activity a = new Activity();
        a.setId(String.valueOf(new Random().nextInt(4)));
        a.setCity(city.getText());
        a.setLocation(location.getText());
        a.setShopname(shopname.getText());
        a.setTitle(title.getText());
        a.setUserId(SessionService.getSession().get("userId").toString());
        
        ActivityService.update(a, a.getId());
        HistoryService.save("Update activity", Date.valueOf(LocalDate.now()));
        setUserInfo(null, a);
    }
    
    public void cancelActivity(ActionEvent event) {
        city.setText("");
        location.setText("");
        shopname.setText("");
        title.setText("");
    }
    
    public void findUser(String userId) throws IOException, NoSuchFieldException, URISyntaxException {
        Profile p = ProfileService.findByUserId(SessionService.getSession().get("userId").toString());
        Activity a = ActivityService.findByUserId(SessionService.getSession().get("userId").toString());
        
        if(a != null) {
            city.setText(a.getCity());
            location.setText(a.getLocation());
            shopname.setText(a.getShopname());
            title.setText(a.getTitle());
        }
        
        if(p != null) {
            firstname.setText(p.getFirstname());
            lastname.setText(p.getLastname());
            birthday.setText(p.getBirthday());
            phone.setText(p.getPhone()); 
        }
        
        setUserInfo(p, a);
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