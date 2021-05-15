package com.example.piston.data.posts;

public class QuoteReply extends Reply {

    private String quoteID, quoteOwner, quote;

    @SuppressWarnings("unused")
    public QuoteReply() {}

    public QuoteReply(String id, String owner, String content, String ownerImageLink,
                      String quoteID, String quoteOwner, String quote) {
        super(id, owner, content, ownerImageLink);
        setQuoteID(quoteID);
        setQuoteOwner(quoteOwner);
        setQuote(quote);
    }

    public String getQuoteID() {
        return quoteID;
    }

    public void setQuoteID(String quoteID) {
        this.quoteID = quoteID;
    }

    public String getQuoteOwner() {
        return quoteOwner;
    }

    public void setQuoteOwner(String quoteOwner) {
        this.quoteOwner = quoteOwner;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}
