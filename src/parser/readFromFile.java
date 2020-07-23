package parser;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

//import org.apache.commons.text.StringEscapeUtils;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class readFromFile {
	
//	public static StringEscapeUtils escaper = new StringEscapeUtils();
	
	public static Reader getReader(String relativePath) {
		try {
			return new InputStreamReader(Example.class.getResourceAsStream(relativePath), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}
	
	public static String escape(String s){
		  return s.replace("\\", "\\\\")
		          .replace("\t", "\\t")
		          .replace("\b", "\\b")
		          .replace("\n", "\\n")
		          .replace("\r", "\\r")
		          .replace("\f", "\\f");
//		          .replace("\'", "\\'")
//		          .replace("\"", "\\\"");
		}
	
	static public void main(String[] args) {
//		Example example = new Example("fe","fe","{}");
		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		TsvParser parser = new TsvParser(settings);
		List<String[]> allRows = parser.parseAll(getReader("../lecture_unit_examples.tsv"));
		
		int i = 49;
//		for (int i =0; i<allRows.size() ; i++) {
			String template = escape(allRows.get(i)[4]);
//			String condition = StringEscapeUtils.escapeJava(allRows.get(i)[5]);			
			String condition = escape(allRows.get(i)[5]);
			String script = allRows.get(i)[6];
			System.out.println(condition);
//			System.out.println(allRows.get(i)[5]);
//			if (!condition.contains("{")) {continue;} 
		
//			System.out.println(allRows.get(i)[0]);
			Example ex = new Example(template, script,condition);
			
			String filledTemplate = ex.fillWithDictionary(template);
			String filledScript = ex.fillWithDictionary(script);
			System.out.println(filledTemplate);
			System.out.println(filledScript);
			
//		}
		
	}
}
