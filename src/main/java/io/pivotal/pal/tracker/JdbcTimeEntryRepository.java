package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements ITimeEntryRepository {

    public JdbcTimeEntryRepository(DataSource dataSource) {
        _jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        String INSERT_SQL = "insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)";

        _jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        String SELECT_SQL = "select id, project_id, user_id, date, hours from time_entries where id = ?";
        return _jdbcTemplate.query(SELECT_SQL, new Object[] { timeEntryId }, _extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return _jdbcTemplate.query("select id, project_id, user_id, date, hours from time_entries", _mapper);
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {

        String UPDATE_SQL = "update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?";

        _jdbcTemplate.update(UPDATE_SQL,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours(),
                timeEntryId);

        return find(timeEntryId);
    }

    @Override
    public void delete(long timeEntryId) {
        _jdbcTemplate.update("delete from time_entries where id = ?", timeEntryId);
    }

    private final RowMapper<TimeEntry> _mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> _extractor = (rs) -> rs.next()
            ? _mapper.mapRow(rs, 1)
            : null;

    private final JdbcTemplate _jdbcTemplate;
}
