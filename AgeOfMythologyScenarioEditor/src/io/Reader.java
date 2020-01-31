package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {

	public List<Byte> read(File file) throws IOException {
		try (FileInputStream inStream = new FileInputStream(file)) {
			List<Byte> bytes = new ArrayList<>();

			int intByte;
			do {
				intByte = inStream.read();
				if (intByte != -1) {
					bytes.add(Byte.valueOf((byte) intByte));
				}
			} while (intByte != -1);
			inStream.close();
			return bytes;
		}
	}

}
