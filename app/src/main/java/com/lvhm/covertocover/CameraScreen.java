package com.lvhm.covertocover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;

import java.util.concurrent.ExecutionException;

public class CameraScreen extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cam, container, false);

        SurfaceView camera_preview = view.findViewById(R.id.camera_preview_surface);
        camera_preview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {}

            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try {
                    ProcessCameraProvider camera_provider = ProcessCameraProvider.getInstance(getContext()).get();
                    Preview preview = new Preview.Builder().build();

                    CameraSelector camera_selector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();

                    preview.setSurfaceProvider((Preview.SurfaceProvider) camera_preview.getHolder().getSurface());

                    camera_provider.bindToLifecycle(CameraScreen.this, camera_selector, preview);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}
        });


        return view;
    }
}