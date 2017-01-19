package anylife.androiddbmanger.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


/**
 * 再单独的维护一张消息分类类别的表
 * <p>
 * Created by zenglb on 2017/1/9.
 */
@Entity
public class Classify {
    @Id
    private Long id;
    private long created;
    private String message;
    private String title;
    private String classify;
    private long unreadNum;


    /**
     * 需要author 维护
     * @param messages
     */
    public Classify(Messages messages){
        this.id=messages.getId();
        this.created=messages.getCreated();
        this.message=messages.getMessage();
        this.title=messages.getTitle();
        this.classify=messages.getClassify();
    }

    public void addUnreadNum(){
        this.unreadNum++;
    }
    //===================================================================

    @Generated(hash = 900685489)
    public Classify(Long id, long created, String message, String title,
            String classify, long unreadNum) {
        this.id = id;
        this.created = created;
        this.message = message;
        this.title = title;
        this.classify = classify;
        this.unreadNum = unreadNum;
    }

    @Generated(hash = 767880343)
    public Classify() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreated() {
        return this.created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassify() {
        return this.classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public long getUnreadNum() {
        return this.unreadNum;
    }

    public void setUnreadNum(long unreadNum) {
        this.unreadNum = unreadNum;
    }

}
