package step2.src.step2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class MyParser extends Parser {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		//On va compter le nombre de méthodes PAR CLASSE
		int nbMethodsPerClass = 0;
		int nbMinMethodPerClass = 10000000;
		int nbMaxMethodPerClass = 0;
		double avgMethodPerClass = 0.0;
		int nbClasses = javaFiles.size();
		int nbFullOfMethod = 0;
		//
		for (File fileEntry : javaFiles) {
			
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());

			// print methods info
			//printMethodInfo(parse);

			//On réccupère le nb de méthode par fichier (Donc par classe normalement)
			nbMethodsPerClass = methodInfoNb(parse);
			if(nbMethodsPerClass<nbMinMethodPerClass){
				nbMinMethodPerClass = nbMethodsPerClass;
			}
			if(nbMethodsPerClass>nbMaxMethodPerClass) {
				nbMaxMethodPerClass = nbMethodsPerClass;
			}
			nbFullOfMethod += nbMethodsPerClass;
			// print variables info
			//printVariableInfo(parse);
			
			//print method invocations
			//printMethodInvocationInfo(parse);

		}
		System.out.println("Le nombre de méthode maximale dans une classe est de : "+nbMaxMethodPerClass);
		System.out.println("Le nombre minimum de méthode dans une classe est de : " +nbMinMethodPerClass);
		System.out.println("Le nombre total de méthodes dans les classes est de : " +nbFullOfMethod);
		System.out.println("NBs CLASSE = " +nbClasses);
		avgMethodPerClass = (double) nbFullOfMethod /  (double) nbClasses;
		System.out.println("La moyenne de méthode est de :"+ avgMethodPerClass);
	}
	
	public static int methodInfoNb(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);
		return visitor.getMethods().size();

	}

}
