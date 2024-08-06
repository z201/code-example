package cn.z201.error.code.config.error;

/**
 * @author z201.coding@gmail.com
 **/
public class ErrorCodeInfo {

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static void main(String[] args) {
        String str = "1%s";
        System.out.println(String.format(str, 2));

        String str2 = "1{x}";
        System.out.println(str2.replace("{x}","2"));
    }
}
