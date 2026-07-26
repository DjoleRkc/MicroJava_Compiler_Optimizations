// generated with ast extension for cup
// version 0.8
// 26/6/2026 23:11:20


package rs.ac.bg.etf.pp1.ast;

public class FormalParameter implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private String paramName;
    private ArraySpecifier ArraySpecifier;
    private FormalParametersList FormalParametersList;

    public FormalParameter (Type Type, String paramName, ArraySpecifier ArraySpecifier, FormalParametersList FormalParametersList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.paramName=paramName;
        this.ArraySpecifier=ArraySpecifier;
        if(ArraySpecifier!=null) ArraySpecifier.setParent(this);
        this.FormalParametersList=FormalParametersList;
        if(FormalParametersList!=null) FormalParametersList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName=paramName;
    }

    public ArraySpecifier getArraySpecifier() {
        return ArraySpecifier;
    }

    public void setArraySpecifier(ArraySpecifier ArraySpecifier) {
        this.ArraySpecifier=ArraySpecifier;
    }

    public FormalParametersList getFormalParametersList() {
        return FormalParametersList;
    }

    public void setFormalParametersList(FormalParametersList FormalParametersList) {
        this.FormalParametersList=FormalParametersList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ArraySpecifier!=null) ArraySpecifier.accept(visitor);
        if(FormalParametersList!=null) FormalParametersList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ArraySpecifier!=null) ArraySpecifier.traverseTopDown(visitor);
        if(FormalParametersList!=null) FormalParametersList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ArraySpecifier!=null) ArraySpecifier.traverseBottomUp(visitor);
        if(FormalParametersList!=null) FormalParametersList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParameter(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+paramName);
        buffer.append("\n");

        if(ArraySpecifier!=null)
            buffer.append(ArraySpecifier.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormalParametersList!=null)
            buffer.append(FormalParametersList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParameter]");
        return buffer.toString();
    }
}
