/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javafx.scene.web.WebEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import wazac.Wazac;
import wazac.entity.Article;

/**
 *
 * @author mardets
 */
public class RenderView {
    
    public static void recursiveList(Document doc, String id, List<Object> list, EventListener listener) throws IllegalArgumentException, IllegalAccessException {
        Element divList = doc.getElementById(id);
        Element ulTag = doc.createElement("ul");
        
        if(list != null) {
            for (Object object : list) {
                Field[] fields = object.getClass().getDeclaredFields();
                for(int i = 0; i < fields.length; i++) {
                    Element liTag = doc.createElement("li");

                    if(fields[i].getName().contains("id")) {
                        liTag.setAttribute("id", fields[i].getName().concat("-").concat(String.valueOf(i)));
                    }


                    liTag.setTextContent(fields[i].get(fields[i]).toString());
                    ((EventTarget) liTag).addEventListener("click", listener, false);
                    ulTag.appendChild(liTag);

                }
            }
        }
        
        divList.appendChild(ulTag);
    }
    
    public static void printListView(Document doc, String id, Collection list, EventListener listener) throws IllegalArgumentException, IllegalAccessException {
        recursiveList(doc, id, (List<Object>) list, listener);
    }
    
    public static void printListView(Document doc, String id, String mode, List<Object> list, EventListener ...listeners) throws IllegalArgumentException, IllegalAccessException {
        Element divList = doc.getElementById(id);
        Element ulTag = doc.createElement("ul");
        
        for (Object object : list) {
            Field[] fields = object.getClass().getDeclaredFields();
            for(int i = 0; i < fields.length; i++) {
                if(mode.equals("multi") && fields[i].getType().isInstance(list)) {
                    recursiveList(doc, id, list, listeners[0]);
                } else {
                    Element liTag = doc.createElement("li");
                    if(fields[i].getName().contains("id")) {
                        liTag.setAttribute("id", fields[i].getName().concat("-").concat(String.valueOf(i)));
                    }
                
                
                    liTag.setTextContent(fields[i].get(fields[i]).toString());
                    ((EventTarget) liTag).addEventListener("click", listeners[1], false);
                    ulTag.appendChild(liTag);
                }
                
                
            }
        }
        
        divList.appendChild(ulTag);
    }
    
    public static void getTableRow(WebEngine eng, Document doc, String id, String classname, Map<String, Object> map, EventListener listener) {
        Element el = doc.getElementById(id);
        String predicat = String.valueOf(id.indexOf('-', id.length()));
        NodeList list = (NodeList) eng.executeScript("document.getElementsBtyClassname('"+classname+"')");
        for (int i = 0; i < list.getLength(); i++) {
            Element node = (Element) list.item(i);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if(node.getAttribute("class").contains(key)) {
                    node.setTextContent(value.toString());
                }
            }
        }
    }
    
}
