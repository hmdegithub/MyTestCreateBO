package cn.hm.psapp.qgform.find.impl;

import cn.hm.psapp.qgform.find.BytesFindProcesser;

public class BMBytesFindProcesser extends BytesFindProcesser {

  @Override
  public int findBytes(byte[] bytes, byte[] matchBytes) {
    return findBytes(bytes, 0, matchBytes);
  }

  public int findBytes(byte[] bytes, int startIndex, byte[] matchBytes) {
    boolean flag = false;
    int length = bytes.length - matchBytes.length + 1;
    for (int i = startIndex; i < length; i++) {
      for (int j = 0; j < matchBytes.length; j++) {
        if (bytes[i + j] != matchBytes[j]) {
          flag = false;
          break;
        } else {
          flag = true;
        }
      }
      if (flag) {
        return i;
      }
    }
    return -1;
  }
}
