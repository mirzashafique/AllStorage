package e.mirzashafique.lib.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import e.mirzashafique.lib.AppExecutors;
import e.mirzashafique.lib.R;
import e.mirzashafique.lib.adapter.RecyclerViewAdapter;
import e.mirzashafique.lib.listener.SubmitAction;
import e.mirzashafique.lib.model.SelectedFiles;


public class FilesFragment extends Fragment implements SubmitAction {
    private RecyclerView recyclerView;
    private List<SelectedFiles> data;
    private RecyclerViewAdapter adapter;
    private String path;
    private SubmitAction submitAction;
    private ProgressBar progressBar;
    private int selectionSize;

    public FilesFragment() {
        // Required empty public constructor
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void setSubmitAction(SubmitAction submitAction) {
        this.submitAction = submitAction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        selectionSize = getArguments().getInt("selection-size");
        path = Environment.getExternalStorageDirectory().toString();
        data = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getActivity(), data, selectionSize, ".");
        adapter.setSubmitAction(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.progress_bar);

        final File directory = new File(path);
        AppExecutors.getsInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                getListFiles2(directory);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if (file.getName().endsWith(".pdf")) {
                    inFiles.add(file);
                    // data.add(new SelectedFiles(file.getName(), file.getAbsolutePath()));
                    adapter.notifyDataSetChanged();
                }
            }
        }

        return inFiles;
    }

    private List<File> getListFiles2(File parentDir) {
        List<File> inFiles = new ArrayList<>();
        Queue<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(parentDir.listFiles()));
        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.isDirectory()) {
                try {
                    files.addAll(Arrays.asList(file.listFiles()));
                } catch (Exception e) {
                }

            } else if (file.getName().endsWith(".xlsx") || file.getName().endsWith(".docx") || file.getName().endsWith(".pdf") || file.getName().endsWith(".zip") || file.getName().endsWith(".pptx") || file.getName().endsWith(".txt")) {
                inFiles.add(file);
                String fileType = null;
                if (file.getName().endsWith(".xlsx")) {
                    fileType = ".xlsx";
                } else if (file.getName().endsWith(".pdf")) {
                    fileType = ".pdf";
                } else if (file.getName().endsWith(".docx")) {
                    fileType = ".docx";
                } else if (file.getName().endsWith(".zip")) {
                    fileType = ".zip";
                } else if (file.getName().endsWith(".pptx")) {
                    fileType = ".pptx";
                } else if (file.getName().endsWith(".txt")) {
                    fileType = ".txt";
                }

                data.add(new SelectedFiles(file.getName(), file.getName(), file.getAbsolutePath(), fileType, getFileSize(file.length()), "", "", false));
                //adapter.notifyDataSetChanged();
            }
        }
        return inFiles;
    }

    @Override
    public void actionMethod() {
        submitAction.actionMethod();
    }
}
