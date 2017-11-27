package com.shaheen.a5thgeneration.signupproject.DataBase;

import io.realm.RealmObject;

/**
 * Created by Shani on 11/17/2017.
 */

public class FilterData extends RealmObject {

    String trip_destination;
    String male;
    String femal;
    String min_age;
    String max_age;
    String extra_ticket;
    String own_ticket;
    String no_ticket;

    public FilterData() {}

    public String getTrip_destination() {
        return trip_destination;
    }

    public void setTrip_destination(String trip_destination) {
        this.trip_destination = trip_destination;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public String getFemal() {
        return femal;
    }

    public void setFemal(String femal) {
        this.femal = femal;
    }

    public String getMin_age() {
        return min_age;
    }

    public void setMin_age(String min_age) {
        this.min_age = min_age;
    }

    public String getMax_age() {
        return max_age;
    }

    public void setMax_age(String max_age) {
        this.max_age = max_age;
    }

    public String getExtra_ticket() {
        return extra_ticket;
    }

    public void setExtra_ticket(String extra_ticket) {
        this.extra_ticket = extra_ticket;
    }

    public String getOwn_ticket() {
        return own_ticket;
    }

    public void setOwn_ticket(String own_ticket) {
        this.own_ticket = own_ticket;
    }

    public String getNo_ticket() {
        return no_ticket;
    }

    public void setNo_ticket(String no_ticket) {
        this.no_ticket = no_ticket;
    }
}
