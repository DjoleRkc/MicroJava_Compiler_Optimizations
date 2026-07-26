// generated with ast extension for cup
// version 0.8
// 26/6/2026 23:11:20


package rs.ac.bg.etf.pp1.ast;

public class VarDeclarations extends VarDeclList {

    private String varName;
    private ArraySpecifier ArraySpecifier;
    private VarDeclList VarDeclList;

    public VarDeclarations (String varName, ArraySpecifier ArraySpecifier, VarDeclList VarDeclList) {
        this.varName=varName;
        this.ArraySpecifier=ArraySpecifier;
        if(ArraySpecifier!=null) ArraySpecifier.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public ArraySpecifier getArraySpecifier() {
        return ArraySpecifier;
    }

    public void setArraySpecifier(ArraySpecifier ArraySpecifier) {
        this.ArraySpecifier=ArraySpecifier;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArraySpecifier!=null) ArraySpecifier.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArraySpecifier!=null) ArraySpecifier.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArraySpecifier!=null) ArraySpecifier.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclarations(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(ArraySpecifier!=null)
            buffer.append(ArraySpecifier.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclarations]");
        return buffer.toString();
    }
}
