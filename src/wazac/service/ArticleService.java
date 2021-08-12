/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import wazac.entity.Activity;
import wazac.entity.Article;
import wazac.entity.Profile;
import wazac.entity.User;
import wazac.util._FileRW;
import wazac.util.JsonArray;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class ArticleService {

    private static final Logger LOG = Logger.getLogger(ArticleService.class.getName());
    
    public static List<Article> find () throws IOException {
        String jsonText = _FileRW.read("collections/articles.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());

            List<Article> articles = new ArrayList<Article>();

            for(Object obj : (Set)set) {
                if(obj instanceof Map) {
                    Article a = new Article();
                    a.setId(Util.stringMapEval("id", (Map<Object, Object>) obj));
                    a.setArticleStatus(Util.stringMapEval("articleStatus", (Map<Object, Object>) obj));
                    a.setArticleType(Util.stringMapEval("articleType", (Map<Object, Object>) obj));
                    a.setClassification(Util.stringMapEval("classification", (Map<Object, Object>) obj));
                    a.setDescription(Util.stringMapEval("description", (Map<Object, Object>) obj));
                    a.setGuarantee(Util.stringMapEval("guarantee", (Map<Object, Object>) obj));
                    a.setName(Util.stringMapEval("name", (Map<Object, Object>) obj));
                    a.setPrice(Double.valueOf((Util.stringMapEval("price", (Map<Object, Object>) obj))));
                    a.setQty(Integer.valueOf(Util.stringMapEval("qty", (Map<Object, Object>) obj)));
                    a.setYear(Integer.valueOf(Util.stringMapEval("year", (Map<Object, Object>) obj)));
                    a.setSeller(Util.stringMapEval("seller", (Map<Object, Object>) obj));
                    a.setCreated(LocalDate.now().toString());
                    articles.add(a);
                }
            }

            return articles;
        } else {
            return null;
        }
    }
    
    public static Article findBySeller(String userId) throws IOException, NoSuchFieldException {
        if(ArticleService.find() != null) {
            for(Article p : ArticleService.find()) {
                return p.getSeller().equals(Util.stringEval(userId)) ? p : null;
            }
        }
        return null;
    }
    
    public static int countByCategory(String categoryId) throws IOException, NoSuchFieldException {
        int counter = 0;
        if(ArticleService.find() != null) {
            for(Article p : ArticleService.find()) {
                if(p.getArticleType().equals(Util.stringEval(categoryId))) {
                    counter++;
                }
            }
        }
        return counter;
    }
    
    public static int countPerDateMonth(int month) throws IOException {
        int counter = 0;
        if(ArticleService.find() != null) {
            for(Article p : ArticleService.find()) {
                if (LocalDate.parse(p.getCreated()).getMonthValue() == month) {
                    counter++;
                }
            }
        }
        return counter;
    }
    
    public static int countPerDateMonth(int month, String status) throws IOException {
        int counter = 0;
        if(ArticleService.find() != null) {
            for(Article p : ArticleService.find()) {
                if (LocalDate.parse(p.getCreated()).getMonthValue() == month && p.getArticleStatus().equals(status)) {
                    counter++;
                }
            }
        }
        return counter;
    }
    
    public static Set<Article> filter (String predicat) throws IOException {
        String jsonText = _FileRW.read("collections/articles.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());

            Set<Article> articles = new HashSet<Article>();

            for(Article a : find()) {
                if(a.getArticleStatus().equals(predicat) || a.getArticleType().equals(predicat)
                   || a.getId().equals(predicat) || a.getClassification().equals(predicat)
                   || a.getSeller().equals(predicat) || a.getName().equals(predicat)
                   || a.getQty().equals(predicat) || a.getPrice().equals(predicat)
                   || a.getYear().equals(predicat)) {
                    articles.add(a);
                }
            }

            return articles;
        }
        return null;
    }
    
    public static Article findOne(String id) throws IOException, NoSuchFieldException {
        
        for(Article a : find()) {
            return a.getId().equals(id) ? a : null;
        }
        return null;
    }
    
    public static Article save(Article article) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder builder = new JsonObject.JsonBuilder();
        builder.add("id", article.getId())
               .add("articleStatus", article.getArticleStatus())
               .add("articleType", article.getArticleType())
               .add("classification", article.getClassification())
               .add("description", article.getDescription())
               .add("guarantee", article.getGuarantee())
               .add("name", article.getName())
               .add("qty", article.getQty())
               .add("seller", article.getSeller())
               .add("year", article.getYear())
               .createBuilder();
        
        JsonObject data = new JsonObject(builder);
        
        String jsonText = _FileRW.read("collections/articles.txt");
        String jsonStrObject = jsonText.substring(1, jsonText.length()-1);
        String jsonEval = Util.jsonEvalChar(jsonStrObject.replace(String.valueOf('"'), String.valueOf(' ')));
        String jsonResult = "[".concat(jsonEval)
                                .concat(",")
                                .concat(Util.jsonEvalChar(data.build().toString().replace(String.valueOf('"'), String.valueOf(' '))))
                                .concat("]");
        
        _FileRW.write("collections/articles.txt", jsonResult);
        
        return article;
    }
    
    public static Article update(Article article, String id) throws IOException, NoSuchFieldException, URISyntaxException {
        Article a = findOne(id);
        if (a == null) {
            throw new Error("No user Exist");
        } else {
            a.setArticleStatus(article.getArticleStatus());
            a.setArticleType(article.getArticleType());
            a.setClassification(article.getClassification());
            a.setDescription(article.getDescription());
            a.setGuarantee(article.getGuarantee());
            a.setName(article.getName());
            a.setPrice(Double.valueOf(article.getPrice()));
            a.setQty(Integer.valueOf(article.getQty()));
            a.setYear(Integer.valueOf(article.getYear()));
            a.setSeller(article.getSeller());
        }
        return save(a);
    }
    
    public static void delete(String id) throws IOException, NoSuchFieldException {
        Article a = findOne(id);
        if(a == null) {
            throw new Error("No user Exist");
        } else {
            find().remove(a);
            
            JsonArray.JsonArrayBuilder jsonArrayBuilder = new JsonArray.JsonArrayBuilder();
            jsonArrayBuilder.setObject(listToSet(find()));
            JsonArray jsonArray = new JsonArray(jsonArrayBuilder);

//            FileRW.write("collections/activities.txt", jsonArray.build().toString());
        }
    }
    
    public static Set<Object> listToSet(List<Article> data) {
        Set<Object> newSet = new HashSet<Object>();
        for(Article a : data) {
            Map<Object, Object> map = new HashMap<>();
            map.put("id", a.getId());
            map.put("articleStatus", a.getArticleStatus());
            map.put("articleType", a.getArticleType());
            map.put("classification", a.getClassification());
            map.put("description", a.getDescription());
            map.put("guarantee", a.getGuarantee());
            map.put("price", a.getPrice());
            map.put("qty", a.getQty());
            map.put("year", a.getYear());
            map.put("userId", a.getSeller());
            newSet.add(map);
        }
        return newSet;
    }
}
