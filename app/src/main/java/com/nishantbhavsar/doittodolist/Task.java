package com.nishantbhavsar.doittodolist;

/**
 * Created by admin on 11/8/2017.
 */

public class Task {

    //private variables
    int _id;
    String _task;
    String _status;
    String _category;
    String _date;
    String _time;
    String _description;

    // Empty constructor
    public Task(){

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_task() {
        return _task;
    }

    public void set_task(String _task) {
        this._task = _task;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }
}
