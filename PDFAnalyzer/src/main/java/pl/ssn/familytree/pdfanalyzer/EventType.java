package pl.ssn.familytree.pdfanalyzer;

public enum EventType {

	GROOM("G"), GROOM_FATHER("GF"), GROOM_MOTHER("GM"),
	BRIDE("B"), BRIDE_FATHER("BF"), BRIDE_MOTHER("BM");

	private final String type;

	EventType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
