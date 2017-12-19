package com.inostudio.taskmanager;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lstTask;
    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBox=(CheckBox)findViewById(R.id.checkBoxBold);
        dbHelper=new DbHelper(this);
        lstTask =(ListView)findViewById(R.id.lstTask);

        loadTaskList();

    }


    private void loadTaskList() {

        ArrayList<String> taskList=dbHelper.getTaskList();
        if(mAdapter==null){
            mAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.task_title,taskList);
            lstTask.setAdapter(mAdapter);
        }else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        Drawable icon =menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_task:
                    final EditText taskEditText = new EditText(this);
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("Добавление новой задачи")
                            .setMessage(null)
                            .setView(taskEditText)
                            .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (taskEditText.getText().toString().equals("")) {
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Пожалуйста введите вашу задачу!\n" +
                                                        "Ваше поле было пустое!",
                                                Toast.LENGTH_LONG);
                                        toast.show();
                                    } else {
                                        String task = taskEditText.getText().toString();
                                        dbHelper.insertNewTask(task,checkBox);
                                        loadTaskList();
                                    }

                                }

                            }).setNegativeButton("Закрыть", null)
                            .create();
                    dialog.show();
                    return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View view){

        View parent =(View)view.getParent();
        TextView taskTextView=(TextView)parent.findViewById(R.id.task_title);
        String task =String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();

    }
}
