package com.example.match_app.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.match_app.R;
import com.example.match_app.dto.IconDTO;

public class IconViewer extends LinearLayout {
    TextView tv_icon;
    ImageView iv_icon;

    public IconViewer(Context context) {
        super(context);
        init(context);
    }

    public IconViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.icon_item,this,true);
    }

    public void setItem(IconDTO dto) {
        tv_icon.setText(dto.getIconId());
        iv_icon.setImageResource(dto.getIconImage());
    }
}
