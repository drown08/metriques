package step2.src.step2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class PackageVisitor extends ASTVisitor{
	List<PackageDeclaration> packages = new ArrayList<PackageDeclaration>();
	
	public boolean visit(PackageDeclaration node) {
		packages.add(node);
		return super.visit(node);
	}
	
	public List<PackageDeclaration> getPackages() {
		return packages;
	}

}
