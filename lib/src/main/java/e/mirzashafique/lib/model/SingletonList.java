package e.mirzashafique.lib.model;

import java.util.ArrayList;
import java.util.List;

public class SingletonList {
    private static SingletonList mInstence;

    private List<SelectedFiles> selectedFiles;

    private SingletonList() {
        selectedFiles = new ArrayList<>();
    }

    public static SingletonList getmInstence() {
        if (mInstence == null)
            mInstence = new SingletonList();
        return mInstence;
    }

    public List<SelectedFiles> getSelectedFiles() {
        return selectedFiles;
    }

    public void setSelectedFiles(List<SelectedFiles> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    public void addSelectedFile(SelectedFiles selectedFiles) {
        this.selectedFiles.add(selectedFiles);
    }

    public void removeSelectedFile(SelectedFiles selectedFiles) {
        this.selectedFiles.remove(selectedFiles);
    }
}

