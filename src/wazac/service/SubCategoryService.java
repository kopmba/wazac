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
import wazac.entity.SubCategory;
import static wazac.service.ActivityService.save;
import wazac.util._FileRW;
import wazac.util.JsonArray;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class SubCategoryService {

    private static final Logger LOG = Logger.getLogger(SubCategoryService.class.getName());
    
    
    public static List<SubCategory> find () throws IOException {
        String jsonText = _FileRW.read("collections/subcategories.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        
            List<SubCategory> categories = new ArrayList<SubCategory>();
        
            for(Object obj : (Set)set) {
                if(obj instanceof Map) {
                    SubCategory c = new SubCategory();
                    c.setId((Util.stringMapEval("id", (Map<Object, Object>) obj)));
                    c.setName((Util.stringMapEval("name", (Map<Object, Object>) obj)));
                    c.setDescription((Util.stringMapEval("description", (Map<Object, Object>) obj)));
                    c.setCategoryId((Util.stringMapEval("categoryId", (Map<Object, Object>) obj)));
                }
            }

            return categories;
        } else {
            return null;
        }
        
    }
    
    public static Set<SubCategory> filter (String predicat) throws IOException {
        String jsonText = _FileRW.read("collections/subcategories.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());

            Set<SubCategory> categories = new HashSet<SubCategory>();

            for(SubCategory c : find()) {
                if(c.getName().equals(predicat) || c.getId().equals(predicat)
                   || c.getDescription().equals(predicat) || c.getCategoryId().equals(predicat)) {
                    categories.add(c);
                }
            }
            

            return categories;
        }
        return null;
    }
    
    public static SubCategory findOne(String id) throws IOException, NoSuchFieldException {
        
        for(SubCategory c : find()) {
            return c.getId().equals(id) ? c : null;
        }
        return null;
    }
    
    public static SubCategory save(SubCategory category) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder builder = new JsonObject.JsonBuilder();
        builder.add("id", category.getId())
               .add("name", category.getName())
               .add("description", category.getDescription())
               .add("categoryId", category.getCategoryId())
               .createBuilder();
        
        JsonObject data = new JsonObject(builder);
        
        
        String jsonText = _FileRW.read("collections/subcategories.txt");
        
        String jsonStrObject = jsonText.substring(1, jsonText.length()-1);
        String jsonEval = Util.jsonEvalChar(jsonStrObject.replace(String.valueOf('"'), String.valueOf(' ')));
        String jsonResult = "[".concat(jsonEval)
                                .concat(",")
                                .concat(Util.jsonEvalChar(data.build().toString().replace(String.valueOf('"'), String.valueOf(' '))))
                                .concat("]");
        
        _FileRW.write("collections/subcategories.txt", jsonResult);
        
        return category;
    }
    
    public static SubCategory update(SubCategory category, String id) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        SubCategory c = findOne(id);
        if(c == null && id != "") {
            return save(category);
        }
        if (c == null) {
            throw new Error("No user Exist");
        } else {
            c.setId(Util.stringEval(id));
            c.setName(category.getName());
            c.setDescription(category.getDescription());
            c.setCategoryId(category.getCategoryId());
        }
        return save(c);
    }
    
    public static void delete(String id) throws IOException, NoSuchFieldException {
        SubCategory c = findOne(id);
        if(c == null) {
            throw new Error("No user Exist");
        } else {
            find().remove(c);
            
            JsonArray.JsonArrayBuilder jsonArrayBuilder = new JsonArray.JsonArrayBuilder();
            jsonArrayBuilder.setObject(listToSet(find()));
            JsonArray jsonArray = new JsonArray(jsonArrayBuilder);

//            FileRW.write("collections/subcategories.txt", jsonArray.build().toString());
        }
    }
    
    public static Set<Object> listToSet(List<SubCategory> data) {
        Set<Object> newSet = new HashSet<Object>();
        for(SubCategory c : data) {
            Map<Object, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("name", c.getName());
            map.put("description", c.getDescription());
            map.put("catgoryId", c.getCategoryId());
            newSet.add(map);
        }
        return newSet;
    }
}
