package com.sirma;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sirma.adapters.EmployeesAdapter;
import com.sirma.io.FileIO;
import com.sirma.io.FileIOImpl;
import com.sirma.model.Team;
import com.sirma.repository.EmployeeRepository;
import com.sirma.repository.EmployeeRepositoryImpl;
import com.sirma.services.EmployeeService;
import com.sirma.services.EmployeeServiceImpl;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final String TAG = "MainActivity";
    private String mSelectedFilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button choose_fileBtn = findViewById(R.id.choose_fileBtn);
        choose_fileBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                myOpenImagePicker();
            }
        });
    }


    @SuppressLint("InlinedApi")
    public void myOpenImagePicker() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("text/plain");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    FILE_SELECT_CODE);


        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            startActivityForResult(intent, FILE_SELECT_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:

                if (resultCode == RESULT_OK && data != null) {

                    Uri originalUri = data.getData();
                    mSelectedFilePath = FileUtils.getPath(
                            getApplicationContext(), originalUri);
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        if (checkPermission()) {
                            calculateLongestPeriodTeam(mSelectedFilePath);
                        } else {
                            requestPermission();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void calculateLongestPeriodTeam(String selectedFilePath) {
        FileIO fileIO = new FileIOImpl();

        EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
        EmployeeService emplService = new EmployeeServiceImpl(employeeRepository);

        Engine engine = new Engine(selectedFilePath, fileIO, emplService);

        ArrayList<Team> teamList = (ArrayList<Team>) engine.run();
        changesUI(teamList);
    }

    private void changesUI(ArrayList<Team> teamList) {
        if (!teamList.isEmpty()) {
            Button choose_fileBtn = findViewById(R.id.choose_fileBtn);
            choose_fileBtn.setVisibility(View.GONE);

            ListView employeesListView = (ListView) findViewById(R.id.employeesListView);
            employeesListView.setVisibility(View.VISIBLE);
            androidx.constraintlayout.widget.ConstraintLayout columnView = (androidx.constraintlayout.widget.ConstraintLayout) findViewById(R.id.employeesColumns);
            columnView.setVisibility(View.VISIBLE);

            EmployeesAdapter adapter = new EmployeesAdapter(this, teamList);
            employeesListView.setAdapter(adapter);

            Team bestTeam = teamList.get(0);

            String result = getString(R.string.best_team, bestTeam.getFirstEmployeeId(),
                    bestTeam.getSecondEmployeeId(), bestTeam.getTotalDuration());
            Log.d(TAG, result);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this,
                    "Write External Storage permission allows us to read  files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                   
                    if (mSelectedFilePath != null) {
                        calculateLongestPeriodTeam(mSelectedFilePath);
                    }else {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}