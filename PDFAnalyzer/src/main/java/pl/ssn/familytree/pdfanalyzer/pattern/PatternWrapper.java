package pl.ssn.familytree.pdfanalyzer.pattern;

import java.util.ArrayList;
import java.util.List;

public class PatternWrapper {
	
	public static final PatternWrapper WIDOW_PATTERN = new PatternWrapper()
			.add(new Pattern().add(Modifier.EQUAL, "wdowiec").add(Modifier.EQUAL, "po")
					.add(Modifier.FIRST_UPPERCASE, Element.WIFE_NAME, true)
					.add(Modifier.FIRST_UPPERCASE, Element.WIFE_MAIDEN_NAME))
			.add(new Pattern().add(Modifier.EQUAL, "wdowiec").add(Modifier.EQUAL, "po")
					.add(Modifier.FIRST_UPPERCASE, Element.WIFE_NAME, true).add(Modifier.EQUAL, "z")
					.add(Modifier.FIRST_UPPERCASE, Element.WIFE_MAIDEN_NAME, true));
	
	public static final PatternWrapper PARENT_PATTERN = new PatternWrapper()
			.add(new Pattern().add(Modifier.EQUAL, "córka").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME, true)
					.add(Modifier.EQUAL, "i").add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME, true))
			.add(new Pattern().add(Modifier.EQUAL, "syn").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME, true)
					.add(Modifier.EQUAL, "i").add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME, true))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME)
					.add(Modifier.EQUAL, "i").add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME)
					.add(Modifier.FIRST_UPPERCASE, Element.MOTHER_MAIDEN_NAME))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME)
					.add(Modifier.REGEX, "\\([0-9]{1,2}\\)", Element.FATHER_AGE).add(Modifier.EQUAL, "i")
					.add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME)
					.add(Modifier.FIRST_UPPERCASE, Element.MOTHER_MAIDEN_NAME)
					.add(Modifier.REGEX, "\\([0-9]{1,2}\\)", Element.MOTHER_AGE))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME)
					.add(Modifier.EQUAL, "(b.d.)").add(Modifier.EQUAL, "i")
					.add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME)
					.add(Modifier.FIRST_UPPERCASE, Element.MOTHER_MAIDEN_NAME).add(Modifier.EQUAL, "(b.d.)"))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME)
					.add(Modifier.EQUAL, "(b.d.)").add(Modifier.EQUAL, "i")
					.add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME).add(Modifier.EQUAL, "(b.d.)"))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.EQUAL, "niewiadomi"));
	
	public static final PatternWrapper TOWN_PATTERN = new PatternWrapper()
			.add(new Pattern(true).add(Modifier.EQUAL, "z").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true).add(Modifier.FIRST_UPPERCASE, Element.TOWN, true))
			.add(new Pattern().add(Modifier.EQUAL, "z").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true))
			.add(new Pattern(true).add(Modifier.EQUAL, "zam").add(Modifier.EQUAL, "w").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true).add(Modifier.FIRST_UPPERCASE, Element.TOWN, true))
			.add(new Pattern().add(Modifier.EQUAL, "zam").add(Modifier.EQUAL, "w").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true));
	
	public static final PatternWrapper BIRTH_TOWN_PATTERN = new PatternWrapper()
			.add(new Pattern(true).add(Modifier.EQUAL, "ur").add(Modifier.EQUAL, "w").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true).add(Modifier.FIRST_UPPERCASE, Element.TOWN, true).add(Modifier.EQUAL, "w").add(Modifier.EQUAL, "par").add(Modifier.FIRST_UPPERCASE, Element.PARISH_TOWN).add(Modifier.FIRST_UPPERCASE, Element.PARISH_TOWN))
			.add(new Pattern(true).add(Modifier.EQUAL, "ur").add(Modifier.EQUAL, "w").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true).add(Modifier.FIRST_UPPERCASE, Element.TOWN, true).add(Modifier.EQUAL, "w").add(Modifier.EQUAL, "par").add(Modifier.FIRST_UPPERCASE, Element.PARISH_TOWN))
			.add(new Pattern(true).add(Modifier.EQUAL, "ur").add(Modifier.EQUAL, "w").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true).add(Modifier.EQUAL, "w").add(Modifier.EQUAL, "par").add(Modifier.FIRST_UPPERCASE, Element.PARISH_TOWN).add(Modifier.FIRST_UPPERCASE, Element.PARISH_TOWN))
			.add(new Pattern(true).add(Modifier.EQUAL, "ur").add(Modifier.EQUAL, "w").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true).add(Modifier.EQUAL, "w").add(Modifier.EQUAL, "par").add(Modifier.FIRST_UPPERCASE, Element.PARISH_TOWN))
			.add(new Pattern(true).add(Modifier.EQUAL, "ur").add(Modifier.EQUAL, "w").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true).add(Modifier.FIRST_UPPERCASE, Element.TOWN, true))
			.add(new Pattern(true).add(Modifier.EQUAL, "ur").add(Modifier.EQUAL, "w").add(Modifier.FIRST_UPPERCASE, Element.TOWN, true));

	private final List<Pattern> patterns = new ArrayList<Pattern>();
	
	public MatchedWrapper matcher(List<String> list, int index) {
		for (Pattern pattern : patterns) {
			MatchedWrapper matches = pattern.matches(list, index);
			if(matches != null) {
				return matches;
			}
		}
		return null;
	}

	public PatternWrapper add(Pattern pattern) {
		this.patterns.add(pattern);
		return this;
	}
	
	public static enum Modifier {
		EQUAL, FIRST_UPPERCASE, REGEX
		
	}
	public static enum Element {
		FATHER_NAME, MOTHER_NAME, MOTHER_MAIDEN_NAME, FATHER_AGE, MOTHER_AGE, WIFE_NAME, WIFE_MAIDEN_NAME, TOWN, PARISH_TOWN;
		
	}
}
