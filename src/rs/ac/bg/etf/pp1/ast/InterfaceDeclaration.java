// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class InterfaceDeclaration extends InterfaceDecl {

    private InterfaceName InterfaceName;
    private InterfaceMethodsList InterfaceMethodsList;

    public InterfaceDeclaration (InterfaceName InterfaceName, InterfaceMethodsList InterfaceMethodsList) {
        this.InterfaceName=InterfaceName;
        if(InterfaceName!=null) InterfaceName.setParent(this);
        this.InterfaceMethodsList=InterfaceMethodsList;
        if(InterfaceMethodsList!=null) InterfaceMethodsList.setParent(this);
    }

    public InterfaceName getInterfaceName() {
        return InterfaceName;
    }

    public void setInterfaceName(InterfaceName InterfaceName) {
        this.InterfaceName=InterfaceName;
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
        if(InterfaceName!=null) InterfaceName.accept(visitor);
        if(InterfaceMethodsList!=null) InterfaceMethodsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceName!=null) InterfaceName.traverseTopDown(visitor);
        if(InterfaceMethodsList!=null) InterfaceMethodsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceName!=null) InterfaceName.traverseBottomUp(visitor);
        if(InterfaceMethodsList!=null) InterfaceMethodsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceDeclaration(\n");

        if(InterfaceName!=null)
            buffer.append(InterfaceName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(InterfaceMethodsList!=null)
            buffer.append(InterfaceMethodsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceDeclaration]");
        return buffer.toString();
    }
}
