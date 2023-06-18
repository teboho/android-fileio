package me.teboho.files;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

import me.teboho.files.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private Button btnCreateFile, btnReadFile, btnUpdateFile, btnDeleteFile;
    private EditText etFileName, etFileContent;
    private FloatingActionButton fabListFiles;
    private FloatingActionButton fabClearInputs;

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
        fabListFiles = binding.fabListFiles;
        fabClearInputs = binding.fabClearInputs;

        // edit text
        etFileName = binding.etFileName;
        etFileContent = binding.etFileContent;

        btnCreateFile.setOnClickListener(v -> createFile());
        btnReadFile.setOnClickListener(v -> readFile());
        btnUpdateFile.setOnClickListener(v -> updateFile());
        btnDeleteFile.setOnClickListener(v -> deleteFile());
        fabListFiles.setOnClickListener(v -> showAllFiles());
        fabClearInputs.setOnClickListener(v -> {
            etFileName.setText("");
            etFileContent.setText("");
        });

        // Show all files in internal storage
        showAllFiles();

        // disappear keyboard when user clicks outside of edit text
        binding.getRoot().setOnClickListener(v -> hideKeyboard(v));
    }
    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void showAllFiles() {
        etFileContent.setText("");
        File[] files = getFilesDir().listFiles();
        for (File file : files) {
            Log.d(TAG, "showAllFiles: " + file.getName());
            etFileContent.append(file.getName().concat("\n"));
        }
        showSnackBar("Files listed successfully");
    }

    // create file
    private void createFile() {
        if (etFileName.getText().toString().trim().isEmpty()) {
            etFileName.setError("Please enter a file name");
            return;
        }
        // Getting access to the internal file directory
        String filename = etFileName.getText().toString().trim().endsWith(".txt") ? etFileName.getText().toString().trim() : etFileName.getText().toString().trim().concat(".txt");
        try (FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);) {
            fos.write(etFileContent.getText().toString().getBytes());
            showSnackBar("File created successfully");
        } catch (FileNotFoundException e) {
            showSnackBar("File not found");
            e.printStackTrace();
        } catch (IOException ex) {
            showSnackBar("Error writing file");
            Log.e(TAG, "Error writing file to internal storage");
        }
    }

    // read file
    private void readFile() {
        etFileContent.setText("");
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
            showSnackBar("File read successfully");
        } catch (FileNotFoundException e) {
            showSnackBar("File not found");
            e.printStackTrace();
        } catch (IOException ex) {
            showSnackBar("Error reading file");
            Log.e(TAG, "Error reading file from internal storage");
        }
    }

    // update file
    private void updateFile() {
        if (etFileName.getText().toString().trim().isEmpty()) {
            etFileName.setError("Please enter a file name");
            return;
        }
        if (etFileContent.getText().toString().trim().isEmpty()) {
            etFileContent.setError("Enter some update data");
            return;
        }
        String filename = etFileName.getText().toString().trim().endsWith(".txt") ? etFileName.getText().toString().trim() : etFileName.getText().toString().trim().concat(".txt");
        try (FileOutputStream fos = openFileOutput(filename, MODE_APPEND)) {
            fos.write("\n".getBytes());
            fos.write(etFileContent.getText().toString().getBytes());
            showSnackBar("File updated successfully");
        } catch (FileNotFoundException e) {
            showSnackBar("File not found");
            e.printStackTrace();
        } catch (IOException e1) {
            showSnackBar("Error updating file");
            e1.printStackTrace();
        }
    }

    // delete file
    private void deleteFile() {
        if (etFileName.getText().toString().trim().isEmpty()) {
            etFileName.setError("Please enter a file name");
            return;
        }
        String filename = etFileName.getText().toString().trim().endsWith(".txt") ? etFileName.getText().toString().trim() : etFileName.getText().toString().trim().concat(".txt");
        File files = getFilesDir();
        File[] matchingFiles = files.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.equals(filename))
                    return true;
                return false;
            }
        });
        File matchingFile = matchingFiles[0];
        if (matchingFile.exists()) {
            matchingFile.delete();
            showSnackBar("File deleted successfully");
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }
}