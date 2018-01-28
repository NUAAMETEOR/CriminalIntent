package cn.edu.nuaa.model;


import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Meteor on 2018/1/11.
 */

public class Crime {
    private UUID    crimeId;
    private String  crimeTitle;
    private Date    crimeDate;
    private boolean crimeSolved;

    public Crime() {
        crimeId = UUID.randomUUID();
        crimeDate = Calendar.getInstance().getTime();
        crimeTitle = "";
    }

    public Date getCrimeDate() {
        return crimeDate;
    }

    @Override
    public String toString() {
        return crimeTitle;
    }

    public void setCrimeDate(Date crimeDate) {
        this.crimeDate = crimeDate;
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

    public boolean isCrimeSolved() {
        return crimeSolved;
    }

    public void setCrimeSolved(boolean crimeSolved) {
        this.crimeSolved = crimeSolved;
    }
}
