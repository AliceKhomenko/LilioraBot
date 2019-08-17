package telegram.bot;

public class ImageResponse {
    public Boolean getHasResponse() {
        return hasResponse;
    }

    public void setHasResponse(Boolean hasResponse) {
        this.hasResponse = hasResponse;
    }

    public String getSearchMessage() {
        return search;
    }

    public void setSearchMessage(String search) {
        this.search = search;
    }

    Boolean hasResponse;
    String search;
}
