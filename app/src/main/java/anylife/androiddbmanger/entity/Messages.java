package anylife.androiddbmanger.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 往事越千年，都21世纪了，还SQL操作sqlite.ORM DB　吧
 * <p>
 * <p>
 * Created by zenglb on 2017/01/04.
 */
@Entity
public class Messages {
    //============ Transient 字段不会自动生成=====================
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    //============ Transient 字段不会自动生成=====================

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getRead() {
        return this.read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }
    public long getCreated() {
        return this.created;
    }
    public void setCreated(long created) {
        this.created = created;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getClassify() {
        return this.classify;
    }
    public void setClassify(String classify) {
        this.classify = classify;
    }

    @Id(autoincrement = true)
    private Long id;
    private boolean read;
    private long created;
    private String title;
    @Index
    private String message;
    @Index
    private String classify;
    @Transient
    private boolean isChecked = false;
    @Generated(hash = 474992805)
    public Messages(Long id, boolean read, long created, String title,
            String message, String classify) {
        this.id = id;
        this.read = read;
        this.created = created;
        this.title = title;
        this.message = message;
        this.classify = classify;
    }
    @Generated(hash = 826815580)
    public Messages() {
    }


    @Override
    public String toString() {
        return "Messages{" +
                "id=" + id +
                ", read=" + read +
                ", created=" + created +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", classify='" + classify + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}