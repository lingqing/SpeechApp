package com.example.liang.speechapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by liang on 2017/6/21.
 */

public class ResultTxtAdapter extends RecyclerView.Adapter<ResultTxtAdapter.ViewHolder>{
    private List<ResultTxt> resultTxtList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layout;
        TextView resultTxtView;

        public ViewHolder(View view){
            super(view);
//            layout = (LinearLayout)view.findViewById(R.id..result_text);
            resultTxtView = (TextView)view.findViewById(R.id.result_txt_id);
        }
    }
    public ResultTxtAdapter(List<ResultTxt> rl){
        resultTxtList = rl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_text, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ResultTxt txt = resultTxtList.get(position);
        holder.resultTxtView.setText(txt.getContent());
    }

    @Override
    public int getItemCount() {
        return resultTxtList.size();
    }
}
