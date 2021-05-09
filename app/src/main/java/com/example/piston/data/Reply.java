package com.example.piston.data;

public class Reply {

    private String content;
    private String owner;
    private String id;
    private String quote;
    private String quoteOwner;
    private String quoteID;


    public Reply() {}

    public Reply(String owner, String content, String id) {
        setContent(content);
        setOwner(owner);
        setId(id);
    }

    public Reply(String owner, String content, String id, String quote, String quoteOwner, String quoteID) {
        setContent(content);
        setOwner(owner);
        setId(id);
        setQuote(quote);
        setQuoteOwner(quoteOwner);
        setQuoteID(quoteID);
    }

    //hi
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getQuoteOwner() {
        return quoteOwner;
    }

    public void setQuoteOwner(String quoteOwner) {
        this.quoteOwner = quoteOwner;
    }

    public String getQuoteID() {
        return quoteID;
    }

    public void setQuoteID(String quoteID) {
        this.quoteID = quoteID;
    }
}
