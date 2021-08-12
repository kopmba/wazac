/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import wazac.entity.Category;
import wazac.entity.Image;
import wazac.util._FileRW;
import wazac.util.JsonArray;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class ImageService {
    
    public static List<Image> list(String data) {
        Object set = Util.jsonMapper(Util.jsonEvalChar(data.replace(String.valueOf('"'), String.valueOf(' '))), new HashSet<Map<Object,Object>>());

        List<Image> imgs = new ArrayList<Image>();

        for(Object obj : (Set)set) {
            if(obj instanceof Map) {
                Image img = new Image();
                img.setUrl((Util.stringMapEval("url", (Map<Object, Object>) obj)));
                imgs.add(img);
            }
        }

        return imgs;
    }
    
    public static List<Image> find (String jsonText) throws IOException {
        //String jsonText = FileRW.read("collections/images.txt");
        if(jsonText != null) {
            if(!jsonText.equals("")) {
               return list(jsonText); 
            }
        } else {
           jsonText += _FileRW.read("collections/images.txt"); 
           if(!jsonText.equals("")) {
               return list(jsonText);
           }
        }
        return null;
    }
    
    public static String save(List<File> listFiles) throws IOException, FileNotFoundException, URISyntaxException {
        
        
        Set set = new HashSet<Object>();
        for(File f : listFiles) {
            set.add(f);
        }
        
        JsonArray.JsonArrayBuilder jsonArrayBuilder = new JsonArray.JsonArrayBuilder();

        jsonArrayBuilder.getObject().add(set);
        JsonArray jsonArray = new JsonArray(jsonArrayBuilder);

        _FileRW.write("collections/images.txt", jsonArray.build().toString());
        return jsonArray.build().toString();
   }
    
}
