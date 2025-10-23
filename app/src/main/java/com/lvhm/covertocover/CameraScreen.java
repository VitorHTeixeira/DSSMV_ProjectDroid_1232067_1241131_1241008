package com.lvhm.covertocover;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import android.graphics.ImageFormat;

import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraScreen extends Fragment {
    private PreviewView preview_view;
    private ListenableFuture<ProcessCameraProvider> camera_provider_future;

    private ImageCapture image_capture;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cam, container, false);
        preview_view = view.findViewById(R.id.camera_preview_view);

        LinearLayout capture_button = view.findViewById(R.id.layout_capture);
        capture_button.setOnClickListener(v -> {
            takePicture();
        });

        NotificationCentral.createNotificationChannel(requireContext());
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
                    .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                    .build();

            image_capture = new ImageCapture.Builder() // verificar se não era preciso o setTargetRotation
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build();

            preview.setSurfaceProvider(preview_view.getSurfaceProvider());
            camera_provider.unbindAll();
            Camera camera = camera_provider.bindToLifecycle(this, camera_selector, preview, image_capture);

        } catch (ExecutionException | InterruptedException error) {
            error.printStackTrace();
        }
    }

    private void takePicture() {
        if (image_capture == null) {
            NotificationCentral.showNotification(requireContext(), "Erro: image_capture está nulo.");
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(requireContext());

        image_capture.takePicture(executor, new ImageCapture.OnImageCapturedCallback() {

            @SuppressLint("UnsafeOptInUsageError")
            public void onImageCaptured(@NonNull ImageProxy imageProxy) {
                Image mediaImage = imageProxy.getImage();
                if (mediaImage != null) {
                    InputImage imageToScan = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.getImageInfo().getRotationDegrees()
                    );

                    NotificationCentral.showNotification(requireContext(), "Foto capturada!");

                    scan_barcode(imageToScan, imageProxy);
                } else {
                    imageProxy.close();
                }
            }

            public void onError(@NonNull ImageCaptureException exception) {
                exception.printStackTrace();

                String error_message = "Erro ao tirar foto: " + exception.getMessage();
                NotificationCentral.showNotification(requireContext(), error_message);
            }
        });
    }
    private void scan_barcode(InputImage imageToScan, ImageProxy imageProxy) {


        imageProxy.close();
    }
}
