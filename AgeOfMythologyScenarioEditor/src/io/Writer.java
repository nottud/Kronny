package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import utility.bytearray.ByteArrayConversion;

public class Writer {

	public void write(File file, List<Byte> bytes) throws IOException {
		try (FileOutputStream outStream = new FileOutputStream(file)) {
			outStream.write(ByteArrayConversion.unbox(bytes));
		}
	}

}
