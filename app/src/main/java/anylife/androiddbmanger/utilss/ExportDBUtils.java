package anylife.androiddbmanger.utilss;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 中国建设银行股份有限公司南昌高新开发区支行
 *
 * 你能否安静一点啊！
 * Created by zenglb on 2016/12/27.
 */
public class ExportDBUtils {

	/**
	 * 导出DB 文件到getExternalStorageDirectory 目录去！
	 *
	 * @param databaseName
	 * @param mContext
	 */
	public static  void exportDatabse(String databaseName,Context mContext) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite()) {
				String currentDBPath = "//data//"+mContext.getPackageName()+"//databases//"+databaseName+"";
				String backupDBPath = databaseName+".db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);
				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
			Log.e("导出DB 文件异常 ！",e.toString());
		}
	}
}
