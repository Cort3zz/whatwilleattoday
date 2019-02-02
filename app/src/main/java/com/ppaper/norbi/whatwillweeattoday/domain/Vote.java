package com.ppaper.norbi.whatwillweeattoday.domain;

import java.util.List;

public class Vote {

    private List<VoteItem> voteItems;

    private String principal;

    List<String> alreadyVoted;

    public List<VoteItem> getVoteItems() {
        return voteItems;
    }

    public void setVoteItems(List<VoteItem> voteItems) {
        this.voteItems = voteItems;
    }

    public List<String> getAlreadyVoted() {
        return alreadyVoted;
    }

    public void setAlreadyVoted(List<String> alreadyVoted) {
        this.alreadyVoted = alreadyVoted;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
