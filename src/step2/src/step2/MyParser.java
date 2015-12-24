package step2.src.step2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MyParser extends Parser {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		//On va compter le nombre de m�thodes PAR CLASSE
		int nbMethodsPerClass = 0;
		int nbMinMethodPerClass = 10000000;
		int nbMaxMethodPerClass = 0;
		double avgMethodPerClass = 0.0;
		int nbClasses = javaFiles.size();
		int nbFullOfMethod = 0;
		
		//Nombre d'attributs
		int minAttrs = 1000000;
		int maxAttrs = 0;
		double avgAttrs = 0.0;
		int totalAttrs = 0;
		
		//Static attributs
		int totalStaticAttrs = 0;
		
		//static methods
		int totalStaticMethod = 0;
		double avgStaticMethod = 0.0;
		
		//packages 
		int minPackage = 1000000;
		int maxPackage = 0;
		int totalPackage = 0;
		double avgPackage = 0.0;
		
		//Lignes de code
		int minLOC = 1100000000;
		int maxLOC = 0;
		int totalLOC = 0;
		double avgLOC = 0.0;
		
		//Abstracts & interfaces
		int abstracts = 0;
		int interfaces = 0;
		
		//Categorie Coupling
		Map<String, ArrayList<String>> couplage = new HashMap<String, ArrayList<String>>();
		
		for (File fileEntry : javaFiles) {
			
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());

			// print methods info
			//printMethodInfo(parse);

			//On r�ccup�re le nb de m�thode par fichier (Donc par classe normalement)
			//1ere metrique, calcul du nombre de m�thodes dans une classe
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
			
			//1. Calcul du nombre d'attributs (NOF)
			int nbAttrs = attributsInfoNb(parse);
			if (nbAttrs < minAttrs) {
				minAttrs = nbAttrs;
			}if (nbAttrs > maxAttrs) {
				maxAttrs = nbAttrs;
			}
			totalAttrs += nbAttrs;
			
			//2. Nombre de variables statiques (NSF)
			totalStaticAttrs += staticAttributs(parse);
			
			//3. Nombre de methodes statiques (NSM)
			int nbStaticsMethods = StaticmethodInfoNb(parse);
			totalStaticMethod += nbStaticsMethods;
			
			//4. Nombre de paquets (NOP) 
			int nbPaquets =NbPackages(parse);
			if (nbPaquets < minPackage) {
				minPackage = nbPaquets;
			}
			if (nbPaquets > maxPackage) {
				maxPackage = nbPaquets;
			}
			totalPackage += nbPaquets;
			
			//5. Lines of code (LOC)
			int LOC = parse.getLength();
			if (LOC < minLOC) {
				minLOC = LOC;
			}
			if (LOC > maxLOC) {
				maxLOC = LOC;
			}
			totalLOC += LOC;
			
			//6. Niveau d'abstraction (A) et nombre d'interfaces (NOI)
			TypeDeclarationVisitor v = TypeClasse(parse);
			abstracts += v.getAbstract().size();
			interfaces += v.getInterfaces().size();
			
			//Categorie Coupling ! 
			VariableDeclarationFragmentVisitor Visitor = constructCoupling(parse);
			ArrayList<String> l = new ArrayList<String>();
			for (String s : Visitor.getCouplage()) {
				l.add(s);
			}
			couplage.put(fileEntry.getName(), l);
		}
		System.out.println("General : ");
		System.out.println("Nombre de classes :  " +nbClasses + "\n");
		System.out.println("");
		
		System.out.println("  ===> Categorie CLASSE ");
		System.out.println("### Methodes (aucune differenciation) ###");
		System.out.println("Le nombre de m�thode maximale dans une classe est de : "+nbMaxMethodPerClass);
		System.out.println("Le nombre minimum de m�thode dans une classe est de : " +nbMinMethodPerClass);
		System.out.println("Le nombre total de m�thodes dans les classes est de : " +nbFullOfMethod);
		avgMethodPerClass = (double) nbFullOfMethod /  (double) nbClasses;
		System.out.println("La moyenne de m�thode est de :"+ avgMethodPerClass);
		System.out.println("");
		
		System.out.println("### M�thodes statiques ### ");
		System.out.println("Nombre de methodes statiques : " + totalStaticMethod);
		System.out.println("La moyenne de methodes statiques est de : " + (double) totalStaticMethod / (double) nbClasses);
		System.out.println("");
		
		System.out.println("### Attributs ###");
		System.out.println("Le nombre d'attributs maximale dans une classe est de : "+maxAttrs);
		System.out.println("Le nombre minimum d'attributs dans une classe est de : " +minAttrs);
		System.out.println("Le nombre total d'attributs dans les classes est de : " +totalAttrs);
		avgAttrs = (double) totalAttrs /  (double) nbClasses;
		System.out.println("La moyenne d'attributs est de :"+ avgAttrs);
		System.out.println("");
		

		System.out.println("### Attributs Statiques ###");
		System.out.println("Le nombre d'attributs statiques dans une classe est de : "+totalStaticAttrs);
		System.out.println("Moyenne d'attributs statiques (utile?) : " + (double) totalStaticAttrs / (double) nbClasses);
		System.out.println("");
		
		System.out.println("### Paquets ### (probleme non?)");
		/*System.out.println("Le nombre de packages maximale dans une classe est de : "+maxPackage);
		System.out.println("Le nombre minimum de packages dans une classe est de : " +minPackage);*/
		System.out.println("Le nombre total de Packages dans les classes est de : " +totalPackage);
		avgPackage = (double) totalPackage /  (double) nbClasses;
		//System.out.println("La moyenne de Package / classe est de :"+ avgPackage);
		System.out.println("");
		
		System.out.println("### Lignes de code ###");
		System.out.println("La plus petite classe fait : " + minLOC + " lignes");
		System.out.println("La plus grosse classe fait : " + maxLOC + " lignes");
		System.out.println("Total de lignes de code : " + totalLOC + " lignes ");
		System.out.println("Une moyenne de : " + (double) totalLOC / (double) nbClasses + " lignes / classe");
		System.out.println("");
		
		System.out.println("### Abstraction et nombre d'interfaces ###");
		System.out.println("Classes abstraites : " + abstracts + ", Interfaces : " + interfaces + ", Abstraction : "+ ((double) (abstracts + interfaces) / (double) nbClasses) * 100 + "%" );
	
	
		System.out.println("\n  ===> Categorie COUPLING ");
		int totalCouplage = 0;
		for (File fileEntry : javaFiles) {
			System.out.println("Fichier : " + fileEntry.getName() + ", Coupling de : " + couplage.get(fileEntry.getName()).size());
			totalCouplage += couplage.get(fileEntry.getName()).size();
		}
		System.out.println("\n Total de couplage des " + nbClasses + ": " + totalCouplage);
		System.out.println("Moyenne de couplage : " + (double) totalCouplage / (double) nbClasses );
		
		
		System.out.println("\n  ===> Categorie REUSE ");
		System.out.println("Reuse ratio : " + ( (double) abstracts * 100 ) / (double) nbClasses + "%");
		
	}
	

	public static int attributsInfoNb (CompilationUnit parse) {
		VariableDeclarationFragmentVisitor visitor = new VariableDeclarationFragmentVisitor();
		parse.accept(visitor);

		return visitor.getVariables().size();
	}
	
	public static int staticAttributs (CompilationUnit parse) {
		VariableDeclarationFragmentVisitor visitor = new VariableDeclarationFragmentVisitor();
		parse.accept(visitor);

		return visitor.getStaticVariables().size();
	}
	
	public static VariableDeclarationFragmentVisitor constructCoupling (CompilationUnit parse) {
		VariableDeclarationFragmentVisitor visitor = new VariableDeclarationFragmentVisitor();
		parse.accept(visitor);

		return visitor;
	}

	public static int methodInfoNb(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);
		return visitor.getMethods().size();

	}
	
	public static int StaticmethodInfoNb(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);
		return visitor.getStaticMethods().size();

	}
	
	public static int NbPackages(CompilationUnit parse) {
		PackageVisitor visitor = new PackageVisitor();
		parse.accept(visitor);
		return visitor.getPackages().size();
		
	}
	
	public static TypeDeclarationVisitor TypeClasse(CompilationUnit parse) {
		TypeDeclarationVisitor visitor = new TypeDeclarationVisitor();
		parse.accept(visitor);
		return visitor;
	}

}