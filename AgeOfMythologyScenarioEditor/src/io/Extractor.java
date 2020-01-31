
package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jzlib.ZInputStream;

public class Extractor {
   
   public List<Byte> extract(File file) throws IOException {
      FileInputStream inStream = new FileInputStream(file);
      inStream.skip(8); // skip l33t! :)
      
      ZInputStream zInputStream = new ZInputStream(inStream);
      
      List<Byte> bytes = new ArrayList<>();
      
      int intByte;
      do {
         intByte = zInputStream.read();
         if (intByte != -1) {
            bytes.add(Byte.valueOf((byte) intByte));
         }
      }
      while (intByte != -1);
      zInputStream.close();
      return bytes;
   }
   
}
