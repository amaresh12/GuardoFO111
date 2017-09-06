package androidapp.com.stalwartsecurity.Pojo;

/**
 * Created by User on 17-08-2017.
 */

public class DeliveryBoys {
    String id,title,is_enable;
    public DeliveryBoys(String id, String title, String is_enable) {
        this.id=id;
        this.title=title;
        this.is_enable=is_enable;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }
}
