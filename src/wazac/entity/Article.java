package wazac.entity;


import java.util.LinkedList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

public class Article {
    
    private SimpleStringProperty id = new SimpleStringProperty("");
    private SimpleStringProperty name = new SimpleStringProperty("") ; 
    private SimpleStringProperty articleType = new SimpleStringProperty("");
    private SimpleStringProperty price = new SimpleStringProperty("");
    private SimpleStringProperty description = new SimpleStringProperty(""); 
    private SimpleStringProperty classification = new SimpleStringProperty(""); 
    private SimpleStringProperty qty = new SimpleStringProperty("") ; 
    private SimpleStringProperty guarantee = new SimpleStringProperty("");
    private SimpleStringProperty year = new SimpleStringProperty("");
    private SimpleStringProperty seller = new SimpleStringProperty("");
    private SimpleStringProperty articleStatus = new SimpleStringProperty("");
    private List<String> images;
    private String created;
    private String edited;

    public Article(String id, String name, String articleType, Double price, String description,
                String classification, Integer qty, String guarantee, Integer year, String seller, String articleStatus
    ) {
        setId(id);
        setName(name);
        setArticleType(articleType);
        setArticleStatus(articleStatus);
        setClassification(classification);
        setDescription(description);
        setGuarantee(guarantee);
        setQty(qty);
        setPrice(price);
        setSeller(seller);
        setYear(year);
    } 
    
    public Article() {
        this("", "", "", 0.00, "", "", 0, "", 2020, "", "");
    }
    
    public Article(Article a) {
        this(a.getId(), a.getName(), a.getArticleType(), Double.valueOf(a.getPrice()), a.getDescription(),
                a.getClassification(), Integer.valueOf(a.getQty()), a.getGuarantee(), Integer.valueOf(a.getYear()),  a.getSeller(), a.getArticleStatus());
    }

    public Article(String id) {
        setId(id);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String value) {
        id.set(value);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public String getArticleType() {
        return articleType.get();
    }

    public void setArticleType(String value) {
        articleType.set(value);
    }

    public String getPrice() {
        return String.valueOf(Double.parseDouble(price.get()));
    }

    public void setPrice(Double value) {
        price.set(String.valueOf(value));
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public String getClassification() {
        return classification.get();
    }

    public void setClassification(String value) {
        classification.set(value);
    }

    public String getQty() {
        return String.valueOf(Integer.valueOf(qty.get()));
    }

    public void setQty(Integer value) {
        qty.set(String.valueOf(value));
    }

    public String getGuarantee() {
        return guarantee.get();
    }

    public void setGuarantee(String value) {
        guarantee.set(value);
    }

    public String getYear() {
        return String.valueOf(Integer.valueOf(year.get()));
    }

    public void setYear(Integer value) {
        year.set(String.valueOf(value));
    }

    public String getSeller() {
        return seller.get();
    }

    public void setSeller(String value) {
        seller.set(value);
    }

    public String getArticleStatus() {
        return articleStatus.get();
    }

    public void setArticleStatus(String value) {
        articleStatus.set(value);
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }
    
    
    
}