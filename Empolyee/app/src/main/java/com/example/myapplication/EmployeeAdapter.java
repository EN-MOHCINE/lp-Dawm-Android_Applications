package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter<Employee> {
    private Context context;
    private List<Employee> employees;

    public EmployeeAdapter(Context context, List<Employee> employees) {
        super(context, 0, employees);
        this.context = context;
        this.employees = employees;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Employee employee = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_employee, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView departmentTextView = convertView.findViewById(R.id.departmentTextView);
        TextView salaryTextView = convertView.findViewById(R.id.salaryTextView);

        // Populate the data into the template view using the data object
        nameTextView.setText(employee.getName());
        departmentTextView.setText(employee.getDepartment());
        salaryTextView.setText(String.valueOf(employee.getSalary()));

        return convertView;
    }

}
