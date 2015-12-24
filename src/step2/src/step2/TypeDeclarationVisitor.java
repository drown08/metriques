package step2.src.step2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDeclarationVisitor extends ASTVisitor {
	List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
	List<TypeDeclaration> abstractClasses = new ArrayList<TypeDeclaration>();
	List<TypeDeclaration> interfacesClasses = new ArrayList<TypeDeclaration>();
	ArrayList<String> superClasses = new ArrayList<String>();
	
	public boolean visit(TypeDeclaration node) {
		//System.out.println(node.resolveBinding().getSuperclass().getName().toString());
		if (node.resolveBinding().toString().contains("public abstract class")) {
			abstractClasses.add(node);
		}
		if (node.resolveBinding().toString().contains("public interface")) {
			interfacesClasses.add(node);
		}
		ArrayList<String> l = new ArrayList<String>();
		types.add(node);
		return super.visit(node);
	}
	
	public List<TypeDeclaration> getTypes() {
		return types;
	}
	
	public List<TypeDeclaration> getAbstract() {
		return abstractClasses;
	}
	
	public List<TypeDeclaration> getInterfaces() {
		return interfacesClasses;
	}
}