// generated with ast extension for cup
// version 0.8
// 26/6/2026 23:11:20


package rs.ac.bg.etf.pp1.ast;

public class InterfaceDeclarations extends Decl {

    private InterfaceDecl InterfaceDecl;

    public InterfaceDeclarations (InterfaceDecl InterfaceDecl) {
        this.InterfaceDecl=InterfaceDecl;
        if(InterfaceDecl!=null) InterfaceDecl.setParent(this);
    }

    public InterfaceDecl getInterfaceDecl() {
        return InterfaceDecl;
    }

    public void setInterfaceDecl(InterfaceDecl InterfaceDecl) {
        this.InterfaceDecl=InterfaceDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceDecl!=null) InterfaceDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceDecl!=null) InterfaceDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceDecl!=null) InterfaceDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceDeclarations(\n");

        if(InterfaceDecl!=null)
            buffer.append(InterfaceDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceDeclarations]");
        return buffer.toString();
    }
}
