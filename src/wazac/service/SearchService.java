/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import wazac.util._FileRW;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author dlrm
 */
public class SearchService {
    private static final Logger LOG = Logger.getLogger(SearchService.class.getName());
    
    public static Map<Object, Object> getFilterParameter() throws IOException {
        String jsonText = _FileRW.read("collections/search.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
            Map map = new HashMap();
            for (Object object : (Set) set) {

                if(object instanceof Map) {
                    Object[] val = ((Map) object).values().toArray();
                    map.put("search", val[0]);
                }
            }
            return map;
        } else {
            return null;
        }
    }
    
    public static void save(String id) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder sessionBuilder = new JsonObject.JsonBuilder();
        sessionBuilder.add("search", id);
        JsonObject sessionData = new JsonObject(sessionBuilder);
        _FileRW.write("collections/search.txt", sessionData.build().toString());
        
    }
}
