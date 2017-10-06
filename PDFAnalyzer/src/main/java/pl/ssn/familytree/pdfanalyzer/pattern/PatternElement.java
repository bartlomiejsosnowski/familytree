package pl.ssn.familytree.pdfanalyzer.pattern;

import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Element;
import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Modifier;

public class PatternElement {

	private final Modifier modifier;
	private final Element element;
	private final String value;

	public PatternElement(Modifier modifier, String value) {
		this.modifier = modifier;
		this.value = value;
		this.element = null;
	}

	public PatternElement(Modifier modifier, Element element) {
		this.modifier = modifier;
		this.value = null;
		this.element = element;
	}

	public PatternElement(Modifier modifier, Element element, String value) {
		this.modifier = modifier;
		this.element = element;
		this.value = value;
	}

	public Matched matches(String word) {
		switch (modifier) {
		case EQUAL:
			if (word.equals(value)) {
				return new Matched(word, element);
			}
			break;
		case FIRST_UPPERCASE:
			if (startsWithUpperCase(word)) {
				return new Matched(word, element);
			}
			break;
		case REGEX:
			if(word.matches(value)) {
				return new Matched(word, element);
			}
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public String toString() {
		return "PatternElement [modifier=" + modifier + ", element=" + element + ", value=" + value + "]";
	}

	private static boolean startsWithUpperCase(String str) {
		if (str == null || str.length() == 0)
			return false;
		return Character.isUpperCase(str.charAt(0));
	}
}
