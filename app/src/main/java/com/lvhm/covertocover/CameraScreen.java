package com.lvhm.covertocover;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;

public class CameraScreen extends Fragment {
    private PreviewView preview_view;
    private ListenableFuture<ProcessCameraProvider> camera_provider_future;
    private ImageAnalysis image_analysis;

    private LinearLayout capture_button;
    private volatile boolean is_capturing = false;
    private volatile boolean analyze_frame = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cam, container, false);
        preview_view = view.findViewById(R.id.camera_preview_view);

        capture_button = view.findViewById(R.id.layout_capture);

        capture_button.setOnClickListener(v -> {
            if (is_capturing) {
                NotificationCentral.showNotification(requireContext(), "A imagem ainda está a ser processada.");
                return;
            }
            is_capturing = true;
            analyze_frame = true;
        });

        NotificationCentral.createNotificationChannel(requireContext());
        camera_provider_future = ProcessCameraProvider.getInstance(requireContext());
        camera_provider_future.addListener(this::startCamera, ContextCompat.getMainExecutor(requireContext()));

        return view;
    }

    private void startCamera() {
        if (!isAdded()) {
            return;
        }

        try {
            ProcessCameraProvider camera_provider = camera_provider_future.get();

            Preview preview = new Preview.Builder()
                    .build();

            CameraSelector camera_selector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();

            image_analysis = new ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build();

            image_analysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), imageProxy -> {
                if (analyze_frame) {
                    analyze_frame = false;
                    scan_barcode(imageProxy);
                } else {
                    imageProxy.close();
                }
            });

            preview.setSurfaceProvider(preview_view.getSurfaceProvider());
            camera_provider.unbindAll();
            camera_provider.bindToLifecycle(this, camera_selector, preview, image_analysis);

        } catch (ExecutionException | InterruptedException error) {
            if (!isAdded()) {
                return;
            }
            String error_message = "Falha ao iniciar a câmara: " + error.getMessage();
            NotificationCentral.showNotification(requireContext(), error_message);
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void scan_barcode(ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage == null) {
            imageProxy.close();
            resetCaptureState();
            return;
        }

        InputImage imageToScan = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        scanner.process(imageToScan)
                .addOnSuccessListener(barcodes -> {
                    if (!isAdded()) {
                        return;
                    }
                    if (barcodes.isEmpty()) {
                        NotificationCentral.showNotification(requireContext(), "Nenhum código de barras encontrado.");
                    } else {
                        String rawValue = barcodes.get(0).getRawValue();
                        String message = "ISBN foi lido: " + rawValue;
                        NotificationCentral.showNotification(requireContext(), message);
                    }
                })
                .addOnFailureListener(e -> {
                    if (!isAdded()) {
                        return;
                    }
                    String error_message = "Falha ao ler o código: " + e.getMessage();
                    NotificationCentral.showNotification(requireContext(), error_message);
                })
                .addOnCompleteListener(task -> {
                    imageProxy.close();
                    if (isAdded()) {
                        resetCaptureState();
                    }
                });
    }

    private void resetCaptureState() {
        is_capturing = false;
    }
}
