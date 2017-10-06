package pl.ssn.familytree.pdfanalyzer.pattern;

import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Element;

public class Matched {

	private final String word;
	private final Element element;

	public Matched(String word, Element element) {
		this.word = word;
		this.element = element;
	}

	public String getWord() {
		return word;
	}

	public Element getElement() {
		return element;
	}

	@Override
	public String toString() {
		return "Matched [word=" + word + ", element=" + element + "]";
	}

}
