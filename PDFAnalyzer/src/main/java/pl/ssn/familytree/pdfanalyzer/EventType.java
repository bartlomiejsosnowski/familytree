package pl.ssn.familytree.pdfanalyzer;

public enum EventType {

	BIRTH("BTH"), BIRTH_FATHER("BTH_FATHER"), BIRTH_MOTHER("BTH_MOTHER"), GROOM("G"), GROOM_FATHER("GF"), GROOM_MOTHER("GM"),
	BRIDE("B"), BRIDE_FATHER("BF"), BRIDE_MOTHER("BM"), DEATH("DTH");

	private final String type;

	EventType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
