package androidapp.com.stalwartsecurity.Pojo;

/**
 * Created by User on 20-07-2017.
 */

public class PrimaryNotice {
    String id,message,date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PrimaryNotice(String id, String message, String date) {
        this.id=id;
        this.message=message;
        this.date=date;

    }
}
