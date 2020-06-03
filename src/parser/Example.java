package parser;

import java.util.Map;
import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class Example {
	// 일단 DB에서 꺼낸다고 생각하고 그냥 넣어둠.
	
	

	public JsonElement parseCondition(String jsonData) {
		
		@SuppressWarnings("deprecation")
		JsonParser parser = new JsonParser();
		@SuppressWarnings("deprecation")
		JsonElement element = parser.parse(jsonData);		
		return element;
		
	}
	
	public String replaceWithJsonElement(String inputStr,JsonElement condOutput) {
		// jsonobject 이용해서 string에 있는거 전부 mapping함.
		String outStr = "";
		return outStr;
	}
	
	public String [] interpretKeyword(String keyword) {
//	returns output numbers or words	
		keyword = keyword.replace("\"", "");
		String[] kSplit = keyword.split("\\\\t",-1);
//		System.out.println(kSplit[0]);
//		for(String item: kSplit) {
//			System.out.println(item);
//		}
		switch (kSplit[0]) {
		case "CHOOSE":
			return this.choose(kSplit);
		case "NUM":
			return this.choose(kSplit);
		case "SAMPLE":
			return this.choose(kSplit);
		case "EVAL":
			return this.choose(kSplit);
		case "IMAGE":
			return this.choose(kSplit);
		}
		
		return null;
		
	}
	
	
	
	private String [] choose(String[] kSplit) {
		// TODO Auto-generated method stub
		int n = Integer.parseInt(kSplit[1]);
		String[] optList = kSplit[2].split(",");
		boolean flag = true;
		while(flag) {
		int rnd = new Random().nextInt(optList.length);
		String output = optList[rnd];
		if (kSplit[2] != null) {
//			update flag
			flag = this.checkCondition(output, kSplit[3]);
		}
		}
		return null;
	}

	private boolean checkCondition(String output, String cond) {
		// TODO Auto-generated method stub
		if (cond.length() == 0) {
			return false;
		}
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String exTemplate = "$y=[a]x+[b]$가 x축과 만나는 점 --> ([x1],0)\r\n" + 
				"y축과 만나는 점 --> (0,[y1])";
		String exDescription = "x에 0을 넣으면 y절편, y에 0을 넣으면 x절편을 구할 수 있습니다. 여기에서 x절편은 ([x1],0), y절편은 (0,[y1])가 되겠네요!";
		String exConditionJson = "{\"[a]\":\"CHOOSE\\t-1,1,-2,2\\t\",\r\n" + 
				"\"[b]\":\"NUM\\tint\\t-2,3\\t\",\r\n" + 
				"\"[x1]\":\"EVAL\\t-[b]/[a]\",\r\n" + 
				"\"[y1]\":\"EVAL\\t[b]\"\r\n" + 
				"}";
		
		
		Example parser = new Example();
		
//		System.out.println(exTemplate);
//		System.out.println(exDescription);
		
		JsonElement condition = parser.parseCondition(exConditionJson);
//		System.out.println(condition.getAsJsonObject().entrySet());
//		System.out.println(condition.getAsJsonObject().entrySet());
		for(Map.Entry<String, JsonElement> entry : condition.getAsJsonObject().entrySet()) {
					System.out.println(entry.getKey() + " - " + entry.getValue().toString());
					parser.interpretKeyword(entry.getValue().toString());
				}
//		KeywordExecutor executor = new KeywordExecutor();
//		executor.execute(condition);
//		JsonElement conditionOutput = executor.execute(condition);
//		String repTemplate = parser.replaceWithJsonElement(exTemplate, conditionOutput);
//		String repDescription = parser.replaceWithJsonElement(exDescription, conditionOutput);
		

//		System.out.println(repTemplate);
//		System.out.println(repDescription);
		
		
	}

	
	
}
