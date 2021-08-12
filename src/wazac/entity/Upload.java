/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.entity;

/**
 *
 * @author mardets
 */
public class Upload {
    
    private String articleId;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String img6;

    public Upload() {
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    public String getImg6() {
        return img6;
    }

    public void setImg6(String img6) {
        this.img6 = img6;
    }
    
    public void setImg(int i, String img) {
        if(i == 1) {
            this.img1 = img;
        } else if(i == 2) {
           this.img2 = img; 
        }
        else if(i == 3) {
           this.img3 = img; 
        }
        else if(i == 4) {
           this.img4 = img; 
        }
        else if(i == 5) {
           this.img5 = img; 
        } else {
            this.img6 = img;
        }
    }
    
}
