/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import wazac.service.ImageService;
import wazac.util._FileRW;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class Uploader {

    private static final Logger LOG = Logger.getLogger(Uploader.class.getName());
    
    public static void uploadFile(File file) throws FileNotFoundException, URISyntaxException {
        try {
            BufferedImage bImage = ImageIO.read(file);
            WritableImage image = SwingFXUtils.toFXImage(bImage, null);
            ImageView img = new ImageView();
            img.setImage(image);
            img.setFitWidth(340);
            img.setFitHeight(300);
            img.scaleXProperty();
            img.scaleYProperty();
            img.setSmooth(true);
            img.setCache(true);
            //FileRW.WriteImage(file.getPath(), Util.stringEvalUrl(file.getPath()));
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for(int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
            }
            byte[] obj_image = bos.toByteArray();
            _FileRW.WriteImage(Util.stringEvalUrl(file.getPath()), obj_image);
        } catch(IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public static void adaptView(File file, ImageView view) throws IOException {
        BufferedImage bImage = ImageIO.read(file);
        WritableImage image = SwingFXUtils.toFXImage(bImage, null);
        view.setImage(image);
        view.setFitWidth(150);
        view.setFitHeight(150);
        view.scaleXProperty();
        view.scaleYProperty();
        view.setSmooth(true);
        view.setCache(true);
    }
    
    public static void configureFileChooser(FileChooser fileChooser, String type, String ...extension) {
        fileChooser.setTitle("View pictures");
        //fileChooser.setInitialDirectory(new File(System.getProperty("images")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(type, extension));
        /*fileChooser.getExtensionFilters().addAll(
		         new FileChooser.ExtensionFilter("Text Files", "*.txt"),
		         new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
		         new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
		         new FileChooser.ExtensionFilter("All Files", "*.*"));*/
    }
    
    public static void openFile(ActionEvent event) throws FileNotFoundException, URISyntaxException, IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        List<File> list = new ArrayList<>();
        list.add(file);
        ImageService.save(list);
        if (file != null) {
            uploadFile(file);
        }
    }
    
    public static Map<String, Object> openFile(ActionEvent event, FileChooser fileChooser) throws FileNotFoundException, URISyntaxException, IOException {
        File file = fileChooser.showOpenDialog(new Stage());
        List<File> list = new ArrayList<>();
        list.add(file);
        String json = ImageService.save(list);
        
        Map<String, Object> mapImg = new HashMap<String, Object>();
        mapImg.put("json", json);
        mapImg.put("file", list);
        if (file != null) {
            uploadFile(file);
        }
        return mapImg;
    }
    
    public static void openMultipleFile(ActionEvent event) throws IOException, FileNotFoundException, URISyntaxException {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, "Image Files", "*.png", "*.jpg", "*.gif");
        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
        ImageService.save(list);
        if(list != null) {
            for(File file : list) {
                uploadFile(file);
            }
        }
    }
    
    public static Map<String, Object> openMultipleFile(ActionEvent event, FileChooser fileChooser) throws IOException, FileNotFoundException, URISyntaxException {
        
        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
        String json = ImageService.save(list);
        
        Map<String, Object> mapImg = new HashMap<String, Object>();
        mapImg.put("json", json);
        mapImg.put("file", list);
        if(list != null) {
            for(File file : list) {
                uploadFile(file);
            }
        }
        return mapImg;
    }
    
}
