package e.mirzashafique.lib.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import e.mirzashafique.lib.adapter.RecyclerViewAdapter;
import e.mirzashafique.lib.listener.SubmitAction;
import e.mirzashafique.lib.model.SelectedFiles;


public class MusicFragment extends Fragment implements SubmitAction {
    private RecyclerView recyclerView;
    private List<SelectedFiles> data;
    private RecyclerViewAdapter adapter;
    private String path;
    private SubmitAction submitAction;
    private ProgressBar progressBar;
    int maxSize;
    public void setSubmitAction(SubmitAction submitAction){
        this.submitAction=submitAction;
    }

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_music, container, false);
        path = Environment.getExternalStorageDirectory().toString();
        progressBar=view.findViewById(R.id.progress_bar);
        maxSize=getArguments().getInt("selection-size");
        data = new ArrayList<>();
        AppExecutors.getsInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                getMusicFiles();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new RecyclerViewAdapter(getActivity(), data,maxSize,"audio");
                        adapter.setSubmitAction(MusicFragment.this);
                        recyclerView = view.findViewById(R.id.recycler_view);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });


        return view;
    }

    public void getMusicFiles() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {

            int fileId = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int fileName = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int fileUri = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            int fileSize = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int fileDate = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED);
            do {

                data.add(new SelectedFiles(songCursor.getString(fileId), songCursor.getString(fileName), songCursor.getString(fileUri), "audio", getFileSize(songCursor.getLong(fileSize)), songCursor.getString(fileDate), "", false));

            } while (songCursor.moveToNext());

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
