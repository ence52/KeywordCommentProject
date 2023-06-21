import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.SourceVersion;



public class Project1 {
	  
	public static void main(String[] args) throws IOException{
		
		File reportFile = new File("output.txt");
		reportFile.createNewFile();
		FileWriter writeReportFile = new FileWriter(reportFile);
		writeReportFile.write(removeComments("input.txt"));
		writeReportFile.close();
		System.out.println("Output file has been generated!\n");
		
		
		System.out.println("---------------------------------\nBuilt-in Contructs:\n");
		detectBuiltInStruct("output.txt");
		System.out.println("---------------------------------\nMethods:\n");
		detectMethods("output.txt");
		System.out.println("---------------------------------\nLoops:\n");
		detectLoops("output.txt");
	
	}
	
	static  String readFile(String path) throws IOException {
		 
		FileReader file = new FileReader(path);
		String st ="" ;
		
		int i ;
		while((i=file.read()) != -1) {	
			 st += (char)i; 							
		}
		file.close();
		return st;
	 }
	
	 static String removeComments(String path) throws IOException {
		String sourceCode = readFile(path);
		String commentRemovedCode="";
		boolean isSingleLine= false;
		boolean isMultiLine=false;
		
		for(int i = 0;i<sourceCode.length();i++) {
			
			if (isSingleLine==true&&sourceCode.charAt(i)=='\n') {
				isSingleLine= false;
				i++;
			}
			else if (isMultiLine==true&&sourceCode.charAt(i)=='*'&&sourceCode.charAt(i+1)=='/') {
				isMultiLine= false;	
				i+=2;
			}
			else if (isMultiLine || isSingleLine)
				continue;
			
			if (sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='/') {
				isSingleLine = true;
				i++;
			}
			else if (sourceCode.charAt(i)=='/'&&sourceCode.charAt(i+1)=='*') {
				isMultiLine = true;

			}
			else commentRemovedCode+=sourceCode.charAt(i);
			
		}		
		return commentRemovedCode;
		
	}
	 
	 static void detectBuiltInStruct(String path) throws FileNotFoundException {
		 File file = new File(path);
		 Scanner scanner = new Scanner(file);
		 ArrayList<String> builtInStructs = new ArrayList<String>();
		
		 while (scanner.hasNext()) {			
				String st = scanner.next();
				
				if (SourceVersion.isKeyword(st) )   {				
		           if (!builtInStructs.contains(st)) {
					builtInStructs.add(st);
		           }
		         }
		 }
		 scanner.close();
		 for (String struct : builtInStructs) {
			System.out.println(struct);
		}
	 }
	 
	 static void detectMethods(String path) throws FileNotFoundException {
		File file = new File(path);
		Scanner scanner = new Scanner(file);
		int lineCounter=0;
		boolean inMethod = false;
		int bracketCounter = 0;
			
			while (scanner.hasNextLine()) {
				lineCounter++;
				String st = scanner.nextLine();
				
					

				if (!inMethod && useRegex(st, "(?=^.*(public|protected|private|static).*$)(?!.*class)") ) {
					inMethod = true;
					System.out.println(st.replaceAll("\\(.*\\)|\\{.*\\}|\\{", "")+"()");
					

					System.out.print("Between Lines "+lineCounter);
				}
				
				
				if (inMethod && st.contains("{")) {
					bracketCounter++;
				}
				if (inMethod && st.contains("}")) {
					bracketCounter--;
				}

				if (inMethod && bracketCounter==0 && st.contains("}"))
				{
					inMethod = false;
					System.out.println("-"+lineCounter);
				}
				
				
				
				
			} 
			scanner.close();
			 
		 
		 
	 }
	 
	 static void detectLoops(String path) throws FileNotFoundException {
		 
		 File file = new File(path);
			Scanner scanner = new Scanner(file);
			int lineCounter=0;
			boolean inLoop = false;
			int bracketCounter = 0;
				
				while (scanner.hasNextLine()) {
					lineCounter++;
					String st = scanner.nextLine();
					
					if (!inLoop &&useRegex(st, "while(\\s|)\\(.*\\)")) {
						inLoop = true;
						System.out.print(st.replaceAll("\\(.*\\)|\\{.*\\}|\\{", ""));
						System.out.print(" loop Between Lines "+lineCounter);
							
					}
					
					if (!inLoop && useRegex(st,"for\\s|\\S\\(.*\\;.*\\;.*\\)") ) {
						inLoop = true;
						System.out.print(st.replaceAll("\\(.*\\)|\\{.*\\}|\\{", ""));
						System.out.print(" loop Between Lines "+lineCounter);
						
					}if (!inLoop && useRegex(st,"for\\s|\\S\\(.*\\:.*\\)\\s\\{") ) {
						inLoop = true;
						System.out.print(st.replaceAll("\\(.*\\)|\\{.*\\}|\\{", ""));
						System.out.print("each loop Between Lines "+lineCounter);
					}

					if (inLoop && st.contains("{")) {
						bracketCounter++;
					}
					
					if (inLoop && st.contains("}")) {
						bracketCounter--;
					}

					if (inLoop && bracketCounter==0 && st.contains("}"))
					{
						inLoop = false;
						System.out.println("-"+lineCounter);
					}		
				} 
				scanner.close(); 
	 }
	 
	 static boolean useRegex(String input,String regex) 	
		{
			Pattern regexPattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);		
			Matcher regexMatcher =regexPattern.matcher(input);
			
			return regexMatcher.find();
		
		}
}
