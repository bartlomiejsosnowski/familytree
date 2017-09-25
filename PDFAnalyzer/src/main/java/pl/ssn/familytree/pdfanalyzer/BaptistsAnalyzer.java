package pl.ssn.familytree.pdfanalyzer;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import pl.ssn.familytree.pdfanalyzer.dict.Dictionary;
import pl.ssn.familytree.pdfanalyzer.mongo.PersonDAO;
import pl.ssn.familytree.pdfanalyzer.person.Person;
import pl.ssn.familytree.pdfanalyzer.person.PersonFactory;
import pl.ssn.familytree.pdfanalyzer.person.Sex;

/**
 * Hello world!
 *
 */
public class BaptistsAnalyzer {
	public static void main(String[] args) {
		File file = new File("D:\\Users\\Bartek\\Documents\\Drzewo genealogiczne\\test.pdf");
		PersonDAO personDAO = new PersonDAO();

		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		try {
			PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
			parser.parse();
			cosDoc = parser.getDocument();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper = new PDFTextStripper();
			// pdfStripper.setPageStart("{PAGE_START}");
			// pdfStripper.setPageEnd("{PAGE_END}");
			// pdfStripper.setParagraphStart("[");
			// pdfStripper.setParagraphStart("]");
			pdfStripper.setSortByPosition(true);
			pdfStripper.setStartPage(161);
			pdfStripper.setEndPage(897);
			// pdfStripper.setStartPage(210);
			// pdfStripper.setEndPage(210);

			Integer year = null;
			StringBuilder str = new StringBuilder();
			boolean chapterStarted = false;

			String parsedText = pdfStripper.getText(pdDoc);
			String[] lines = parsedText.split(pdfStripper.getLineSeparator());
			for (String line : lines) {
				if (line.trim().matches("^[0-9]+ \\| S t r o n a")) {
					continue;
				}
				if (line.trim().startsWith("Sebastian Jędrych")) {
					continue;
				}
				if (line.trim().startsWith("Wydanie 1")) {
					continue;
				}
				if (line.trim().matches("^Chrzty w [0-9]{4} roku")) {
					Matcher matcher = Pattern.compile("[0-9]{4}").matcher(line);
					if (matcher.find()) {
						year = Integer.parseInt(matcher.group(0));
					}
					if (chapterStarted) {
						parseYear(year, str.toString(), personDAO);
						str.setLength(0);
					}
					chapterStarted = true;
					continue;
				}
				if (chapterStarted) {
					str.append(line);
					// System.out.println(line);
				}
			}
			parseYear(year, str.toString(), personDAO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void parseYear(Integer year, String text, PersonDAO personDAO) {
		System.out.println(year);
		char[] charArray = text.toCharArray();
		StringBuilder sb = new StringBuilder();
		Person child = null, father = null, mother = null;
		Integer index = null;
		String town = null, name = null, firstName = null, lastName = null;
		String fatherName = null, fatherFirstName = null, fatherLastName = null, fatherDetails = null;
		Integer fatherAge = null, fatherBornYear = null;
		String motherName = null, motherFirstName = null, motherLastName = null, motherMaidenName = null;
		Integer motherAge = null, motherBornYear = null;
		for (int i = 0; i < charArray.length; i++) {
			boolean digitsFound = false;
			while (Character.isDigit(charArray[i])) {
				sb.append(charArray[i++]);
				digitsFound = true;
			}
			if (digitsFound && index == null && charArray[i] == '.' && sb.length() > 0) {
				index = Integer.parseInt(sb.toString());
				i = i + 2;
			}
			sb.setLength(0);
			if (index != null) {
				town = readUntil(i, charArray, ". ");
				if (town != null) {
					i = i + town.length() + 2;
				}
				name = readUntil(i, charArray, ", ");
				if (name != null) {
					String[] parts = name.split(" ");
					firstName = parts[parts.length - 1];
					if (name.contains("niewiadomego nazwiska")) {
						lastName = "(nazwisko nieznane)";
					} else {
						lastName = parts[0];
					}
					Sex sex = (firstName.trim().endsWith("a")) ? Sex.FEMALE : Sex.MALE;
					child = PersonFactory.getPerson("B/" + year + "/" + index + "/C", sex, firstName, lastName, null,
							null, town, year);
					i = i + name.length() + 2;
				}
				if (i + 5 < charArray.length && charArray[i] == 'r' && charArray[i + 1] == 'o'
						&& charArray[i + 2] == 'd' && charArray[i + 3] == 'z' && charArray[i + 4] == '.'
						&& charArray[i + 5] == ':') {
					i = i + 7;
					fatherName = readUntil(i, charArray, " i ");
					if (fatherName != null) {
						i = i + fatherName.length() + 3;
						if (i < charArray.length && Character.isLowerCase(charArray[i])) {
							String fatherNameSecondPart = readUntil(i, charArray, " i ");
							if (fatherNameSecondPart != null) {
								fatherName = fatherName + " i " + fatherNameSecondPart;
								i = i + fatherNameSecondPart.length() + 3;
							}
						}
						if (fatherName.contains("oj. niewiadomy")) {
							father = PersonFactory.getUnkownPerson("B/" + year + "/" + index + "/F", Sex.MALE, town);
						} else {
							if (child.getSex() == Sex.FEMALE) {
								fatherLastName = Dictionary.translateLastName(lastName);
							} else {
								fatherLastName = lastName;
							}
							Matcher matcher = Pattern
									.compile("([a-zA-Zęóąśłżń]+) \\- ([a-zA-Zęóąśłżń ]+) \\(([0-9]+)\\)")
									.matcher(fatherName);
							if (matcher.find()) {
								fatherFirstName = matcher.group(1);
								fatherDetails = matcher.group(2);
								fatherAge = Integer.parseInt(matcher.group(3));
								fatherBornYear = year - fatherAge;
							} else {
								fatherFirstName = fatherName;
							}
							father = PersonFactory.getPerson("B/" + year + "/" + index + "/F", Sex.MALE,
									fatherFirstName, fatherLastName, fatherDetails, null, town, fatherBornYear);
						}
					}
					motherName = readUntil(i, charArray, ". ");
					if (motherName != null) {
						i = i + motherName.length() + 1;
						if (motherName.contains("matka niewiadoma")) {
							mother = PersonFactory.getUnkownPerson("B/" + year + "/" + index + "/M", Sex.FEMALE, town);
						} else {
							if (child.getSex() == Sex.MALE) {
								motherLastName = Dictionary.translateLastName(lastName);
							} else {
								motherLastName = lastName;
							}
							Matcher matcher1 = Pattern.compile("([a-zA-Zęóąśłżń]+) ([a-zA-Zęóąśłżń]+) \\(([0-9]+)\\)")
									.matcher(motherName);
							if (matcher1.find()) {
								motherFirstName = matcher1.group(1);
								motherMaidenName = matcher1.group(2);
								motherAge = Integer.parseInt(matcher1.group(3));
								motherBornYear = year - motherAge;
							} else {
								Matcher matcher2 = Pattern.compile(
										"([a-zA-Zęóąśłżń]+) ([a-zA-Zęóąśłżń]+) \\- [a-zA-Zęóąśłżń]+ \\(([0-9]+)\\)")
										.matcher(motherName);
								if (matcher2.find()) {
									motherFirstName = matcher2.group(1);
									motherMaidenName = matcher2.group(2);
									motherAge = Integer.parseInt(matcher2.group(3));
									motherBornYear = year - motherAge;
								} else {
									motherFirstName = motherName;
								}
							}
							mother = PersonFactory.getPerson("B/" + year + "/" + index + "/M", Sex.FEMALE,
									motherFirstName, motherLastName, null, motherMaidenName, town, motherBornYear);
						}
					}
					if (child != null) {
						child.setFather(father);
						child.setMother(mother);
						if (mother != null && father != null) {
							mother.setPartner(father);
						}
					}
					personDAO.storePerson(child);
					personDAO.storePerson(father);
					personDAO.storePerson(mother);
					// System.out.println(child);
					// System.out.println(id + "|" + town + "|" + firstName + " " + lastName);
					// System.out.println("\t" + fatherFirstName + " " + fatherLastName + " - " +
					// fatherDetails
					// + ", lat: " + fatherAge + " (ur.: " + fatherBornYear + " r.) [" + fatherName
					// + "]");
					// System.out.println("\t" + motherFirstName + " " + motherLastName + " z domu "
					// + motherMaidenName
					// + ", lat: " + motherAge + " (ur.: " + motherBornYear + " r.) [" + motherName
					// + "]");
					sb.setLength(0);
					index = null;
					town = null;
					firstName = null;
					lastName = null;
					fatherFirstName = null;
					fatherLastName = null;
					fatherDetails = null;
					fatherAge = null;
					fatherName = null;
					motherFirstName = null;
					motherLastName = null;
					motherMaidenName = null;
					motherAge = null;
					motherName = null;
					child = null;
					father = null;
					mother = null;
				}
			}
		}
	}

	private static String readUntil(int i, char[] arr, String str) {
		if (i < arr.length) {
			StringBuilder sb = new StringBuilder();
			char[] strArray = str.toCharArray();
			for (; i < arr.length; i++) {
				boolean strFound = true;
				for (int j = 0; j < strArray.length; j++) {
					if (arr[i + j] != strArray[j]) {
						strFound = false;
						break;
					}
				}
				if (strFound) {
					break;
				} else {
					sb.append(arr[i]);
				}
			}
			return sb.toString();
		}
		return null;
	}
}
