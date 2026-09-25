package id.nusantara.events;

import id.nusantara.model.Source;

/**
 * One version of what happened, attributed to a source. An event with several
 * accounts is an event the sources disagree on -- keep the disagreement visible
 * instead of resolving it in the data layer.
 */
public final class Account {

    private final Source source;
    private final String narrative;
    private final String perspective;

    public Account(Source source, String narrative, String perspective) {
        this.source = source;
        this.narrative = narrative;
        this.perspective = perspective;
    }

    public static Account of(Source source, String narrative) {
        return new Account(source, narrative, null);
    }

    public Source getSource() {
        return source;
    }

    public String getNarrative() {
        return narrative;
    }

    /** Whose viewpoint this account reflects, e.g. "Majapahit", "Sunda". */
    public String getPerspective() {
        return perspective;
    }

    @Override
    public String toString() {
        return (perspective == null ? "" : "[" + perspective + "] ") + narrative;
    }
}
