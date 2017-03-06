/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lib.net;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import httploglib.lib.R;
import httploglib.lib.been.HttpBeen;
import lib.theming.HoverTheme;

/**
 * RecyclerView Adapter to display App Activitys and Services.
 */
public class HttpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<HttpBeen> beens;

    private HoverTheme mTheme;
    private Context mContext;

    public HttpAdapter(List beens,Context context) {
        this.beens = beens;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.result_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        MyViewHolder myViewHolder = (MyViewHolder) holder.list_item_text.setText(beens.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return beens.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView list_item_text;

        public MyViewHolder(View view) {
            super(view);
            list_item_text = (TextView) view.findViewById(R.id.list_item_text);
        }
    }
}
