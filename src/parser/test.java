import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.ScriptContext;
import javax.script.Bindings;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class test{

	public static void main(String[] args) {
		String test = "{\"1\":\"두 변의 길이와 그 끼인각의 크기를 알 때\n${\overline{AH}}={c\ {\sin{B}}}, {\overline{BH}}={c\ {\cos{B}}}$이므로\n${\overline{CH}}={a-c\ {\cos{B}}}$\n$\therefore\ {\overline{AC}}={\sqrt{{{\overline{AH}}^2}+{{\overline{CH}}^2}}}={\sqrt{{{(c\ {\sin{B}})}^2}+{{(a-c\ {\cos{B}})}^2}}}$\",\"2\":\"한 변의 길이와 그 양 끝 각의 크기를 알 때\n${\overline{CH}}={a\ {\sin{B}}}$\n$\therefore\ {\overline{AC}}={\overline{CH}\over{\sin{A}}}={{a\ {\sin{B}}}\over{\sin{A}}}$\"}";
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(test);
		System.out.println(element);

	}

}