package anylife.androiddbmanger.activity.messageCenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import anylife.androiddbmanger.R;
import anylife.androiddbmanger.entity.Classify;

/**
 * Created by zenglb on 2016/9/17.
 */
public class MessageClassifyListAdapter extends RecyclerView.Adapter<MessageClassifyListAdapter.ViewHolder> {
    private int checkedIndex;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Classify> data = new ArrayList<>();

    DisplayImageOptions viewImageOption =new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
            .cacheOnDisk(true)
            .showImageForEmptyUri(R.drawable.def_msg)
            .showImageOnFail(R.drawable.def_msg)
            .cacheInMemory(true)
            .displayer(new FadeInBitmapDisplayer(200, false, false, false))
            .build();

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * @param mContext
     * @param data
     */
    public MessageClassifyListAdapter(Context mContext,List<Classify> data) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.data = data;
    }

    //====================================RecyclerView 更新封装 开始=================================================

    /**
     * 删除一条数据
     *
     * @param position
     */
    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position); //一定要是这样的，不然不会有动画的
    }

    /**
     * 删除所有数据
     */
    public void removeAllItems() {
        data.clear();
        notifyItemMoved(0, data.size() - 1);
    }


    //==================================== RecyclerView 更新封装完毕 =================================================

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.adapter_item_message_classify, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.itemView.setClickable(true);
        Classify classify=data.get(position);
        viewHolder.tv_1.setText(classify.getClassify());
        viewHolder.tv_2.setText(classify.getMessage());
        viewHolder.time.setText(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date(classify.getCreated())));
        if(classify.getUnreadNum()==0){
            viewHolder.tv_unread.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.tv_unread.setVisibility(View.VISIBLE);
            long unReadNum=classify.getUnreadNum();
            if(unReadNum>99){
                viewHolder.tv_unread.setText("99+");
            }else{
                viewHolder.tv_unread.setText(""+unReadNum);
            }
        }

//        ImageLoader.getInstance().displayImage(classify.getIcon(), viewHolder.msgIcon, viewImageOption);

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedIndex = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, checkedIndex);
                    notifyDataSetChanged();
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(viewHolder.itemView, viewHolder.getLayoutPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
		return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tv_1;
        private TextView tv_2;
        private TextView time;
        private TextView tv_unread;
        private ImageView msgIcon;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_1 = (TextView) itemView.findViewById(R.id.title1);
            tv_2 = (TextView) itemView.findViewById(R.id.title2);
            tv_unread = (TextView) itemView.findViewById(R.id.tv_unread);
            time = (TextView) itemView.findViewById(R.id.time);
            msgIcon=(ImageView)itemView.findViewById(R.id.msg_icon);
        }
    }
}