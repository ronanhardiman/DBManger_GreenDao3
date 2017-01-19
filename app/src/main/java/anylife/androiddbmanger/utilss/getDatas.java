package anylife.androiddbmanger.utilss;

import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;
import java.util.List;

import anylife.androiddbmanger.entity.Messages;

/**
 * 模拟获取数据
 * Created by zenglb on 2017/1/18.
 */

public class GetDatas {
    private static String [] classifys={"扣款提示","信用提升","转账成功","密码重置","信用卡还款","取款通知"};
    private static String [] titles={"扣款成功提示","信用额度提升","异地转账成功","密码重置","信用卡还款","取款通知"};

    public static List<Messages> getSomeDatas(int size){
        List<Messages> messagesList=new ArrayList<>();
        for(int i=0;i<size;i++){
            Messages messagesTemp=new Messages(null,false,System.currentTimeMillis(),titles[i%5],
                    "你你你"+classifys[i%5]+System.currentTimeMillis(),classifys[i%5]);
            messagesList.add(0,messagesTemp);
        }

        return messagesList;
    }
}
