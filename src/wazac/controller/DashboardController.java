/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import wazac.Config;
import wazac.Data;
import wazac.Wazac;
import wazac.WazacController;
import wazac.controller.handler.ArticleHandler;
import wazac.service.SessionService;
import wazac.util._FileRW;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class DashboardController extends BorderPane implements WazacController, Initializable {

    private static final Logger LOG = Logger.getLogger(DashboardController.class.getName());

    private Wazac wazac;
    private Config pageConfig;
    
    @FXML
    LineChart lineChart;
    
    @FXML
    BarChart barChart;
    
    @FXML
    PieChart pieChart;
    
    @FXML
    Hyperlink textLink;
    
    public static DashboardController getInstance() {
        return new DashboardController();
    }
    
    @Override
    public void setWazacInstance(Wazac wazac) {
        this.wazac = wazac;
        //this.pageConfig = new Config();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        createLineChart();
        createBarAreaChart();
        createPieChart();
        try {
            textLink.setText(Util.stringEval(SessionService.getSession().get("userId").toString()));
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void createLineChart() {
        NumberAxis xAxis = new NumberAxis("Values for X-Axis", 0, 3, 1);
        NumberAxis yAxis = new NumberAxis("Values for Y-Axis", 0, 3, 1);
        
        ObservableList<XYChart.Series<String,Integer>> lineChartData = FXCollections.observableArrayList(
            new LineChart.Series<String,Integer>("Status article data", Data.dataChartArticle) 
        );
        
        //LineChart chart = new LineChart(xAxis, yAxis, lineChartData);
        lineChart.setData(lineChartData);
    }
    
    protected void createBarAreaChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        
        ObservableList<XYChart.Series<String,Integer>> barAreaChartData = FXCollections.observableArrayList(
           new AreaChart.Series<String, Integer>("Sold", Data.dataChartArticleSoldSell),
           new AreaChart.Series<String, Integer>("Sell", Data.dataChartArticleDirectSell)
        );
        
        //AreaChart areaChart = new AreaChart(xAxis, yAxis, barAreaChartData);
        barChart.setData(barAreaChartData);
    }
    
    protected void createPieChart() {
        pieChart.setData(Data.dataChartCategoryList);
        pieChart.setId("BasicPie");
        pieChart.setTitle("Category details");
    }
    
    public void goToDashboard() {
        LOG.log(Level.INFO, "Go to Dashboard!");
        wazac.setPane(wazac.getStage(), DashboardController.getInstance(), "view/dashboard.fxml", "Contrôlez vos ressources via ce dasboard!");
    }
    
    public void goToArticles() {
        LOG.log(Level.INFO, "Go to Articles list!");
        wazac.setPane(wazac.getStage(), ArticleController.getInstance(), "view/article/list.fxml", "List of articles");
    }
    
    public void goToCategories() throws IOException, URISyntaxException {
        LOG.log(Level.INFO, "Go to Categories list!");
        _FileRW.remove("collections/search.txt");
        wazac.setPane(wazac.getStage(), CategoryController.getInstance(), "view/category/list.fxml", "List of categories");
        //this.pageConfig.setPane("view/category/list.fxml", "List of categories");
    }
    
    public void goToSettingsProfile() throws URISyntaxException, IOException {
        LOG.log(Level.INFO, "Go to Settings Pofile!");
        wazac.viewpane(wazac, "view/pages/category-list.html");
        //wazac.setPane(wazac.getStage(), ArticleHandler.getInstance(), "view/article/create-upload.fxml", "");
    }
    
    public void goToAddCategory() throws URISyntaxException, IOException {
        LOG.log(Level.INFO, "Go to Add New Category!");
        wazac.viewpane(wazac, "view/pages/category-create.html");
    }

    @Override
    public void create(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelCreate(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDialog(Dialog dialog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
