package ru.startandroid.management;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.sql.SQLException;
import java.util.ArrayList;

import ru.startandroid.management.Classes.Invent;

public class Add extends AppCompatActivity {
    EditText add_name;
    EditText invent_number;
    MaterialBetterSpinner add_object_;
    EditText add_date;
    EditText amongtime;
    MaterialBetterSpinner add_corpus;
    MaterialBetterSpinner add_cabinet_;
    MaterialBetterSpinner add_condition_;
    MaterialBetterSpinner add_status_;
    DatabaseHelper2 sqlHelper;
    Cursor userCursor;
    Button buttonAdd;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        add_name = (EditText)findViewById(R.id.edit_name);
        invent_number = (EditText)findViewById(R.id.invent_add_number);
        add_date = (EditText)findViewById(R.id.date_add);
        amongtime = (EditText)findViewById(R.id.amond_add_date);
        add_object_ = (MaterialBetterSpinner) findViewById(R.id.add_object);
        add_cabinet_ = (MaterialBetterSpinner)findViewById(R.id.add_cabinet);
        add_condition_ = (MaterialBetterSpinner)findViewById(R.id.add_condition);
        add_status_ = (MaterialBetterSpinner)findViewById(R.id.add_status);
        add_corpus = (MaterialBetterSpinner)findViewById(R.id.corpus);
        buttonAdd = (Button)findViewById(R.id.button_add);
        delete = (Button)findViewById(R.id.button_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_condition_.setText("");
                add_status_.setText("");
                add_cabinet_.setText("");
                add_object_.setText("");
                add_date.setText("");
                add_name.setText("");
                add_corpus.setText("");
                invent_number.setText("");
                amongtime.setText("");
            }
        });
        sqlHelper = new DatabaseHelper2(getApplicationContext());

        //add obj
        ArrayAdapter<String> objAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, addItems("obj_name","objects"));
        add_object_.setAdapter(objAdapter);
        //add_obj
        ArrayAdapter<String> cabinetAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, addItems("cab_number","cabinet"));
        add_cabinet_.setAdapter(cabinetAdapter);
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, addItems("cond_name","Condition"));
        add_condition_.setAdapter(conditionAdapter);
        ArrayAdapter<String> corpusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, addItems("korp_name","korpus"));
        add_corpus.setAdapter(corpusAdapter);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, addItems("stat_name","status"));
        add_status_.setAdapter(statusAdapter);
        add_cabinet_.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (add_corpus.getText().toString().matches("")){
                    add_cabinet_.setText("");
                    Toast.makeText(Add.this, "Выберите сначала корпус!", Toast.LENGTH_LONG).show();
                }
            }
        });
        add_corpus.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(!add_corpus.getText().toString().matches("")){
                    ArrayAdapter<String> cabinetAdapter = new ArrayAdapter<>(Add.this,
                            android.R.layout.simple_dropdown_item_1line, addItems("cab_number",
                            "cabinet inner join korpus on cabinet.incorp = korpus.korp_id where korpus.korp_name = '"+add_corpus.getText().toString()+"'"));
                    add_cabinet_.setAdapter(cabinetAdapter);
                }else{
                    ArrayAdapter<String> cabinetAdapter = new ArrayAdapter<>(Add.this,
                            android.R.layout.simple_dropdown_item_1line, addItems("cab_number","cabinet"));
                    add_cabinet_.setAdapter(cabinetAdapter);
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_name.getText().toString().matches("") ||
                        add_cabinet_.getText().toString().matches("") ||
                        add_condition_.getText().toString().matches("") ||
                        add_status_.getText().toString().matches("") ||
                        add_date.getText().toString().matches("") ||
                        add_object_.getText().toString().matches("") ||
                        invent_number.getText().toString().matches("") ||
                        amongtime.getText().toString().matches("")){
                    Toast.makeText(Add.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                    return;


                }
                Log.d("FUN",invent_number.getText().toString());
                userCursor = sqlHelper.database.rawQuery("select  obj_id from  objects where obj_name = '" + add_object_.getText()+"'", null);
                int obj_id = 0;
                if (userCursor.moveToFirst()) {
                    obj_id= userCursor.getInt(userCursor.getColumnIndexOrThrow("obj_id"));
                }
                userCursor = sqlHelper.database.rawQuery("select cab_id from  Cabinet where cab_number = '" + add_cabinet_.getText()+"'", null);
                int room_id = 0;
                if (userCursor.moveToFirst()) {
                    room_id = userCursor.getInt(userCursor.getColumnIndexOrThrow("cab_id"));
                }
                userCursor = sqlHelper.database.rawQuery("select stat_id from status where stat_name = '" + add_status_.getText()+"'", null);
                int stat_id = 0;
                if (userCursor.moveToFirst()) {
                    stat_id = userCursor.getInt(userCursor.getColumnIndexOrThrow("stat_id"));
                }
                userCursor = sqlHelper.database.rawQuery("select cond_id from Condition where cond_name = '" + add_condition_.getText()+"'", null);
                int con_id = 0;
                if (userCursor.moveToFirst()) {
                    con_id = userCursor.getInt(userCursor.getColumnIndexOrThrow("cond_id"));
                }
                if (room_id == 0 || stat_id == 0 || con_id == 0 || con_id == 0){
                    Toast.makeText(Add.this, "Заполняйте правильно", Toast.LENGTH_SHORT).show();
                }
                sqlHelper.database.execSQL("insert into inventories (serial_numb,fullname,obj_id,uchet_date,amor_gr,room_id,stat_id,con_id)" +
                            " values("+"'"+invent_number.getText()+"'"+","
                            +"'"+add_name.getText()+"'"+","
                            +obj_id+","
                            +"'"+add_date.getText().toString()+"',"
                            +amongtime.getText()+","
                            +room_id+","
                            +stat_id+","
                            +con_id+");"
                );
                Toast.makeText(Add.this, "Добавлено успешно!", Toast.LENGTH_SHORT).show();
                buttonAdd.setOnClickListener(null);
                buttonAdd.setEnabled(false);
                Intent i = new Intent(Add.this,Inventory.class);
                startActivity(i);

            }
        });
    }
    public ArrayList<String> addItems(String name, String table){

        ArrayList<String> object_items = new ArrayList<>();
        try {
            sqlHelper.open();
            userCursor = sqlHelper.database.rawQuery("select " + name + " from  "  + table +  ";", null);
            if (userCursor.getCount() > 0)
            {
                userCursor.moveToFirst();
                do {
                    object_items.add( userCursor.getString(userCursor.getColumnIndexOrThrow(name)));
                } while (userCursor.moveToNext());
                userCursor.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  object_items;

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}