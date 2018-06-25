package com.sample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sample.api.ApiClient;
import com.sample.databinding.RowVideoItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.materialSearchView)
    MaterialSearchView materialSearchView;

    @BindView(R.id.rvVideos)
    RecyclerView rvVideos;

    @BindView(R.id.tvNoRecordFound)
    TextView tvNoRecordFound;

    private ArrayList<VideoData> videoList = new ArrayList<>();
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ButterKnife.bind(this);

            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

            GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
            rvVideos.setHasFixedSize(true);
            rvVideos.setLayoutManager(layoutManager);

            GPHApi client = new GPHApiClient(getString(R.string.giphy_api_key));

            materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //Do some magic
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.isEmpty()) {
                        getData(newText);
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
        materialSearchView.performClick();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void getData(String queryText) {
        try {
            HashMap<String, String> requestParams = new HashMap<>();
            requestParams.put("api_key", getString(R.string.giphy_api_key));
            requestParams.put("q", queryText);

            final Call<JsonObject> labelResponse = ApiClient.getService().callGetMethod(ApiClient.SEARCH_URL, requestParams);
            labelResponse.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        Log.e("Response", response.toString());
                        if (response.body() != null) {
                            String resp = response.body().get("meta").getAsJsonObject().get("status").getAsString();
                            if (resp.equalsIgnoreCase("200")) {
                                Gson gson = new Gson();
                                videoList = gson.fromJson(response.body().get("data").getAsJsonArray(), new TypeToken<ArrayList<VideoData>>() {
                                }.getType());
                                Log.e("videoList size", videoList.size() + "");
                                videoAdapter = new VideoAdapter();
                                rvVideos.setAdapter(videoAdapter);
                                tvNoRecordFound.setVisibility(videoList.isEmpty() ? View.VISIBLE : View.GONE);
                            } else {
                                tvNoRecordFound.setVisibility(videoList.isEmpty() ? View.VISIBLE : View.GONE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        tvNoRecordFound.setVisibility(videoList.isEmpty() ? View.VISIBLE : View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    tvNoRecordFound.setVisibility(videoList.isEmpty() ? View.VISIBLE : View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

        private LayoutInflater layoutInflater;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private final RowVideoItemBinding binding;

            public MyViewHolder(final RowVideoItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.binding = itemBinding;
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.getContext());
            }
            RowVideoItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_video_item, parent, false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.binding.setItem(videoList.get(position));

            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("VideoUrl", videoList.get(position).getImageData().getVideoUrl().getUrl()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return videoList.size();
        }
    }
}