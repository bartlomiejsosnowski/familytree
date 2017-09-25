package pl.ssn.familytree.pdfanalyzer.mongo;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import pl.ssn.familytree.pdfanalyzer.person.Person;
import pl.ssn.familytree.pdfanalyzer.person.Sex;

public class PersonDAO {

	private MongoDatabase database;

	private MongoCollection<Document> collection;

	public PersonDAO() {
		// Creating a Mongo client
		MongoClient mongo = new MongoClient("localhost", 27017);

		// Creating Credentials
		// MongoCredential credential = MongoCredential.createCredential("sampleUser",
		// "myDb", "password".toCharArray());
		// System.out.println("Connected to the database successfully");

		database = mongo.getDatabase("familytree");

		collection = database.getCollection("person");
	}

	public void storePerson(Person person) {
		try {
			Document document = PersonConverter.personToDocument(person);
			collection.insertOne(document);
		} catch (Exception e) {
			System.out.println("Error in " + person);
			throw e;
		}
	}

	public MongoCursor<Document> getAllPersons(String lastName, int limit) {
		FindIterable<Document> iterDoc = collection.find(new Document("lastName", lastName)).sort(new Document("_id", -1)).limit(limit);
		return iterDoc.iterator();
	}

	public MongoCursor<Document> findSimiliarPersons(Person person) {
		Document document = new Document();
		document.append("_id", new Document("$ne", person.getId()));
		document.append("sex", person.getSex().name());
		document.append("yearOfBirth",
				new Document("$gt", person.getYearOfBirth() - 2).append("$lt", person.getYearOfBirth() + 2));
		document.append("firstName", person.getFirstName());
		{
			if(person.getSex() == Sex.FEMALE) {
				document.append("lastName", new Document("$in", Arrays.asList(person.getLastName(), person.getMaidenName())));
				document.append("maidenName", new Document("$in", Arrays.asList(person.getLastName(), person.getMaidenName())));
			} else {
				document.append("lastName", person.getLastName());
			}
		}
		FindIterable<Document> iterable = collection.find(document);
		return iterable.iterator();
	}
}
