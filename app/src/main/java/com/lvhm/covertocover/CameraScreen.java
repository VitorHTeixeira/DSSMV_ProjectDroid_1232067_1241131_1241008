package com.lvhm.covertocover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;

import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CameraScreen extends Fragment {
    private PreviewView preview_view;
    private ListenableFuture<ProcessCameraProvider> camera_provider_future;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cam, container, false);
        preview_view = view.findViewById(R.id.camera_preview_view);
        camera_provider_future = ProcessCameraProvider.getInstance(requireContext());

        camera_provider_future.addListener(this::startCamera, ContextCompat.getMainExecutor(requireContext()));


        return view;
    }

    private void startCamera() {
        try {
            ProcessCameraProvider camera_provider = camera_provider_future.get();

            Preview preview = new Preview.Builder()
                    .build();

            CameraSelector camera_selector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();
            preview.setSurfaceProvider(preview_view.getSurfaceProvider());
            Camera camera = camera_provider.bindToLifecycle(this, camera_selector, preview);

        } catch (ExecutionException | InterruptedException error) {
            error.printStackTrace();
        }
    }
}