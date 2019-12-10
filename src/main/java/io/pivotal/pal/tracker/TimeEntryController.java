package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    public TimeEntryController(ITimeEntryRepository timeEntryRepo, MeterRegistry meterRegistry) {
        _timeEntryRepo = timeEntryRepo;

        _timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        _actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry timeEntryCreated = _timeEntryRepo.create(timeEntryToCreate);

        _actionCounter.increment();
        _timeEntrySummary.record(_timeEntryRepo.list().size());

        return new ResponseEntity<>(timeEntryCreated, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {

        TimeEntry timeEntry = _timeEntryRepo.find(id);

        if (timeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        _actionCounter.increment();

        return new ResponseEntity<>(timeEntry, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {

        _actionCounter.increment();
        List<TimeEntry> timeEntries = _timeEntryRepo.list();
        return new ResponseEntity<>(timeEntries, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable long id, @RequestBody TimeEntry timeEntryToUpdate) {

        TimeEntry updatedTimeEntry = _timeEntryRepo.update(id, timeEntryToUpdate);

        if (updatedTimeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        _actionCounter.increment();

        return new ResponseEntity<>(updatedTimeEntry, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {

        _timeEntryRepo.delete(id);

        _actionCounter.increment();
        _timeEntrySummary.record(_timeEntryRepo.list().size());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ITimeEntryRepository _timeEntryRepo;
    private final DistributionSummary _timeEntrySummary;
    private final Counter _actionCounter;
}
