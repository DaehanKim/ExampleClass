package parser;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class Example {
	// 일단 DB에서 꺼낸다고 생각하고 그냥 넣어둠.
	
	public Dictionary<String, ArrayList<String>> templateVar = new Hashtable(); 
	

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
			ret = this.num(kSplit);
			this.templateVar.put(var, ret);
			return ret;
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
		String type = kSplit[1];
		String[] optList = kSplit[2].split(",");
		Random random = new Random();
		ArrayList<String> ret = new ArrayList<String> ();
		if(type.equals("int")){
			int number=0;
			number = random.nextInt(Integer.parseInt(optList[1])-Integer.parseInt(optList[0])+1);
			number+=Integer.parseInt(optList[0]);
			ret.add(Integer.toString(number));
		}
		else if(type.equals("Dec")){
			float number=0;
			number = random.nextFloat()*(Integer.parseInt(optList[1])+Integer.parseInt(optList[0]));
			number-=(float)Integer.parseInt(optList[0]);
			ret.add(String.valueOf(number));
		}
		else if(type.equals("rat")){
			int number=0;int number2=0;
			ArrayList<Integer> denominator = new ArrayList<Integer> ();
			denominator.add(2);
			denominator.add(3);
			denominator.add(5);
			denominator.add(7);
			while(true){
				number2=denominator.get(random.nextInt(4));
				number=random.nextInt((Integer.parseInt(optList[1])-Integer.parseInt(optList[0]))*number2)+Integer.parseInt(optList[0])*number2;
				if(number%number2!=0)
					break;
			}
			System.out.println(number);
			System.out.println(number2);
			ret.add(String.format("\\frac{%s}{%s}",Integer.toString(number),Integer.toString(number2)));
		}
		return ret;
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
		String exTemplate = "$y=[a]x+[b]$가 x축과 만나는 점 --> ([x1],0)\r\n" + 
				"y축과 만나는 점 --> (0,[y1])";
		String exDescription = "x에 0을 넣으면 y절편, y에 0을 넣으면 x절편을 구할 수 있습니다. 여기에서 x절편은 ([x1],0), y절편은 (0,[y1])가 되겠네요!";
		String exConditionJson = "{\"[a]\":\"CHOOSE\\t2\\tthis,is,-2,2,3,5\\t\",\r\n" + 
				"\"[b]\":\"NUM\\trat\\t-2,3\\t\",\r\n" + 
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
