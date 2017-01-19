package anylife.androiddbmanger.activity.messageCenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import java.util.List;

import anylife.androiddbmanger.MyApplication;
import anylife.androiddbmanger.R;
import anylife.androiddbmanger.entity.Classify;
import anylife.androiddbmanger.entity.Messages;
import anylife.androiddbmanger.entity.daoManger.ClassifyDao;
import anylife.androiddbmanger.entity.daoManger.DaoSession;
import anylife.androiddbmanger.entity.daoManger.MessagesDao;
import anylife.androiddbmanger.utilss.GetDatas;

/**
 * 消息分类列表,所有的分类  !
 *
 * @author zenglb 2017.1.6
 */
public class MessageClassifyListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView = null;
    private MessageClassifyListAdapter messageClassifyListAdapter;
    private TextView mTipsTxt;
    private SpringView springView;

//    private List<Messages> data = new ArrayList<>();
    private List<Classify> classifyList = new ArrayList<>();

    private DaoSession daoSession;
    private MessagesDao messagesDao;
    private ClassifyDao classifyDao;

    private String account; //当前登录的账号

    private boolean isMsgUpdateOver = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_msg_classifylist);
        setTitle("大数据制造");

        viewsInit();
        isMsgUpdateOver = false;
//        account = dao.getSafe(SharedPreferenceDao.KEY_USERNAME);
//        ExportDBUtils.exportDatabse(dao.getSafe(SharedPreferenceDao.KEY_USERNAME), MessageClassifyListActivity.this);

    }


    /**
     * init views !
     *
     * @param
     */
    private void viewsInit() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(this)
                .colorResId(R.color.bg_common)
                .size(4)
                .build());

        messageClassifyListAdapter = new MessageClassifyListAdapter(this, classifyList);
        mRecyclerView.setAdapter(messageClassifyListAdapter);
        messageClassifyListAdapter.setOnItemClickListener(new MessageClassifyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!isMsgUpdateOver) {
                    Intent intent = new Intent(MessageClassifyListActivity.this, SomeTypeMsgListActivity.class);
                    intent.putExtra("CLASSIFY_NAME", classifyList.get(position).getClassify());
                    startActivity(intent);
                } else {
                    Toast.makeText(MessageClassifyListActivity.this, "数据加载中，请稍候", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                final String classify = classifyList.get(position).getClassify();
                //弹框提示用户操作信息
                new AlertDialog.Builder(MessageClassifyListActivity.this)
                        .setTitle("温馨提示")
                        .setMessage("是否要删除 [" + classify + "]分类下的所有消息? 删除后将无法恢复！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //1.删除分类表 和 所有的class内容！
                                Classify classifyTemp = classifyDao.queryBuilder().where(ClassifyDao.Properties.Classify.eq(classify)).unique();
                                if(classifyTemp!=null){
                                    classifyDao.delete(classifyTemp);
                                    classifyList.remove(position);
                                    messageClassifyListAdapter.notifyDataSetChanged();

                                    //查询删除！
                                    messagesDao.deleteInTx(messagesDao.queryBuilder().where(MessagesDao.Properties.Classify.eq(classify)).list());
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        springView = (SpringView) findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                getMessageClassifyList();
            }

            @Override
            public void onLoadmore() {
                //there is no need here, any more!
            }
        });

        if (messagesDao == null) {
            DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
            messagesDao = daoSession.getMessagesDao();
        }
        if (classifyDao == null) {
            daoSession = ((MyApplication) getApplication()).getDaoSession();
            classifyDao = daoSession.getClassifyDao();
        }

        mTipsTxt = (TextView) findViewById(R.id.tips_txt);
        mTipsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                springView.callFresh();
            }
        });
        springView.setHeader(new DefaultHeader(this));


        classifyList.clear();
        classifyList.addAll(classifyDao.loadAll());
        messageClassifyListAdapter.notifyDataSetChanged();

    }


    /**
     * 请求所有的消息分类信息（all）
     *
     * @param
     */
    private void getMessageClassifyList() {
        int time=5;
        for(int i=0;i<time;i++){
            disposeHttpResult(GetDatas.getSomeDatas(1000));
        }

        isMsgUpdateOver = true;//加载完毕了，可以点击进入
        springView.onFinishFreshAndLoad();

    }



    /**
     * 更新分类信息
     */
    private void updateClassify(List<Messages> messagesList) {
        classifyDao.deleteInTx(classifyDao.loadAll());
        for (int i = 0; i < messagesList.size(); i++) {
            Messages messagesTemp = messagesList.get(i);

            if (classifyList.size() == 0) {
                classifyList.add(0, new Classify(messagesTemp));
                if (!messagesTemp.getRead()) {
                    classifyList.get(0).addUnreadNum();
                }
            } else {
                boolean isLive = false;
                int index = 0;
                for (int j = 0; j < classifyList.size(); j++) {
                    if (classifyList.get(j).getClassify().equals(messagesTemp.getClassify())) {
                        isLive = true;
                        index = j;
                    }
                }
                if (isLive) {
                    if (!messagesTemp.getRead()) {
                        classifyList.get(index).addUnreadNum();
                    }
                    classifyList.get(index).setCreated(messagesTemp.getCreated());
                    classifyList.get(index).setMessage(messagesTemp.getMessage());
                    classifyList.get(index).setTitle(messagesTemp.getTitle());

                } else {
                    classifyList.add(0, new Classify(messagesTemp));
                    if (!messagesTemp.getRead()) {
                        classifyList.get(0).addUnreadNum();  //未读个数加一个
                    }
                }
            }
        }
        classifyDao.insertInTx(classifyList);
        messageClassifyListAdapter.notifyDataSetChanged();
    }

    /**
     * 处理http返回来的结果
     *
     * @return
     */
    private void disposeHttpResult(List<Messages> messagesList) {
        if (messagesList != null && messagesList.size() != 0) {
            mTipsTxt.setVisibility(View.GONE);
            updateClassify(messagesList);
            messagesDao.insertOrReplaceInTx(messagesList);  //还有重复的ID ！
            springView.callFresh();
        }
        if (classifyList == null || classifyList.size() == 0) {
            mTipsTxt.setVisibility(View.VISIBLE);
        }
        messageClassifyListAdapter.notifyDataSetChanged();
    }

    protected void onResume() {
        super.onResume();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                springView.callFresh();
//            }
//        }, 500);

    }

}