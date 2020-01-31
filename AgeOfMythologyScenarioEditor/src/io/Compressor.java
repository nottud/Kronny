package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;

import utility.bytearray.ByteArrayConversion;

public class Compressor {
	
	public void write(File file, List<Byte> bytes) throws IOException {
		FileOutputStream outStream = new FileOutputStream(file);
        ZOutputStream zOutputStream = new ZOutputStream(outStream, JZlib.Z_BEST_COMPRESSION);
        outStream.write("l33t! :)".getBytes(), 0, 8);
        
//        for(Byte byteElement : bytes) {
//        	zOutputStream.write(byteElement.intValue());
//        }
        zOutputStream.write(ByteArrayConversion.unbox(bytes));
        zOutputStream.close();
        outStream.close();
	}

}
