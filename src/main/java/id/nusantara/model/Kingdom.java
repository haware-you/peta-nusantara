package id.nusantara.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A polity: kingdom, sultanate or principality. Built with a fluent builder
 * because most entries fill only a few of the optional fields.
 *
 * <p>{@code id} is the stable key that {@link id.nusantara.events.HistoricalEvent}
 * uses to reference participants, so it must not change once data refers to it.
 */
public final class Kingdom {

    private final String id;
    private final String name;
    private final Island island;
    private final YearRange span;
    private final String capital;
    private final String religion;
    private final List<Ruler> rulers;
    private final List<Source> sources;
    private final String summary;

    private Kingdom(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.island = builder.island;
        this.span = builder.span;
        this.capital = builder.capital;
        this.religion = builder.religion;
        this.rulers = Collections.unmodifiableList(new ArrayList<Ruler>(builder.rulers));
        this.sources = Collections.unmodifiableList(new ArrayList<Source>(builder.sources));
        this.summary = builder.summary;
    }

    public static Builder builder(String id, String name, Island island) {
        return new Builder(id, name, island);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Island getIsland() {
        return island;
    }

    public YearRange getSpan() {
        return span;
    }

    public String getCapital() {
        return capital;
    }

    public String getReligion() {
        return religion;
    }

    public List<Ruler> getRulers() {
        return rulers;
    }

    public List<Source> getSources() {
        return sources;
    }

    public String getSummary() {
        return summary;
    }

    /** The era this kingdom's founding falls in. Derived, never stored. */
    public Era getFoundingEra() {
        return span == null || !span.hasKnownStart() ? null : Era.of(span.getStart());
    }

    public boolean existedIn(int year) {
        return span != null && span.contains(year);
    }

    /** Slider predicate: keep this kingdom if it overlaps the visible window. */
    public boolean existedBetween(int fromYear, int toYear) {
        return span != null && span.overlaps(new YearRange(fromYear, toYear));
    }

    public Ruler rulerIn(int year) {
        for (Ruler ruler : rulers) {
            if (ruler.getReign() != null && ruler.getReign().contains(year)) {
                return ruler;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name + " [" + island.getDisplayName() + ", " + span + "]";
    }

    public static final class Builder {
        private final String id;
        private final String name;
        private final Island island;
        private YearRange span;
        private String capital;
        private String religion;
        private final List<Ruler> rulers = new ArrayList<Ruler>();
        private final List<Source> sources = new ArrayList<Source>();
        private String summary;

        private Builder(String id, String name, Island island) {
            this.id = id;
            this.name = name;
            this.island = island;
        }

        public Builder span(YearRange span) {
            this.span = span;
            return this;
        }

        public Builder span(int start, int end) {
            return span(new YearRange(start, end));
        }

        public Builder capital(String capital) {
            this.capital = capital;
            return this;
        }

        public Builder religion(String religion) {
            this.religion = religion;
            return this;
        }

        public Builder ruler(Ruler ruler) {
            this.rulers.add(ruler);
            return this;
        }

        public Builder source(Source source) {
            this.sources.add(source);
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Kingdom build() {
            return new Kingdom(this);
        }
    }
}
