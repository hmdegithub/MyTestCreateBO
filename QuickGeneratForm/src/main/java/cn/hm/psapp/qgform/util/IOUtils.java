package cn.hm.psapp.qgform.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

  public static byte[] read(InputStream input) throws IOException {
    ByteArrayOutputStream arrayBuffer = new ByteArrayOutputStream();

    byte[] buffer = new byte[3030];
    int length = -1;
    while ((length = input.read(buffer)) != -1) {
      // 系统底层的实现方式不适合多次写入，如需要多次写入性能，需要重写此处.
      arrayBuffer.write(buffer, 0, length);
    }

    /*
     * FileChannel channel = fileInput.getChannel(); ByteBuffer buffer =
     * ByteBuffer.allocate(3030); while ((length = channel.read(buffer)) != -1)
     * { // 系统底层的实现方式不适合多次写入，如需要多次写入性能，需要重写此处. arrayBuffer.write(buffer.array(),
     * 0, length); }
     */

    return arrayBuffer.toByteArray();
  }

}
