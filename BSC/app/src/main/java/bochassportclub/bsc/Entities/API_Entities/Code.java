package bochassportclub.bsc.Entities.API_Entities;

public class Code {
    private String statusCode;

    public Code(String statusCode) {
        this.statusCode = statusCode;
    }

    public Code() {
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
