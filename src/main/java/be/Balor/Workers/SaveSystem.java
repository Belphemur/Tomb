/*This file is part of Tomb.

    Tomb is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Tomb is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Tomb.  If not, see <http://www.gnu.org/licenses/>.*/
package be.Balor.Workers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import be.Balor.bukkit.Tomb.Tomb;
import be.Balor.bukkit.Tomb.TombSave;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class SaveSystem {
	String path;

	public SaveSystem(String path) {
		this.path = path;
		File dir = new File(path);
		if (!dir.exists())
			dir.mkdir();
	}

	/**
	 * Save all the tombs to the file tombs.dat
	 * 
	 * @param toBeSaved
	 */
	public void save(HashMap<String, Tomb> toBeSaved) {
		File saveFile = new File(this.path + File.separator + "tombs.dat");
		if (!saveFile.exists())
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		HashMap<String, TombSave> toWrite = new HashMap<String, TombSave>();
		for (String name : toBeSaved.keySet())
			toWrite.put(name, toBeSaved.get(name).save());

		try {
			FileOutputStream fos = new FileOutputStream(saveFile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(toWrite);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Tomb> load() {
		HashMap<String, Tomb> result = new HashMap<String, Tomb>();
		HashMap<String, TombSave> saved=null;
		File saveFile = new File(this.path + File.separator + "tombs.dat");
		if (!saveFile.exists())
			return new HashMap<String, Tomb>();

		FileInputStream fis = null;
		ObjectInputStream in = null;

		try {
			fis = new FileInputStream(saveFile);
			in = new ObjectInputStream(fis);
			saved = (HashMap<String, TombSave>) in.readObject();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		if(saved == null)
			return new HashMap<String, Tomb>();
		for(String name :  saved.keySet())
			result.put(name, saved.get(name).load());
		
		return result;

	}

}
