package id.nusantara.events;

import id.nusantara.model.Era;
import id.nusantara.model.Island;
import id.nusantara.model.YearRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A dated episode. Events live in their own package rather than inside an
 * island's data file because they routinely involve more than one polity, and
 * sometimes more than one island -- no single geographic file can own them.
 *
 * <p>Participants are referenced by {@code Kingdom#getId()} so an event never
 * holds a hard reference into island data; the registry resolves ids on demand.
 */
public final class HistoricalEvent {

    private final String id;
    private final String name;
    private final YearRange when;
    private final EventType type;
    private final List<String> participantIds;
    private final List<Island> islands;
    private final String location;
    private final String outcome;
    private final String summary;
    private final List<Account> accounts;
    private final boolean contested;

    private HistoricalEvent(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.when = builder.when;
        this.type = builder.type;
        this.participantIds = Collections.unmodifiableList(new ArrayList<String>(builder.participantIds));
        this.islands = Collections.unmodifiableList(new ArrayList<Island>(builder.islands));
        this.location = builder.location;
        this.outcome = builder.outcome;
        this.summary = builder.summary;
        this.accounts = Collections.unmodifiableList(new ArrayList<Account>(builder.accounts));
        this.contested = builder.contested || builder.accounts.size() > 1;
    }

    public static Builder builder(String id, String name, EventType type) {
        return new Builder(id, name, type);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public YearRange getWhen() {
        return when;
    }

    public int getYear() {
        return when == null ? YearRange.UNKNOWN : when.getStart();
    }

    public EventType getType() {
        return type;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public String getLocation() {
        return location;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getSummary() {
        return summary;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    /** True when the sources disagree on what happened or why. */
    public boolean isContested() {
        return contested;
    }

    public Era getEra() {
        return when == null || !when.hasKnownStart() ? null : Era.of(when.getStart());
    }

    public boolean involves(String kingdomId) {
        return participantIds.contains(kingdomId);
    }

    public boolean spansMultipleIslands() {
        return islands.size() > 1;
    }

    /** Slider predicate: keep this event if it falls inside the visible window. */
    public boolean occurredBetween(int fromYear, int toYear) {
        return when != null && when.overlaps(new YearRange(fromYear, toYear));
    }

    @Override
    public String toString() {
        return when + " - " + name;
    }

    public static final class Builder {
        private final String id;
        private final String name;
        private final EventType type;
        private YearRange when;
        private final List<String> participantIds = new ArrayList<String>();
        private final List<Island> islands = new ArrayList<Island>();
        private String location;
        private String outcome;
        private String summary;
        private final List<Account> accounts = new ArrayList<Account>();
        private boolean contested;

        private Builder(String id, String name, EventType type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }

        public Builder year(int year) {
            this.when = YearRange.of(year);
            return this;
        }

        public Builder years(int from, int to) {
            this.when = new YearRange(from, to);
            return this;
        }

        public Builder when(YearRange when) {
            this.when = when;
            return this;
        }

        public Builder participants(String... kingdomIds) {
            for (String kingdomId : kingdomIds) {
                this.participantIds.add(kingdomId);
            }
            return this;
        }

        public Builder islands(Island... values) {
            for (Island island : values) {
                if (!this.islands.contains(island)) {
                    this.islands.add(island);
                }
            }
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder outcome(String outcome) {
            this.outcome = outcome;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder account(Account account) {
            this.accounts.add(account);
            return this;
        }

        public Builder contested(boolean contested) {
            this.contested = contested;
            return this;
        }

        public HistoricalEvent build() {
            return new HistoricalEvent(this);
        }
    }
}
