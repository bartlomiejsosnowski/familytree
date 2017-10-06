package pl.ssn.familytree.pdfanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import pl.ssn.familytree.pdfanalyzer.dict.Dictionary;
import pl.ssn.familytree.pdfanalyzer.person.Person;
import pl.ssn.familytree.pdfanalyzer.person.PersonFactory;
import pl.ssn.familytree.pdfanalyzer.person.Sex;

public class MarriageImporter {

	private static final Pattern AGE_PATTERN = Pattern.compile("[0-9]{2}");

	private static final Pattern AGE_PARENTHESESS_PATTERN = Pattern.compile("\\(([0-9]{2})\\)");

	public static void main(String[] args) {
		File file1 = new File(
				"D:\\Users\\Bartek\\Workspace\\FamilyTree\\PDFAnalyzer\\src\\main\\java\\resources\\marriages_1777-1825.txt");
		File file2 = new File(
				"D:\\Users\\Bartek\\Workspace\\FamilyTree\\PDFAnalyzer\\src\\main\\java\\resources\\marriages_1826-1928.txt");
		// importFile(file1);
		importFile(file2);
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
					Matcher matcher = Pattern.compile("[0-9]{4}").matcher(line);
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
						Matcher matcher = Pattern.compile("^([0-9]+)\\.").matcher(line);
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
						Person groom = getGroomOrBride(year, index, null, Sex.MALE, _groom);
						Person bride = getGroomOrBride(year, index, null, Sex.FEMALE, _bride);
						// System.out.println(year + "/" + index + " " + null + ":\r\n\t " + groom +
						// "\r\n\t " + bride);
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
						Person groom = getGroomOrBride(year, index, town, Sex.MALE, _groom);
						Person bride = getGroomOrBride(year, index, town, Sex.FEMALE, _bride);
						// System.out.println(year + "/" + index + " " + town + ":\r\n\t " + groom +
						// "\r\n\t " + bride);
					} else {
						// System.out.println("Skomplikowane: " + line);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Person getGroomOrBride(Integer year, int index, String marriageTown, Sex sex, List<String> list) {
		if (list.isEmpty())
			return null;
		String firstName = null;
		String lastName = null;
		Integer yearOfBirth = null;
		String fatherName = null;
		String fatherLastName = null;
		String motherName = null;
		String motherMaidenName = null;
		String currentTown = null;
		String birthTown = null;
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
				Matcher matcher = AGE_PARENTHESESS_PATTERN.matcher(word);
				if (matcher.find()) {
					yearOfBirth = year - Integer.parseInt(matcher.group(1));
				}
			} else if ("lat".equals(word) || "lata".equals(word)) {
				Matcher matcher = null;
				if (i > 0) {
					matcher = AGE_PATTERN.matcher(list.get(i - 1));
				} else if (i < list.size() - 1) {
					matcher = AGE_PATTERN.matcher(list.get(i + 1));
				}
				if (matcher != null && matcher.find()) {
					yearOfBirth = year - Integer.parseInt(matcher.group(0));
				}
			}
			if (sex == Sex.MALE && "syn".equals(word)) {
				if (i < list.size() - 1 && startsWithUpperCase(list.get(i + 1))) {
					fatherName = Dictionary.translateFirstName(list.get(i + 1));
					if (i < list.size() - 3 && list.get(i + 2).equals("i") && startsWithUpperCase(list.get(i + 3))) {
						motherName = Dictionary.translateFirstName(list.get(i + 3));
						if (i < list.size() - 4 && startsWithUpperCase(list.get(i + 4))) {
							motherMaidenName = Dictionary.translateFirstName(list.get(i + 4));
							i = i + 4;
							continue;
						}
						i = i + 3;
						continue;
					}
					i = i + 1;
					continue;
				}
			} else if (sex == Sex.FEMALE && "córka".equals(word)) {
				if (i < list.size() - 1 && startsWithUpperCase(list.get(i + 1))) {
					fatherName = Dictionary.translateFirstName(list.get(i + 1));
					if (i < list.size() - 3 && list.get(i + 2).equals("i") && startsWithUpperCase(list.get(i + 3))) {
						motherName = Dictionary.translateFirstName(list.get(i + 3));
						if (i < list.size() - 4 && startsWithUpperCase(list.get(i + 4))) {
							motherMaidenName = list.get(i + 4);
							i = i + 4;
							continue;
						}
						i = i + 3;
						continue;
					}
					i = i + 1;
					continue;
				}
			} else if ("rodz.:".equals(word)) {
				if (i < list.size() - 1
						&& (startsWithUpperCase(list.get(i + 1)) || "niewiadomi".equals(list.get(i + 1)))) {
					if (!"niewiadomi".equals(list.get(i + 1)))
						fatherName = list.get(i + 1);
					if (i < list.size() - 3) {
						if (list.get(i + 2).equals("i") && startsWithUpperCase(list.get(i + 3))) {
							motherName = list.get(i + 3);
							if (i < list.size() - 4 && startsWithUpperCase(list.get(i + 4))) {
								motherMaidenName = list.get(i + 4);
								i = i + 4;
								continue;
							}
							i = i + 3;
							continue;
						} else if (startsWithUpperCase(list.get(i + 2))) {
							fatherLastName = list.get(i + 2);
							i = i + 2;
							continue;
						}
						if (i < list.size() - 4 && list.get(i + 3).equals("i")
								&& startsWithUpperCase(list.get(i + 4))) {
							motherName = list.get(i + 4);
							if (i < list.size() - 5 && startsWithUpperCase(list.get(i + 5))) {
								motherMaidenName = list.get(i + 5);
								i = i + 5;
								continue;
							}
							i = i + 4;
							continue;
						}
					}
					i = i + 1;
					continue;
				}
			} else if ("z".equals(word) && i < list.size() - 1 && startsWithUpperCase(list.get(i + 1))) {
				if (i < list.size() - 2 && startsWithUpperCase(list.get(i + 2))) {
					currentTown = Dictionary.translateTown(list.get(i + 1) + " " + list.get(i + 2));
					i = i + 2;
					continue;
				} else {
					currentTown = Dictionary.translateTown(list.get(i + 1));
					i = i + 1;
					continue;
				}
			} else if ("zam".equals(word) && i < list.size() - 2 && "w".equals(list.get(i + 1))
					&& startsWithUpperCase(list.get(i + 2))) {
				if (i < list.size() - 3 && startsWithUpperCase(list.get(i + 3))) {
					currentTown = Dictionary.translateTown(list.get(i + 2) + " " + list.get(i + 3));
					i = i + 3;
					continue;
				} else {
					currentTown = Dictionary.translateTown(list.get(i + 2));
					i = i + 2;
					continue;
				}
			} else if ("ur".equals(word) && i < list.size() - 2 && "w".equals(list.get(i + 1))) {
				if (startsWithUpperCase(list.get(i + 2))) {
					if (i < list.size() - 3 && startsWithUpperCase(list.get(i + 3))) {
						birthTown = Dictionary.translateTown(list.get(i + 2) + " " + list.get(i + 3));
						i = i + 3;
						continue;
					} else {
						birthTown = Dictionary.translateTown(list.get(i + 2));
						i = i + 2;
						continue;
					}
				} else if (i < list.size() - 3 && "par".equals(list.get(i + 2))
						&& startsWithUpperCase(list.get(i + 3))) {
					if (i < list.size() - 4 && startsWithUpperCase(list.get(i + 4))) {
						birthTown = Dictionary.translateTown(list.get(i + 3) + " " + list.get(i + 4));
						i = i + 4;
						continue;
					} else {
						birthTown = Dictionary.translateTown(list.get(i + 3));
						i = i + 3;
						continue;
					}
				}
			} else if ("wdowiec".equals(word) && i < list.size() - 2 && "po".equals(list.get(i + 1))
					&& startsWithUpperCase(list.get(i + 2))) {
				if (i < list.size() - 3 && startsWithUpperCase(list.get(i + 3))) {
					System.out.println("\twdowiec po " + list.get(i + 2) + " " + list.get(i + 3));
					i = i + 3;
					continue;
				} else if (i < list.size() - 4 && "z".equals(list.get(i + 3)) && startsWithUpperCase(list.get(i + 4))) {
					System.out.println("\twdowiec po " + list.get(i + 2) + " z " + list.get(i + 4));
					i = i + 4;
					continue;
				}
			}
		}
		if (fatherLastName == null) {
			if (sex == Sex.FEMALE)
				fatherLastName = Dictionary.translateLastName(lastName);
			else
				fatherLastName = lastName;
		}
		if (fatherName != null) {
			father = PersonFactory.getPerson(year + "/" + index + "/GF", Sex.MALE, fatherName, fatherLastName, null,
					null, marriageTown, null);
		}
		if (motherName != null || motherMaidenName != null) {
			mother = PersonFactory.getPerson(year + "/" + index + "/GM", Sex.FEMALE, motherName, lastName, null,
					motherMaidenName, marriageTown, null);
		}
		Person groom = PersonFactory.getPerson(year + "/" + index + "/G", Sex.MALE, firstName, lastName, null, null,
				marriageTown, yearOfBirth);
		if (groom != null) {
			groom.setFather(father);
			groom.setMother(mother);
		}
		return groom;
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
		if (str.length() >= 2 && (str.endsWith(".") || str.endsWith(",") || str.endsWith(")")))
			return str.substring(0, str.length() - 1);
		return str;
	}
}
