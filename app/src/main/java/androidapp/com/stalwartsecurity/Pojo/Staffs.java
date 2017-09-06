package androidapp.com.stalwartsecurity.Pojo;

/**
 * Created by User on 12-08-2017.
 */

public class Staffs {
    String id,name,mobile,au_id,entryby,in_time,overstay,appartment,out_time,exit_by,photo;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAu_id() {
        return au_id;
    }

    public void setAu_id(String au_id) {
        this.au_id = au_id;
    }

    public String getEntryby() {
        return entryby;
    }

    public void setEntryby(String entryby) {
        this.entryby = entryby;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOverstay() {
        return overstay;
    }

    public void setOverstay(String overstay) {
        this.overstay = overstay;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getExit_by() {
        return exit_by;
    }

    public void setExit_by(String exit_by) {
        this.exit_by = exit_by;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Staffs(String id, String name, String mobile, String au_id,
                  String entry_by, String in_time, String overstay, String appartment, String outtime, String exitby, String photo) {
        this.id=id;

        this.name=name;
        this.mobile=mobile;
        this.au_id=au_id;
        this.entryby=entry_by;
        this.in_time=in_time;
        this.overstay=overstay;
        this.appartment=appartment;
        this.out_time=outtime;
        this.exit_by=exitby;
        this.photo=photo;




    }
}
