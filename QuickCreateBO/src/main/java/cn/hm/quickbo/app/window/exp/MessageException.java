package cn.hm.quickbo.app.window.exp;

public class MessageException extends RuntimeException {

  public MessageException() {
    super();
  }

  public MessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public MessageException(String message, Throwable cause) {
    super(message, cause);
  }

  public MessageException(String message) {
    super(message);
  }

  public MessageException(Throwable cause) {
    super(cause);
  }

}
