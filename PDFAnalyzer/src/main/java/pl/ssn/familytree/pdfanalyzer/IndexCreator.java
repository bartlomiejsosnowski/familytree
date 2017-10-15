package pl.ssn.familytree.pdfanalyzer;

public class IndexCreator {

	private static final char SEP = '/';

	public static String createIndex(Integer year, int number, EventType eventType) {
		return String.valueOf(year) + SEP + String.valueOf(number) + SEP + eventType.getType();
	}
}
