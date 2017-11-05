package pl.ssn.familytree.pdfanalyzer.person;

public class PersonFactory {

	private static Integer ID = 0;

	public static Person getPerson(String index, Sex sex, String firstName, String lastName, String details,
			String maidenName, String town, String placeOfBirth, Integer yearOfBirth) {
		return new Person(ID++, sex, firstName, lastName, maidenName, town, placeOfBirth, yearOfBirth, index);
	}

	public static Person getUnkownPerson(String index, Sex sex, String town) {
		if (sex == Sex.FEMALE) {
			return new Person(ID++, sex, "matka", "niewiadoma", null, town, null, null, index);
		} else if (sex == Sex.MALE) {
			return new Person(ID++, sex, "ojciec", "nieznany", null, town, null, null, index);
		}
		return null;
	}
}
