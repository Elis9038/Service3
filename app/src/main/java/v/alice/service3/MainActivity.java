package v.alice.service3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private ListView lstView;
    private ArrayAdapter<String> adapter;
    private LocalService service;
    private List<String> primesList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstView = findViewById(R.id.lstView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, primesList);
        lstView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    public void onGo(View view) {
        service.start();
    }

    public void onClear(View view) {
        adapter.clear();
    }

    public void onWait(View view) {
        service.pause();
    }

    public void onService(View view) {
        adapter.add(String.format("%d: %d", service.getIndex(), service.getPrime()));
        Toast.makeText(this, "List is updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        if (service == null) service = ((LocalService.PrimesBinder) binder).getService();
        Toast.makeText(this, "Service connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
        Toast.makeText(this, "Service disconnected", Toast.LENGTH_SHORT).show();
    }
}
