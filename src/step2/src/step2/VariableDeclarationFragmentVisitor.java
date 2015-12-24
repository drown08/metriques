package step2.src.step2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class VariableDeclarationFragmentVisitor extends ASTVisitor {
	private List<VariableDeclarationFragment> variables = new ArrayList<VariableDeclarationFragment>();
	private List<VariableDeclarationFragment> staticVariables = new ArrayList<VariableDeclarationFragment>();
	private List<String> Couplage = new ArrayList<String>();
	
	public boolean visit(VariableDeclarationFragment node) {
		
		if (node.resolveBinding().toString().contains("static")) {
			staticVariables.add(node);
		}
		String s = node.resolveBinding().getType().getName();
		//Filtrage des tableaux
		if (s.contains("[]")) {
			s = s.substring(0, s.length() - 2);
		}
		if (!Couplage.contains(s)) {
			Couplage.add(s);
		}
		variables.add(node);
		return super.visit(node);
	}
	
	public List<VariableDeclarationFragment> getVariables() {
		return variables;
	}
	
	public List<VariableDeclarationFragment> getStaticVariables() {
		return staticVariables;
	}
	
	public List<String> getCouplage() {
		return Couplage;
	}
}