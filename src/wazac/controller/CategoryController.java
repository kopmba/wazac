package wazac.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import wazac.Config;
import wazac.Wazac;
import wazac.WazacController;
import wazac.entity.Article;
import wazac.entity.Category;
import wazac.entity.SubCategory;
import wazac.service.CategoryService;
import wazac.service.SearchService;
import wazac.service.SubCategoryService;
import wazac.util._FileRW;

public class CategoryController extends BorderPane implements WazacController, Initializable {

    private static final Logger LOG = Logger.getLogger(CategoryController.class.getName());
    
    public ObservableList<Category> cList = FXCollections.observableArrayList();
    
    private Wazac wazac;
    private Dialog dialog;
    
    @FXML
    Label labelContext;
    
    @FXML
    TextField searchForm;
    
    @FXML
    Button searchButton;
    
    @FXML
    TableView<Category> tableview;
    
    @FXML
    TableView<SubCategory> tableviewSubCat;
    
    @FXML
    TableColumn<Category, SimpleStringProperty> columnId;
    @FXML
    TableColumn<SubCategory, SimpleStringProperty> columnSubId;
    @FXML
    TableColumn<Category, SimpleStringProperty> columnName;
    @FXML
    TableColumn<SubCategory, SimpleStringProperty> columnSubName;
    @FXML
    TableColumn<Category, SimpleStringProperty> columnDescription;
    @FXML
    TableColumn<SubCategory, SimpleStringProperty> columnSubDescription;
    @FXML
    TableColumn<SubCategory, SimpleStringProperty> columnCategory;
    
    @FXML
    Button cCancel;
    @FXML
    Button cSave;
    
    @FXML
    TextField title;
    
    @FXML
    TextArea description;
    
    @FXML
    ComboBox subcategory;
    @FXML
    ComboBox category;
    
    public static CategoryController getInstance() {
        return new CategoryController();
    }
    
    @Override
    public void setWazacInstance(Wazac wazac) {
        this.wazac = wazac;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
        try {
            String searchStr = null;
            if(SearchService.getFilterParameter() != null) {
                searchStr = SearchService.getFilterParameter().get("search").toString();
            }
            if(comboListCategory() != null && category != null) {
                 category.setItems(FXCollections.observableArrayList(comboListCategory()));
            }
            if(tableview != null) {
                columnId.setCellValueFactory(new PropertyValueFactory<Category, SimpleStringProperty>("id"));
                columnName.setCellValueFactory(new PropertyValueFactory<Category, SimpleStringProperty>("name"));
                columnDescription.setCellValueFactory(new PropertyValueFactory<Category, SimpleStringProperty>("description"));
                
                if(CategoryService.find() != null) {
                    for(Category c : CategoryService.find()) {
                        cList.add(new Category(c.getId(), c.getName(), c.getDescription()));
                    }
                }
                if(searchStr == null) {
                    //insertCategory(tableview, CategoryService.find());
                    tableview.setItems(cList);
                } else {
                    insertCategory(tableview, searchCategory(searchStr));
                }
                
                tableview.setRowFactory(tv -> {
                    TableRow<Category> row = new TableRow<Category>();
                    row.setOnMouseClicked(event -> {
                        if(!row.isEmpty() && event.getClickCount() == 2) {
                            Category item = row.getItem();

                            try {
                                //this.wazac.setContentDialog("view/category/create.fxml", "Modifier la categorie!", this);
                                wazac.setPane(wazac.getStage(), CategoryController.getInstance(), "view/category/create.fxml", "Update category");
                                labelContext.setText("Modifier la categorie : " + item.getName());
                                title.setText(item.getName());
                                description.setText(item.getDescription());
                                category.setItems((ObservableList) comboListCategory());

                            } catch (IOException ex) {
                                Logger.getLogger(ArticleController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    return row;
                });
            }
            
            if(tableviewSubCat != null) {
                columnSubId.setCellValueFactory(new PropertyValueFactory<SubCategory, SimpleStringProperty>("id"));
                columnSubName.setCellValueFactory(new PropertyValueFactory<SubCategory, SimpleStringProperty>("name"));
                columnSubDescription.setCellValueFactory(new PropertyValueFactory<SubCategory, SimpleStringProperty>("description"));
                columnCategory.setCellValueFactory(new PropertyValueFactory<SubCategory, SimpleStringProperty>("categoryId"));
                if(searchStr == null) {
                    
                    insertSubCategory(tableviewSubCat, SubCategoryService.find());
                } else {
                    insertSubCategory(tableviewSubCat, searchSubCategory(searchStr));
                }
                
                tableviewSubCat.setRowFactory(tv -> {
                TableRow<SubCategory> row = new TableRow<SubCategory>();
                row.setOnMouseClicked(event -> {
                    if(!row.isEmpty() && event.getClickCount() == 2) {
                        SubCategory item = row.getItem();
                        
                        try {
                            //this.wazac.setContentDialog("view/subcategory/create.fxml", "Nouvelle categorie!", this);
                            wazac.setPane(wazac.getStage(), CategoryController.getInstance(), "view/subcategory/create.fxml", "Update subcategory");
                            labelContext.setText("Modifier la souscategorie : " + item.getName());
                            title.setText(item.getName());
                            description.setText(item.getDescription());
                            category.setItems((ObservableList) comboListCategory());
                            
                        } catch (IOException ex) {
                            Logger.getLogger(ArticleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                return row;
            });
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> comboListCategory() throws IOException {
        List<String> result = new ArrayList<>();
        if(CategoryService.find() != null) {
            for (Category c : CategoryService.find()) {
                result.add(c.getName());
            }
        }
        return result;
    }
    
    public void goToDashboard() {
        LOG.log(Level.INFO, "Go to Dashboard!");
        wazac.setPane(wazac.getStage(), DashboardController.getInstance(), "view/dashboard.fxml", "Contrôlez vos ressources via ce dasboard!");
    }
    
    public void goToArticles() {
        LOG.log(Level.INFO, "Go to Articles list!");
        wazac.setPane(wazac.getStage(), ArticleController.getInstance(), "view/article/list.fxml", "List of articles");
    }
    
    public void goToCategories() throws URISyntaxException, IOException {
        LOG.log(Level.INFO, "Go to Categories list!");
        _FileRW.remove("collections/search.txt");
        wazac.setPane(wazac.getStage(), CategoryController.getInstance(), "view/category/list.fxml", "List of categories");
    }
    
    public void goToSettings() {
        LOG.log(Level.INFO, "Go to Settings!");
        wazac.setPane(wazac.getStage(), SettingsController.getInstance(), "view/settings.fxml", "Settings");
    }
    
    public void goToCreateCategory() throws IOException {
        LOG.log(Level.INFO, "Go to Create Catgory!");
        wazac.setPane(wazac.getStage(), CategoryController.getInstance(), "view/category/create.fxml", "Ajouter une catgorie");
    }
    
    public void goToCreateSubCategory() throws IOException {
        LOG.log(Level.INFO, "Go to Create SubCatgory!");
        wazac.setPane(wazac.getStage(), CategoryController.getInstance(), "view/subcategory/create.fxml", "Ajouter une sous catgorie");
    }
    
    public void cancel(ActionEvent event) throws URISyntaxException, IOException {
      if(category != null) {
        title.setText("");
        description.setText(""); 
        goToSubCategories();
      } else {
        title.setText("");
        description.setText("");
        goToCategories();
      }
      
    }
    
    public void createCategory(ActionEvent event) throws IOException, FileNotFoundException, URISyntaxException {
        //labelContext.setText("Creation d'une categorie");
        Category category = new Category();
        category.setId(String.valueOf(CategoryService.find().size()+1));
        category.setName(title.getText());
        category.setDescription(description.getText());
        
        CategoryService.save(category);
        //list(category);
        goToCategories();
    }
    
    public void createSubCategory(ActionEvent event) throws IOException, FileNotFoundException, URISyntaxException {
        labelContext.setText("Creation d'une souscategorie");
        SubCategory subcategory = new SubCategory();
        subcategory.setId(String.valueOf(SubCategoryService.find().size()+1));
        subcategory.setName(title.getText());
        subcategory.setDescription(description.getText());
        subcategory.setCategoryId(category.getSelectionModel().getSelectedItem().toString());
        SubCategoryService.save(subcategory);
        //list(subcategory);
        goToSubCategories();
    }
    
    public void updateCategory(ActionEvent event) throws IOException, FileNotFoundException, URISyntaxException {
        createCategory(event);
    }
    
    public void updateSubCategory(ActionEvent event) throws IOException, FileNotFoundException, URISyntaxException {
        createSubCategory(event);
    }
    
    public void searchSubcategory(ActionEvent event) throws IOException, FileNotFoundException, URISyntaxException {
        SearchService.save(searchForm.getText());
        goToCategories();
    }
    
    public void searchCategory(ActionEvent event) throws IOException, FileNotFoundException, URISyntaxException {
        SearchService.save(searchForm.getText());
        goToCategories();
    }
    
    private List<SubCategory> searchSubCategory(String search) throws IOException {
        Set<SubCategory> dataSet = SubCategoryService.filter(search);
        List<SubCategory> dataList = new ArrayList<>();
        
        for (SubCategory subCategory : dataSet) {
            dataList.add(subCategory);
        }
        return dataList;
    }
    
    private List<Category> searchCategory(String search) throws IOException {
        Set<Category> dataSet = CategoryService.filter(search);
        List<Category> dataList = new ArrayList<>();
        
        for (Category category : dataSet) {
            dataList.add(category);
        }
        return dataList;
    }
    
    public void list(Object category) throws URISyntaxException, IOException {
        if(category != null) {
           if(category instanceof Category) {
               cList.add((Category)category);
               goToCategories();
           }
           else {
               ObservableList<SubCategory> data = tableviewSubCat.getItems();
               data.add((SubCategory)category);
               goToSubCategories();
           }
        } 
    }
    
    public void insertCategory(TableView table, List<Category> list) {
        for (Category c : list) {
           table.getItems().add(new Category(c.getId(), c.getName(), c.getDescription()));
        }
        return;
    }
    
    public void insertSubCategory(TableView table, List<SubCategory> list) {
        for (SubCategory c : list) {
           table.getItems().add(new SubCategory(c.getId(), c.getName(), c.getDescription(), c.getCategoryId()));
        }
        return;
    }

    private void goToSubCategories() throws URISyntaxException, IOException {
        _FileRW.remove("collections/search.txt");
        wazac.setPane(wazac.getStage(), CategoryController.getInstance(), "view/subcategory/list.fxml", "List of subcategories");
    }

    public void create(ActionEvent event, Object clazz) {
        try {
            if(category == null) {
                if(labelContext != null) {
                    System.out.println(clazz);
                    updateCategory(event);
                } else {
                    createCategory(event);
                }
            } else {
                if(labelContext != null) {
                    updateSubCategory(event);
                } else {
                    createSubCategory(event);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void cancelCreate(ActionEvent event) {
        cancelCreate(event);
    }
    
    @Override
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void create(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
        
}