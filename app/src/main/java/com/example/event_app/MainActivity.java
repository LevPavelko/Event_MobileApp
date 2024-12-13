package com.example.event_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_app.adapters.EventAdapter;
import com.example.event_app.data.DBHelper;
import com.example.event_app.data.Event;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        List<Event> list = new ArrayList<>();
        try (DBHelper helper = new DBHelper(this)) {
//            Event event = new Event();
//            event.setId(1);
//            event.setName("Coldplay");
//            event.setDate("24.03.2025");
//            event.setPlace("Wiener StadHalle");
//            event.setPhone("0996582306");
//
//            helper.insert(event);
            list = helper.selectAll();
        }



        eventAdapter = new EventAdapter(this, list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(eventAdapter);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
    }

    public final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResult result) -> {
                Intent data = result.getData();
                if (result.getResultCode() == RESULT_OK && data != null) {
                    int position = data.getIntExtra(EventActivity.POSITION, -1);
                    int mode = data.getIntExtra(EventActivity.MODE, -1);
                    Event event = data.getSerializableExtra(EventActivity.EVENT, Event.class);
                    if (event != null) {
                        if (mode == EventActivity.INSERT) {
                            eventAdapter.insertData(event);
                        }
                        if (mode == EventActivity.DETAILS) {
                            eventAdapter.updateData(position, event);
                        }
                    }
                }
            }
    );
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

}