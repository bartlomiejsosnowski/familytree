package pl.ssn.familytree.pdfanalyzer.pattern;

import java.util.ArrayList;
import java.util.List;

import pl.ssn.familytree.pdfanalyzer.dict.Dictionary;
import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Element;

public class MatchedWrapper {
	private final List<Matched> result = new ArrayList<Matched>();

	private int lastMatchedWordIndex;

	public void add(Matched matched) {
		this.result.add(matched);
	}

	public String getByElement(Element element) {
		for (Matched matched : result) {
			if (element.equals(matched.getElement())) {
				return translateIfNeeded(matched);
			}
		}
		return null;
	}

	public void setLastMatchedWordIndex(int lastMatchedWordIndex) {
		this.lastMatchedWordIndex = lastMatchedWordIndex;
	}

	public int getLastMatchedWordIndex() {
		return lastMatchedWordIndex;
	}

	@Override
	public String toString() {
		return "MatchedWrapper [result=" + result + "]";
	}

	private static final String translateIfNeeded(Matched matched) {
		if (Element.FATHER_NAME_IN_CASE == matched.getElement()
				|| Element.MOTHER_NAME_IN_CASE == matched.getElement() || Element.WIFE_NAME_IN_CASE == matched.getElement()) {
			return Dictionary.translateFirstName(matched.getWord());
		}
		if (Element.WIFE_MAIDEN_NAME_IN_CASE == matched.getElement()) {
			return Dictionary.translateLastNameSex(matched.getWord());
		}
		return matched.getWord();

	}
}
