package com.dev.nathan.testtcc.model;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class AttestantPostId {

    @Exclude
    public String BlogPostId;

    public <T extends AttestantPostId> T withId(@NonNull final String id) {
        this.BlogPostId = id;
        return (T) this;
    }

}
