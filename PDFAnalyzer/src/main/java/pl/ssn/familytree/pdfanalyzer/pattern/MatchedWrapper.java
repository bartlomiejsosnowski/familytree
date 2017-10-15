package pl.ssn.familytree.pdfanalyzer.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.ssn.familytree.pdfanalyzer.dict.Dictionary;
import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Element;

public class MatchedWrapper {

	private final List<Matched> result = new ArrayList<Matched>();

	private int lastMatchedWordIndex;

	public void add(Matched matched) {
		this.result.add(matched);
	}

	public String getByElement(Element element) {
		List<Matched> filtered = result.stream().filter((matched) -> element == matched.getElement())
				.collect(Collectors.toList());
		if (!filtered.isEmpty()) {
			String word = filtered.stream().map((matched) -> matched.getWord()).collect(Collectors.joining(" "));
			if (filtered.stream().anyMatch((matched) -> matched.isInCase())) {
				return translate(element, word);
			}
			return word;
		}
		return null;
	}

	public void setLastMatchedWordIndex(int lastMatchedWordIndex) {
		this.lastMatchedWordIndex = lastMatchedWordIndex;
	}

	public int getLastMatchedWordIndex() {
		return lastMatchedWordIndex;
	}

	public int getSize() {
		return result.size();
	}

	@Override
	public String toString() {
		return "MatchedWrapper [result=" + result + "]";
	}

	private static final String translate(Element element, String word) {
		if (Element.FATHER_NAME == element || Element.MOTHER_NAME == element || Element.WIFE_NAME == element) {
			return Dictionary.translateFirstName(word);
		}
		if (Element.WIFE_MAIDEN_NAME == element) {
			return Dictionary.translateLastNameSex(word);
		}
		if (Element.TOWN == element) {
			return Dictionary.translateTown(word);
		}
		return word;
	}
}
