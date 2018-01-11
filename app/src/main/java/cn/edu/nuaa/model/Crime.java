package cn.edu.nuaa.model;

import java.util.UUID;

/**
 * Created by Meteor on 2018/1/11.
 */

public class Crime {
    private UUID   crimeId;
    private String crimeTitle;

    public Crime() {
        crimeId = UUID.randomUUID();
    }

    public UUID getCrimeId() {
        return crimeId;
    }

    public void setCrimeId(UUID crimeId) {
        this.crimeId = crimeId;
    }

    public String getCrimeTitle() {
        return crimeTitle;
    }

    public void setCrimeTitle(String crimeTitle) {
        this.crimeTitle = crimeTitle;
    }
}
