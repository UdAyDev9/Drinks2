package com.uday.drinks.Utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.uday.drinks.HomeActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {


    private File file;
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private UploadCallback uploadCallbackListener;

    public ProgressRequestBody(File file, UploadCallback uploadCallbackListener) {
        this.file = file;
        this.uploadCallbackListener = uploadCallbackListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse("image/*");
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        long fileLength = file.length();

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        FileInputStream fileInputStream = new FileInputStream(file);
        long uploaded = 0;

        try{

            int read;

            Handler handler = new Handler(Looper.getMainLooper());

            while ((read = fileInputStream.read(buffer))!= -1){

                handler.post(new ProgressUpdater(uploaded,fileLength));

                uploaded += read;
                sink.write(buffer,0,read);
            }

        }finally {

            fileInputStream.close();
        }

    }

    private class ProgressUpdater implements Runnable {

        private long uploaded,fileLength;

        public ProgressUpdater(long uploaded, long fileLength) {

            this.uploaded = uploaded;
            this.fileLength = fileLength;

        }

        @Override
        public void run() {

            uploadCallbackListener.onProgressUpdate((int)(100*uploaded/fileLength));
        }
    }
}
