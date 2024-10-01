package org.example;
public class Episode {
    String title;
    String duration;

    public Episode(String title, String duration) {
        this.title = title;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }
}
