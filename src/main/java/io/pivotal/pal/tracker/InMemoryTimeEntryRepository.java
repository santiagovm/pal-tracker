package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements ITimeEntryRepository {

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        Long id = _nextId;
        _nextId += 1;

        TimeEntry newTimeEntry = new TimeEntry(
                id,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours()
        );

        _timeEntries.put(id, newTimeEntry);

        return newTimeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return _timeEntries.get(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(_timeEntries.values());
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        if (find(timeEntryId) == null) return null;

        TimeEntry newEntry = new TimeEntry(
                timeEntryId,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours()
        );

        _timeEntries.replace(timeEntryId, newEntry);

        return newEntry;
    }

    @Override
    public void delete(long timeEntryId) {
        _timeEntries.remove(timeEntryId);
    }

    private HashMap<Long, TimeEntry> _timeEntries = new HashMap<>();
    private Long _nextId = 1L;
}
