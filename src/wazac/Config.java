/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import static javafx.scene.input.DataFormat.URL;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import static javafx.scene.input.KeyEvent.KEY_TYPED;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;
import wazac.controller.handler.ArticleHandler;
import wazac.controller.handler.CategoryHandler;
import wazac.controller.handler.SettingsHandler;
import wazac.controller.handler.SubCategoryHandler;
import wazac.entity.Activity;
import wazac.entity.Article;
import wazac.entity.Category;
import wazac.entity.Image;
import wazac.entity.Profile;
import wazac.entity.SubCategory;
import wazac.service.CategoryService;
import wazac.service.ProfileService;
import wazac.service.SubCategoryService;
import wazac.util._FileRW;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class Config {
    
    WebEngine engine;

    public Config(WebEngine engine) {
        this.engine = engine;
    }
    
    public boolean checkSolution(String solveSolution) {
        System.out.println("Input: " + solveSolution);
        engine.executeScript("document.getElementById()\"in\").classList.add(\"enabled\");");
        return true;
    }
    
    public static void setListener(Document doc, Element el, EventListener listener, Stage stage) throws IOException {
        
        ((EventTarget) el).addEventListener("click", listener, false);
        
    }
    
    public static void uploader(Image img) throws IOException, URISyntaxException {
        String imgData= img.getImgSrc().substring(img.getImgSrc().indexOf(",")+1);
        byte[] imageDataBytes = DatatypeConverter.parseBase64Binary(imgData);
        InputStream is = new ByteArrayInputStream(imageDataBytes);
        BufferedImage bim = ImageIO.read(is);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for(int readNum; (readNum = is.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }
        byte[] obj_image = bos.toByteArray();
        _FileRW.WriteImage(img.getImgName(), obj_image);
        try {
            is.close();
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Map<String, Object> formTextAreaData(Document doc, WebEngine engine, Map<String, Object> map) {
        NodeList textAreas = doc.getElementsByTagName("textarea");
        for (int i = 0; i < textAreas.getLength(); i++) {
            Element text = (Element) textAreas.item(i);
            String id = text.getAttribute("id");
            String name = text.getAttribute("name");
            
            if(id != null) {
                String value = (String) engine.executeScript("document.getElementById('"+id+"').value");
                map.put(name, value);
            }
            
        }
        return map;
    }
    
    public static Map<String, Object> formDataSelect(Document doc, WebEngine engine, Map<String, Object> map) throws IOException, URISyntaxException {
        NodeList selects = doc.getElementsByTagName("select");
        for (int i = 0; i < selects.getLength(); i++) {
            Element select = (Element) selects.item(i);
            String id = select.getAttribute("id");
            String name = select.getAttribute("name");
            
            if(id != null) {
                
                String value = (String) engine.executeScript("document.getElementById('"+id+"').value");
                map.put(name, value);
            }
            
        }
        return map;
    }
    
    public static Map<String, Object> formData(Document doc, WebEngine engine) {
        NodeList inputList = doc.getElementsByTagName("input");
        
        Element button = doc.getElementById("fileSelect");
        if(button != null) {
            ((EventTarget) button).addEventListener("click", selectFiles(doc), false);
        }
        Map<String, Object> mapper = new HashMap<>();
        for (int i = 0; i < inputList.getLength(); i++) {
            Element input = (Element) inputList.item(i);
            String id = input.getAttribute("id");
            String name = input.getAttribute("name");
            
            NodeList imgList = doc.getElementsByTagName("img");
            List<Image> images = new ArrayList<>();
            for (int j = 0; j < imgList.getLength(); j++) {
                Image image = new Image();
                Element img = (Element) imgList.item(j);
                String imgId = input.getAttribute("id");
                String imgSrc = (String) engine.executeScript("document.getElementById('"+imgId+"').src");
                String imgName = (String) engine.executeScript("document.getElementById('"+imgId+"').title");
                image.setImgSrc(imgSrc);
                image.setImgName(imgName);
                images.add(image);
            }
            
            if(id != null && !input.getAttribute("type").equals("file")) {
                String value = (String) engine.executeScript("document.getElementById('"+id+"').value");
                mapper.put(name, value);
            }
            
            mapper.put("images", images);
        }
        return mapper;
    }
    
    public static List<Map<String, String>> associateMapList(List<String> columns, Map<String, Object> mapper) {
        List<Map<String, String>> list = new ArrayList<>();
        List<String> values = new ArrayList<>();
        
        Map<String, String> newMap = new HashMap<>();
        
        
        for (Object o : mapper.values()) {
            values.add(o.toString());
        }
        
        values.add(values.size(), "");
        for (int i = 0; i < columns.size(); i++) {
            if(!columns.get(i).equals("Action")) {
                for (Map.Entry<String, Object> entry : mapper.entrySet()) {
                    String key = entry.getKey();
                    String value = (String) entry.getValue();
                    if(key.equals(columns.get(i))) {
                        newMap.put(columns.get(i), value);
                        list.add(newMap);
                    }
                }
            } else {
                newMap.put("Action", "");
                list.add(newMap);
            }
        }
        return list;
    }
    
    public static void fillColumnName(Document doc, WebEngine engine, List<String> mapperColumnName) {
        Element table = (Element) doc.getElementById("table-view");
        Element thead = doc.createElement("thead");
        
        Element trName = doc.createElement("tr");
        thead.appendChild(trName);
        
        for (String name : mapperColumnName) {
            Element thName = doc.createElement("th");
            thName.setAttribute("scope", "col");
            thName.setTextContent(name);
            trName.appendChild(thName);
        }
        table.appendChild(thead);
    }
    
    public static void fillRow(Document doc, WebEngine engine, List<String> mapperColumnName, Map<String, Object> mapperColumnValue) {
        //List<Map<String, String>> kvalues = associateMapList(mapperColumnName, mapperColumnValue);
        List<String> kvalues = new ArrayList<>();
        kvalues.add(0, mapperColumnName.get(0));
        kvalues.add(1, mapperColumnName.get(1));
        kvalues.add(2, mapperColumnName.get(2));
        
        Element table = (Element) doc.getElementById("table-view");
        Element tbody = doc.createElement("tbody");
        
        Element trValue = doc.createElement("tr");
        for (int i = 0; i < mapperColumnName.toArray().length; i++) {
            String key = mapperColumnName.toArray()[i].toString();
            
            Element tdValue = doc.createElement("td");
            if(i == 0) {
                tdValue.setAttribute("scope", "row");
            }
            
            //Set<String> keys = kvalues.get(i).keySet();
            
            if(i < kvalues.size()) {
                if(kvalues.get(i).equals(key)) {
                    tdValue.setTextContent(mapperColumnValue.get(key).toString());
                }
            } else {
                if(key.contains("Action")) {
                    Element viewTag = doc.createElement("a");
                    Element editTag = doc.createElement("a");
                    Element deleteTag = doc.createElement("a");
                    
                    viewTag.setAttribute("href", "#");
                    editTag.setAttribute("href", "#");
                    deleteTag.setAttribute("href", "#");
                    
                    viewTag.setAttribute("id", "view-item-".concat(mapperColumnValue.get("#").toString()));
                    editTag.setAttribute("id", "edit-item-".concat(mapperColumnValue.get("#").toString()));
                    deleteTag.setAttribute("id", "delete-item-".concat(mapperColumnValue.get("#").toString()));
                    
                    viewTag.setTextContent("Voir ");
                    editTag.setTextContent("Modifier ");
                    deleteTag.setTextContent("Supprimmer");
                    
                    tdValue.appendChild(viewTag);
                    tdValue.appendChild(editTag);
                    tdValue.appendChild(deleteTag);
                }
            }
            
            
            trValue.appendChild(tdValue);
        }
        tbody.appendChild(trValue);
        table.appendChild(tbody);
    }
    
    public static void fillTable(Document doc, WebEngine engine, List<String> mapperColumnName, List<Map<String, Object>> data) {
        fillColumnName(doc, engine, mapperColumnName);
        for (Map<String, Object> map : data) {
            fillRow(doc, engine, mapperColumnName, map);
        }
    }
    
    public static void fillFormSelect(Document doc, WebEngine engine) throws IOException {
        NodeList selects = doc.getElementsByTagName("select");
        for (int i = 0; i < selects.getLength(); i++) {
            Element select = (Element) selects.item(i);
            String id = select.getAttribute("id");
            String name = select.getAttribute("name");
            
            if(id != null) {
                if(id.equals("categories")) {
                    List<Category> list = CategoryService.find();
                    if(list != null) {
                        for (Category category : list) {
                            Element option = doc.createElement("option");
                            option.setTextContent(category.getName());
                            select.appendChild(option);
                        }
                    }
                }
                
                if(id.equals("subcategories")) {
                    List<SubCategory> list = SubCategoryService.find();
                    if(list != null) {
                        for (SubCategory subcategory : list) {
                            Element option = doc.createElement("option");
                            option.setTextContent(subcategory.getName());
                            select.appendChild(option);
                        }
                    }
                }
            }
            
        }
    }
    
    public static void fillForm(Document doc, WebEngine engine, Map<String, Object> mapper) {
        NodeList inputList = doc.getElementsByTagName("input");
        
        for (int i = 0; i < inputList.getLength(); i++) {
            Element input = (Element) inputList.item(i);
            String id = input.getAttribute("id");
            String name = input.getAttribute("name");
            
            if(id != null && !input.getAttribute("type").equals("file")) {
                if(mapper.get(name) != null) {
                    engine.executeScript("document.getElementById('"+id+"').value='"+mapper.get(name).toString()+"'");
                }
                
            }
        }
    }
    
    public static EventListener selectFiles(Document doc) {
        
        return new EventListener() {
            @Override
            public void handleEvent(Event event) {
                HTMLInputElement fileElem = (HTMLInputElement) doc.getElementById("fileElem");
                if(fileElem != null) {
                    fileElem.click();
                }
                event.preventDefault();
            }
        };
    }
    
    public static EventListener handleFiles(Document doc, WebEngine eng) {
        
        return new EventListener() {
            @Override
            public void handleEvent(Event event) {
                eng.setOnVisibilityChanged(handler -> {
                    
                });
            }
        };
    }
    
    public static List<Element> findElementByParam(Document doc, String objectName) {
        NodeList linksTag = doc.getElementsByTagName("a");
        List<Element> elements = new ArrayList<Element>();
        for (int i = 0; i < linksTag.getLength(); i++) {
            Element node = (Element)linksTag.item(i);
            if(node.getAttribute("id").contains(objectName.concat("-"))) {
                elements.add(node);
            }
        }
        return elements;
    }
    
    public static List<Element> findIputElement(Document doc) {
        NodeList inputsTag = doc.getElementsByTagName("input");
        List<Element> elements = new ArrayList<Element>();
        for (int i = 0; i < inputsTag.getLength(); i++) {
            Element node = (Element)inputsTag.item(i);
            elements.add(node);
        }
        return elements;
    }
    
    public static void fillPagination(Document doc, WebEngine engine, List list) throws IOException, URISyntaxException {
        NodeList lis = doc.getElementsByTagName("li");
        Element ul = (Element) doc.getElementById("pagination-items");
        
        String page = _FileRW.readContent("page.txt");
        int lastPage = Integer.valueOf(page);
        int pageIndex = Integer.valueOf(_FileRW.readContent("pageItem.txt"));
        
        double dsize = list.size()/2;
        String strSize = String.valueOf(dsize);
        int size = Integer.valueOf(strSize.substring(0, strSize.indexOf(',')));
        
        if(size > 1) {
            
            if(list.size() > 8) {
               Element li = doc.createElement("li");
               Element lidot = doc.createElement("li");
               
               li.setAttribute("id", "previous");
               li.setAttribute("class", "page-item"); 
               Element a = doc.createElement("a");
               a.setAttribute("id", "category-list");
               a.setAttribute("href", "#");
               a.setTextContent("Previous");
               
               lidot.setAttribute("id", "previous-dot");
               lidot.setAttribute("class", "page-item"); 
               Element adot = doc.createElement("a");
               adot.setAttribute("id", "category-list");
               adot.setAttribute("href", "#");
               adot.setTextContent("...");
               
               li.appendChild(a);
               lidot.appendChild(adot);
               
               ul.appendChild(li);
               ul.appendChild(lidot);
               
               if(pageIndex == 1) {
                   li.setAttribute("class", "page-item disabled");
               }
            }
            
            for(int i = 0; i < size; i++) {
                //if(pageIndex > 1)
                Element li = doc.createElement("li");
                li.setAttribute("id", "item-".concat(String.valueOf(i)));
                li.setAttribute("class", "page-item");
                Element a = doc.createElement("a");
                a.setAttribute("id", "category-list");
                a.setAttribute("href", "#");
                a.setTextContent(String.valueOf(i));
                li.appendChild(a);
                ul.appendChild(li);
                
                if(pageIndex == i) {
                    li.setAttribute("class", "page-item disabled");
                }
                
                if(i + 1 == size && list.size() > 8) {
                    Element _li = doc.createElement("li");
                    Element _lidot = doc.createElement("li");
                    
                    _li.setAttribute("id", "next");
                    _li.setAttribute("class", "page-item"); 
                    Element _a = doc.createElement("a");
                    _a.setAttribute("id", "category-list");
                    _a.setAttribute("href", "#");
                    _a.setTextContent("Next");
                    
                    _lidot.setAttribute("id", "next-dot");
                    _lidot.setAttribute("class", "page-item"); 
                    Element _adot = doc.createElement("a");
                    _adot.setAttribute("id", "category-list");
                    _adot.setAttribute("href", "#");
                    _adot.setTextContent("...");
                    
                    _li.appendChild(_a);
                    _lidot.appendChild(_adot);
               
                    ul.appendChild(_li);
                    ul.appendChild(_lidot);
                }
                
            }
            
        }
        
        if(pageIndex > 1 && list.size() > 10) {
           NodeList liList = doc.getElementsByTagName("li");
            for(int i = 0; i < liList.getLength(); i++) {
                Element li = (Element) liList.item(i);
                String id = li.getAttribute("id");
                Integer index = null;
                
                if(id.contains("item")) {
                    index = Integer.valueOf(id.charAt(id.length()));
                }
                
                if(index > index + 2 || li.getAttribute("id").equals("previous-dot")) {
                    if(li.getAttribute("id").contains("item")) {
                        li.setAttribute("class", "page-item undisplay");
                    }
                }
                
                if(index == pageIndex) {
                    li.setAttribute("class", "page-item active");
                }
                
                if(li.getAttribute("id").contains("next") || li.getAttribute("id").equals("previous")) {
                    li.setAttribute("class", "page-item");
                }
            } 
        }
        
        if(pageIndex == size && pageIndex > 3) {
            NodeList liList = doc.getElementsByTagName("li");
            for(int i = 0; i < liList.getLength(); i++) {
                Element li = (Element) liList.item(i);
                String id = li.getAttribute("id");
                Integer index = null;
                
                if(id.contains("item")) {
                    index = Integer.valueOf(id.charAt(id.length()));
                }
                
                if(li.getAttribute("id").contains("previous")) {
                    li.setAttribute("class", "page-item");
                }
                
                if(index < index - 2 || li.getAttribute("id").equals("next-dot")) {
                    if(li.getAttribute("id").contains("item")) {
                        li.setAttribute("class", "page-item undisplay");
                    }
                }
                
                if(index == pageIndex) {
                    li.setAttribute("class", "page-item active");
                }
                
                if(li.getAttribute("id").equals("next")) {
                    li.setAttribute("class", "page-item disabled");
                }
            }
            
        }
        
        
    }
    
    public static List<Element> findPaginationElementByParam(Document doc, String objectName) {
        NodeList linksTag = doc.getElementsByTagName("li");
        List<Element> elements = new ArrayList<Element>();
        for (int i = 0; i < linksTag.getLength(); i++) {
            Element node = (Element)linksTag.item(i);
            if(node.getAttribute("id").contains(objectName.concat("-"))) {
                elements.add(node);
            }else {
               if(node.getAttribute("id").contains("previous") || node.getAttribute("id").contains("next")) {
                   elements.add(node);
               } 
            }
        }
        return elements;
    }
    
}
