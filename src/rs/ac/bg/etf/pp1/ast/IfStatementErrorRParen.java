// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class IfStatementErrorRParen extends Statement {

    private Statement Statement;
    private OptionalElse OptionalElse;

    public IfStatementErrorRParen (Statement Statement, OptionalElse OptionalElse) {
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.OptionalElse=OptionalElse;
        if(OptionalElse!=null) OptionalElse.setParent(this);
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public OptionalElse getOptionalElse() {
        return OptionalElse;
    }

    public void setOptionalElse(OptionalElse OptionalElse) {
        this.OptionalElse=OptionalElse;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Statement!=null) Statement.accept(visitor);
        if(OptionalElse!=null) OptionalElse.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(OptionalElse!=null) OptionalElse.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(OptionalElse!=null) OptionalElse.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfStatementErrorRParen(\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalElse!=null)
            buffer.append(OptionalElse.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfStatementErrorRParen]");
        return buffer.toString();
    }
}
