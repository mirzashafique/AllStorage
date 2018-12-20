package e.mirzashafique.lib.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import e.mirzashafique.lib.R;
import e.mirzashafique.lib.listener.SubmitAction;
import e.mirzashafique.lib.model.SelectedFiles;
import e.mirzashafique.lib.model.SingletonList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<SelectedFiles> data;
    private Context context;
    private LayoutInflater inflater;
    private SubmitAction submitAction;
    private int maxSize;
    private String fileType;
    private int currentSelectedFiles;

    public void setSubmitAction(SubmitAction submitAction) {
        this.submitAction = submitAction;
    }

    public RecyclerViewAdapter(Context context, List<SelectedFiles> data, int maxSize, String fileType) {
        this.context = context;
        this.data = data;
        this.maxSize = maxSize;
        this.fileType = fileType;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_row_for_rv_adapter, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        final SelectedFiles current = data.get(i);
        holder.fileName.setText(current.getFileName());
        holder.fileUri.setText(current.getFileSize());
        if (current.getFileType().equals(".csv")) {
            holder.fileIcon.setImageResource(R.drawable.pdf);
        } else if (current.getFileType().equals(".pdf")) {
            holder.fileIcon.setImageResource(R.drawable.pdf);
        } else if (current.getFileType().equals(".zip")) {
            holder.fileIcon.setImageResource(R.drawable.zip);
        } else if (current.getFileType().equals(".ppt")) {
            holder.fileIcon.setImageResource(R.drawable.ppt);
        } else if (current.getFileType().equals(".txt")) {
            holder.fileIcon.setImageResource(R.drawable.txt);
        } else if (current.getFileType().equals("audio")) {
            holder.fileIcon.setImageResource(R.drawable.audio);
        } else if (current.getFileType().equals("video")) {
            ContentResolver crThumb = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, Integer.valueOf(current.getFileId()), MediaStore.Video.Thumbnails.MICRO_KIND, options);
            holder.fileIcon.setImageBitmap(curThumb);
        }


        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(data.get(i).isSelectStatus());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(i).setSelectStatus(isChecked);

                if (isChecked) {
                    SingletonList.getmInstence().addSelectedFile(current);
                    submitAction.actionMethod();

                    currentSelectedFiles = 0;
                    if (fileType.contains(".")) {
                        for(int i=0;i<SingletonList.getmInstence().getSelectedFiles().size();i++){
                            if(SingletonList.getmInstence().getSelectedFiles().get(i).getFileType().contains(".")){
                                currentSelectedFiles++;
                                if (currentSelectedFiles > maxSize) {
                                    holder.checkBox.setChecked(false);
                                    SingletonList.getmInstence().removeSelectedFile(current);
                                    submitAction.actionMethod();
                                    Toast.makeText(context, "Max Limit is " + maxSize, Toast.LENGTH_SHORT).show();
                                    currentSelectedFiles--;
                                }
                            }
                        }
                    } else {
                        for(int i=0;i<SingletonList.getmInstence().getSelectedFiles().size();i++){
                            if(SingletonList.getmInstence().getSelectedFiles().get(i).getFileType().equals(fileType)){
                                currentSelectedFiles++;
                                if (currentSelectedFiles > maxSize) {
                                    holder.checkBox.setChecked(false);
                                    SingletonList.getmInstence().removeSelectedFile(current);
                                    submitAction.actionMethod();
                                    Toast.makeText(context, "Max Limit is " + maxSize, Toast.LENGTH_SHORT).show();
                                    currentSelectedFiles--;
                                }
                            }
                        }
                    }
                } else {
                    SingletonList.getmInstence().removeSelectedFile(current);
                    submitAction.actionMethod();
                    currentSelectedFiles--;
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (current.isSelectStatus()) {
                    // current.setSelectStatus(false);
                    holder.checkBox.setChecked(false);
                    //SingletonList.getmInstence().removeSelectedFile(current);
                    //  submitAction.actionMethod();
                } else {
                    //current.setSelectStatus(true);
                    holder.checkBox.setChecked(true);
                    //SingletonList.getmInstence().addSelectedFile(current);
                    // submitAction.actionMethod();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView fileName, fileUri;
        private ImageView fileIcon;
        private CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_name);
            fileUri = itemView.findViewById(R.id.file_uri);
            fileIcon = itemView.findViewById(R.id.file_icon);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}

