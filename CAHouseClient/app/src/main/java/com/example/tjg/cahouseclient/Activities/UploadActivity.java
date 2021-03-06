package com.example.tjg.cahouseclient.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjg.cahouseclient.JavaFiles.FilePath;
import com.example.tjg.cahouseclient.R;
import com.example.tjg.cahouseclient.Storage.AppConfig;
import com.example.tjg.cahouseclient.Storage.MyPreference;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    final int PICK_FILE_REQUEST = 101;
    final int STORAGE_PERMISSION_CODE = 100;
    Button btnSubmit, btnBrowse, btnFetch;
    AppConfig appConfig = new AppConfig();
    ProgressDialog progressDialog;
    String SERVER_URL = appConfig.getUrl() + "upload/fileuploads/upload_client_file.php";
    String selectedFilePath;
    TextView tvFileName;
    EditText edtFileName;
    MyPreference myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myPreference = new MyPreference(this);
        tvFileName = findViewById(R.id.tvFileName);
        //edtFileName = findViewById(R.id.edtFileName);
        btnBrowse = findViewById(R.id.btnBrowse);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnFetch = findViewById(R.id.btnFetch);


        btnBrowse.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnFetch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnBrowse) {
            if (isReadStorageAllowed()) {
                ShowFileChooser();
            }
            requestStoragePermission();
        }

        if (v == btnSubmit) {
//            if(edtFileName.getText().toString().length() == 0){
//                edtFileName.setError("Required");
//                return;
//            }
            if (selectedFilePath != null && selectedFilePath != "") {
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Please Wait...");
                progressDialog.setMessage("File Uploading");
                progressDialog.setCancelable(true);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(selectedFilePath);
                    }
                }).start();

            } else {
                Toast.makeText(this, "First Select The Document", Toast.LENGTH_SHORT).show();
            }
        }
        if (v == btnFetch){
            startActivity(new Intent(UploadActivity.this, FetchDataActivity.class));
        }
    }

    public int uploadFile(final String selectedFilePath) {


        Log.e("uploadFile", selectedFilePath);
        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            progressDialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL + "?userid=" + myPreference.GetSession() + "&filename=" + fileName + "&uploadedby=" + myPreference.getName().replace(" ","%20"));

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.e("Server Response is: ", serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "/Applications/XAMPP/htdocs/webservices/upload/fileuploads/uploads/" + fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String decodeString;
                while ((decodeString = bufferedReader.readLine()) != null) {
                    Log.e("Output", decodeString);
                }
                bufferedReader.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();

                Log.e("Error",""+e.getMessage());
                Toast.makeText(UploadActivity.this,""+e,Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadActivity.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(UploadActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(UploadActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            return serverResponseCode;
        }

    }

    public void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose file to upload"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) return;

                Uri SelectedFileUri = data.getData();
                Log.e("Data", SelectedFileUri.toString());
                selectedFilePath = FilePath.getPath(this, SelectedFileUri);
                Log.e("Selected File Path =", selectedFilePath);

                if (selectedFilePath != null && selectedFilePath != "") {
                    tvFileName.setText(selectedFilePath);
                   // edtFileName.setText(selectedFilePath);
                } else {
                    Toast.makeText(this, "Can not upload file to server", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    private void requestStoragePermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
            return;
        }
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission Required");
                builder.setMessage("The permission is needed to browse files");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
