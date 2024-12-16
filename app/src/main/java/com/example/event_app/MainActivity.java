package com.example.event_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String localeTag = preferences.getString("locale", Locale.ENGLISH.toLanguageTag());
        setLocale(Locale.forLanguageTag(localeTag));


        String theme = preferences.getString("theme", "light");

        if (theme.equals("light")) {
            setTheme(R.style.Theme_Event_Light);
        } else if (theme.equals("dark")) {
            setTheme(R.style.Theme_Event_Dark);
        }

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        //Shared Pref
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //Orientations
        if(itemId == R.id.ukLang){
            Log.i("uk", "in");
            editor.putString("locale", Locale.forLanguageTag("UK").toLanguageTag());
            editor.apply();
            setLocale(new Locale("uk"));
            recreate();
        } else if (itemId == R.id.deLang) {
            Log.i("de", "in");
            editor.putString("locale", Locale.forLanguageTag("DE").toLanguageTag());
            editor.apply();
            setLocale(new Locale("de"));
            recreate();
        } else if (itemId == R.id.enLang) {
            Log.i("en", "in");
            editor.putString("locale", Locale.forLanguageTag("EN").toLanguageTag());
            editor.apply();
            setLocale(new Locale("en"));
            recreate();
        } else if (itemId == R.id.lightTheme) {
            saveThemePreference("light");
            setTheme(R.style.Theme_Event_Light);
            recreate();
        } else if (itemId == R.id.darkTheme) {
            saveThemePreference("dark");
            setTheme(R.style.Theme_Event_Dark);
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }
    private void setLocale(Locale locale){
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
    private void saveThemePreference(String theme) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("theme", theme);
        editor.apply();
    }




}