package pl.ssn.familytree.matcher;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

import pl.ssn.familytree.pdfanalyzer.mongo.PersonConverter;
import pl.ssn.familytree.pdfanalyzer.mongo.PersonDAO;
import pl.ssn.familytree.pdfanalyzer.person.Person;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		PersonDAO personDAO = new PersonDAO();
		MongoCursor<Document> persons = personDAO.getAllPersons("Sosnowski", 30);
		while(persons.hasNext()) {
			Person person = PersonConverter.documentToPerson(persons.next());
			System.out.println(person);
			MongoCursor<Document> similiarPersons = personDAO.findSimiliarPersons(person);
			while(similiarPersons.hasNext()) {
				System.out.println("\t" + PersonConverter.documentToPerson(similiarPersons.next()));
			}
		}
	}
}
