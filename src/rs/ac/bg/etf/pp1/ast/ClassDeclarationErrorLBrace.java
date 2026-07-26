// generated with ast extension for cup
// version 0.8
// 26/6/2026 23:11:20


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclarationErrorLBrace extends ClassDecl {

    private ClassVarDeclList ClassVarDeclList;
    private OptionalClassMethodsList OptionalClassMethodsList;

    public ClassDeclarationErrorLBrace (ClassVarDeclList ClassVarDeclList, OptionalClassMethodsList OptionalClassMethodsList) {
        this.ClassVarDeclList=ClassVarDeclList;
        if(ClassVarDeclList!=null) ClassVarDeclList.setParent(this);
        this.OptionalClassMethodsList=OptionalClassMethodsList;
        if(OptionalClassMethodsList!=null) OptionalClassMethodsList.setParent(this);
    }

    public ClassVarDeclList getClassVarDeclList() {
        return ClassVarDeclList;
    }

    public void setClassVarDeclList(ClassVarDeclList ClassVarDeclList) {
        this.ClassVarDeclList=ClassVarDeclList;
    }

    public OptionalClassMethodsList getOptionalClassMethodsList() {
        return OptionalClassMethodsList;
    }

    public void setOptionalClassMethodsList(OptionalClassMethodsList OptionalClassMethodsList) {
        this.OptionalClassMethodsList=OptionalClassMethodsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassVarDeclList!=null) ClassVarDeclList.accept(visitor);
        if(OptionalClassMethodsList!=null) OptionalClassMethodsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassVarDeclList!=null) ClassVarDeclList.traverseTopDown(visitor);
        if(OptionalClassMethodsList!=null) OptionalClassMethodsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassVarDeclList!=null) ClassVarDeclList.traverseBottomUp(visitor);
        if(OptionalClassMethodsList!=null) OptionalClassMethodsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclarationErrorLBrace(\n");

        if(ClassVarDeclList!=null)
            buffer.append(ClassVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalClassMethodsList!=null)
            buffer.append(OptionalClassMethodsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclarationErrorLBrace]");
        return buffer.toString();
    }
}
