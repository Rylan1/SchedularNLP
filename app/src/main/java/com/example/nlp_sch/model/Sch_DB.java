package com.example.nlp_sch.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Sch_DB extends RealmObject {
    @Required
    private String topic;
    private String title,notes;
    @Required
    private Date start;
    @Required
    private Date end;

    public Sch_DB() {
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStart(Date start) {
        this.start = start;
   }

    public void setEnd(Date end) {
        this.end = end;
    }


    public String getTopic() {
        return topic;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
