package sg.edu.rp.c346.id21024611.l13polycourses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    Context parent_context;
    int layout_id;
    ArrayList<Polytechnic> polyList;

    public CustomAdapter(Context context, int resource, ArrayList<Polytechnic> objects) {
        super(context, resource, objects);

        parent_context = context;
        layout_id = resource;
        polyList = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtain the LayoutInflater object
        LayoutInflater inflater = (LayoutInflater)
                parent_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // "Inflate" the View for each row
        View rowView = inflater.inflate(layout_id, parent, false);

        // Obtain the UI components and do the necessary binding
        TextView tvCourse = rowView.findViewById(R.id.textViewCourse);
        TextView tvYear = rowView.findViewById(R.id.textViewYear);
        TextView tvIntake = rowView.findViewById(R.id.textViewIntake);
        TextView tvEnrollment = rowView.findViewById(R.id.textViewEnrollment);
        TextView tvGraduates = rowView.findViewById(R.id.textViewGraduates);
        ImageView ivGender = rowView.findViewById(R.id.imageViewGender);

        Polytechnic currentItem = polyList.get(position);
        tvCourse.setText(currentItem.getCourse());
        tvYear.setText(currentItem.getYear());
        tvIntake.setText(currentItem.getIntake());
        tvGraduates.setText(currentItem.getGraduates());
        tvEnrollment.setText(currentItem.getEnrollment());


        return rowView;

    }
}