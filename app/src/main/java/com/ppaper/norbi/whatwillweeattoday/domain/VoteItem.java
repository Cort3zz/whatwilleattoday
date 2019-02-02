package com.ppaper.norbi.whatwillweeattoday.domain;

import java.util.List;

public class VoteItem {

    private int numberOfVote;

    private String foodName;

    private String encodedPicture;

    public String getEncodedPicture() {
        return encodedPicture;
    }

    public void setEncodedPicture(String encodedPicture) {
        this.encodedPicture = encodedPicture;
    }

    public int getNumberOfVote() {
        return numberOfVote;
    }

    public void setNumberOfVote(int numberOfVote) {
        this.numberOfVote = numberOfVote;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
