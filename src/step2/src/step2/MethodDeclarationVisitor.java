package step2.src.step2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationVisitor extends ASTVisitor {
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	List<MethodDeclaration> staticMethods = new ArrayList<MethodDeclaration>();
	
	public boolean visit(MethodDeclaration node) {
		if (node.toString().contains("public static ")) {
			staticMethods.add(node);
		}
		methods.add(node);
		return super.visit(node);
	}
	
	public List<MethodDeclaration> getMethods() {
		return methods;
	}
	
	public List<MethodDeclaration> getStaticMethods() {
		return staticMethods;
	}
}