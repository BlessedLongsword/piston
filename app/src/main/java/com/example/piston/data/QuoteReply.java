package com.example.piston.data;

public class QuoteReply extends Reply {

    private String quote;
    private String quoteOwner;
    private String quoteID;

    public QuoteReply() {

    }
    public QuoteReply(String owner, String content, String id, String quote, String quoteOwner, String quoteID) {
        super(owner, content, id);
        setQuote(quote);
        setQuoteOwner(quoteOwner);
        setQuoteID(quoteID);
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
