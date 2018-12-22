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

import java.io.File;
import java.util.List;

import e.mirzashafique.lib.R;
import e.mirzashafique.lib.listener.SubmitAction;
import e.mirzashafique.lib.model.SelectedFiles;
import e.mirzashafique.lib.model.SingletonList;

public class CameraImagesAdapter extends RecyclerView.Adapter<CameraImagesAdapter.MyViewHolder> {

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

    public CameraImagesAdapter(Context context, List<SelectedFiles> data, int maxSize, String fileType) {
        this.context = context;
        this.data = data;
        this.maxSize = maxSize;
        this.fileType = fileType;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = inflater.inflate(R.layout.custom_row_for_camera_images_rv_adapter, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        final SelectedFiles current = data.get(i);
        Glide.with(context).load(current.getFileUri()).into(holder.currentImage);
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (int i1 = 0; i1 < SingletonList.getmInstence().getSelectedFiles().size(); i1++) {
                    if (current.getFileUri().equals(SingletonList.getmInstence().getSelectedFiles().get(i1).getFileUri())) {
                        SingletonList.getmInstence().getSelectedFiles().remove(i1);
                        submitAction.actionMethod();
                        Toast.makeText(context, "Image deleted", Toast.LENGTH_SHORT).show();
                        data.remove(i);
                        File file = new File(current.getFileUri());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView currentImage, cancelButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            currentImage = itemView.findViewById(R.id.images);
            cancelButton = itemView.findViewById(R.id.cancel_action);
        }
    }
}
