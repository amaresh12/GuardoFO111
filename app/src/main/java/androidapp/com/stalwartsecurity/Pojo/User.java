package androidapp.com.stalwartsecurity.Pojo;

/**
 * Created by User on 15-07-2017.
 */

public class User {
    String id,name,email_id,mobile,photo,address,user_type;
    public User(String id, String name, String email_id, String mobile,
                String photo, String address, String user_type) {
        this.id=id;
        this.name=name;
        this.email_id=email_id;
        this.mobile=mobile;
        this.photo=photo;
        this.address=address;
        this.user_type=user_type;

    }

    public User() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail_id() {

        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
