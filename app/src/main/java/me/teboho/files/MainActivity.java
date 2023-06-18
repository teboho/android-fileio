package me.teboho.files;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import me.teboho.files.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private Button btnCreateFile, btnReadFile, btnUpdateFile, btnDeleteFile;
    private EditText etFileName, etFileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // binding
         binding = ActivityMainBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

        // buttons
        btnCreateFile = binding.btnCreateFile;
        btnReadFile = binding.btnReadFile;
        btnUpdateFile = binding.btnUpdateFile;
        btnDeleteFile = binding.btnDeleteFile;

        // edit text
        etFileName = binding.etFileName;
        etFileContent = binding.etFileContent;

        btnCreateFile.setOnClickListener(v -> createFile());
        btnReadFile.setOnClickListener(v -> readFile());
        btnUpdateFile.setOnClickListener(v -> updateFile());
        btnDeleteFile.setOnClickListener(v -> deleteFile());
    }

    // create file
    private void createFile() {
        // Getting access to the internal file directory
        String filename = etFileName.getText().toString().trim().endsWith(".txt") ? etFileName.getText().toString().trim() : etFileName.getText().toString().trim().concat(".txt");
        try (FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);) {
            fos.write(etFileContent.getText().toString().getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ex) {
            Log.e(TAG, "Error writing file to internal storage");
        }
    }

    // read file
    private void readFile() {
        // code
        if (etFileName.getText().toString().trim().isEmpty()) {
            etFileName.setError("Please enter a file name");
            return;
        }
        String filename = etFileName.getText().toString().trim().endsWith(".txt") ? etFileName.getText().toString().trim() : etFileName.getText().toString().trim().concat(".txt");
        try (FileInputStream fis = openFileInput(filename)){
            Scanner scanner = new Scanner(fis);
            scanner.useDelimiter("\\Z");
            while (scanner.hasNext()) {
                etFileContent.append(scanner.nextLine().concat("\n"));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ex) {
            Log.e(TAG, "Error writing file to internal storage");
        }
    }

    // update file
    private void updateFile() {
        // code
    }

    // delete file
    private void deleteFile() {
        // code
    }
}