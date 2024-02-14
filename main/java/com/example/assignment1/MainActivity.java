package com.example.assignment1;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
// AJ DEV
public class MainActivity extends AppCompatActivity {
    Database db;
    FloatingActionButton button;

    RecyclerView recyclerView;
    CustomAdapter adapter;

    ArrayList<Notes> NoteList;

    Notes note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new Database(this);
        button = findViewById(R.id.add);
        recyclerView = findViewById(R.id.rvview);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteList = new ArrayList<>();
        adapter = new CustomAdapter(this, NoteList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Notes items = NoteList.get(position);
                showDialog(items);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddshowDialog();

            }
        });
        showAll();
    }

    private void refresh() {
        showAll();
        adapter.notifyDataSetChanged();
    }

    private void showAll() {
        Cursor cursor = db.getAllRecord();
        NoteList.clear();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String note = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                NoteList.add(new Notes(id, title, note, date, time));
            }

        }
    }

    private void AddshowDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Adding Schedule");

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialoglayout, null);


        TextInputEditText titles, notes;
        TextView dates, times;


        titles = view.findViewById(R.id.titleText);
        notes = view.findViewById(R.id.NotesText);
        dates = view.findViewById(R.id.date);
        times = view.findViewById(R.id.time);

        times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {


                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        String DayNight;

                        if (hour < 12) {
                            DayNight = "AM";
                        } else {
                            DayNight = "PM";
                        }
                        int hourDay = hours % 12;
                        if (hourDay == 0) {
                            hourDay = 12;

                        }
                        String timeday = String.format("%02d:%02d%s", hourDay, minutes, DayNight);
                        times.setText(timeday);


                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }

            ;
        });

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar  = Calendar.getInstance();
                int years = calendar.get(Calendar.YEAR);
                int months = calendar.get(Calendar.MONTH);

                int days = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        //Showing the picked value in the textView
                        dates.setText(String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(day));

                    }
                }, years, months, days);

                datePickerDialog.show();

            }
        });


        dialog.setView(view);


        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = titles.getText().toString();
                String note = notes.getText().toString();
                String date = dates.getText().toString();
                String time = times.getText().toString();
                if(title.isEmpty() || note.isEmpty() || date.equals("Set Date") || time.equals("Set Time")){
                    Toast.makeText(MainActivity.this, "SET DATA", Toast.LENGTH_SHORT).show();
                }else {

                    db.AddRecord(title, note, date, time);
                    refresh();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }



    private void showDialog(Notes notelist){
        CharSequence[] charSequences = new CharSequence[] {"Edit","Delete"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);



        alertDialog.setTitle("Choose an Action");

        alertDialog.setItems(charSequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which == 0){
                    EditDialog(notelist);

                }else{
                    db.DeleteRecord(notelist.getId());
                    refresh();
                }
            }
        });
        alertDialog.show();
    }


    private void EditDialog(Notes noteList){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialoglayout,null);

        final TextInputEditText etTitle = view1.findViewById(R.id.titleText);
        final TextInputEditText etNotes = view1.findViewById(R.id.NotesText);
        final TextView dates = view1.findViewById(R.id.date);
        final TextView times = view1.findViewById(R.id.time);

        etTitle.setText(noteList.getTitle());
        etNotes.setText(noteList.getNotes());
        dates.setText(noteList.getDate());
        times.setText(noteList.getTime());

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar  = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);

                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog  datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dates.setText( String.valueOf(year) + "/"+ String.valueOf(month +1) + "/" + String.valueOf(day));
                    }
                },year,month,day);
                datePickerDialog.show();
            }

        });

        times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int time = calendar.get(Calendar.MINUTE);
                int hours = calendar.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                        String AMPM;
                        if(hour  < 12){
                            AMPM = "AM";
                        }else{
                            AMPM = "PM";

                        }


                        int hourday = hour % 12;
                        if(hourday == 0) {
                            hourday = 12;
                        }

                        String setTime = String.format("%02d:%02d%s",hourday,minute,AMPM);
                        times.setText(setTime);



                    }
                },time,hours,false);
                timePickerDialog.show();
            }
        });

        dialog.setTitle("EDIT NOTES");
        dialog.setView(view1);


        dialog.setPositiveButton("UPDATE",new DialogInterface.OnClickListener(){


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



                String newTitle = etTitle.getText().toString();
                String newNotes = etNotes.getText().toString();



                String newDate = dates.getText().toString();
                String newtime =  times.getText().toString();




                if(newTitle.isEmpty() || newNotes.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Title and Notes Cant be empty", Toast.LENGTH_SHORT).show();
                }else {
                    db.EditRecord(noteList.getId(), newTitle, newNotes, newDate, newtime);
                    refresh();
                }


            }
        });dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }



}
