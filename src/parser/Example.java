package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.ScriptContext;
import javax.script.Bindings;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class Example {
	// �ϴ� DB���� �����ٰ� �����ϰ� �׳� �־��.
	
	public Dictionary<String, ArrayList<String>> templateVar = new Hashtable(); 
	
	private String exTemplate;
	private String exDescription;
	private String exConditionJson;
	Example(String exTemplate, String exDescription, String exConditionJson){
		this.exTemplate=exTemplate;
		this.exDescription=exDescription;
		this.exConditionJson=exConditionJson;
		JsonElement condition = this.parseCondition();
// iterate through conditions and make template variable hashtable
		for(Map.Entry<String, JsonElement> entry : condition.getAsJsonObject().entrySet()) {
					ArrayList<String> out = this.interpretKeyword(entry.getKey(),entry.getValue().toString());
				}
	}

	public String getExTemplate(){
		return this.exTemplate;
	}
	public String getExDescription(){
		return this.exDescription;
	}
	public String getExConditionJson(){
		return this.exConditionJson;
	}

	public JsonElement parseCondition() {
		
		@SuppressWarnings("deprecation")
		JsonParser parser = new JsonParser();
		@SuppressWarnings("deprecation")
		JsonElement element = parser.parse(this.exConditionJson);		
		return element;
		
	}
		
	public ArrayList<String> interpretKeyword(String var, String keyword) {
//	returns output numbers or words	
		keyword = keyword.replace("\"", "");
		String[] kSplit = keyword.split("\\\\t",-1);
		ArrayList<String> ret;
		switch (kSplit[0]) {
		case "CHOOSE":
			while(true){
				ret = this.choose(kSplit);
				if(checkCondition(ret,kSplit[3]))
					break;
				System.out.println("Don't meet the conditions "+ret.get(0));
			}
			this.templateVar.put(var, ret);
			return ret;
		case "NUM":
			while(true){
				ret = this.num(kSplit);
				if(checkCondition(ret,kSplit[3]))
					break;
				System.out.println("Don't meet the conditions "+ret.get(0));
			}
			this.templateVar.put(var, ret);
			return ret;
		case "SAMPLE":
			while(true){
				ret = this.sample(kSplit);
				if(checkCondition(ret,kSplit[3]))
					break;
				System.out.println("Don't meet the conditions "+ret.get(0));
			}
			this.templateVar.put(var, ret);
			return ret;
		case "EVAL":
			ret = this.eval(kSplit);
			this.templateVar.put(var, ret);
			return ret;
		case "IMAGE":
			ret = this.image(kSplit);
			this.templateVar.put(var, ret);
			return ret;

		}
		
		return null;
		
	}

	private ArrayList<String> eval(String[] kSplit) {
		// --�� +�̹Ƿ� ����
		String foo = this.fillWithDictionary(kSplit[1]).replace("--","");
		ArrayList<String> ret = new ArrayList<String> ();
		try{
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			//engine.eval("require('core-js')");
			ret.add(String.valueOf(engine.eval(foo)));
			}catch(ScriptException e){
				System.err.println("ERROR "+e.getMessage());
		}
		
		return ret;
	}

	private ArrayList<String> image(String[] kSplit) {
		ArrayList<String> ret = new ArrayList<String> ();
		ret.add(kSplit[1]);
		return ret;
	}

	private ArrayList<String> sample(String[] kSplit) {
		ArrayList<String> ret = new ArrayList<String> ();
		int n = Integer.parseInt(this.fillWithDictionary(kSplit[1]));
		String slc = kSplit[2].substring(4, kSplit[2].length()-1);
		slc=this.fillWithDictionary(slc);
		String[] slcs = slc.split(",");
		Random random = new Random();
		double sample=0;
		int prev=Integer.parseInt(slcs[0]);
		int next=Integer.parseInt(slcs[1]);
		if(kSplit[2].contains("nor")){
			for(int i=0;i<n;i++)
			{
				sample = random.nextGaussian()*next+prev;
				if(kSplit[3].equals("nodup"))
				{
					if(ret.contains(Integer.toString((int)sample)))
						i--;
					else
						ret.add(Integer.toString((int)sample));
				}
				else
					ret.add(Integer.toString((int)sample));
			}
		}
		else if(kSplit[2].contains("Uni")){
			for(int i=0;i<n;i++)
			{
				sample = random.nextDouble()*(next-prev);
				sample+=(double)prev;
				if(kSplit[3].equals("nodup"))
				{
					if(ret.contains(Integer.toString((int)sample)))
						i--;
					else
						ret.add(Integer.toString((int)sample));
				}
				else
					ret.add(Integer.toString((int)sample));
			}
		}
		return ret;
	}

	private ArrayList<String> num(String[] kSplit) {		
		String type = kSplit[1];
		String[] optList = this.fillWithDictionary(kSplit[2]).split(",");
		Random random = new Random();
		ArrayList<String> ret = new ArrayList<String> ();
		if(type.equals("int")){
			int number=0;
			number = random.nextInt(Integer.parseInt(optList[1])-Integer.parseInt(optList[0]));
			number+=Integer.parseInt(optList[0]);
			ret.add(Integer.toString(number));
		}
		else if(type.equals("Dec")){
			float number=0;
			number = random.nextFloat()*(Integer.parseInt(optList[1])-Integer.parseInt(optList[0]));
			number+=(float)Integer.parseInt(optList[0]);
			ret.add(String.format("%.1f",number));	
		}
		else if(type.equals("rat")){
			int number=0;int number2=0;
			//�и�� 2, 3, 5, 7�� ����
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
			//�м� �ϴ� frac���� ����
			ret.add(String.format("\\frac{%s}{%s}",Integer.toString(number),Integer.toString(number2)));
		}
		return ret;
	}

	private ArrayList<String> choose(String[] kSplit) {
		// implement choose method
		int n = Integer.parseInt(this.fillWithDictionary(kSplit[1]));
		String[] optList = this.fillWithDictionary(kSplit[2]).split(",");
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

	private boolean checkCondition(ArrayList<String> outputs, String cond) {
		if(cond.equals(""))
			return true;
		String filledCond = this.fillWithDictionary(cond);
		String[] conds = filledCond.split(",");
		Float sum = 0f;
		for(int i=0;i<outputs.size();i++){
			Float output = Float.parseFloat(outputs.get(i));
			sum+=output;
			for(int j=0;j<conds.length;j++)
			{
				//�м� ó���� ���� eval����
				Float checker=null;
				try{
					checker=Float.parseFloat(conds[j].substring(conds[j].indexOf('(')+1,conds[j].indexOf(')')));
				}catch(NumberFormatException e){
					try{
						ScriptEngineManager mgr = new ScriptEngineManager();
						ScriptEngine engine = mgr.getEngineByName("JavaScript");							
						checker=Float.parseFloat(String.valueOf(engine.eval(conds[j].substring(conds[j].indexOf('(')+1,conds[j].indexOf(')')))));
						}catch(ScriptException se){
							System.err.println("ERROR "+se.getMessage());
					}

				}
				//condition check
				//x�� �ٸ�
				if(conds[j].contains("neq")){
					if(output.compareTo(checker)==0)
						return false;
				}
				//x���� ����
				else if(conds[j].contains("lt")){
					if(output.compareTo(checker)==0||output.compareTo(checker)==1)
						return false;
				}
				//�Ҽ� ���� ��
				else if(conds[j].contains("isPrime")){
					for(int k=2;k<output/2;k++){
						if(output.intValue()%k==0)
							return false;
					}
				}
			}
		}
		for(int j=0;j<conds.length;j++)
		{
			//�м� ó���� ���� eval����
			Float checker=null;
			try{
				checker=Float.parseFloat(conds[j].substring(conds[j].indexOf('(')+1,conds[j].indexOf(')')));
			}catch(NumberFormatException e){
				try{
					ScriptEngineManager mgr = new ScriptEngineManager();
					ScriptEngine engine = mgr.getEngineByName("JavaScript");							
					checker=Float.parseFloat(String.valueOf(engine.eval(conds[j].substring(conds[j].indexOf('(')+1,conds[j].indexOf(')')))));
					}catch(ScriptException se){
						System.err.println("ERROR "+se.getMessage());
				}

			}
			//condition check
			//output���� ���� x������
			if(conds[j].contains("sum")){
				if(sum.compareTo(checker)!=0)
					return false;
			}
		}
		return true;
	}

	public String fillWithDictionary(String template) {
		String filledTemplate = new String(template);
		Enumeration<String> keySet = this.templateVar.keys();
		while (keySet.hasMoreElements()) {
			String key = keySet.nextElement();
			// System.out.println("key : "+key);
			// if key is not in the template, pass on
			if (!template.contains(key)) {continue;}
			// if key is in the template, replace it with actual values
			StringBuilder valString = new StringBuilder();
			for (int i =0; i < this.templateVar.get(key).size(); i++) {
				valString.append(this.templateVar.get(key).get(i));
				if (i < this.templateVar.get(key).size()-1)
					{valString.append(", ");}
			}
			// System.out.println("converted string : "+valString.toString());
			filledTemplate = filledTemplate.replace(key, valString.toString());
			
		}

		return filledTemplate;
	}
	
	public String arrayToString(ArrayList<String> values){
		String ret ="";
		for(String s : values)
			ret+=", " + s;
		ret=ret.replaceFirst(", ","");
		return ret;
	}

	//������ ǥ�� ����� ���� ������
	public ArrayList<String> makeVar(){
		if(this.templateVar.get("[�ڷ�]")!=null)
		{
			ArrayList<String> samples = new ArrayList<String>();
			samples.add(arrayToString(this.templateVar.get("[����]")));
			samples.add(arrayToString(this.templateVar.get("[����]")));
			samples.add(arrayToString(this.templateVar.get("[�ڷ�]")));
			return samples;
		}
		System.err.println("ERROR: doesn't have data");
		return null;
	}

	//�ٱ�� �� �׸��� ǥ�� ����� ���� ������
	public ArrayList<String> makeSal(){
		if(this.templateVar.get("[�ڷ�]")!=null)
		{
			ArrayList<String> samples = new ArrayList<String>();
			samples.add(arrayToString(this.templateVar.get("[����]")));
			samples.add(arrayToString(this.templateVar.get("[����]")));
			samples.add("�ٱ�");
			samples.add("��");
			String[] temp = arrayToString(this.templateVar.get("[�ڷ�]")).split(", ");
			Arrays.sort(temp);
			String stems="",leaves="";
			for(int i=0;i<temp.length;i++){		
				if(stems.contains(temp[i].substring(0,1)))
					leaves+=" "+temp[i].substring(1,2);
				else
				{
					stems+="\n"+temp[i].substring(0,1);
					leaves+="\n"+temp[i].substring(1,2);
				}
			}
			stems=stems.replaceFirst("\n","");
			leaves=leaves.replaceFirst("\n","");
			samples.add(stems);
			samples.add(leaves);
			return samples;
		}
		System.err.println("ERROR: doesn't have data");
		return null;
	}

	//��������ǥ�� ����� ���� ������
	public ArrayList<String> makeFdt(){
		if(this.templateVar.get("[�ڷ�]")!=null)
		{
			ArrayList<String> samples = new ArrayList<String>();
			samples.add(String.format("%s(%s)", arrayToString(this.templateVar.get("[����]")), arrayToString(this.templateVar.get("[����]"))));
			samples.add("����(��)");
			String[] frequency = arrayToString(this.templateVar.get("[�ڷ�]")).split(", ");
			for(int i=0;i<frequency.length;i++){
				int lvalue = Integer.parseInt(arrayToString(this.templateVar.get("[�ּڰ�]")))+i*Integer.parseInt(arrayToString(this.templateVar.get("[����� ũ��]")));
				int rvalue = Integer.parseInt(arrayToString(this.templateVar.get("[�ּڰ�]")))+(i+1)*Integer.parseInt(arrayToString(this.templateVar.get("[����� ũ��]")));
				samples.add(String.format("%d ~ %d", lvalue, rvalue));
				samples.add(frequency[i]);
			}
			samples.add("�հ�");
			samples.add(arrayToString(this.templateVar.get("[������ ����]")));
			return samples;
		}
		System.err.println("ERROR: doesn't have data");
		return null;
	}

	//��뵵������ǥ�� ����� ���� ������
	public ArrayList<String> makeRfdt(){		
		if(this.templateVar.get("[�ڷ�]")!=null)
		{
			ArrayList<String> samples = new ArrayList<String>();
			samples.add(String.format("%s(%s)", arrayToString(this.templateVar.get("[����]")), arrayToString(this.templateVar.get("[����]"))));
			samples.add("����(��)");
			samples.add("��뵵��");
			String[] frequency = arrayToString(this.templateVar.get("[�ڷ�]")).split(", ");
			for(int i=0;i<frequency.length;i++){
				int lvalue = Integer.parseInt(arrayToString(this.templateVar.get("[�ּڰ�]")))+i*Integer.parseInt(arrayToString(this.templateVar.get("[����� ũ��]")));
				int rvalue = Integer.parseInt(arrayToString(this.templateVar.get("[�ּڰ�]")))+(i+1)*Integer.parseInt(arrayToString(this.templateVar.get("[����� ũ��]")));
				samples.add(String.format("%d ~ %d", lvalue, rvalue));
				samples.add(frequency[i]);
				samples.add(String.format("$\\frac{%s}{%s}$", frequency[i], arrayToString(this.templateVar.get("[������ ����]"))));
			}
			samples.add("�հ�");
			samples.add(arrayToString(this.templateVar.get("[������ ����]")));
			samples.add("1");
			return samples;
		}
		System.err.println("ERROR: doesn't have data");
		return null;
	}

	//���� ǥ�� ����� ���� ������
	public ArrayList<String> makeDev(){
		if(this.templateVar.get("[�ڷ�]")!=null)
		{
			ArrayList<String> samples = new ArrayList<String>();
			samples.add("����");
			String[] frequency = arrayToString(this.templateVar.get("[�ڷ�]")).split(", ");
			int sum=0;
			for(int i=0;i<frequency.length;i++){
				samples.add(frequency[i]);
				sum+=Integer.parseInt(frequency[i]);
			}
			double mean = (double)sum/frequency.length;
			samples.add("����");
			for(int i=0;i<frequency.length;i++)
				samples.add(String.format("%.1f", Double.valueOf(frequency[i])-mean));
			return samples;
		}
		System.err.println("ERROR: doesn't have data");
		return null;
	}

	//�ΰ��� ������ ǥ�� ����� ���� ������
	public ArrayList<String> makeDoubleVar(){
		if(this.templateVar.get("[�ڷ�1]")!=null || this.templateVar.get("[�ڷ�2]")!=null)
		{
			ArrayList<String> samples = new ArrayList<String>();
			samples.add(arrayToString(this.templateVar.get("[����]")));
			int num = Integer.parseInt(arrayToString(this.templateVar.get("[����]")));
			for(int i=0;i<num;i++)
				samples.add(Character.toString((char)(i+65)));
			samples.add(String.format("%s(%s)", arrayToString(this.templateVar.get("[����1]")), arrayToString(this.templateVar.get("[����]"))));
			for(String res : this.templateVar.get("[�ڷ�1]"))
				samples.add(res);
			samples.add(String.format("%s(%s)", arrayToString(this.templateVar.get("[����2]")), arrayToString(this.templateVar.get("[����]"))));
			for(String res : this.templateVar.get("[�ڷ�2]"))
				samples.add(res);
			return samples;
		}
		System.err.println("ERROR: doesn't have data");
		return null;
	}

	//�ΰ��� ��뵵������ǥ�� ����� ���� ������
	public ArrayList<String> makeDoubleRfdt(){		
		if(this.templateVar.get("[�ڷ�]")!=null)
		{
			ArrayList<String> samples = new ArrayList<String>();
			samples.add(String.format("%s(%s)", arrayToString(this.templateVar.get("[����]")), arrayToString(this.templateVar.get("[����]"))));
			samples.add("����(��)");
			samples.add("��뵵��");
			samples.add("����(��)");
			samples.add("��뵵��");
			String[] frequency = arrayToString(this.templateVar.get("[�ڷ�]")).split(", ");
			String[] frequency2 = arrayToString(this.templateVar.get("[�ڷ�2]")).split(", ");
			for(int i=0;i<frequency.length;i++){
				int lvalue = Integer.parseInt(arrayToString(this.templateVar.get("[�ּڰ�]")))+i*Integer.parseInt(arrayToString(this.templateVar.get("[����� ũ��]")));
				int rvalue = Integer.parseInt(arrayToString(this.templateVar.get("[�ּڰ�]")))+(i+1)*Integer.parseInt(arrayToString(this.templateVar.get("[����� ũ��]")));
				samples.add(String.format("%d ~ %d", lvalue, rvalue));
				samples.add(frequency[i]);
				samples.add(String.format("$\\frac{%s}{%s}$", frequency[i], arrayToString(this.templateVar.get("[������ ����]"))));
				samples.add(frequency2[i]);
				samples.add(String.format("$\\frac{%s}{%s}$", frequency2[i], arrayToString(this.templateVar.get("[������ ����2]"))));
			}
			samples.add("�հ�");
			samples.add(arrayToString(this.templateVar.get("[������ ����]")));
			samples.add("1");
			samples.add(arrayToString(this.templateVar.get("[������ ����2]")));
			samples.add("1");
			return samples;
		}
		System.err.println("ERROR: doesn't have data");
		return null;
	}

	public static void main(String[] args) {
		Example parser = new Example("���� ���� ����\r\n" + 
				"1. �����ϴ� �� Ȧ���� ���� [x1]�� ��, ���� ū Ȧ���� ���Ͻÿ�.\r\n" + 
				"2. �����ϴ� �� Ȧ���� $x-2, x, x+2$��� �ϸ�,\r\n" + 
				"3.  $(x-2) + x + (x+2) = [x1] \\rightarrow 3x = [x1] \\rightarrow x = [x2] $\r\n" + 
				"4. ���� �����ϴ� �� Ȧ���� [x3], [x2], [x4]�̹Ƿ� ���� ū Ȧ���� [x4]�̴�.",
				"1. ������ ���� �����ϴ� �� Ȧ����� ���� ���ɴϴ�.\r\n" + 
				"2. �� Ȧ���� ��� �ִ� ���� ������� �ϸ�, ���� Ȧ���� ���� ���̳ʽ� 2, ū Ȧ���� ���� �÷��� 2�� �˴ϴ�.\r\n" + 
				"3. ���� �� ���� ���ؼ� [x1]�� �ȴٰ� �߱� ������ ���� �̷��� ���� �� �ְ���? �������� Ǯ�� ������ [x2]�Դϴ�.\r\n" + 
				"4. �����ϴ� �� Ȧ���� ���� ū Ȧ���� �׷� �����? �ٷ� [x2]�� 2�� ���� [x4]�� �˴ϴ�.",
				"{\"[x1]\":\"CHOOSE\\t1\\t39,42,45\\t\", \r\n" + 
				"\"[x2]\":\"EVAL\\t[x1]/3\", \r\n" + 
				"\"[x3]\":\"EVAL\\t[x2]-2\", \r\n" + 
				"\"[x4]\":\"EVAL\\t[x2]+2\"}");

		
		
		String filledTemplate = parser.fillWithDictionary(parser.getExTemplate());
		String filledScript = parser.fillWithDictionary(parser.getExDescription());
		System.out.println("condition : ");
		System.out.println("{\"[x1]\":\"CHOOSE\\t1\\t39,42,45\\t\"," + 
				"\"[x2]\":\"EVAL\\t[x1]/3\", " + 
				"\"[x3]\":\"EVAL\\t[x2]-2\", " + 
				"\"[x4]\":\"EVAL\\t[x2]+2\"}");
		
		
		System.out.println("filled up template : \n"+filledTemplate);
		System.out.println("filled up script : \n"+filledScript);		
		
//		"sample" keyword debugger
		
//		ArrayList<String> samples =  parser.makeDoubleRfdt();
//		for(String sample : samples)
//			System.out.println(sample);
	}

	
	
}
