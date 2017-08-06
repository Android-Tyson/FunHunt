package com.party.funhunt.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ermike on 4/1/2017.
 */

public class VenueData extends RealmObject {
    @PrimaryKey
    public String id;

    public VenueData() {
    }

    public VenueData(String id) {
        this.id = id;
    }
}
