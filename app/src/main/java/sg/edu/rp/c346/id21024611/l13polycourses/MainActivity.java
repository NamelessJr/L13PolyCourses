package sg.edu.rp.c346.id21024611.l13polycourses;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {
    ListView lv;
    AsyncHttpClient client;
    Spinner spnYear;
    ArrayList<String> alYear;
    ArrayAdapter<String> aaYear;
    ToggleButton tbCourse, tbIntake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.listView);
        spnYear = findViewById(R.id.spinnerYear);
        tbCourse = findViewById(R.id.toggleButtonCourse);
        tbIntake = findViewById(R.id.toggleButtonIntake);
        client = new AsyncHttpClient();



    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Polytechnic> al = new ArrayList<>();

        client.get("https://data.gov.sg/api/action/datastore_search?resource_id=f358cf0d-61fa-4eeb-93a2-1eca971cf1a4&limit=60", new JsonHttpResponseHandler() {

            String year;
            String gender;
            String course;
            String intake;
            String enrollment;
            String graduates;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject resultObj = response.getJSONObject("result");
                    JSONArray jsonArrRecords = resultObj.getJSONArray("records");
                    for(int i = 0; i < jsonArrRecords.length(); i++) {
                        JSONObject jsonObjRecords = jsonArrRecords.getJSONObject(i);
                        year = jsonObjRecords.getString("year");
                        gender = jsonObjRecords.getString("sex");
                        course = jsonObjRecords.getString("course");
                        intake = jsonObjRecords.getString("intake");
                        enrollment = jsonObjRecords.getString("enrolment");
                        graduates = jsonObjRecords.getString("graduates");
                        Polytechnic poly = new Polytechnic(year, gender, course, intake, enrollment, graduates);
                        al.add(poly);

                    }
                }
                catch(JSONException e){

                }

                //POINT X – Code to display List View
                CustomAdapter aa = new CustomAdapter(MainActivity.this, R.layout.row, al);
                lv.setAdapter(aa);

                alYear = getDistinctYears(al);
                alYear.add(0,"Filter by year");
                aaYear = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_spinner_item,alYear);
                spnYear.setAdapter(aaYear);

                ArrayList<Polytechnic> originalAl = new ArrayList<>();
                originalAl.addAll(al);

                tbCourse.setChecked(true);
                Collections.sort(al,Polytechnic.courseComparatorAsc);
                aa.notifyDataSetChanged();
                formatButtons(50,"#999999",255,"#FF000000");

                tbCourse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        formatButtons(50,"#999999",255,"#FF000000");

                        if (tbCourse.isChecked()) { // sort asc order
                            Collections.sort(al,Polytechnic.courseComparatorAsc);
                        }
                        else { //sort desc order
                            Collections.sort(al,Polytechnic.courseComparatorDesc);
                        }

                        // filter year portion
                        filterYear(originalAl, aa);

                    }
                });

                tbIntake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        formatButtons(255,"#FF000000",50,"#999999");

                        if(tbIntake.isChecked()){ // sort highest
                            Collections.sort(al,Polytechnic.intakeComparatorHighest);
                        } else{
                            Collections.sort(al,Polytechnic.intakeComparatorLowest);
                        }
                        filterYear(originalAl, aa);
                    }
                });

                spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        al.clear();
                        al.addAll(originalAl);
                        filterYear(originalAl, aa);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });



            }//end onSuccess

            private void formatButtons(int intakeOpacity,String intakeColor, int courseOpacity, String courseColor) {
                tbIntake.getBackground().setAlpha(intakeOpacity);
                tbIntake.setTextColor(Color.parseColor(intakeColor));
                tbCourse.getBackground().setAlpha(courseOpacity);
                tbCourse.setTextColor(Color.parseColor(courseColor));
            }

            private void filterYear(ArrayList<Polytechnic> originalAl, CustomAdapter aa) {


                String selectedYear = spnYear.getSelectedItem().toString();
                if(selectedYear.equals("Filter by year")) {
                    //do nothing
                } else if (selectedYear.equals("Remove filter")){
                    alYear.set(0, "Filter by year");
                    aaYear.notifyDataSetChanged();
                    al.clear();
                    al.addAll(originalAl);
                } else {
                    alYear.set(0, "Remove filter");
                    ArrayList<Polytechnic> notSelected = new ArrayList<>();
                    for (Polytechnic item : al){
                        if (!item.getYear().equals(selectedYear)){
                            notSelected.add(item);
                        }
                    }
                    al.removeAll(notSelected);
                }
                aa.notifyDataSetChanged();
            }
        });
    }//end onResume

    public static ArrayList<String> getDistinctYears(ArrayList<Polytechnic> polyList) {
        ArrayList<String> yearList = new ArrayList<>();
        for (Polytechnic item : polyList) {
            String year = item.getYear();
            if (!yearList.contains(year)) {
                yearList.add(year);
            }
        }
        return yearList;

    }

}