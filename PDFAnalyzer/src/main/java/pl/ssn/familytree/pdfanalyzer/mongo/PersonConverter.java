package pl.ssn.familytree.pdfanalyzer.mongo;

import java.util.stream.Collectors;

import org.bson.Document;

import pl.ssn.familytree.pdfanalyzer.person.Person;
import pl.ssn.familytree.pdfanalyzer.person.Sex;

public class PersonConverter {
	public static Person documentToPerson(Document doc) {
		Integer _id = doc.getInteger("_id");
		Sex sex = Sex.valueOf(doc.getString("sex"));
		String firstName = doc.getString("firstName");
		String lastName = doc.getString("lastName");
		String details = doc.getString("details");
		String maidenName = doc.getString("maidenName");
		String town = doc.getString("town");
		Integer yearOfBirth = doc.getInteger("yearOfBirth");
		String index = doc.getString("index");
		String fatherName = doc.getString("fatherName");
		String motherName = doc.getString("motherName");
		String motherMaidenName = doc.getString("motherMaidenName");
		return new Person(_id, sex, firstName, lastName, details, maidenName, town, yearOfBirth, index, fatherName,
				motherName, motherMaidenName);
	}

	public static Document personToDocument(Person person) {
		Document document = new Document("_id", person.getId()).append("sex", person.getSex().name())
				.append("firstName", person.getFirstName()).append("lastName", person.getLastName())
				.append("maidenName", person.getMaidenName()).append("details", person.getDetails())
				.append("town", person.getTown()).append("yearOfBirth", person.getYearOfBirth())
				.append("fatherName", person.getFatherName()).append("motherName", person.getMotherName())
				.append("motherMaidenName", person.getMotherMaidenName()).append("index", person.getIndex());
		if (person.getMother() != null) {
			document.append("mother", person.getMother().getId());
		}
		if (person.getFather() != null) {
			document.append("father", person.getFather().getId());
		}
		if (person.getPartner() != null) {
			document.append("partner", person.getPartner().getId());
		}
		if (person.getChildren() != null) {
			document.append("children",
					person.getChildren().stream().map(child -> child.getId()).collect(Collectors.toList()));
		}
		return document;
	}
}
