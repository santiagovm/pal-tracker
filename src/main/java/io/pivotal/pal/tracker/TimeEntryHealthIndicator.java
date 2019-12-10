package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {

    public TimeEntryHealthIndicator(ITimeEntryRepository timeEntryRepo) {
        _timeEntryRepo = timeEntryRepo;
    }

    @Override
    public Health health() {

        Health.Builder builder = new Health.Builder();

        int MAX_TIME_ENTRIES = 5;

        if (_timeEntryRepo.list().size() < MAX_TIME_ENTRIES) {
            builder.up();
        } else {
            builder.down();
        }

        return builder.build();
    }

    private ITimeEntryRepository _timeEntryRepo;
}
