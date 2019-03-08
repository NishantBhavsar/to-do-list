package com.nishantbhavsar.doittodolist;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by admin on 11/8/2017.
 */

public class TasksAdapter extends ArrayAdapter<Task> implements Filterable {

    private static ArrayList<Task> listTasks;
    private LayoutInflater mInflater;
    ArrayList<Boolean> positionArray;

    public TasksAdapter(Context context, ArrayList<Task> listTask){
        super(context, 0, listTask);

        listTasks = listTask;
        mInflater = LayoutInflater.from(context);

        positionArray = new ArrayList<Boolean>(listTask.size());
        for(int i =0;i<listTask.size();i++){
            positionArray.add(false);
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        Task currentTask = getItem(position);
        View listTaskView = convertView;

        if(listTaskView == null){
            listTaskView = mInflater.inflate(R.layout.task_list_item, null);
            holder = new Holder();
            holder.task_view = (TextView) listTaskView.findViewById(R.id.show_task);
            holder.date_view = (TextView) listTaskView.findViewById(R.id.show_date);
            holder.time_view = (TextView) listTaskView.findViewById(R.id.show_time);
            holder.category_view = (TextView) listTaskView.findViewById(R.id.category_view);
            holder.status_checkbox_view = (CheckBox) listTaskView.findViewById(R.id.status_checkbox);

            listTaskView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
            holder.status_checkbox_view.setOnCheckedChangeListener(null);
        }

        if(!currentTask.get_category().equals("Uncategorized")){
            holder.category_view.setText(currentTask.get_category());
        }
        else{
            holder.category_view.setText("");
        }

        //TextView task_view = (TextView) listTaskView.findViewById(R.id.show_task);
        holder.task_view.setText(currentTask.get_task());

        //TextView id_view = (TextView) listTaskView.findViewById(R.id.database_id);
        //id_view.setText(String.valueOf(currentTask.get_id()));

        //TextView date_view = (TextView) listTaskView.findViewById(R.id.show_date);
        //TextView time_view = (TextView) listTaskView.findViewById(R.id.show_time);
        holder.status_checkbox_view.setFocusable(false);

        if(!currentTask.get_date().equals("")){
            Calendar myCalendar = Calendar.getInstance();
            String myFormat = "dd/MM/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String today = sdf.format(myCalendar.getTime());
            String taskDate = currentTask.get_date();

            if(taskDate.equals(today)){
                holder.date_view.setText("Today");
            }
            else if(taskDate.substring(2).equals(today.substring(2))){
                int taskday =Integer.parseInt(taskDate.substring(0,2));
                int todayDay = Integer.parseInt(today.substring(0,2));
                if(todayDay+1 == taskday){
                    holder.date_view.setText("Tomorrow");
                }
                else if(todayDay-1 == taskday){
                    holder.date_view.setText("Yesterday");
                }
                else {
                    holder.date_view.setText(taskDate);
                }
            }
            else {
                holder.date_view.setText(taskDate);
            }
        }
        else{
            holder.date_view.setText("");
        }
        if(!currentTask.get_time().equals("")){
            holder.time_view.setText(currentTask.get_time());
        }
        else {
            holder.time_view.setText("");
        }

        if(currentTask.get_status().equals("yes")){
            positionArray.set(position, true);
            //holder.status_checkbox_view.setChecked(true);
        }
        if(currentTask.get_status().equals("no")){
            positionArray.set(position, false);
            //holder.status_checkbox_view.setChecked(true);
        }


        if(positionArray.get(position)){
            holder.task_view.setPaintFlags(holder.task_view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.task_view.setPaintFlags(holder.task_view.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

       // CheckBox status_checkbox_view = (CheckBox) listTaskView.findViewById(R.id.status_checkbox);
        holder.status_checkbox_view.setChecked(positionArray.get(position));

        holder.status_checkbox_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked ){
                    //System.out.println(position+"--- :)");
                    positionArray.set(position, true);

                }else
                    positionArray.set(position, false);
            }
        });



        LinearLayout taskList_view = (LinearLayout) listTaskView.findViewById(R.id.tasklist_container);

        taskList_view.setBackgroundResource(R.drawable.task_shape);

        return listTaskView;
    }

    static class Holder{
        TextView task_view;
        TextView date_view;
        TextView time_view;
        TextView category_view;
        CheckBox status_checkbox_view;
    }

    /*

    Override getItem method of adapter and return   private static ArrayList<Task> listTasks for this to work

    @NonNull
    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Task> tempList = new ArrayList<Task>();

            String constr = constraint.toString().toLowerCase();
            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if( constraint != null  && listTasks!=null) {
                int length = listTasks.size();
                int i = 0;
                System.out.println("######################################################################################################################################################");
                while(i<length){
                    Task item = listTasks.get(i);
                    //do whatever you wanna do here
                    //adding result set output array
                    String temp = item.get_task().toLowerCase();
                    //item.set_task(constr);
                    if(temp.contains(constr)){
                        System.out.println("********************************************************************************************************************");
                        tempList.add(item);
                    }

                    i++;
                }
                //following two lines is very important
                //as publish result can only take FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listTasks.clear();
            listTasks = (ArrayList<Task>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };
    */

    public ArrayList<Task>  filter (ArrayList<Task> tasklist, String constraint){

        ArrayList<Task> tempList = new ArrayList<Task>();
        String constr = constraint.toLowerCase();

        if( constraint.length() != 0  && (!tasklist.isEmpty())) {
            int length = tasklist.size();
            int i = 0;
            System.out.println("######################################################################################################################################################");
            while (i < length) {
                Task item = tasklist.get(i);
                String temp = item.get_task().toLowerCase();
                if (temp.contains(constr)) {
                    System.out.println("********************************************************************************************************************");
                    tempList.add(item);
                }
                i++;
            }
        }
        else {
            tempList.addAll(tasklist);
        }

        return tempList;
    }

}
