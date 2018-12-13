package com.ppaper.norbi.whatwillweeattoday.domain;

import java.io.File;

public class Food {
    private String name;
    private String receipt;
    private String encodedPicture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getEncodedPicture() {
        return encodedPicture;
    }

    public void setEncodedPicture(String encodedPicture) {
        this.encodedPicture = encodedPicture;
    }
}
