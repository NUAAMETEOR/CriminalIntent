package cn.edu.nuaa.model;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Meteor on 2018/1/13.
 */

public class CrimeRepository {
    private static CrimeRepository              crimeRepository;
    private        ArrayList<Crime>             crimeList;
    private        Context                      applicationContext;
    private        CriminalIntentJSONSerializer serializer;

    private CrimeRepository(Context context) {
        applicationContext = context;
        serializer = new CriminalIntentJSONSerializer(applicationContext, "crime.json");
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
            if (c.getCrimeId().equals(uuid)) {
                return c;
            }
        }
        return null;
    }

    public void saveCrime() {
        serializer.saveCrimes(crimeList);
    }

    private void initCrimeList() {
        crimeList = serializer.loadCrimeFromFile();
    }

    public void deleteCrime(int index) {
        if (index < crimeList.size()) {
            crimeList.remove(index);
        }
    }


    public ArrayList<Crime> getCrimeList() {
        return crimeList;
    }
}
