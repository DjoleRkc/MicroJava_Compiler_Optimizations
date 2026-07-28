// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class InterfaceMethodSignatureList extends InterfaceMethodsList {

    private InterfaceMethodSignature InterfaceMethodSignature;
    private InterfaceMethodsList InterfaceMethodsList;

    public InterfaceMethodSignatureList (InterfaceMethodSignature InterfaceMethodSignature, InterfaceMethodsList InterfaceMethodsList) {
        this.InterfaceMethodSignature=InterfaceMethodSignature;
        if(InterfaceMethodSignature!=null) InterfaceMethodSignature.setParent(this);
        this.InterfaceMethodsList=InterfaceMethodsList;
        if(InterfaceMethodsList!=null) InterfaceMethodsList.setParent(this);
    }

    public InterfaceMethodSignature getInterfaceMethodSignature() {
        return InterfaceMethodSignature;
    }

    public void setInterfaceMethodSignature(InterfaceMethodSignature InterfaceMethodSignature) {
        this.InterfaceMethodSignature=InterfaceMethodSignature;
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
        if(InterfaceMethodSignature!=null) InterfaceMethodSignature.accept(visitor);
        if(InterfaceMethodsList!=null) InterfaceMethodsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceMethodSignature!=null) InterfaceMethodSignature.traverseTopDown(visitor);
        if(InterfaceMethodsList!=null) InterfaceMethodsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceMethodSignature!=null) InterfaceMethodSignature.traverseBottomUp(visitor);
        if(InterfaceMethodsList!=null) InterfaceMethodsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceMethodSignatureList(\n");

        if(InterfaceMethodSignature!=null)
            buffer.append(InterfaceMethodSignature.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(InterfaceMethodsList!=null)
            buffer.append(InterfaceMethodsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceMethodSignatureList]");
        return buffer.toString();
    }
}
