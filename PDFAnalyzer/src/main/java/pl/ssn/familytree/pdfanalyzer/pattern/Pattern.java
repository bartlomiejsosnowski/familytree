package pl.ssn.familytree.pdfanalyzer.pattern;

import java.util.ArrayList;
import java.util.List;

import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Element;
import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Modifier;

public class Pattern {

	private List<PatternElement> elements = new ArrayList<PatternElement>();

	public Pattern add(Modifier modifier, String word) {
		this.elements.add(new PatternElement(modifier, word));
		return this;
	}

	public Pattern add(Modifier modifier, Element element) {
		this.elements.add(new PatternElement(modifier, element));
		return this;
	}

	public Pattern add(Modifier modifier, String word, Element element) {
		this.elements.add(new PatternElement(modifier, element, word));
		return this;
	}

	public MatchedWrapper matches(List<String> list, int index) {
		MatchedWrapper result = new MatchedWrapper();
		int lastMatchedWordIndex = index;
		for (int i = 0; i < elements.size() && index < list.size(); i++, index++) {
			Matched matches = elements.get(i).matches(list.get(index));
			if (matches != null) {
				if (matches.getElement() != null) {
					lastMatchedWordIndex = index;
					result.add(matches);
				}
			} else {
				return null;
			}
		}
		result.setLastMatchedWordIndex(lastMatchedWordIndex);
		return result;
	}

	@Override
	public String toString() {
		return "Pattern [elements=" + elements + "]";
	}

}
