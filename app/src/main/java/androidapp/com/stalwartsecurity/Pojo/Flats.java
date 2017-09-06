package androidapp.com.stalwartsecurity.Pojo;

/**
 * Created by User on 02-08-2017.
 */

public class Flats {
    String flat,name,mobile,loc;
    private boolean selected;
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Flats(String flat, String mobile, String person_name, String loc_name) {
        this.flat=flat;
        this.name=person_name;
        this.mobile=mobile;
        this.loc=loc_name;

    }
}
