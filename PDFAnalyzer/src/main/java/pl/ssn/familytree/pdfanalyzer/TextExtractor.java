package pl.ssn.familytree.pdfanalyzer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import pl.ssn.familytree.pdfanalyzer.dict.Dictionary;

public class TextExtractor {

	public TextExtractor(File file, File toFile, int startPageValue, int endPageValue, boolean sortByPosition) {
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		try {
			PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
			parser.parse();
			cosDoc = parser.getDocument();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper = new PDFTextStripper();
			pdfStripper.setSortByPosition(sortByPosition);
			pdfStripper.setStartPage(startPageValue);
			pdfStripper.setEndPage(endPageValue);

			String parsedText = pdfStripper.getText(pdDoc);
			String[] lines = parsedText.split(pdfStripper.getLineSeparator());
			StringBuilder str = new StringBuilder();
			boolean marriages = false;
			PrintWriter out = new PrintWriter(toFile);
			for (String line : lines) {
				if (line.trim().matches("^[0-9]+ \\| S t r o n a")) {
					continue;
				} else if (line.trim().startsWith("Sebastian Jędrych")) {
					continue;
				} else if (line.trim().startsWith("Wydanie 1")) {
					continue;
				} else if (line.trim().startsWith("Śluby")) {
					marriages = true;
					out.write(line);
					out.write("\r\n");
				} else if (line.trim().startsWith("Indeks")) {
					marriages = false;
					String[] parts = str.toString().split("\\d+\\.");
					for (int i = 0; i < parts.length; i++) {
						String trim = parts[i].trim();
						if (trim.length() > 0) {
							out.write(String.valueOf(i));
							out.write(". ");
							out.write(parts[i].trim());
							out.write("\r\n");
						}
					}
					str.setLength(0);
				} else {
					if (marriages)
						str.append(line).append(" ");
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		File file = new File("D:\\Users\\Bartek\\Documents\\Drzewo genealogiczne\\test.pdf");
		File toFile = new File(
				"D:\\Users\\Bartek\\Workspace\\FamilyTree\\PDFAnalyzer\\src\\main\\java\\resources\\marriages_1826-1928_.txt");
		new TextExtractor(file, toFile, 939, 1276, true);
	}
}
