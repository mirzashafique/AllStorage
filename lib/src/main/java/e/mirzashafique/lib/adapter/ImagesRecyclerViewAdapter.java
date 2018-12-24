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

import com.bumptech.glide.Glide;


import java.util.List;

import e.mirzashafique.lib.R;
import e.mirzashafique.lib.listener.SubmitAction;
import e.mirzashafique.lib.model.SelectedFiles;
import e.mirzashafique.lib.model.SingletonList;

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesRecyclerViewAdapter.MyViewHolder> {

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

    public ImagesRecyclerViewAdapter(Context context, List<SelectedFiles> data, int maxSize, String fileType) {
        this.context = context;
        this.data = data;
        this.maxSize = maxSize;
        this.fileType = fileType;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ImagesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_row_for_images_rv_adapter, viewGroup, false);
        ImagesRecyclerViewAdapter.MyViewHolder holder = new ImagesRecyclerViewAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagesRecyclerViewAdapter.MyViewHolder holder, int i) {
        final SelectedFiles current = data.get(i);
        holder.imageName.setText(current.getFileName());
        holder.imageSize.setText(current.getFileSize());
        if (current.getFileType().equals("video")) {
            holder.typeIcon.setImageResource(R.drawable.vidoe);
            ContentResolver crThumb = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, Integer.valueOf(current.getFileId()), MediaStore.Video.Thumbnails.MINI_KIND, options);
            holder.currentImage.setImageBitmap(curThumb);
        } else {
            Glide.with(context).load(current.getFilePath()).into(holder.currentImage);
        }


        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(current.isSelectStatus());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    current.setSelectStatus(isChecked);
                    SingletonList.getmInstence().addSelectedFile(current);
                    submitAction.actionMethod();

                    currentSelectedFiles=0;

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

                } else {
                    current.setSelectStatus(isChecked);
                    SingletonList.getmInstence().removeSelectedFile(current);
                    submitAction.actionMethod();
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
        private TextView imageName, imageSize;
        private ImageView currentImage, typeIcon;
        private CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageName = itemView.findViewById(R.id.image_name);
            imageSize = itemView.findViewById(R.id.image_size);
            currentImage = itemView.findViewById(R.id.curent_image);
            typeIcon = itemView.findViewById(R.id.icon);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
