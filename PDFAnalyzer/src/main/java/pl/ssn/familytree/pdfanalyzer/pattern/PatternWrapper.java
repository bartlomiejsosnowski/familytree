package pl.ssn.familytree.pdfanalyzer.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatternWrapper {

	private final List<Pattern> patterns = new ArrayList<Pattern>();
	
	public List<Matched> matcher(List<String> list, int index) {
		for (Pattern pattern : patterns) {
			List<Matched> matches = pattern.matches(list, index);
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
	
	public static void main(String[] args) {
		String str1 = "córka Jana i Marianny";
		String str2 = "syn Andrzeja i Marianny";
		String str3 = "rodz.: Antoni i Marianna Majewska";
		String str4 = "rodz.: Błażej (56) i Katarzyna Gorczyk (42)";
		String str5 = "rodz.: Jakub (b.d.) i Marianna Pałyska (b.d.)";
		String str6 = "rodz.: Grzegorz (b.d.) i Jadwiga (b.d.)";
		String str7 = "rodz.: niewiadomi";
		PatternWrapper wrapper = new PatternWrapper()
			.add(new Pattern().add(Modifier.EQUAL, "córka").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME).add(Modifier.EQUAL, "i").add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME))
			.add(new Pattern().add(Modifier.EQUAL, "syn").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME).add(Modifier.EQUAL, "i").add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME).add(Modifier.EQUAL, "i").add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME).add(Modifier.FIRST_UPPERCASE, Element.MOTHER_MAIDEN_NAME))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME).add(Modifier.REGEX, "\\([0-9]{1,2}\\)", Element.FATHER_AGE).add(Modifier.EQUAL, "i").add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME).add(Modifier.FIRST_UPPERCASE, Element.MOTHER_MAIDEN_NAME).add(Modifier.REGEX, "\\([0-9]{1,2}\\)", Element.MOTHER_AGE))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME).add(Modifier.EQUAL, "(b.d.)").add(Modifier.EQUAL, "i").add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME).add(Modifier.FIRST_UPPERCASE, Element.MOTHER_MAIDEN_NAME).add(Modifier.EQUAL, "(b.d.)"))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.FIRST_UPPERCASE, Element.FATHER_NAME).add(Modifier.EQUAL, "(b.d.)").add(Modifier.EQUAL, "i").add(Modifier.FIRST_UPPERCASE, Element.MOTHER_NAME).add(Modifier.EQUAL, "(b.d.)"))
			.add(new Pattern().add(Modifier.EQUAL, "rodz.:").add(Modifier.EQUAL, "niewiadomi"));
		
		System.out.println(wrapper.matcher(Arrays.asList(str1.split(" ")), 0));
		System.out.println(wrapper.matcher(Arrays.asList(str2.split(" ")), 0));
		System.out.println(wrapper.matcher(Arrays.asList(str3.split(" ")), 0));
		System.out.println(wrapper.matcher(Arrays.asList(str4.split(" ")), 0));
		System.out.println(wrapper.matcher(Arrays.asList(str5.split(" ")), 0));
		System.out.println(wrapper.matcher(Arrays.asList(str6.split(" ")), 0));
		System.out.println(wrapper.matcher(Arrays.asList(str7.split(" ")), 0));
	}
	
	

	public static enum Modifier {
		EQUAL, FIRST_UPPERCASE, REGEX
		
	}
	public static enum Element {
		FATHER_NAME, MOTHER_NAME, MOTHER_MAIDEN_NAME, FATHER_AGE, MOTHER_AGE
		
	}
}
