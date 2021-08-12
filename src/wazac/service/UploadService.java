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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import wazac.entity.Category;
import wazac.entity.Image;
import wazac.entity.Upload;
import static wazac.service.CategoryService.find;
import wazac.util._FileRW;
import wazac.util.JsonArray;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class UploadService {
    
    private static final Logger LOG = Logger.getLogger(CategoryService.class.getName());
    
    public static List<Upload> find () throws IOException {
        String jsonText = _FileRW.read("collections/uploads.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(Util.jsonEvalChar(jsonText.replace(String.valueOf('"'), String.valueOf(' '))), new HashSet<Map<Object,Object>>());

            List<Upload> images = new ArrayList<Upload>();

            for(Object obj : (Set)set) {
                if(obj instanceof Map) {
                    Upload i = new Upload();
                    i.setArticleId((Util.stringMapEval("articleId", (Map<Object, Object>) obj)));
                    i.setImg1((Util.stringMapEval("img1", (Map<Object, Object>) obj)));
                    i.setImg2((Util.stringMapEval("img2", (Map<Object, Object>) obj)));
                    i.setImg3((Util.stringMapEval("img3", (Map<Object, Object>) obj)));
                    i.setImg4((Util.stringMapEval("img4", (Map<Object, Object>) obj)));
                    i.setImg5((Util.stringMapEval("img5", (Map<Object, Object>) obj)));
                    i.setImg6((Util.stringMapEval("img6", (Map<Object, Object>) obj)));
                    images.add(i);
                }
            }

            return images;
        }
        return null;
    }
    
    public static Set<Upload> filter (String predicat) throws IOException {
        String jsonText = _FileRW.read("collections/uploads.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());

            Set<Upload> images = new HashSet<Upload>();

            for(Upload i : find()) {
                if(i.getArticleId().equals(predicat)) {
                    images.add(i);
                }
            }

            return images;
        }
        return null;
    }
    
    public static Upload findOne(String id) throws IOException, NoSuchFieldException {
        if(find() != null) {
            for(Upload i : find()) {
                return i.getArticleId().equals(id) ? i : null;
            }
        }
        return null;
    }
    
    public static Upload save(Upload image) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder builder = new JsonObject.JsonBuilder();
        builder.add("articleId", image.getArticleId())
               .add("img1", image.getImg1())
                .add("img2", image.getImg2())
                .add("img3", image.getImg3())
                .add("img4", image.getImg4())
                .add("img5", image.getImg5())
                .add("img6", image.getImg6())
               .createBuilder();
        
        JsonObject data = new JsonObject(builder);
        
        String jsonText = _FileRW.read("collections/uploads.txt");
        String jsonStrObject = jsonText.substring(1, jsonText.length()-1);
        String jsonEval = Util.jsonEvalChar(jsonStrObject.replace(String.valueOf('"'), String.valueOf(' ')));
        String jsonResult = "[".concat(jsonEval)
                                .concat(",")
                                .concat(Util.jsonEvalChar(data.build().toString().replace(String.valueOf('"'), String.valueOf(' '))))
                                .concat("]");
        
        _FileRW.write("collections/uploads.txt", jsonResult);
//        
        
        return image;
    }
    
    public static Upload update(Upload upload, String id) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        Upload i = findOne(id);
        if(i == null && id != "") {
            return save(i);
        }
        if (i == null) {
            throw new Error("No article with files Exist");
        } else {
            i.setArticleId(Util.stringEval(id));
            i.setImg1(upload.getImg1());
            i.setImg2(upload.getImg2());
            i.setImg3(upload.getImg3());
            i.setImg4(upload.getImg4());
            i.setImg5(upload.getImg5());
            i.setImg6(upload.getImg6());
        }
        return save(i);
    }
    
}
