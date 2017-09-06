package androidapp.com.stalwartsecurity.Pojo;

/**
 * Created by User on 31-07-2017.
 */

public class OfflineVisitors {
    String id, name,phone,coming_from;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComing_from() {
        return coming_from;
    }

    public void setComing_from(String coming_from) {
        this.coming_from = coming_from;
    }

    public OfflineVisitors(String id, String name, String mobile, String coming_from) {
        this.id=id;
        this.name=name;
        this.phone=mobile;
        this.coming_from=coming_from;

    }
}

