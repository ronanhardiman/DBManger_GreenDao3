package anylife.androiddbmanger.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zenglb on 2016/12/20.
 */

@Entity   //表示这个实体类一会会在数据库中生成对应的表
public class User {
	@Id
	private Long id;
	@Property(nameInDb = "USERNAME")
	private String username;
	@Property(nameInDb = "NICKNAME")
	private String nickname;
	@Generated(hash = 523935516)
	public User(Long id, String username, String nickname) {
					this.id = id;
					this.username = username;
					this.nickname = nickname;
	}
	@Generated(hash = 586692638)
	public User() {
	}
	public Long getId() {
					return this.id;
	}
	public void setId(Long id) {
					this.id = id;
	}
	public String getUsername() {
					return this.username;
	}
	public void setUsername(String username) {
					this.username = username;
	}
	public String getNickname() {
					return this.nickname;
	}
	public void setNickname(String nickname) {
					this.nickname = nickname;
	}
}