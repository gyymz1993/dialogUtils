package com.afollestad.materialdialogssample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Simple adapter example for custom items in the dialog
 */
class ButtonItemAdapter extends RecyclerView.Adapter<ButtonItemAdapter.ButtonVH> {

    private List<String> mItems;
    private ItemCallback itemCallback;
    private ButtonCallback buttonCallback;
    private int mSelectedPos = -1;//实现单选  方法二，变量保存当前选中的position
    //实现不可选择的选项
    private int itemPositionEnabled = -1;

    public void setmSelectedPos(int mSelectedPos) {
        this.mSelectedPos = mSelectedPos;
    }

    public void setItemPositionEnabled(int itemPositionEnabled) {
        this.itemPositionEnabled = itemPositionEnabled;
    }

    ButtonItemAdapter(List<String> items) {
        this.mItems = items;
    }


    void setCallbacks(ItemCallback itemCallback, ButtonCallback buttonCallback) {
        this.itemCallback = itemCallback;
        this.buttonCallback = buttonCallback;
    }

    @Override
    public ButtonVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dialog_customlistitem, parent, false);
        return new ButtonVH(view, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ButtonVH holder, int position) {
        holder.title.setText(mItems.get(position));
        holder.checkBox.setTag(position);
        if (itemPositionEnabled == mSelectedPos) {
            //设置默认项
            new Exception("被禁用项和默认项不能为同一个");
        }
        holder.checkBox.setChecked(mSelectedPos == position);
        holder.checkBox.setSelected(mSelectedPos == position);
        if (itemPositionEnabled == position) {
            for (int i = 0; i < mItems.size(); i++) {
                if (i != itemPositionEnabled) {
                    mSelectedPos = i;
                }
            }
            holder.idRyRootItme.setEnabled(false);
            holder.checkBox.setEnabled(false);
        }

        if (mSelectedPos == position) {
            holder.idRyRootItme.setBackgroundResource(R.drawable.dialog_item_select_bg);
        } else if (itemPositionEnabled == position) {
            holder.idRyRootItme.setBackgroundResource(R.drawable.dialog_item_enabled_bg);
        } else if (mSelectedPos == position) {
            //holder.checkBox.setChecked(false);
        } else {
            holder.idRyRootItme.setBackgroundResource(R.drawable.dialog_item_normal_bg);
        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        ItemCheckListener  itemCheckListener=new ItemCheckListener(holder,position);
        holder.itemView.setOnClickListener(itemCheckListener);
        holder.checkBox.setOnClickListener(itemCheckListener);
    }


    public class ItemCheckListener implements View.OnClickListener {
        ButtonVH holder;
        int position;
        public  ItemCheckListener (ButtonVH buttonVH,int mposition){
            this.holder=buttonVH;
            this.position=mposition;
        }
        @Override
        public void onClick(View v) {
            if (holder.adapter.itemCallback == null) {
                return;
            }
            if (mSelectedPos == position) {
                return;
            }
            holder.adapter.buttonCallback.onButtonClicked(position);
            if (mSelectedPos != position) {//当前选中的position和上次选中不是同一个position 执行
                holder.checkBox.setChecked(true);
                holder.checkBox.setSelected(true);
                mSelectedPos = position;
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    interface ItemCallback {

        void onItemClicked(int itemIndex);
    }

    interface ButtonCallback {

        void onButtonClicked(int buttonIndex);
    }

    class ButtonVH extends RecyclerView.ViewHolder {

        final TextView title;
        final CheckBox checkBox;
        final ButtonItemAdapter adapter;
        final RelativeLayout idRyRootItme;

        ButtonVH(View itemView, ButtonItemAdapter adapter) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.md_title);
            checkBox = (CheckBox) itemView.findViewById(R.id.md_button);
            idRyRootItme = (RelativeLayout) itemView.findViewById(R.id.id_ry_root);
            this.adapter = adapter;
            //实现单选方法三： RecyclerView另一种定向刷新方法：不会有白光一闪动画 也不会重复onBindVIewHolder

            //checkBox.setChecked(false);
        }


    }
}
