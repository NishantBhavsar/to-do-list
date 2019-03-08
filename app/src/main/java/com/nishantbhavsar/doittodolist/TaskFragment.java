package com.nishantbhavsar.doittodolist;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TaskFragment extends Fragment {

    DatabaseHandler dbHandle;
    Task task;
    ArrayList<Task> taskList = new ArrayList<Task>();
    ArrayList<Task> originalList = new ArrayList<Task>();
    String whichlist;
    ListView lv;
    TasksAdapter adapter;
    ImageView iv ;
    TextView tv;


    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("#####################  on create fragment ##################");
        super.onCreate(savedInstanceState);
        whichlist = getArguments().getString("whichlist");
        dbHandle = DatabaseHandler.getInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        iv = (ImageView)rootView.findViewById(R.id.img_view);
        tv = (TextView)  rootView.findViewById(R.id.message_view);

        if(whichlist.equals("To Do Lists")){
            taskList = dbHandle.getAllTasks();
        }
        else if(whichlist.equals("Today")){
            taskList = dbHandle.getTodayTasks();
        }
        else if(whichlist.equals("Completed")){
            taskList = dbHandle.getStatusTasks("yes");
        }
        else if(whichlist.equals("UnComplete")){
            taskList = dbHandle.getStatusTasks("no");
        }
        else{
            taskList = dbHandle.getCategoryTasks(whichlist);
        }

        if(!taskList.isEmpty()) {
            System.out.println("#################### original list ###########");
            Collections.reverse(taskList);
            originalList.clear();
            originalList.addAll(taskList);
            adapter = new TasksAdapter(getActivity(), taskList);
            lv = (ListView) rootView.findViewById(R.id.tasks_list);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override public void onItemClick(AdapterView<?> arg, View view,int position, long arg3)
                {
                    //Toast.makeText(getContext(), "Stop Clicking me", Toast.LENGTH_SHORT).show();
                    Task temp = (Task) arg.getItemAtPosition(position);
                    if(temp.get_status().equals("no")){
                        temp.set_status("yes");
                        if(whichlist.equals("UnComplete")){
                            taskList.remove(temp);
                        }
                        //TextView tv = (TextView) view.findViewById(R.id.show_task);
                        //tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else {
                        temp.set_status("no");
                        if(whichlist.equals("Completed")){
                            taskList.remove(temp);
                        }
                        //TextView tv = (TextView) view.findViewById(R.id.show_task);
                        //tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    int response =dbHandle.updateTask(temp);
                    adapter.notifyDataSetChanged();
                }
            });

            //TextView tv = (TextView) rootView.findViewById(R.id.show_message);
            //tv.setText("has some data   " +whichlist );
            System.out.println("has some data");
        }
        else{
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
        }
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!originalList.isEmpty()) {
                    ArrayList<Task> templist = adapter.filter(originalList, s);
                    taskList.clear();
                    taskList.addAll(templist);
                    adapter.notifyDataSetChanged();
                    if(taskList.isEmpty()){
                        iv.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.VISIBLE);
                    }
                    else{
                        iv.setVisibility(View.GONE);
                        tv.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //String whichlist =  getArguments().getString("whichlist");
        //TextView textView = (TextView) getView().findViewById(R.id.show_data);
        //textView.setText(whichlist);

    }


/*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
*/

}
