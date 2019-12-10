package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.Objects;

public class TimeEntry {

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public long getProjectId() {
        return _projectId;
    }

    public void setProjectId(long projectId) {
        _projectId = projectId;
    }

    public long getUserId() {
        return _userId;
    }

    public void setUserId(long userId) {
        _userId = userId;
    }

    public LocalDate getDate() {
        return _date;
    }

    public void setDate(LocalDate date) {
        _date = date;
    }

    public int getHours() {
        return _hours;
    }

    public void setHours(int hours) {
        _hours = hours;
    }

    public TimeEntry(long projectId, long userId, LocalDate date, int hours) {
        _id = -1;
        _projectId = projectId;
        _userId = userId;
        _date = date;
        _hours = hours;
    }

    public TimeEntry(long id, long projectId, long userId, LocalDate date, int hours) {
        _id = id;
        _projectId = projectId;
        _userId = userId;
        _date = date;
        _hours = hours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return _id == timeEntry._id &&
                _projectId == timeEntry._projectId &&
                _userId == timeEntry._userId &&
                _hours == timeEntry._hours &&
                Objects.equals(_date, timeEntry._date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _projectId, _userId, _date, _hours);
    }

    private long _id;
    private long _projectId;
    private long _userId;
    private LocalDate _date;
    private int _hours;
}
