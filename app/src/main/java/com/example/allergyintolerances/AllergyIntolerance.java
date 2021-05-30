package com.example.allergyintolerances;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.ShareCompat;
import androidx.core.view.KeyEventDispatcher;
import androidx.core.view.MenuItemCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;



import java.util.ArrayList;

import static com.example.allergyintolerances.R.id.addNew;
import static com.example.allergyintolerances.R.id.logout;
import static com.example.allergyintolerances.R.id.search_bar;



public class AllergyIntolerance extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private FirebaseUser user;
    private RecyclerView mRecyclerView;
    private ArrayList<AllergyItem> mItemList;
    private AllergyItemAdapter mAdapter;
    private int gridNumber = 1;

    private int queryLimit=10;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private SharedPreferences preferences;

    private AlarmManager mAlarm;
    private JobScheduler mJob;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy_intolerance);

        user=FirebaseAuth.getInstance().getCurrentUser();

        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,gridNumber));
        mItemList = new ArrayList<>();
        mAdapter=new AllergyItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems=mFirestore.collection("Items");

        queryData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //setAlarmManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJob = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            setJobSceduler();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setJobSceduler() {
        int networkType = JobInfo.NETWORK_TYPE_UNMETERED;
        int hardDeadLine = 5000;
        ComponentName name = new ComponentName(getPackageName(), NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(0, name).setRequiredNetworkType(networkType).setRequiresCharging(true).setOverrideDeadline(hardDeadLine);
        mJob.schedule(builder.build());
    }

    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == null){
                return;
            }

            switch (action){
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit=10;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit=5;
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }


    private void queryData() {
        mItemList.clear();

        mItems.orderBy("patients").limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                AllergyItem item = document.toObject(AllergyItem.class);
                item.setId(document.getId());
                mItemList.add(item);
            }

            if(mItemList.size()==0){
                initializeData();
                queryData();
            }
            mAdapter.notifyDataSetChanged();
        });

    }

    public void deleteItem(AllergyItem item){
        DocumentReference ref = mItems.document(item._getId());
        ref.delete().addOnSuccessListener(succes -> {

        }).addOnSuccessListener(failure-> {
            Toast.makeText(AllergyIntolerance.this, "Item " + item._getId()+ " cannot be deleted!", Toast.LENGTH_SHORT).show();
        });
        queryData();
    }

    public void updateItem(AllergyItem item){
        String type = item.getType();
        switch (type){
            case "allergia":
                item.setType("intolerancia");
                break;
            case "intolerancia":
                item.setType("gyógyszerallergia");
                break;
            case "gyógyszerallergia":
                item.setType("allergia");
                break;
        }
        mAdapter.notifyDataSetChanged();
    }


    private void initializeData() {
        String[] itemList = getResources().getStringArray(R.array.allergy_item_patient);
        String[] itemManifestation= getResources().getStringArray(R.array.allergy_item_manifest);
        String[] itemType= getResources().getStringArray(R.array.allergy_item_type);
        String[] itemSubstance= getResources().getStringArray(R.array.allergy_item_substance);

       // mItemList.clear();

        for(int i =0; i < itemList.length; i++){
            mItems.add(new AllergyItem(itemList[i], itemManifestation[i], itemType[i], itemSubstance[i])); // lehet autista vagyok, de nem értem mért nem tölti fel
                                                                                                           // ha csak az mItemList-et töltöm fel akkor működik -.-

            //mItemList.add(new AllergyItem(itemList[i], itemManifestation[i], itemType[i], itemSubstance[i]));
        }

        //mAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.allergy_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case addNew:
                addNewPatient();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }



    public void addNewPatient() {
        Intent intent = new Intent(this, addPatient.class);
        startActivity(intent);
    }

    private void setAlarmManager(){
        long repetinterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerTime= SystemClock.elapsedRealtime() + repetinterval;
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,repetinterval,pIntent);
        //mAlarm.cancel(pIntent);
    }




    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }



}