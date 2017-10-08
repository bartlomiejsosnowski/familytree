package pl.ssn.familytree.pdfanalyzer.dict.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.ssn.familytree.pdfanalyzer.dict.Dictionary;

public class DictionaryTest {

	@Test
	public void firstNames() {
		assertEquals("Bartłomiej", Dictionary.translateFirstName("Bartłomieja"));
	}
	@Test
	public void lastNames() {
		assertEquals("Sosnowska", Dictionary.translateLastNameSex("Sosnowski"));
		assertEquals("Sosnowski", Dictionary.translateLastNameSex("Sosnowska"));
		assertEquals("Kamaszewski", Dictionary.translateLastNameSex("Kamaszewska"));
		assertEquals("Zadrożny", Dictionary.translateLastNameSex("Zadrożna"));
		assertEquals("Zadrożna", Dictionary.translateLastNameSex("Zadrożny"));
	}
	
	@Test
	public void town() {
		assertEquals("Garwolin", Dictionary.translateTown("Garwolinie"));
	}
	@Test
	public void lastNamesInCase() {
		assertEquals("Zadrożna", Dictionary.translateLastNameCase("Zadrożnych"));
		assertEquals("Pawliszewska", Dictionary.translateLastNameCase("Pawliszewskich"));
		assertEquals("Wójcik", Dictionary.translateLastNameCase("Wójcików"));
		assertEquals("Zarzycka", Dictionary.translateLastNameCase("Zarzyckich"));
	}
}
