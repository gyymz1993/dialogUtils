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

/**
 * Simple adapter example for custom items in the dialog
 */
class ButtonItemAdapter extends RecyclerView.Adapter<ButtonItemAdapter.ButtonVH> {

    private final CharSequence[] items;
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

    ButtonItemAdapter(Context context, @ArrayRes int arrayResId) {
        this(context.getResources().getTextArray(arrayResId));
    }

    private ButtonItemAdapter(CharSequence[] items) {
        this.items = items;
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
        holder.title.setText(items[position] + " (" + position + ")");
        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(mSelectedPos == position);

//        if (itemPositionEnabled == -1) {
//            //设置默认项
//            mSelectedPos = 0;
//        }
        if (itemPositionEnabled == position) {
            for (int i = 0; i < items.length; i++) {
                if (i != itemPositionEnabled) {
                    mSelectedPos = i;
                }
            }
            holder.idRyRootItme.setEnabled(false);
        }

        if (mSelectedPos == position) {
            holder.idRyRootItme.setBackgroundResource(R.drawable.dialog_item_select_bg);
        } else if (itemPositionEnabled == position) {
            holder.idRyRootItme.setBackgroundResource(R.drawable.dialog_item_enabled_bg);
        } else {
            holder.idRyRootItme.setBackgroundResource(R.drawable.dialog_item_normal_bg);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.adapter.itemCallback == null) {
                    return;
                }
                if (mSelectedPos==position){
                    return;
                }
                holder.adapter.buttonCallback.onButtonClicked(position);
                if (mSelectedPos != position) {//当前选中的position和上次选中不是同一个position 执行
                    holder.checkBox.setChecked(true);
                    mSelectedPos = position;
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.length;
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
