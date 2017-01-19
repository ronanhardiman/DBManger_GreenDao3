package anylife.androiddbmanger.activity.messageCenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import anylife.androiddbmanger.MyApplication;
import anylife.androiddbmanger.R;
import anylife.androiddbmanger.entity.Classify;
import anylife.androiddbmanger.entity.Messages;
import anylife.androiddbmanger.entity.daoManger.ClassifyDao;
import anylife.androiddbmanger.entity.daoManger.DaoSession;
import anylife.androiddbmanger.entity.daoManger.MessagesDao;

/**
 * 某一种类型分类的列表，比如任务的分类
 * 这里需要前面的页面获取到所有的数据，API 不提供分类信息查询，服务器压力大！！！Cry
 *
 * 134 1736 4587    j123456
 * 137 9820 5229    zxcv1234
 *
 * @author zenglb 2017.1.6
 */
public class SomeTypeMsgListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView = null;
    private SomeTypeMsgListAdapter someTypeMsgListAdapter;
    private LinearLayout editLineLayout, searchLayout;
    private MaterialSearchView searchView;

    private Button btn_delete, btn_read;
    private SpringView springView;
    private List<Messages> data = new ArrayList<>();  //没有必要把很久以前的消息加载出来吧，后期优化
    private List<Long> checkedIdList = new ArrayList<>();
    private ClassifyDao classifyDao;
    private MessagesDao messagesDao;
    private String classifyType;    //哪个类别的消息
    private boolean isSearchMode = false;
    private boolean isEditMode = false;
    private boolean isAllChecked = false;     //全部选择，有百万级别的数据需要优化
    private static final int limit = 10000;   //限制加载2W 条数据 ！

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_msg_sometype);
        classifyType = getIntent().getStringExtra("CLASSIFY_NAME");
        setTitle(classifyType);
//        setRightBtnText("编辑");
        viewsInit();
    }

//    @Override
//    public void onRightBtnClick(View view) {
//        super.onRightBtnClick(view);
//        if (!isEditMode) {
//            isEditMode = true;
//            someTypeMsgListAdapter.setEdidMode(isEditMode);
//            someTypeMsgListAdapter.notifyDataSetChanged();
//            setRightBtnText("全选");
//            editLineLayout.setVisibility(View.VISIBLE);
//        } else {
//            isAllChecked = !isAllChecked;
//            for (int i = 0; i < data.size(); i++) {
//                data.get(i).setChecked(isAllChecked);
//            }
//            someTypeMsgListAdapter.notifyDataSetChanged();
//        }
//    }

    /**
     * 退回编辑状态
     */
    private void backToEdit() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setChecked(false);
        }
        isEditMode = false;
        someTypeMsgListAdapter.setEdidMode(isEditMode);
//        setRightBtnText("编辑");
        editLineLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * 获取选中的ID
     */
    private void getCheckedList() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isChecked()) {
                checkedIdList.add(data.get(i).getId());
            }
        }
        if (isAllChecked) {
            checkedIdList.add(0, -1l);
        }
    }

    /**
     * 把选中的标记为已读了
     */
    private void setAsReaded() {
        if (isAllChecked) {
            //测试同时操作标记 10W 条数据的情况 ！
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setRead(true);
            }
            messagesDao.updateInTx(data);
        } else {
            List<Messages> needToFlagReaded = new ArrayList<>();

            for (int i = 0; i < checkedIdList.size(); i++) {
                Messages messages = messagesDao.load(checkedIdList.get(i));
                messages.setRead(true);
                needToFlagReaded.add(messages);
            }
            messagesDao.updateInTx(needToFlagReaded);
        }
        checkedIdList.clear();
        backToEdit();
        someTypeMsgListAdapter.notifyDataSetChanged();
    }

    /**
     * 把选中的删除,这样做有点粗糙啊，后期再优化
     */
    private void deleteCheckedAndFresh() {
        if (isAllChecked) {  //所有的删除了，那么
            Classify classify = classifyDao.queryBuilder().where(ClassifyDao.Properties.Classify.eq(classifyType)).unique();
            if(null!=classify){
                classifyDao.delete(classify);
            }
        }
        messagesDao.deleteByKeyInTx(checkedIdList);
        backToEdit();
        checkedIdList.clear();
        springView.callFresh();
    }

    /**
     * 请求服务器标记已读
     */
    protected void requestMarkMsgRead() {
        getCheckedList();
        if (checkedIdList == null || checkedIdList.size() == 0) {
            Toast.makeText(SomeTypeMsgListActivity.this, "请选择你有操作的条目", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (checkedIdList.get(0) == -1) {
//            long[] ids = new long[2];
//            ids[0] = -1;
//            Intent intent = new Intent(this, MarkMsgReadIntentService.class);
//            intent.putExtra("msgId", ids);
//            startService(intent);
//        } else {
//            long[] ids = new long[checkedIdList.size()];
//            for (int i = 0; i < checkedIdList.size(); i++) {
//                ids[i] = checkedIdList.get(i);
//            }
//            Intent intent = new Intent(this, MarkMsgReadIntentService.class);
//            intent.putExtra("msgId", ids);
//            startService(intent);
//        }
        setAsReaded();
    }

    /*
    * 请求服务器删除某些消息
    * @param pos
    */
    private void deleteMessage() {
        getCheckedList();
        if (checkedIdList == null || checkedIdList.size() == 0) {
            Toast.makeText(SomeTypeMsgListActivity.this, "请选择你有操作的条目", Toast.LENGTH_SHORT).show();
            return;
        }

        long[] ids = new long[checkedIdList.size()];
        if (checkedIdList.get(0) == -1) {//删除全部
            ids[0] = -1;
        } else {
            for (int i = 0; i < checkedIdList.size(); i++) {
                ids[i] = checkedIdList.get(i);
            }
        }

//        dialog.show();
//        MarkMessageParam param = new MarkMessageParam();
//        param.setRequestId(HttpApiConfig.POST_MESSAGES_DELETE_ID);
//        param.addHeader(HEADER_TOKEN_KEY, getHeaderToken());
//        param.setId(ids);
//        ActResponseHandler<Response> handler = new ActResponseHandler<>(this, Response.class);
//        HttpExcutor.getInstance().post(this, HttpApiConfig.POST_MESSAGES_DELETE, param, handler);
    }

    /**
     *  怎样取消这个过程，有可能这个方法还没有执行完，又来执行了！
     *
     * @param keyStr
     */
    private void searchMessage(String keyStr) {
        data.clear();

        data.addAll(messagesDao.queryBuilder()
                .where(MessagesDao.Properties.Message.like("%"+keyStr+"%"))
                .where(MessagesDao.Properties.Classify.eq(classifyType))
                .limit(200)
                .list());
        someTypeMsgListAdapter.notifyDataSetChanged();
    }


    /**
     * init views
     *
     * @param
     */
    private void viewsInit() {
        searchLayout = (LinearLayout) findViewById(R.id.search_ll);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //search Layout
                searchView.showSearch(true);
            }
        });

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setEllipsize(true);
        searchView.setHint("请输入你要搜索的内容");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                closeInputMethod();

                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(SomeTypeMsgListActivity.this, "无效的输入", Toast.LENGTH_SHORT).show();
                }

                if (data != null && data.size() == 0) {
                    Toast.makeText(SomeTypeMsgListActivity.this, "无结果", Toast.LENGTH_SHORT).show();
                }
                searchMessage(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    return true;
                }
                searchMessage(newText);
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //显示出了搜索框
                isSearchMode=true;
                searchLayout.setVisibility(View.GONE);
                data.clear();
                someTypeMsgListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSearchViewClosed() {
                isSearchMode=false;
                searchLayout.setVisibility(View.VISIBLE);
                springView.callFresh();
            }
        });


        editLineLayout = (LinearLayout) findViewById(R.id.ll_edit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_read = (Button) findViewById(R.id.btn_read);
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMarkMsgRead();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMessage();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(this)
                .colorResId(R.color.bg_common)
                .size(5)
                .build());

        someTypeMsgListAdapter = new SomeTypeMsgListAdapter(this, data);
        mRecyclerView.setAdapter(someTypeMsgListAdapter);
        someTypeMsgListAdapter.setOnItemClickListener(new SomeTypeMsgListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isEditMode) {
                    isAllChecked = false;//需要处理点击了全选后单独的取消选择了某一个Item 的情况！
                    data.get(position).setChecked(!data.get(position).isChecked());
                    someTypeMsgListAdapter.notifyItemChanged(position); //flush ui
                } else {
                    Messages messagesClick = data.get(position);
                    data.get(position).setRead(true);
                    messagesDao.save(messagesClick);  //把数据库中的数据标为已读
                    someTypeMsgListAdapter.notifyItemChanged(position); //flush ui

//                    gotoMsgDetail(messagesClick);
                }
            }
        });

        if (messagesDao == null) {
            DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
            messagesDao = daoSession.getMessagesDao();
        }
        if (classifyDao == null) {
            DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
            classifyDao = daoSession.getClassifyDao();
        }
        springView = (SpringView) findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (isSearchMode){
                    springView.onFinishFreshAndLoad();
                    return;
                }

                data.clear();
                data.addAll(messagesDao.queryBuilder()
                        .where(MessagesDao.Properties.Classify.eq(classifyType))
                        .limit(2*limit)
                        .orderDesc(MessagesDao.Properties.Id) //根据ID 降序排列
                        .list());

                if (data == null || data.size() == 0) {
                    SomeTypeMsgListActivity.this.finish();
                }

                // TODO: 2017/1/11 不明白为什么会记住以前的check 状态，先实现功能再优化吧 ！！ maybe db bug
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setChecked(false);
                }
                someTypeMsgListAdapter.notifyDataSetChanged();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {

            }
        });

        springView.setHeader(new DefaultHeader(this));
//        springView.setFooter(new DefaultFooter(this));   //暂时还没有处理要加载4W 加的情况
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                springView.callFresh();
            }
        }, 500);
    }


//    @Override
//    public void onHttpSuc(int status, int what, Object obj) {
//        dialog.dismiss();
//        super.onHttpSuc(status, what, obj);
//        switch (what) {
//            case HttpApiConfig.GET_PAYMENT_DETAIL_ID:
//                messagesClassifyResponse = (MessagesClassifyResponse) obj;
//                break;
//            case HttpApiConfig.POST_MESSAGES_DELETE_ID:
//                deleteCheckedAndFresh();
//                ToastUtil.toastSuccess(this, "删除成功");
//                break;
//        }
//    }
//
//    @Override
//    public void onHttpFail(int status, int what, String jstr) {
//        dialog.dismiss();
//        ErrorResponse response = parsErrorResponse(jstr);
//        if (response == null) return;
//        if (response.getCode() == ConstantErrors.CODE_NOT_FOUND) {
//            Toast.makeText(SomeTypeMsgListActivity.this, "没有更多的数据", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        super.onHttpFail(status, what, jstr);
//
//        switch (what) {
//            case HttpApiConfig.GET_PAYMENT_DETAIL_ID:  //让重试OK
//
//                break;
//        }
//    }


    @Override
    public void onStop() {
        super.onStop();
        Classify classify = classifyDao.queryBuilder().where(ClassifyDao.Properties.Classify.eq(classifyType)).unique();
        if (messagesDao.queryBuilder().where(MessagesDao.Properties.Classify.eq(classifyType)).count() == 0) {
            if (classify != null) {
                classifyDao.delete(classify);
            }
        } else {
            long num = messagesDao.queryBuilder().where(MessagesDao.Properties.Read.eq("false")).where(MessagesDao.Properties.Classify.eq(classifyType)).count();
            if (classify != null) {
                classify.setMessage(data.get(0).getMessage());
                classify.setCreated(data.get(0).getCreated());  //需要更改吗？
                classify.setUnreadNum((int) num);
                classifyDao.update(classify);
            }
        }
    }//onStop  is over


}