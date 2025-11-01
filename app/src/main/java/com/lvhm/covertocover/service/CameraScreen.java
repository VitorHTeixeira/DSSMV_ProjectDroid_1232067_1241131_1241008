package com.lvhm.covertocover.service;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.resolutionselector.AspectRatioStrategy;
import androidx.camera.core.resolutionselector.ResolutionSelector;
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
import com.lvhm.covertocover.BarcodeFrame;
import com.lvhm.covertocover.adapter.BookNavigationListener;
import com.lvhm.covertocover.api.BookAPICallback;
import com.lvhm.covertocover.api.BookAPIClient;
import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.datamodels.BookResponse;

import java.util.concurrent.ExecutionException;

public class CameraScreen extends Fragment {
    private PreviewView preview_view;
    private BarcodeFrame barcode_frame;
    private ListenableFuture<ProcessCameraProvider> camera_provider_future;
    private ImageAnalysis image_analysis;
    private LinearLayout capture_button;
    private volatile boolean should_read_barcode = false;
    private Bundle book_details_bundle = new Bundle();


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cam, container, false);
        preview_view = view.findViewById(R.id.camera_preview_view);
        barcode_frame = view.findViewById(R.id.barcode_frame);

        capture_button = view.findViewById(R.id.layout_capture);

        capture_button.setOnClickListener(v -> {
            if (!isAdded()) {
                return;
            }
            capture_button.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            should_read_barcode = true;
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

            ResolutionSelector resolution_selector = new ResolutionSelector.Builder()
                    .setAspectRatioStrategy(
                            new AspectRatioStrategy(
                                    AspectRatio.RATIO_16_9,
                                    AspectRatioStrategy.FALLBACK_RULE_AUTO
                            )
                    )
                    .build();

            image_analysis = new ImageAnalysis.Builder()
                    .setResolutionSelector(resolution_selector)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build();

            image_analysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), this::scanBarcode);

            preview.setSurfaceProvider(preview_view.getSurfaceProvider());
            camera_provider.unbindAll();
            camera_provider.bindToLifecycle(this, camera_selector, preview, image_analysis);

        } catch (ExecutionException | InterruptedException error) {
            if (!isAdded()) {
                return;
            }
            String error_message = "Error initializing camera: " + error.getMessage();
            NotificationCentral.showNotification(requireContext(), error_message);
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void scanBarcode(ImageProxy image_proxy) {
        Image media_image = image_proxy.getImage();
        if (media_image == null) {
            image_proxy.close();
            return;
        }

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(options);
        processImageFeed(scanner, image_proxy, media_image);
    }
    private void processImageFeed(BarcodeScanner scanner, ImageProxy image_proxy, Image media_image) {
        InputImage image_to_scan = InputImage.fromMediaImage(media_image, image_proxy.getImageInfo().getRotationDegrees());
        scanner.process(image_to_scan)
                .addOnSuccessListener(barcodes -> {
                    if (!isAdded()) {
                        return;
                    }
                    if (barcodes.isEmpty()) {
                        barcode_frame.updateBarcode(null);
                    } else {
                        Barcode barcode = barcodes.get(0);
                        if (barcode.getBoundingBox() != null) {
                            barcode_frame.updateBarcode(mapBarcodeFrame(barcode.getBoundingBox(), image_proxy));
                        }
                        if (should_read_barcode) {
                            should_read_barcode = false;
                            String raw_value = barcode.getRawValue();
                            getBookFromAPI(raw_value);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    barcode_frame.updateBarcode(null);
                    if (should_read_barcode) {
                        should_read_barcode = false;
                        if (isAdded()) {
                            String error_message = "Error reading code: " + e.getMessage();
                            NotificationCentral.showNotification(requireContext(), error_message);
                        }
                    }
                })
                .addOnCompleteListener(task -> {
                    image_proxy.close();
                });
    }

    private void getBookFromAPI(String barcodeValue) {
        if (!isAdded()) {return;}
        BookAPIClient.getBookFromAPI(requireContext(), barcodeValue, new BookAPICallback() {
            @Override
            public void onBookFound(BookResponse.Item item, String isbn, Bundle response_bundle) {
                if (!isAdded()) {return;}
                navigateToBookScreen(item, isbn, response_bundle);
            }
            @Override
            public void onNoBookFound(String isbn) {
                if (!isAdded()) {return;}
                navigateToManualBookScreen(isbn);
            }
            @Override
            public void onAPIFailure(String errorMessage) {}
        });
    }

    private void navigateToBookScreen(BookResponse.Item item, String isbn, Bundle response_bundle) {
        Bundle book_info = item.getVolumeInfo().getBundle();
        book_info.putString("isbn", isbn);
        book_info.putBundle("response", response_bundle);
        if (getActivity() instanceof BookNavigationListener) {
            ((BookNavigationListener) getActivity()).navigateToBookScreenFromAPI(book_info);
        }
    }

    private void navigateToManualBookScreen(String isbn) {
        Fragment manual_book_screen_fragment = new ManualBookScreen();
        Bundle book_info = new Bundle();
        book_info.putString("isbn", isbn);
        manual_book_screen_fragment.setArguments(book_info);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, manual_book_screen_fragment)
                .addToBackStack(null)
                .commit();
    }



    public Rect mapBarcodeFrame(Rect barcode_box, ImageProxy image_proxy) {
        int image_width = image_proxy.getWidth();
        int image_height = image_proxy.getHeight();

        int view_width = preview_view.getWidth();
        int view_height = preview_view.getHeight();

        float scale_x = (float) view_width / image_height;
        float scale_y = (float) view_height / image_width;

        float offset_x = (view_width - image_height * scale_x) / 2;
        float offset_y = (view_height - image_width * scale_y) / 2;

        int left = (int) (barcode_box.left * scale_x + offset_x);
        int top = (int) (barcode_box.top * scale_y + offset_y);
        int right = (int) (barcode_box.right * scale_x + offset_x);
        int bottom = (int) (barcode_box.bottom * scale_y + offset_y);

        return new Rect(left, top, right, bottom);
    }
}
