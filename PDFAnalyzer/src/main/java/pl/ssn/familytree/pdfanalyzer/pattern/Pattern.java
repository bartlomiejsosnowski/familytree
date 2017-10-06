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

	public List<Matched> matches(List<String> list, int index) {
		List<Matched> result = new ArrayList<Matched>();
		for (int i = 0; i < elements.size() && index < list.size(); i++, index++) {
			Matched matches = elements.get(i).matches(list.get(index));
			if (matches != null) {
				if (matches.getElement() != null) {
					result.add(matches);
				}
			} else {
				return null;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "Pattern [elements=" + elements + "]";
	}

}
