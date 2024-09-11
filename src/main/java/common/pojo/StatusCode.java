package common.pojo;

public enum StatusCode {
    success(200),
    fail(500);

    private final int value;
    StatusCode(int code){this.value=code;}

    public int value() {
        return value;
    }
}
