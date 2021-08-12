/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

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
import wazac.entity.User;
import static wazac.service.ActivityService.save;
import static wazac.service.UserService.find;
import wazac.util._FileRW;
import wazac.util.JsonArray;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class CategoryService {

    private static final Logger LOG = Logger.getLogger(CategoryService.class.getName());
    
    public static List<Category> find () throws IOException {
        String jsonText = _FileRW.read("collections/categories.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(Util.jsonEvalChar(jsonText.replace(String.valueOf('"'), String.valueOf(' '))), new HashSet<Map<Object,Object>>());

            List<Category> categories = new ArrayList<Category>();

            for(Object obj : (Set)set) {
                if(obj instanceof Map) {
                    Category c = new Category();
                    c.setId((Util.stringMapEval("id", (Map<Object, Object>) obj)));
                    c.setName((Util.stringMapEval("name", (Map<Object, Object>) obj)));
                    c.setDescription((Util.stringMapEval("description", (Map<Object, Object>) obj)));
                    categories.add(c);
                }
            }

            return categories;
        }
        return null;
    }
    
    public static Set<Category> filter (String predicat) throws IOException {
        String jsonText = _FileRW.read("collections/categories.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());

            Set<Category> categories = new HashSet<Category>();

            for(Category c : find()) {
                if(c.getName().equals(predicat) || c.getId().equals(predicat)
                   || c.getDescription().equals(predicat)) {
                    categories.add(c);
                }
            }

            return categories;
        }
        return null;
    }
    
    public static Category findOne(String id) throws IOException, NoSuchFieldException {
        if(find() != null) {
            for(Category c : find()) {
                return c.getId().equals(id) ? c : null;
            }
        }
        return null;
    }
    
    public static Category save(Category category) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder builder = new JsonObject.JsonBuilder();
        builder.add("id", category.getId())
               .add("name", category.getName())
               .add("description", category.getDescription())
               .add("created", category.getCreated())
               .createBuilder();
        
        JsonObject data = new JsonObject(builder);
        
        String jsonText = _FileRW.read("collections/categories.txt");
        String jsonStrObject = jsonText.substring(1, jsonText.length()-1);
        String jsonEval = Util.jsonEvalChar(jsonStrObject.replace(String.valueOf('"'), String.valueOf(' ')));
        String jsonResult = "[".concat(jsonEval)
                                .concat(",")
                                .concat(Util.jsonEvalChar(data.build().toString().replace(String.valueOf('"'), String.valueOf(' '))))
                                .concat("]");
        
        _FileRW.write("collections/categories.txt", jsonResult);
//        
        
        return category;
    }
    
    public static Category update(Category category, String id) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        Category c = findOne(id);
        if(c == null && id != "") {
            return save(category);
        }
        if (c == null) {
            throw new Error("No user Exist");
        } else {
            c.setId(Util.stringEval(id));
            c.setName(category.getName());
            c.setDescription(category.getDescription());
        }
        return save(c);
    }
    
    public static void delete(String id) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        Category c = findOne(id);
        if(c == null) {
            throw new Error("No user Exist");
        } else {
            find().remove(c);
            
            JsonArray.JsonArrayBuilder jsonArrayBuilder = new JsonArray.JsonArrayBuilder();
            jsonArrayBuilder.setObject(listToSet(find()));
            JsonArray jsonArray = new JsonArray(jsonArrayBuilder);

            _FileRW.write("collections/categories.txt", jsonArray.build().toString());
        }
    }
    
    public static Set<Object> listToSet(List<Category> data) {
        Set<Object> newSet = new HashSet<Object>();
        for(Category c : data) {
            Map<Object, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("name", c.getName());
            map.put("description", c.getDescription());
            newSet.add(map);
        }
        return newSet;
    }
}
