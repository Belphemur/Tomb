/************************************************************************
 * This file is part of Tomb.									
 *																		
 * Tomb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by	
 * the Free Software Foundation, either version 3 of the License, or		
 * (at your option) any later version.									
 *																		
 * Tomb is distributed in the hope that it will be useful,	
 * but WITHOUT ANY WARRANTY; without even the implied warranty of		
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the			
 * GNU General Public License for more details.							
 *																		
 * You should have received a copy of the GNU General Public License
 * along with Tomb.  If not, see <http://www.gnu.org/licenses/>.
 ************************************************************************/
package be.Balor.Workers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {
	protected static int count;
	protected static int total;
	protected static int itemCount;
	protected static int itemTotal;
	protected static long lastModified;
	protected static String error;
	protected static boolean cancelled;
	public static String pluginName;

	public synchronized void cancel() {
		cancelled = true;
	}

	public static void install(String location, String filename) {
		try {
			cancelled = false;
			count = Downloader.total = Downloader.itemCount = Downloader.itemTotal = 0;
			System.out.println("[" + pluginName + "] Downloading Dependencies");
			if (cancelled) {
				return;
			}
			System.out.println("   + " + filename + " downloading...");
			download(location, filename);
			System.out.println("   - " + filename + " finished.");
			System.out.println("[" + pluginName + "] Downloading " + filename + "...");
		} catch (IOException ex) {
			System.out.println("[" + pluginName + "] Error Downloading File: " + ex);
		}
	}

	protected static synchronized void download(String location, String filename)
			throws IOException {
		URLConnection connection = new URL(location).openConnection();
		connection.setUseCaches(false);
		lastModified = connection.getLastModified();
		@SuppressWarnings("unused")
		int filesize = connection.getContentLength();
		String destination = "lib" + File.separator + filename;
		File parentDirectory = new File(destination).getParentFile();

		if (parentDirectory != null) {
			parentDirectory.mkdirs();
		}

		InputStream in = connection.getInputStream();
		OutputStream out = new FileOutputStream(destination);

		byte[] buffer = new byte[65536];
		int currentCount = 0;

		while (!cancelled) {
			int count = in.read(buffer);

			if (count < 0) {
				break;
			}
			out.write(buffer, 0, count);
			currentCount += count;
		}

		in.close();
		out.close();
	}

	public long getLastModified() {
		return lastModified;
	}
}
