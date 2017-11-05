package pl.ssn.familytree.pdfanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import pl.ssn.familytree.pdfanalyzer.dict.Dictionary;
import pl.ssn.familytree.pdfanalyzer.pattern.MatchedWrapper;
import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper;
import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Element;
import pl.ssn.familytree.pdfanalyzer.person.Person;
import pl.ssn.familytree.pdfanalyzer.person.PersonFactory;
import pl.ssn.familytree.pdfanalyzer.person.Sex;

public class MarriageImporter {

	private static final java.util.regex.Pattern AGE_PATTERN = java.util.regex.Pattern.compile("[0-9]{2}");

	private static final java.util.regex.Pattern AGE_PARENTHESESS_PATTERN = java.util.regex.Pattern
			.compile("\\(([0-9]{2})\\)");

	public static void main(String[] args) {
		File file1 = new File(
				"D:\\Users\\Bartek\\Workspace\\FamilyTree\\PDFAnalyzer\\src\\main\\java\\resources\\marriages_1777-1825.txt");
		File file2 = new File(
				"D:\\Users\\Bartek\\Workspace\\FamilyTree\\PDFAnalyzer\\src\\main\\java\\resources\\marriages_1826-1928.txt");
		File file3 = new File(
				"D:\\Users\\Bartek\\Workspace\\FamilyTree\\PDFAnalyzer\\src\\main\\java\\resources\\marr_test.txt");
//		importFile(file1);
		 importFile(file2);
//		 importFile(file3);
		Dictionary.printLackingTowns();
	}

	private static void importFile(File file) {

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			Integer year = null;
			int index = 0;
			while ((line = br.readLine()) != null) {
				// if (year != null && year == 1781)
				// return;

				if (line.length() < 2)
					continue;
				if (line.startsWith("Śluby w")) {
					Matcher matcher = java.util.regex.Pattern.compile("[0-9]{4}").matcher(line);
					if (matcher.find()) {
						year = Integer.parseInt(matcher.group(0));
					}
					System.out.println(year);
					index = 0;
				} else {
					index++;
					List<String> list = Arrays.asList(line.split(" ")).stream().filter(str -> str.trim().length() > 0)
							.collect(Collectors.toList());
					String[] parts = new String[list.size()];
					list.toArray(parts);
					if (parts[0].matches("^[0-9]+\\.")) {
						Matcher matcher = java.util.regex.Pattern.compile("^([0-9]+)\\.").matcher(line);
						if (matcher.find()) {
							index = Integer.parseInt(matcher.group(1));
						}
						List<String> _groom = new ArrayList<String>();
						List<String> _bride = new ArrayList<String>();
						List<String> currentList = _groom;
						for (int i = 1; i < parts.length; i++) {
							if (isBrideSectionStart(parts, i)) {
								currentList = _bride;
								continue;
							}
							String word = parts[i].trim();
							if (word.length() > 0)
								currentList.add(removeDotOrComma(word));
						}
						// System.out.println(_groom + " " + _bride);
						Person groom = parsePerson(year, index, null, Sex.MALE, _groom);
						Person bride = parsePerson(year, index, null, Sex.FEMALE, _bride);
						System.out.println(year + "/" + index + " " + null + ":\r\n\t " + groom + "\r\n\t " + bride);
					} else if (isTownFirst(parts, 3)) {
						List<String> _town = new ArrayList<String>();
						List<String> _groom = new ArrayList<String>();
						List<String> _bride = new ArrayList<String>();
						List<String> currentList = _town;
						for (int i = 0; i < parts.length; i++) {
							String word = parts[i].trim();
							if (currentList == _town && word.endsWith(".")) {
								currentList.add(word.substring(0, word.length() - 1));
								currentList = _groom;
								continue;
							} else if (isBrideSectionStart(parts, i)) {
								currentList = _bride;
								continue;
							}
							if (word.length() > 0)
								currentList.add(removeDotOrComma(word));
						}
						// System.out.println(_groom + " " + _bride);
						String town = String.join(" ", _town);
						Person groom = parsePerson(year, index, town, Sex.MALE, _groom);
						Person bride = parsePerson(year, index, town, Sex.FEMALE, _bride);
						System.out.println(year + "/" + index + " " + town + ":\r\n\t " + groom + "\r\n\t " + bride);
					} else {
						System.out.println("Skomplikowane: " + line);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Person parsePerson(Integer currentYear, int number, String marriageTown, Sex sex,
			List<String> list) {
		if (list.isEmpty())
			return null;
		String firstName = null;
		String lastName = null;
		Integer yearOfBirth = null;
		String fatherName = null;
		String fatherLastName = null;
		Integer fatherYearOfBirth = null;
		String motherName = null;
		String motherMaidenName = null;
		Integer motherYearOfBirth = null;
		String currentTown = null;
		String birthTown = null;
		String birthParishTown = null;
		Person father = null;
		Person mother = null;
		if (startsWithUpperCase(list.get(0))) {
			lastName = list.get(0);
		}
		if (startsWithUpperCase(list.get(1))) {
			firstName = list.get(1);
		}
		for (int i = 0; i < list.size(); i++) {
			String word = list.get(i);
			if (word.startsWith("(") && word.endsWith(")")) {
				yearOfBirth = parseYearOfBirth(currentYear, word);
			} else if ("lat".equals(word) || "lata".equals(word)) {
				Matcher matcher = null;
				if (i > 0) {
					matcher = AGE_PATTERN.matcher(list.get(i - 1));
				} else if (i < list.size() - 1) {
					matcher = AGE_PATTERN.matcher(list.get(i + 1));
				}
				if (matcher != null && matcher.find()) {
					yearOfBirth = currentYear - Integer.parseInt(matcher.group(0));
				}
			}
			if ("syn".equals("word") || "córka".equals(word) || "rodz.:".equals(word)) {
				MatchedWrapper matched = PatternWrapper.PARENT_PATTERN.matcher(list, i);
				if (matched != null) {
					fatherName = matched.getByElement(Element.FATHER_NAME);
					motherName = matched.getByElement(Element.MOTHER_NAME);
					motherMaidenName = matched.getByElement(Element.MOTHER_MAIDEN_NAME);
					String fatherAge = matched.getByElement(Element.FATHER_AGE);
					if (fatherAge != null) {
						fatherYearOfBirth = parseYearOfBirth(currentYear, fatherAge);
					}
					String motherAge = matched.getByElement(Element.MOTHER_AGE);
					if (motherAge != null) {
						motherYearOfBirth = parseYearOfBirth(currentYear, motherAge);
					}
					i = matched.getLastMatchedWordIndex();
					continue;
				}
			} else if ("z".equals(word) && i < list.size() - 1 && startsWithUpperCase(list.get(i + 1))) {
				MatchedWrapper matched = PatternWrapper.TOWN_PATTERN.matcher(list, i);
				if (matched != null) {
					currentTown = matched.getByElement(Element.TOWN);
					i = matched.getLastMatchedWordIndex();
					continue;
				}
			} else if ("ur".equals(word) && i < list.size() - 2 && "w".equals(list.get(i + 1))) {
				MatchedWrapper matched = PatternWrapper.TOWN_PATTERN.matcher(list, i);
				if (matched != null) {
					birthTown = matched.getByElement(Element.TOWN);
					birthParishTown = matched.getByElement(Element.PARISH_TOWN);
					i = matched.getLastMatchedWordIndex();
					continue;
				}
			} else if ("wdowiec".equals(word)) {
				MatchedWrapper matched = PatternWrapper.WIDOW_PATTERN.matcher(list, i);
				if (matched != null) {
					// System.out.println(matched);
					i = matched.getLastMatchedWordIndex();
					continue;
				}
			}
		}
		if (fatherLastName == null) {
			if (sex == Sex.FEMALE)
				fatherLastName = Dictionary.translateLastNameSex(lastName);
			else
				fatherLastName = lastName;
		}
		String town = null;
		if(marriageTown != null) {
			town = marriageTown;
		} else {
			town = currentTown;
		}
		if (fatherName != null) {
			final String index = IndexCreator.createIndex(currentYear, number,
					(sex == Sex.MALE) ? EventType.GROOM_FATHER : EventType.BRIDE_FATHER);
			father = PersonFactory.getPerson(index, Sex.MALE, fatherName, fatherLastName, null, null, town,
					birthTown, fatherYearOfBirth);
		}
		if (motherName != null || motherMaidenName != null) {
			final String index = IndexCreator.createIndex(currentYear, number,
					(sex == Sex.MALE) ? EventType.GROOM_MOTHER : EventType.BRIDE_MOTHER);
			mother = PersonFactory.getPerson(index, Sex.FEMALE, motherName, lastName, null, town,
					marriageTown, birthTown, motherYearOfBirth);
		}
		final String index = IndexCreator.createIndex(currentYear, number,
				(sex == Sex.MALE) ? EventType.GROOM : EventType.BRIDE);
		Person person = PersonFactory.getPerson(index, sex, firstName, lastName, null, null, town,
				birthTown, yearOfBirth);
		if (person != null) {
			person.setFather(father);
			person.setMother(mother);
		}
		return person;
	}

	private static Integer parseYearOfBirth(Integer currentYear, String age) {
		if (age.startsWith("(") && age.endsWith(")")) {
			Matcher matcher = AGE_PARENTHESESS_PATTERN.matcher(age);
			if (matcher.find()) {
				try {
					return currentYear - Integer.parseInt(matcher.group(1));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

	private static boolean isBrideSectionStart(String[] parts, int i) {
		if (parts[i].equals("oraz")) {
			if (i < parts.length - 2) {
				if (startsWithUpperCase(parts[i + 1]) && isFemaleName(parts[i + 2]))
					return true;
				if (i < parts.length - 3) {
					if (startsWithUpperCase(parts[i + 1])
							&& parts[i + 2].matches("\\([A-ZĘÓĄŚŁŻŹĆŃa-zęóąśłżźń\\?\\ ]*\\)")
							&& isFemaleName(parts[i + 3]))
						return true;
					if (startsWithUpperCase(parts[i + 1]) && parts[i + 2].equals("nieznanego")
							&& parts[i + 3].equals("imienia"))
						return true;
					if (parts[i + 1].equals("nieznanego") && parts[i + 2].equals("nazwiska")
							&& startsWithUpperCase(parts[i + 3]))
						return true;
					if (startsWithUpperCase(parts[i + 1]) && parts[i + 2].equals("z")
							&& (parts[i + 3].equals("domu") || parts[i + 3].equals("d.")))
						return true;
				}
			}
		}
		return false;
	}

	private static boolean isFemaleName(String str) {
		return startsWithUpperCase(str) && str.charAt(str.length() - 1) == 'a';
	}

	private static boolean startsWithUpperCase(String str) {
		return Character.isUpperCase(str.charAt(0));
	}

	private static boolean isTownFirst(String[] parts, int max) {
		for (int i = 0; i < max && i < parts.length; i++) {
			if (parts[i].matches("\\d+\\."))
				return false;
			if (parts[i].endsWith(".")) {
				return true;
			}
		}
		return false;
	}

	private static String removeDotOrComma(String str) {
		if (str.length() >= 2 && (str.endsWith(".") || str.endsWith(",")))
			return str.substring(0, str.length() - 1);
		return str;
	}
}
