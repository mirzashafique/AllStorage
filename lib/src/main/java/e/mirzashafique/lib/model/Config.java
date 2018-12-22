package e.mirzashafique.lib.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Config implements Parcelable {

    private boolean camera;
    private int maxCamera;
    private boolean files;
    private int maxFile;
    private boolean images;
    private int maxImages;
    private boolean videos;
    private int maxVideos;
    private boolean audios;
    private int maxAudios;

    public Config(boolean camera, int maxCamera, boolean files, int maxFile, boolean images, int maxImages, boolean videos, int maxVideos, boolean audios, int maxAudios) {
        this.camera = camera;
        this.maxCamera = maxCamera;
        this.files = files;
        this.maxFile = maxFile;
        this.images = images;
        this.maxImages = maxImages;
        this.videos = videos;
        this.maxVideos = maxVideos;
        this.audios = audios;
        this.maxAudios = maxAudios;
    }

    protected Config(Parcel in) {
        camera = in.readByte() != 0;
        maxCamera = in.readInt();
        files = in.readByte() != 0;
        maxFile = in.readInt();
        images = in.readByte() != 0;
        maxImages = in.readInt();
        videos = in.readByte() != 0;
        maxVideos = in.readInt();
        audios = in.readByte() != 0;
        maxAudios = in.readInt();
    }

    public static final Creator<Config> CREATOR = new Creator<Config>() {
        @Override
        public Config createFromParcel(Parcel in) {
            return new Config(in);
        }

        @Override
        public Config[] newArray(int size) {
            return new Config[size];
        }
    };

    public boolean isFiles() {
        return files;
    }

    public void setFiles(boolean files) {
        this.files = files;
    }

    public int getMaxFile() {
        return maxFile;
    }

    public void setMaxFile(int maxFile) {
        this.maxFile = maxFile;
    }

    public boolean isImages() {
        return images;
    }

    public void setImages(boolean images) {
        this.images = images;
    }

    public int getMaxImages() {
        return maxImages;
    }

    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }

    public boolean isVideos() {
        return videos;
    }

    public void setVideos(boolean videos) {
        this.videos = videos;
    }

    public int getMaxVideos() {
        return maxVideos;
    }

    public void setMaxVideos(int maxVideos) {
        this.maxVideos = maxVideos;
    }

    public boolean isAudios() {
        return audios;
    }

    public void setAudios(boolean audios) {
        this.audios = audios;
    }

    public int getMaxAudios() {
        return maxAudios;
    }

    public void setMaxAudios(int maxAudios) {
        this.maxAudios = maxAudios;
    }

    public boolean isCamera() {
        return camera;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }

    public int getMaxCamera() {
        return maxCamera;
    }

    public void setMaxCamera(int maxCamera) {
        this.maxCamera = maxCamera;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (camera ? 1 : 0));
        dest.writeInt(maxCamera);
        dest.writeByte((byte) (files ? 1 : 0));
        dest.writeInt(maxFile);
        dest.writeByte((byte) (images ? 1 : 0));
        dest.writeInt(maxImages);
        dest.writeByte((byte) (videos ? 1 : 0));
        dest.writeInt(maxVideos);
        dest.writeByte((byte) (audios ? 1 : 0));
        dest.writeInt(maxAudios);
    }
}
