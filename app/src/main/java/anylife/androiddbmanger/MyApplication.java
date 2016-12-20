package anylife.androiddbmanger;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import org.greenrobot.greendao.database.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import anylife.androiddbmanger.entity.DaoMaster;
import anylife.androiddbmanger.entity.DaoSession;
import anylife.androiddbmanger.sharedprefence.SharedPreferencesDao;

/**
 *
 */
public class MyApplication extends Application {
	public static final String TAG = MyApplication.class.getSimpleName();
	/**
	 * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
	 */
	public static final boolean ENCRYPTED = false;
	private DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();
		String processName = getProcessName();
		Log.d(TAG, processName + "Application onCreate");
		if (!TextUtils.isEmpty(processName) && processName.equals(this.getPackageName())) { //main Process
			SharedPreferencesDao.initSharePrefenceDao(this);
			setDaoSession(SharedPreferencesDao.getInstance().getData("Account","",String.class));
		} else {

		}
	}

	/**
	 * 设置数据库操作对象
	 * 1.在Application 中设置一个默认的上传登陆的Session,在登录成功后创建一个新的Session
	 *
	 */
	public void setDaoSession(String account){
		if(!TextUtils.isEmpty(account)){
			String DBName=ENCRYPTED ? account+"encrypted" : account;
			DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,DBName );
			Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
			daoSession = new DaoMaster(db).newSession();
		}else{
			Log.e(TAG,"Account is empty,init db failed");
		}

	}

	/**
	 *
	 * @return
	 */
	public DaoSession getDaoSession() {
		return daoSession;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	/**
	 * 获取进程名字
	 *
	 * @return
	 */
	public String getProcessName() {
		try {
			File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
			BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
			String processName = mBufferedReader.readLine().trim();
			mBufferedReader.close();
			return processName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
