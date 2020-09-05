package com.example.tjg.cahouseadmin.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.tjg.cahouseadmin.Adapters.DashboardAdapter;
import com.example.tjg.cahouseadmin.R;
import com.example.tjg.cahouseadmin.Storage.MyPreference;

public class DashboardActivity extends AppCompatActivity {


    GridView gvDashboard;
    MyPreference myPreference;
    String[] prgmNameList = {"Request Document", "Upload Document", "News", "Create Users", "Documents", "Change Password", "Forum", "Feedback"};
    int[] prgmImages = {
            R.drawable.requestdoc,
            R.drawable.uploaddoc,
            R.drawable.news,
            R.drawable.createuser,
            R.drawable.doc,
            R.drawable.changepassword,
            R.drawable.forums,
            R.drawable.feedback
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        myPreference = new MyPreference(this);
        gvDashboard = findViewById(R.id.gvDashboard);

        DashboardAdapter adapter = new DashboardAdapter(DashboardActivity.this, prgmNameList, prgmImages);

        gvDashboard.setAdapter(adapter);

        gvDashboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    startActivity(new Intent(DashboardActivity.this, RequestActivity.class));
                }

                if (position == 1) {
                    startActivity(new Intent(DashboardActivity.this, UploadActivity.class));
                }

                if (position == 2) {
                    startActivity(new Intent(DashboardActivity.this, NewsActivity.class));
                }

                if (position == 3) {
                    startActivity(new Intent(DashboardActivity.this, FetchClientsActivity.class));
                }

                if (position == 4) {
                    startActivity(new Intent(DashboardActivity.this, DocumentsActivity.class));
                }

                if (position == 5) {
                    startActivity(new Intent(DashboardActivity.this, ChangePasswordActivity.class));
                }

                if (position == 6){
                    startActivity(new Intent(DashboardActivity.this, ForumQuestionActivity.class));
                }

                if (position == 7){
                    startActivity(new Intent(DashboardActivity.this, FeedbackActivity.class));
                }

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnLogout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout?");
            builder.setMessage("Are you sure you want to logout?");
            builder.setIcon(R.drawable.icon);


            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myPreference.Logout();
                    finish();
                    startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit?");
        builder.setMessage("Do you really want to exit?");
        builder.setIcon(R.drawable.icon);


        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
