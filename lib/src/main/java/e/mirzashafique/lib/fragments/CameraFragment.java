package e.mirzashafique.lib.fragments;

import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import e.mirzashafique.lib.CameraPreview;
import e.mirzashafique.lib.R;
import e.mirzashafique.lib.adapter.CameraImagesAdapter;
import e.mirzashafique.lib.listener.SubmitAction;
import e.mirzashafique.lib.model.SelectedFiles;
import e.mirzashafique.lib.model.SingletonList;


public class CameraFragment extends Fragment implements SubmitAction {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    static String TAG = "asdf";

    //views
    private FrameLayout frameLayout;
    private RecyclerView recyclerView;
    private ImageView switchCamera, saveButton, backButton, captureButton, nextImage;

    //variables
    private Camera mCamera;
    private CameraPreview mPreview;
    private SubmitAction submitAction;
    private boolean cameraFront = false;
    private CameraImagesAdapter adapter;
    private List<SelectedFiles> mData;


    public void setSubmitAction(SubmitAction submitAction) {
        this.submitAction = submitAction;
    }

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        //variables initialization
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        mPreview = new CameraPreview(getActivity(), mCamera);
        mData = new ArrayList<>();
        adapter = new CameraImagesAdapter(getActivity(), mData, 5, "image");
        adapter.setSubmitAction(this);
//        Camera.Parameters params= mCamera.getParameters();
//        params.set("rotation", 90);
        //       mCamera.setParameters(params);

        //views initialization
        frameLayout = view.findViewById(R.id.camera_preview);
        frameLayout.addView(mPreview);
        switchCamera = view.findViewById(R.id.switch_camera);
        saveButton = view.findViewById(R.id.save_button);
        backButton = view.findViewById(R.id.back_button);
        captureButton = view.findViewById(R.id.capture_button);
        nextImage = view.findViewById(R.id.next_image);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        //listeners
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCamera();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.startPreview();
                captureButton.setVisibility(View.VISIBLE);
                switchCamera.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
                nextImage.setVisibility(View.GONE);
            }
        });
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mCamera.takePicture(null, null, mPicture);
                } catch (Exception e) {
                }


            }
        });

        return view;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d("wtf2", "ERROR " + e);
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {

            saveButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
            nextImage.setVisibility(View.VISIBLE);
            captureButton.setVisibility(View.GONE);
            switchCamera.setVisibility(View.GONE);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

                    if (pictureFile == null) {
                        Log.d(TAG, "Error creating media file, check storage permissions");
                        return;
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(data);
                        fos.close();
                        Toast.makeText(getActivity(), "Image saved", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        Log.d(TAG, "File not found: " + e.getMessage());
                    } catch (IOException e) {
                        Log.d(TAG, "Error accessing file: " + e.getMessage());
                    }


                    mData.add(new SelectedFiles("", pictureFile.getName(), pictureFile.getAbsolutePath(), "image", "", "", "", false));
                    adapter.notifyDataSetChanged();
                    SingletonList.getmInstence().getSelectedFiles().add(new SelectedFiles("", pictureFile.getName(), pictureFile.getAbsolutePath(), "image", "", "", "", false));
                    submitAction.actionMethod();

                    captureButton.setVisibility(View.VISIBLE);
                    switchCamera.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);
                    backButton.setVisibility(View.GONE);
                    nextImage.setVisibility(View.GONE);
                    mCamera.startPreview();
                }
            });

            nextImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

                    if (pictureFile == null) {
                        Log.d(TAG, "Error creating media file, check storage permissions");
                        return;
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(data);
                        fos.close();
                        Toast.makeText(getActivity(), "Image saved", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        Log.d(TAG, "File not found: " + e.getMessage());
                    } catch (IOException e) {
                        Log.d(TAG, "Error accessing file: " + e.getMessage());
                    }


                    mData.add(new SelectedFiles("", pictureFile.getName(), pictureFile.getAbsolutePath(), "image", "", "", "", false));
                    adapter.notifyDataSetChanged();
                    SingletonList.getmInstence().getSelectedFiles().add(new SelectedFiles("", pictureFile.getName(), pictureFile.getAbsolutePath(), "image", "", "", "", false));
                    submitAction.actionMethod();

                    captureButton.setVisibility(View.VISIBLE);
                    switchCamera.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);
                    backButton.setVisibility(View.GONE);
                    nextImage.setVisibility(View.GONE);
                    mCamera.startPreview();
                }
            });
        }
    };


    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type) {

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
//        // This location works best if you want the created images to be shared
//        // between applications and persist after your app has been uninstalled.
//
//        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        Uri uri;

        String address;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
            address = mediaFile.getPath();
            Log.d("address1", "address for img is " + address);
            uri = Uri.fromFile(mediaFile);
            Log.d("address2", "uri for img is " + uri);

        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
            address = mediaFile.getPath();
            Log.d("address1", "address for vid is " + address);
            uri = Uri.fromFile(mediaFile);
            Log.d("address2", "uri for vid is " + uri);
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void actionMethod() {
        adapter.notifyDataSetChanged();
        submitAction.actionMethod();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.release();
    }

    public void chooseCamera() {
        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                mCamera.release();
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPreview = new CameraPreview(getActivity(), mCamera);
                frameLayout.addView(mPreview);
                // mPicture = getPictureCallback();
                // mPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                mCamera.release();
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPreview = new CameraPreview(getActivity(), mCamera);
                frameLayout.addView(mPreview);
                // mPicture = getPictureCallback();
                // mPreview.refreshCamera(mCamera);
            }
        }
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

}
