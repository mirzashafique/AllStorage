package e.mirzashafique.lib.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import e.mirzashafique.lib.AppExecutors;
import e.mirzashafique.lib.R;
import e.mirzashafique.lib.adapter.ImagesRecyclerViewAdapter;
import e.mirzashafique.lib.listener.SubmitAction;
import e.mirzashafique.lib.model.SelectedFiles;


public class VideosFragment extends Fragment implements SubmitAction {
    private RecyclerView recyclerView;
    private List<SelectedFiles> data;
    private ImagesRecyclerViewAdapter adapter;
    private String path;
    private SubmitAction submitAction;
    private ProgressBar progressBar;
    private int maxSize;

    public void setSubmitAction(SubmitAction submitAction) {
        this.submitAction = submitAction;
    }

    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_videos, container, false);
        path = Environment.getExternalStorageDirectory().toString();
        maxSize=getArguments().getInt("selection-size");
        progressBar = view.findViewById(R.id.progress_bar);
        data = new ArrayList<>();
        AppExecutors.getsInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                getAllMedia();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ImagesRecyclerViewAdapter(getActivity(), data,maxSize,"video");
                        adapter.setSubmitAction(VideosFragment.this);
                        recyclerView = view.findViewById(R.id.recycler_view);
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });


//        File directory = new File(path);
//        final File[] file = directory.listFiles();
//        for (int i = 0; i < file.length; i++) {
//            SelectedFiles current = new SelectedFiles(file[i].getName(), file[i].getAbsolutePath(), "", "");
//            data.add(current);
//            adapter.notifyDataSetChanged();
//        }
        return view;
    }

    public void getAllMedia() {

        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media._ID};
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        try {
            int fileId = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int fileName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
            int fileUri = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA);
            int fileSize = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int fileDate = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
            int fileDuration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            cursor.moveToFirst();
            do {
                data.add(new SelectedFiles(cursor.getString(fileId), cursor.getString(fileName), cursor.getString(fileUri), "video", getFileSize(cursor.getLong(fileSize)), cursor.getString(fileDate), cursor.getString(fileDuration), false));
            } while (cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    public void actionMethod() {
        submitAction.actionMethod();
    }
}