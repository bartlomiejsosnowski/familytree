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
		assertEquals("Sosnowska", Dictionary.translateLastName("Sosnowski"));
		assertEquals("Sosnowski", Dictionary.translateLastName("Sosnowska"));
		assertEquals("Kamaszewski", Dictionary.translateLastName("Kamaszewska"));
		assertEquals("Zadrożny", Dictionary.translateLastName("Zadrożna"));
		assertEquals("Zadrożna", Dictionary.translateLastName("Zadrożny"));
	}
	
	@Test
	public void town() {
		assertEquals("Garwolin", Dictionary.translateTown("Garwolinie"));
	}
}
