// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclaration extends ClassDecl {

    private ClassName ClassName;
    private ExtendsClause ExtendsClause;
    private ClassVarDeclList ClassVarDeclList;
    private OptionalClassMethodsList OptionalClassMethodsList;

    public ClassDeclaration (ClassName ClassName, ExtendsClause ExtendsClause, ClassVarDeclList ClassVarDeclList, OptionalClassMethodsList OptionalClassMethodsList) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.ExtendsClause=ExtendsClause;
        if(ExtendsClause!=null) ExtendsClause.setParent(this);
        this.ClassVarDeclList=ClassVarDeclList;
        if(ClassVarDeclList!=null) ClassVarDeclList.setParent(this);
        this.OptionalClassMethodsList=OptionalClassMethodsList;
        if(OptionalClassMethodsList!=null) OptionalClassMethodsList.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public ExtendsClause getExtendsClause() {
        return ExtendsClause;
    }

    public void setExtendsClause(ExtendsClause ExtendsClause) {
        this.ExtendsClause=ExtendsClause;
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
        if(ClassName!=null) ClassName.accept(visitor);
        if(ExtendsClause!=null) ExtendsClause.accept(visitor);
        if(ClassVarDeclList!=null) ClassVarDeclList.accept(visitor);
        if(OptionalClassMethodsList!=null) OptionalClassMethodsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(ExtendsClause!=null) ExtendsClause.traverseTopDown(visitor);
        if(ClassVarDeclList!=null) ClassVarDeclList.traverseTopDown(visitor);
        if(OptionalClassMethodsList!=null) OptionalClassMethodsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(ExtendsClause!=null) ExtendsClause.traverseBottomUp(visitor);
        if(ClassVarDeclList!=null) ClassVarDeclList.traverseBottomUp(visitor);
        if(OptionalClassMethodsList!=null) OptionalClassMethodsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclaration(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExtendsClause!=null)
            buffer.append(ExtendsClause.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

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
        buffer.append(") [ClassDeclaration]");
        return buffer.toString();
    }
}
