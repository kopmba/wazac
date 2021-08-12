/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import static wazac.Data.getDataChartArticle;
import wazac.entity.Category;
import wazac.service.ArticleService;
import wazac.service.CategoryService;

/**
 *
 * @author mardets
 */
public class Data {
    
    public static ObservableList getDataChartArticle() {
        ObservableList listData = null;
        try {
            listData = FXCollections.observableArrayList(
                    new XYChart.Data<String, Integer>("January", ArticleService.countPerDateMonth(1)),
                    new XYChart.Data<String, Integer>("February", ArticleService.countPerDateMonth(2)),
                    new XYChart.Data<String, Integer>("March", ArticleService.countPerDateMonth(3)),
                    new XYChart.Data<String, Integer>("April", ArticleService.countPerDateMonth(4)),
                    new XYChart.Data<String, Integer>("May", ArticleService.countPerDateMonth(5)),
                    new XYChart.Data<String, Integer>("June", ArticleService.countPerDateMonth(6)),
                    new XYChart.Data<String, Integer>("July", ArticleService.countPerDateMonth(7)),
                    new XYChart.Data<String, Integer>("August", ArticleService.countPerDateMonth(8)),
                    new XYChart.Data<String, Integer>("September", ArticleService.countPerDateMonth(9)),
                    new XYChart.Data<String, Integer>("October", ArticleService.countPerDateMonth(10)),
                    new XYChart.Data<String, Integer>("November", ArticleService.countPerDateMonth(11)),
                    new XYChart.Data<String, Integer>("December", ArticleService.countPerDateMonth(12))
            );
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listData;
    }
    
    public static ObservableList getDataArticleSoldSell() {
        ObservableList listData = null;
        try {
            listData = FXCollections.observableArrayList(
                    new XYChart.Data<String, Integer>("January", ArticleService.countPerDateMonth(1, "sold")),
                    new XYChart.Data<String, Integer>("February", ArticleService.countPerDateMonth(2, "sold")),
                    new XYChart.Data<String, Integer>("March", ArticleService.countPerDateMonth(3, "sold")),
                    new XYChart.Data<String, Integer>("April", ArticleService.countPerDateMonth(4, "sold")),
                    new XYChart.Data<String, Integer>("May", ArticleService.countPerDateMonth(5, "sold")),
                    new XYChart.Data<String, Integer>("June", ArticleService.countPerDateMonth(6, "sold")),
                    new XYChart.Data<String, Integer>("July", ArticleService.countPerDateMonth(7, "sold")),
                    new XYChart.Data<String, Integer>("August", ArticleService.countPerDateMonth(8, "sold")),
                    new XYChart.Data<String, Integer>("September", ArticleService.countPerDateMonth(9, "sold")),
                    new XYChart.Data<String, Integer>("October", ArticleService.countPerDateMonth(10, "sold")),
                    new XYChart.Data<String, Integer>("November", ArticleService.countPerDateMonth(11, "sold")),
                    new XYChart.Data<String, Integer>("December", ArticleService.countPerDateMonth(12, "sold"))
            );
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listData;
    }
    
    public static ObservableList getDataArticleDirectSell() {
        ObservableList listData = null;
        try {
            listData = FXCollections.observableArrayList(
                    new XYChart.Data<String, Integer>("January", ArticleService.countPerDateMonth(1, "direct")),
                    new XYChart.Data<String, Integer>("February", ArticleService.countPerDateMonth(2, "direct")),
                    new XYChart.Data<String, Integer>("March", ArticleService.countPerDateMonth(3, "direct")),
                    new XYChart.Data<String, Integer>("April", ArticleService.countPerDateMonth(4, "direct")),
                    new XYChart.Data<String, Integer>("May", ArticleService.countPerDateMonth(5, "direct")),
                    new XYChart.Data<String, Integer>("June", ArticleService.countPerDateMonth(6, "direct")),
                    new XYChart.Data<String, Integer>("July", ArticleService.countPerDateMonth(7, "direct")),
                    new XYChart.Data<String, Integer>("August", ArticleService.countPerDateMonth(8, "direct")),
                    new XYChart.Data<String, Integer>("September", ArticleService.countPerDateMonth(9, "direct")),
                    new XYChart.Data<String, Integer>("October", ArticleService.countPerDateMonth(10, "direct")),
                    new XYChart.Data<String, Integer>("November", ArticleService.countPerDateMonth(11, "direct")),
                    new XYChart.Data<String, Integer>("December", ArticleService.countPerDateMonth(12, "direct"))
            );
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listData;
    }
    
    public static ObservableList getDataPieChart() {
        ObservableList listData = null;
        try {
            if(CategoryService.find() != null) {
                for (Category category : CategoryService.find()) {
                    listData = FXCollections.observableArrayList(
                            new PieChart.Data(category.getName(), ArticleService.countByCategory(category.getName()))
                    );
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listData;
    }
    
    /**
     *
     */
    public static ObservableList dataChartArticle = getDataChartArticle();
    
    public static ObservableList dataChartArticleSoldSell = getDataArticleSoldSell();
    
    public static ObservableList dataChartArticleDirectSell = getDataArticleDirectSell();
    
    public static ObservableList dataChartCategoryList = getDataPieChart();
}
