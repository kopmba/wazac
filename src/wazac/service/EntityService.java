/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import main.wewaz.auth.helper.Helper;
import wazac.entity.Article;
import wazac.entity.Category;
import wazac.entity.SubCategory;
import wazac.util._FileRW;

/**
 * @author Pro3010
 */
public class EntityService {
    
    public static List get(String collectionFileName) throws IOException {
        String content = _FileRW.read(collectionFileName);
        List data = (List) Helper.deserialize(content.getBytes());
        return data;
    }
    
    public static List sortData(List data) {
        List result = new ArrayList();
        for(int i = data.size() - 1; i >= 0; i--) {
            result.add(data.get(i));
        }
        return result;
    }
    
    public static List find(String collectionFileName) throws IOException {
        return get(collectionFileName);
    } 
    
    public static List find(String collectionFileName, int page, int ndata) throws IOException {
        List data = get(collectionFileName);
        List dataFilter = new ArrayList();
        int size = data.size();
        
        if(size > page * ndata) {
            for(int i = (page - 1) * ndata; i < page * ndata; i++) {
                dataFilter.add(data.get(i));
            }
        }
        
        if(size < page * ndata) {
            for(int i = size - 1; i < (page - 1) * ndata; i++) {
                dataFilter.add(data.get(i));
            }
        }
        
        return sortData(dataFilter);
    } 
    
    public static List filter(String collectionFileName, String id) throws IOException {
        List filterResult = new ArrayList();
        for(Object filterObject : get(collectionFileName)) {
            if(filterObject instanceof Category) {
                if(((Category) filterObject).getId().equals(id)) {
                    filterResult.add(filterObject);
                }
            }
            
            if(filterObject instanceof SubCategory) {
                if(((SubCategory) filterObject).getId().equals(id)) {
                    filterResult.add(filterObject);
                }
            }
            
            if(filterObject instanceof Article) {
                if(((Article) filterObject).getId().equals(id)) {
                    filterResult.add(filterObject);
                }
            }
        }
        return filterResult;
    }
    
    public static Object findOne(String collectionFileName, String id) throws IOException {
        for(Object filterObject : get(collectionFileName)) {
            if(filterObject instanceof Category) {
                return ((Category) filterObject).getId() == id ? filterObject : null;
            }
            
            if(filterObject instanceof SubCategory) {
                return ((SubCategory) filterObject).getId() == id ? filterObject : null;
            }
            
            if(filterObject instanceof Article) {
                return ((Article) filterObject).getId() == id ? filterObject : null;
            }
        }
        return null;
    }
    
    public static void add(String collectionFileName, Object o) throws IOException, URISyntaxException {
        _FileRW.remove(collectionFileName);
        Helper.serialize(o, collectionFileName);
    } 
    
}
