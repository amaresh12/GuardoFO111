package androidapp.com.stalwartsecurity.Pojo;

/**
 * Created by mobileapplication on 8/14/17.
 */

public class UnityDetailsLocation {

    private String location_id;
    private String location_name;




    public UnityDetailsLocation(String location_id, String location_name) {
        this.location_id=location_id;
        this.location_name=location_name;

    }

    public String getLocation_id() {
        return location_id;
    }

    public  void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public  void setLocation_name(String location_name) {
       this. location_name = location_name;
    }
}
