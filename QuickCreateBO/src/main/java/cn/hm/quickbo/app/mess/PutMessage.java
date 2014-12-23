package cn.hm.quickbo.app.mess;

/**
 * 消息传递接口.
 * 
 * @author huangming
 *
 */
public interface PutMessage {

  /**
   * 添加消息.
   * @param message
   */
  public void putMessage(String message);
  /**
   * 清空消息.
   */
  public void clearMessage();

}
