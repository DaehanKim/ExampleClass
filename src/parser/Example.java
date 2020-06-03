package parser;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class Example {
	// �ϴ� DB���� �����ٰ� �����ϰ� �׳� �־��.
	
	public Dictionary<String, ArrayList<String>> templateVar = new Hashtable(); 
	

	public JsonElement parseCondition(String jsonData) {
		
		@SuppressWarnings("deprecation")
		JsonParser parser = new JsonParser();
		@SuppressWarnings("deprecation")
		JsonElement element = parser.parse(jsonData);		
		return element;
		
	}
	
	public String replaceWithJsonElement(String inputStr,JsonElement condOutput) {
		// jsonobject �̿��ؼ� string�� �ִ°� ���� mapping��.
		String outStr = "";
		return outStr;
	}
	
	public ArrayList<String> interpretKeyword(String var, String keyword) {
//	returns output numbers or words	
		keyword = keyword.replace("\"", "");
		String[] kSplit = keyword.split("\\\\t",-1);
		ArrayList<String> ret;
		switch (kSplit[0]) {
		case "CHOOSE":
			ret = this.choose(kSplit);
			this.templateVar.put(var, ret);
			return ret;
		case "NUM":
			return this.num(kSplit);
		case "SAMPLE":
			return this.sample(kSplit);
		case "EVAL":
			return this.eval(kSplit);
		case "IMAGE":
			return this.image(kSplit);
		}
		
		return null;
		
	}
	
	
	


	private ArrayList<String> eval(String[] kSplit) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> image(String[] kSplit) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> sample(String[] kSplit) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> num(String[] kSplit) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> choose(String[] kSplit) {
		// implement choose method
		int n = Integer.parseInt(kSplit[1]);
		String[] optList = kSplit[2].split(",");
		ArrayList<String> outputList = new ArrayList<String>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		
		// sample options from candidates without replacement
		while (outputList.size() < n) {
			int rnd = new Random().nextInt(optList.length);
			if (indexList.contains(rnd)) { continue; }
			outputList.add(optList[rnd]);
			indexList.add(rnd);
		}
				
		return outputList;
	}

	private boolean checkCondition(String output, String cond) {
		// not implemented yet
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String exTemplate = "$y=[a]x+[b]$�� x��� ������ �� --> ([x1],0)\r\n" + 
				"y��� ������ �� --> (0,[y1])";
		String exDescription = "x�� 0�� ������ y����, y�� 0�� ������ x������ ���� �� �ֽ��ϴ�. ���⿡�� x������ ([x1],0), y������ (0,[y1])�� �ǰڳ׿�!";
		String exConditionJson = "{\"[a]\":\"CHOOSE\\t2\\tthis,is,-2,2,3,5\\t\",\r\n" + 
				"\"[b]\":\"NUM\\tint\\t-2,3\\t\",\r\n" + 
				"\"[x1]\":\"EVAL\\t-[b]/[a]\",\r\n" + 
				"\"[y1]\":\"EVAL\\t[b]\"\r\n" + 
				"}";
		
		
		Example parser = new Example();

		
		JsonElement condition = parser.parseCondition(exConditionJson);

		for(Map.Entry<String, JsonElement> entry : condition.getAsJsonObject().entrySet()) {
					System.out.println(entry.getKey() + " - " + entry.getValue().toString());
					ArrayList<String> out = parser.interpretKeyword(entry.getKey(),entry.getValue().toString());
					System.out.println(out);
					System.out.println(parser.templateVar);
				}

		
		
	}

	
	
}
