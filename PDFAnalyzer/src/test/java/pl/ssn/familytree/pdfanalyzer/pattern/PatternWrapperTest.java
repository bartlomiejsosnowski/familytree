package pl.ssn.familytree.pdfanalyzer.pattern;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import pl.ssn.familytree.pdfanalyzer.pattern.PatternWrapper.Element;

public class PatternWrapperTest {

	@Test
	public void widow() {
		{
			String str = "wdowiec po Teresie z Wójcików";
			MatchedWrapper matcher = PatternWrapper.WIDOW_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Teresa", matcher.getByElement(Element.WIFE_NAME));
			assertEquals("Wójcików", matcher.getByElement(Element.WIFE_MAIDEN_NAME));
		}
		{
			String str = "wdowiec po Teresie Wójcik";
			MatchedWrapper matcher = PatternWrapper.WIDOW_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Teresa", matcher.getByElement(Element.WIFE_NAME));
			assertEquals("Wójcik", matcher.getByElement(Element.WIFE_MAIDEN_NAME));
		}
	}

	@Test
	public void parents() {
		{
			String str = "córka Jana i Marianny";
			MatchedWrapper matcher = PatternWrapper.PARENT_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Jan", matcher.getByElement(Element.FATHER_NAME));
			assertEquals("Marianna", matcher.getByElement(Element.MOTHER_NAME));
		}
		{
			String str = "syn Andrzeja i Marianny";
			MatchedWrapper matcher = PatternWrapper.PARENT_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Andrzej", matcher.getByElement(Element.FATHER_NAME));
			assertEquals("Marianna", matcher.getByElement(Element.MOTHER_NAME));
		}
		{
			String str = "rodz.: Antoni i Marianna Majewska";
			MatchedWrapper matcher = PatternWrapper.PARENT_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Antoni", matcher.getByElement(Element.FATHER_NAME));
			assertEquals("Marianna", matcher.getByElement(Element.MOTHER_NAME));
			assertEquals("Majewska", matcher.getByElement(Element.MOTHER_MAIDEN_NAME));
		}
		{
			String str = "rodz.: Błażej (56) i Katarzyna Gorczyk (42)";
			MatchedWrapper matcher = PatternWrapper.PARENT_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Błażej", matcher.getByElement(Element.FATHER_NAME));
			assertEquals("(56)", matcher.getByElement(Element.FATHER_AGE));
			assertEquals("Katarzyna", matcher.getByElement(Element.MOTHER_NAME));
			assertEquals("(42)", matcher.getByElement(Element.MOTHER_AGE));
			assertEquals("Gorczyk", matcher.getByElement(Element.MOTHER_MAIDEN_NAME));
		}
		{
			String str = "rodz.: Jakub (b.d.) i Marianna Pałyska (b.d.)";
			MatchedWrapper matcher = PatternWrapper.PARENT_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Jakub", matcher.getByElement(Element.FATHER_NAME));
			assertEquals("Marianna", matcher.getByElement(Element.MOTHER_NAME));
			assertEquals("Pałyska", matcher.getByElement(Element.MOTHER_MAIDEN_NAME));
		}
		{
			String str = "rodz.: Grzegorz (b.d.) i Jadwiga (b.d.)";
			MatchedWrapper matcher = PatternWrapper.PARENT_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Grzegorz", matcher.getByElement(Element.FATHER_NAME));
			assertEquals("Jadwiga", matcher.getByElement(Element.MOTHER_NAME));
		}
		{
			String str = "rodz.: niewiadomi";
			MatchedWrapper matcher = PatternWrapper.PARENT_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals(null, matcher.getByElement(Element.FATHER_NAME));
			assertEquals(null, matcher.getByElement(Element.MOTHER_NAME));
		}
	}

	@Test
	public void towns() {
		{
			String str = "z Rębkowa";
			MatchedWrapper matcher = PatternWrapper.TOWN_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Rębków", matcher.getByElement(Element.TOWN));
		}
		{
			String str = "z Rudy Talubskiej";
			MatchedWrapper matcher = PatternWrapper.TOWN_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Ruda Talubska", matcher.getByElement(Element.TOWN));
		}
		{
			String str = "zam w Rębkowie";
			MatchedWrapper matcher = PatternWrapper.TOWN_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Rębków", matcher.getByElement(Element.TOWN));
		}
		{
			String str = "zam w Rudzie Talubskiej";
			MatchedWrapper matcher = PatternWrapper.TOWN_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Ruda Talubska", matcher.getByElement(Element.TOWN));
		}
	}
	
	@Test
	public void birthTowns() {
		{
			String str = "ur w Górkach w par Górki";
			MatchedWrapper matcher = PatternWrapper.BIRTH_TOWN_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Górki", matcher.getByElement(Element.TOWN));
			assertEquals("Górki", matcher.getByElement(Element.PARISH_TOWN));
		}
		{
			String str = "ur w Górkach";
			MatchedWrapper matcher = PatternWrapper.BIRTH_TOWN_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Górki", matcher.getByElement(Element.TOWN));
		}
		{
			String str = "ur w Rudzie Tarnowskiej";
			MatchedWrapper matcher = PatternWrapper.BIRTH_TOWN_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Ruda Tarnowska", matcher.getByElement(Element.TOWN));
		}
		{
			String str = "ur w Rudzie Tarnowskiej w par Górki";
			MatchedWrapper matcher = PatternWrapper.BIRTH_TOWN_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Ruda Tarnowska", matcher.getByElement(Element.TOWN));
			assertEquals("Górki", matcher.getByElement(Element.PARISH_TOWN));
		}
		{
			String str = "ur w Zwoli w par Zwola";
			MatchedWrapper matcher = PatternWrapper.BIRTH_TOWN_PATTERN.matcher(Arrays.asList(str.split(" ")), 0);
			assertEquals("Zwola", matcher.getByElement(Element.TOWN));
			assertEquals("Zwola", matcher.getByElement(Element.PARISH_TOWN));
		}
	}
}
