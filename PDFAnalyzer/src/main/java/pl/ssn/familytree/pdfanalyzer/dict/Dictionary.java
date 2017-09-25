package pl.ssn.familytree.pdfanalyzer.dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Dictionary {

	private static final Map<String, String> FIRST_NAME = new HashMap<String, String>();

	private static final Map<String, String> LAST_NAME_WILDCARD = new HashMap<String, String>();

	private static final Map<String, String> LAST_NAME_FIXED = new HashMap<String, String>();
	
	private static final Map<String, String> TOWNS = new HashMap<String, String>();

	static {
		LAST_NAME_WILDCARD.put("ska", "ski");
		LAST_NAME_WILDCARD.put("ski", "ska");
		LAST_NAME_WILDCARD.put("cka", "cki");
		LAST_NAME_WILDCARD.put("cki", "cka");
		LAST_NAME_FIXED.put("Zadrożny", "Zadrożna");
	}

	public static String translateFirstName(String firstName) {
		if (FIRST_NAME.isEmpty()) {
			URL resource = Dictionary.class.getClassLoader().getResource("./resources/namesDictionary.txt");
			File file = new File(resource.getFile());
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] parts = line.split("\t");
					FIRST_NAME.put(parts[0], parts[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String translated = FIRST_NAME.get(firstName);
		if(translated == null) {
			System.err.println("Translation not found for: " + firstName);
		}
		return translated;
	}

	public static String translateLastName(String lastName) {
		if (lastName != null) {
			Iterator<Entry<String, String>> it = LAST_NAME_WILDCARD.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				if (lastName.endsWith(entry.getKey())) {
					char[] lastNameArray = lastName.toCharArray();
					char[] replacementArray = entry.getValue().toCharArray();
					for (int i = replacementArray.length - 1; i >= 0; i--) {
						int index = lastNameArray.length - 1 - (replacementArray.length - 1 - i);
						lastNameArray[index] = replacementArray[i];
					}
					return new String(lastNameArray);
				}
			}
			Iterator<Entry<String, String>> it2 = LAST_NAME_FIXED.entrySet().iterator();
			while (it2.hasNext()) {
				Entry<String, String> entry = it2.next();
				if (lastName.equals(entry.getKey()))
					return entry.getValue();
				else if (lastName.equals(entry.getValue()))
					return entry.getKey();
			}
		}
		return lastName;
	}
	
	public static String translateTown(String town) {
		if (TOWNS.isEmpty()) {
			URL resource = Dictionary.class.getClassLoader().getResource("./resources/townsDictionary.txt");
			File file = new File(resource.getFile());
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] parts = line.split("\t");
					TOWNS.put(parts[0], parts[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String translated = TOWNS.get(town);
		if(translated == null) {
			System.err.println("Translation not found for: " + town);
		}
		return translated;
	}
}
