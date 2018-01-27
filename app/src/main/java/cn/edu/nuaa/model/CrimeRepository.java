package cn.edu.nuaa.model;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Meteor on 2018/1/13.
 */

public class CrimeRepository {
    private static CrimeRepository  crimeRepository;
    private        ArrayList<Crime> crimeList;
    private        Context          applicationContext;

    private CrimeRepository(Context context) {
        applicationContext = context;
        initCrimeList();
    }

    public static CrimeRepository getCrimeRepository(@NonNull Context context) {
        if (crimeRepository == null) {
            synchronized (CrimeRepository.class) {
                if (crimeRepository == null) {
                    crimeRepository = new CrimeRepository(context.getApplicationContext());
                }
            }
        }
        return crimeRepository;
    }

    public Crime getCrime(UUID uuid) {
        for (Crime c :
                crimeList) {
            if (c.getCrimeId() .equals(uuid)) {
                return c;
            }
        }
        return null;
    }

    private void initCrimeList() {
        crimeList = new ArrayList<Crime>();
    }

    public ArrayList<Crime> getCrimeList() {
        return crimeList;
    }
}
