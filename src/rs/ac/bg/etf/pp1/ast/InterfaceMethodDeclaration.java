// generated with ast extension for cup
// version 0.8
// 26/6/2026 23:11:20


package rs.ac.bg.etf.pp1.ast;

public class InterfaceMethodDeclaration extends InterfaceMethodsList {

    private MethodDecl MethodDecl;
    private InterfaceMethodsList InterfaceMethodsList;

    public InterfaceMethodDeclaration (MethodDecl MethodDecl, InterfaceMethodsList InterfaceMethodsList) {
        this.MethodDecl=MethodDecl;
        if(MethodDecl!=null) MethodDecl.setParent(this);
        this.InterfaceMethodsList=InterfaceMethodsList;
        if(InterfaceMethodsList!=null) InterfaceMethodsList.setParent(this);
    }

    public MethodDecl getMethodDecl() {
        return MethodDecl;
    }

    public void setMethodDecl(MethodDecl MethodDecl) {
        this.MethodDecl=MethodDecl;
    }

    public InterfaceMethodsList getInterfaceMethodsList() {
        return InterfaceMethodsList;
    }

    public void setInterfaceMethodsList(InterfaceMethodsList InterfaceMethodsList) {
        this.InterfaceMethodsList=InterfaceMethodsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodDecl!=null) MethodDecl.accept(visitor);
        if(InterfaceMethodsList!=null) InterfaceMethodsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodDecl!=null) MethodDecl.traverseTopDown(visitor);
        if(InterfaceMethodsList!=null) InterfaceMethodsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodDecl!=null) MethodDecl.traverseBottomUp(visitor);
        if(InterfaceMethodsList!=null) InterfaceMethodsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceMethodDeclaration(\n");

        if(MethodDecl!=null)
            buffer.append(MethodDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(InterfaceMethodsList!=null)
            buffer.append(InterfaceMethodsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceMethodDeclaration]");
        return buffer.toString();
    }
}
