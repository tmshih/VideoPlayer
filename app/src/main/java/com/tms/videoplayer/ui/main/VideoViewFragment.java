package com.tms.videoplayer.ui.main;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tms.videoplayer.R;

import java.io.File;

/**
 * Example of VideoView.
 */
public class VideoViewFragment extends Fragment {

    public static VideoViewFragment newInstance() {
        VideoViewFragment fragment = new VideoViewFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    private VideoView videoView;
    private ImageView ivPreview;
    private EditText etMediaFile;
    private Button btnPlay;
    private Button btnStop;
    private Button btnCapture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_video_view, container, false);
        videoView = root.findViewById(R.id.videoView);
        ivPreview = root.findViewById(R.id.ivPreview);
        etMediaFile = root.findViewById(R.id.etMediaFile);
        btnPlay = root.findViewById(R.id.btnPlay);
        btnStop = root.findViewById(R.id.btnStop);
        btnCapture = root.findViewById(R.id.btnCapture);
        ClickListener clickListener = new ClickListener();
        btnPlay.setOnClickListener(clickListener);
        btnStop.setOnClickListener(clickListener);
        btnCapture.setOnClickListener(clickListener);
        return root;
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnPlay) {
                handleClickPlay();
            } else if (view.getId() == R.id.btnStop) {
                handleClickStop();
            } else if (view.getId() == R.id.btnCapture) {
                handleClickCapture();
            }
        }

        private void handleClickPlay() {
            /* Clear preview. */
            ivPreview.setImageBitmap(null);

            /* Play specified video. */
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
            }
            String filepath = etMediaFile.getText().toString();
            MediaPlayerListener mediaPlayerListener = new MediaPlayerListener();
            videoView.setMediaController(null);
            videoView.setOnCompletionListener(mediaPlayerListener);
            videoView.setOnErrorListener(mediaPlayerListener);
            videoView.setOnPreparedListener(mediaPlayerListener);
            videoView.setVideoURI(Uri.fromFile(new File(filepath)));
            videoView.start();
        }

        private void handleClickStop() {
            /* Clear preview. */
            ivPreview.setImageBitmap(null);

            /* Stop videoView. */
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
            }
        }

        private void handleClickCapture() {
            String filepath = etMediaFile.getText().toString();
            Bitmap bitmap = getVideoFrame(filepath);
            ivPreview.setImageBitmap(bitmap);
        }

        private Bitmap getVideoFrame(String filePath) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(filePath);
                Bitmap videoFrame = retriever.getFrameAtTime();
                retriever.release();
                return videoFrame;
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "getVideoFrame(): " + e);
            } catch (RuntimeException e) {
                Log.w(TAG, "getVideoFrame(): " + e);
            }
            return null;
        }
    }

    private class MediaPlayerListener implements MediaPlayer.OnCompletionListener,
            MediaPlayer.OnErrorListener,
            MediaPlayer.OnPreparedListener {

        private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Log.d(TAG, "onCompletion(): " + mediaPlayer);
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            Log.d(TAG, "onPrepared(): " + mediaPlayer);
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.w(TAG, "onError(): " + mp + " what=" + what + " extra=" + extra);
            return false;
        }
    }
}