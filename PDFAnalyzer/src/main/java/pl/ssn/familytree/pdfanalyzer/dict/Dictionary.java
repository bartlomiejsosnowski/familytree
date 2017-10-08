package pl.ssn.familytree.pdfanalyzer.dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Dictionary {

	private static final Map<String, String> FIRST_NAME = new HashMap<String, String>();

	private static final Map<String, String> LAST_NAME_WILDCARD = new HashMap<String, String>();

	private static final Map<String, String> LAST_NAME_FIXED = new HashMap<String, String>();

	private static final Map<String, String> LAST_NAME_IN_CASE = new HashMap<String, String>();
	
	private static final Map<String, String> LAST_NAME_IN_CASE_WILDCARD = new HashMap<String, String>();

	private static final Map<String, String> TOWNS = new HashMap<String, String>();

	static {
		LAST_NAME_WILDCARD.put("ska", "ski");
		LAST_NAME_WILDCARD.put("ski", "ska");
		LAST_NAME_WILDCARD.put("cka", "cki");
		LAST_NAME_WILDCARD.put("cki", "cka");
		LAST_NAME_FIXED.put("Zadrożny", "Zadrożna");
		LAST_NAME_IN_CASE.put("Wójcików", "Wójcik");
		LAST_NAME_IN_CASE.put("Jarząbków", "Jarząbek");
		LAST_NAME_IN_CASE.put("Makulców", "Makulec");
		LAST_NAME_IN_CASE.put("Kałasków", "Kałaska");
		LAST_NAME_IN_CASE.put("Michalików", "Michalik");
		LAST_NAME_IN_CASE_WILDCARD.put("skich", "ska");
		LAST_NAME_IN_CASE_WILDCARD.put("ckich", "cka");
		LAST_NAME_IN_CASE_WILDCARD.put("skiej", "ska");
		LAST_NAME_IN_CASE_WILDCARD.put("ckiej", "cka");
		LAST_NAME_IN_CASE_WILDCARD.put("ych", "a");
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
		if (translated == null) {
			System.err.println("Translation not found for: " + firstName);
		}
		return translated;
	}

	public static String translateLastNameSex(String lastName) {
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
	
	public static String translateLastNameCase(String lastName) {
		if (lastName != null) {
			Iterator<Entry<String, String>> it = LAST_NAME_IN_CASE_WILDCARD.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				int lastIndexOf = lastName.lastIndexOf(entry.getKey());
				if (lastIndexOf >= 0 && lastName.length() - lastIndexOf == entry.getKey().length()) {
					return lastName.substring(0, lastIndexOf) + entry.getValue();
				}
			}
			Iterator<Entry<String, String>> it2 = LAST_NAME_IN_CASE.entrySet().iterator();
			while (it2.hasNext()) {
				Entry<String, String> entry = it2.next();
				if (lastName.equals(entry.getKey()))
					return entry.getValue();
			}
		}
		return lastName;
	}

	private static final Set<String> LACKING_TOWNS = new HashSet<String>();

	public static String translateTown(String town) {
		if (TOWNS.isEmpty()) {
			URL resource = Dictionary.class.getClassLoader().getResource("./resources/townsDictionary.txt");
			File file = new File(resource.getFile());
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] parts = line.split("\t");
					if (parts[1].trim().length() > 0)
						TOWNS.put(parts[0], parts[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String translated = TOWNS.get(town);
		if (translated == null) {
			LACKING_TOWNS.add(town);
//			System.err.println("\tNot found: " + town);
			return town;
		}
		return translated;
	}

	public static void printLackingTowns() {
		List<String> lackingTowns = new ArrayList<>(LACKING_TOWNS);
		Collections.sort(lackingTowns);
		for (String string : lackingTowns) {
			System.out.println(string);
		}
	}
}
