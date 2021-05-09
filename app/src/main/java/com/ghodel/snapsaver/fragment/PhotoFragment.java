package com.ghodel.snapsaver.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.adapter.SnapSaverAdapter;
import com.ghodel.snapsaver.helper.SnapSaverData;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvPhoto;
    private SwipeRefreshLayout srl;
    private SnapSaverAdapter adapter;

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void initView(View view) {
        rvPhoto = view.findViewById(R.id.rv_photo);
        srl = view.findViewById(R.id.swipe_refresh);
    }

    @Override
    public void initLogic(View view) {
        loadPhoto();
        srl.setColorSchemeColors(Color.BLUE, Color.RED, Color.BLUE);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rvPhoto.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvPhoto.setVisibility(View.VISIBLE);
                        loadPhoto();
                        srl.setRefreshing(false);
                    }
                }, 2000);


            }
        });

    }

    private void loadPhoto(){
        new SnapSaverData(getActivity(), srl, true, new SnapSaverData.SnapSaverTaskListener() {
            @Override
            public void onResult(ArrayList<File> list) {
                rvPhoto.setHasFixedSize(true);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvPhoto.setLayoutManager(staggeredGridLayoutManager);
                adapter = new SnapSaverAdapter(list, getActivity());
                rvPhoto.setAdapter(adapter);
                rvPhoto.getAdapter().notifyDataSetChanged();
                Log.d("Result", list.toString());

            }

            @Override
            public void onError(String error) {
                Log.e("error", error);
            }
        }).execute();
    }

    @Override
    public void initListener(View view) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        initView(view);
        initLogic(view);
        initListener(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPhoto();
    }
}