# ExampleClass
Build Example class to parse conditions, execute keywords and fill up templates


### Module Architecture

```
Example 
+--conditionParser : String 형태로 된 condition을 읽고 각 변수마다 해당 Keyword를 실행하도록 routing 해주고(KeywordExecutor 사용) 결과물을dictionary형태로 반환함       
+--KeywordExecutor : choose/num/eval/sample/image의 다섯개 키워드를 실행하는 함수를 가지고 있는 wrapper                    
+--Filler : conditionParser에서 나온 dictionary와 data object를 활용해 template과 해설을 채워넣음. 
```
