package androidapp.com.stalwartsecurity.Pojo;


public class ImageModel {


    private String image_drawable;
    private String title;

    public ImageModel(String title, String banner) {
        this.image_drawable=banner;
        this.title=title;
    }

    public String getImage_drawable() {
        return image_drawable;
    }
    public String getTitle() {
        return title;
    }

    public void setImage_drawable(String image_drawable) {
        this.image_drawable = image_drawable;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
