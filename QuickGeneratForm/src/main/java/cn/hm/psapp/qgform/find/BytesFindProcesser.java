package cn.hm.psapp.qgform.find;

public abstract class BytesFindProcesser {

  /**
   * 匹配第一次出现的match的byte的位置.
   * 
   * @param bytes
   * @param matchBytes
   * @return
   */
  public abstract int findBytes(byte[] bytes, byte[] matchBytes);

  /**
   * 替换方法.
   * 
   * @param bytes
   * @param matchBytes
   * @param replaceBytes
   * @return
   */
  public byte[] replace(byte[] bytes, byte[] matchBytes, byte[] replaceBytes) {
    int startIndex = findBytes(bytes, matchBytes);
    if (startIndex == -1) {
      return bytes;
    } else {
      byte[] resultBuytes = new byte[bytes.length - matchBytes.length + replaceBytes.length];

      // 前缀复制
      System.arraycopy(bytes, 0, resultBuytes, 0, startIndex);
      // 替换复制
      System.arraycopy(replaceBytes, 0, resultBuytes, startIndex, replaceBytes.length);
      // 后缀复制
      System.arraycopy(bytes, startIndex + matchBytes.length, resultBuytes, startIndex + replaceBytes.length, bytes.length - startIndex - matchBytes.length);

      return resultBuytes;
    }

  }
}
