package e.mirzashafique.lib.fragments;


import android.database.Cursor;
import android.net.Uri;
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
import java.util.Collections;
import java.util.List;

import e.mirzashafique.lib.AppExecutors;
import e.mirzashafique.lib.R;
import e.mirzashafique.lib.adapter.ImagesRecyclerViewAdapter;
import e.mirzashafique.lib.listener.SubmitAction;
import e.mirzashafique.lib.model.SelectedFiles;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment implements SubmitAction {
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

    public ImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_images, container, false);
        path = Environment.getExternalStorageDirectory().toString();
        progressBar = view.findViewById(R.id.progress_bar);
        maxSize = getArguments().getInt("selection-size");
        data = new ArrayList<>();
        AppExecutors.getsInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                data = getAllShownImagesPath();
                Collections.reverse(data);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter = new ImagesRecyclerViewAdapter(getActivity(), data, maxSize,"image");
                        adapter.setSubmitAction(ImagesFragment.this);
                        recyclerView = view.findViewById(R.id.recycler_view);
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });


        return view;
    }

    private ArrayList<SelectedFiles> getAllShownImagesPath() {
        Uri uri;
        Cursor cursor;
        ArrayList<SelectedFiles> listOfAllImages = new ArrayList<>();
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE}

        cursor = getActivity().getContentResolver().query(uri, null, null, null, null);


        int fileId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int fileName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        int fileUri = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int fileSize = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
        int fileDate = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);

        while (cursor.moveToNext()) {
            listOfAllImages.add(new SelectedFiles(cursor.getString(fileId), cursor.getString(fileName), cursor.getString(fileUri), "image", getFileSize(cursor.getLong(fileSize)), cursor.getString(fileDate), "", false));
        }
        return listOfAllImages;
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
