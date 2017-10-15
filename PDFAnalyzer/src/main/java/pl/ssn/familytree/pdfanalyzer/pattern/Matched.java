package pl.ssn.familytree.pdfanalyzer.pattern;

import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Element;

public class Matched {

	private final String word;

	private final Element element;

	private final boolean inCase;

	public Matched(String word, Element element, boolean inCase) {
		this.word = word;
		this.element = element;
		this.inCase = inCase;
	}

	public String getWord() {
		return word;
	}

	public Element getElement() {
		return element;
	}

	public boolean isInCase() {
		return inCase;
	}

	@Override
	public String toString() {
		return "Matched [word=" + word + ", element=" + element + "]";
	}

}
