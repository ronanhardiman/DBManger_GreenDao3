package anylife.androiddbmanger.activity.messageCenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import anylife.androiddbmanger.R;
import anylife.androiddbmanger.entity.Messages;

/**
 *
 *
 * Created by zenglb on 2017/01/7.
 */
public class SomeTypeMsgListAdapter extends RecyclerView.Adapter<SomeTypeMsgListAdapter.ViewHolder> {

    private boolean isEdidMode=false;
    private int checkedIndex;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Messages> data=new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public void setEdidMode(boolean isEdidMode){
        this.isEdidMode=isEdidMode;
    }
    /**
     * @param mContext
     * @param data
     */
    public SomeTypeMsgListAdapter(Context mContext, List<Messages> data) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.data = data;
    }

    //====================================RecyclerView 更新封装 开始=================================
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
        return new ViewHolder(mLayoutInflater.inflate(R.layout.adapter_item_sometype, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.itemView.setClickable(true);
        Messages message=data.get(position);

        viewHolder.tv_1.setText(message.getMessage());
        viewHolder.time.setText(""+message.getCreated());

        if(message.getRead()){
            viewHolder.tv_1.setTextColor(mContext.getResources().getColor(R.color.common_gray));
        }else{
            viewHolder.tv_1.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        if(isEdidMode){
            viewHolder.imageView.setVisibility(View.VISIBLE);
            if(message.isChecked()){
                viewHolder.imageView.setImageResource (R.drawable.checkbox_selected);
            }else {
                viewHolder.imageView.setImageResource (R.drawable.checkbox_disselected);
            }
        }else{
            viewHolder.imageView.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedIndex = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, checkedIndex);
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
        private TextView time;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_1 = (TextView) itemView.findViewById(R.id.title1);
            time = (TextView) itemView.findViewById(R.id.time);
            imageView = (ImageView) itemView.findViewById(R.id.img_check_flag);
        }
    }
}