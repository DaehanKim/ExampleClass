package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

//import org.apache.commons.text.StringEscapeUtils;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class readFromFile {
	
private static final List<String[]>[] List = null;

//	public static StringEscapeUtils escaper = new StringEscapeUtils();
	
	public static Reader getReader(File relativePath) throws FileNotFoundException {
		try {
			return new InputStreamReader(new FileInputStream(relativePath), "UTF8");
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
//		System.out.println(args[0]);
//		System.out.println(new File("").getAbsolutePath()+"\\"+args[0]);
		File filePath = new File("src\\sample.tsv");
		System.out.println("Current file path : "+filePath.getAbsolutePath());
//		String filePath = "C:\\Users\\daehan_kim\\eclipse-workspace\\ExampleTemplateReader\\src\\sample.tsv";
//		String filePath = getClass().getResource("/src/sample.tsv");
//		System.out.println(filePath);
//		Example example = new Example("fe","fe","{}");
		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		TsvParser parser = new TsvParser(settings);
		List<String[]> allRows;
		allRows = new ArrayList<String[]>();
		try {
			allRows = parser.parseAll(getReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		int i = 49;
//		for (int i =0; i<allRows.size() ; i++) {
//			String template = escape(allRows.get(i)[4]);
//			String condition = StringEscapeUtils.escapeJava(allRows.get(i)[5]);			
//			String condition = escape(allRows.get(i)[5]);
//			String script = allRows.get(i)[6];
		String template = escape(allRows.get(0)[0]);
//		String condition = StringEscapeUtils.escapeJava(allRows.get(1)[0]);			
		String condition = escape(allRows.get(1)[0]);
		String script = escape(allRows.get(2)[0]);
		System.out.println("condition : ");
			System.out.println(condition);

	
//			System.out.println(allRows.get(i)[5]);
//			if (!condition.contains("{")) {continue;} 
		
//			System.out.println(allRows.get(i)[0]);
			Example ex = new Example(template, script,condition);
			System.out.println("condition instance: ");
			System.out.println(ex.templateVar);
			String filledTemplate = ex.fillWithDictionary(template);
			String filledScript = ex.fillWithDictionary(script);
			System.out.println("filled template : ");
			System.out.println(filledTemplate);
			System.out.println("filled script : ");
			System.out.println(filledScript);
			
//		}
		
	}
}
