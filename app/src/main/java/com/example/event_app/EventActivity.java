package com.example.event_app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.event_app.data.DBHelper;
import com.example.event_app.data.Event;
import com.example.event_app.databinding.ActivityEventBinding;

public class EventActivity extends AppCompatActivity {

    public static final int INSERT = 0;
    public static final int DETAILS = 1;
    public static final String MODE = "mode";
    public static final String POSITION = "position";
    public static final String EVENT = "event";

    private int mode;
    private Integer eventId = 0;

    private ActivityEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String theme = preferences.getString("theme", "light");

        if (theme.equals("light")) {
            setTheme(R.style.Theme_Event_Light);
        } else if (theme.equals("dark")) {
            setTheme(R.style.Theme_Event_Dark);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.callButton.setOnClickListener(v -> {
            phonePermission.launch(Manifest.permission.CALL_PHONE);
        });

        Intent intent = getIntent();
        mode = intent.getIntExtra(MODE, -1);
        if (mode == DETAILS){
            eventId = intent.getSerializableExtra(EVENT, Event.class).getId();
            //
            try (DBHelper helper = new DBHelper(this)) {
                Event event = helper.selectById(eventId);
                binding.nameText.setText(event.getName());
                binding.dateText.setText(event.getDate());
                binding.placeText.setText(event.getPlace());
                binding.phoneText.setText(event.getPhone());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String backLabel = getString(R.string.Back);
        MenuItem save = menu.add(backLabel);
        save.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        save.setOnMenuItemClickListener(item  -> {
            finish();
            return true;
        });
        return true;

    }

    final ActivityResultLauncher<String> phonePermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            (Boolean permitted) -> {
                if (permitted){
                    String phone = "tel:" + binding.phoneText.getText().toString();

                    Log.i("s", "call");
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(phone)));
                }
                else{
                    Toast.makeText(this, "App doesn't have accesses to phone calls.",
                            Toast.LENGTH_SHORT).show();
                }
            }
    );


}