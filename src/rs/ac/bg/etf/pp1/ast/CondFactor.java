// generated with ast extension for cup
// version 0.8
// 26/6/2026 23:11:20


package rs.ac.bg.etf.pp1.ast;

public class CondFactor extends CondFact {

    private Expr Expr;
    private OptionalRelop OptionalRelop;

    public CondFactor (Expr Expr, OptionalRelop OptionalRelop) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.OptionalRelop=OptionalRelop;
        if(OptionalRelop!=null) OptionalRelop.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public OptionalRelop getOptionalRelop() {
        return OptionalRelop;
    }

    public void setOptionalRelop(OptionalRelop OptionalRelop) {
        this.OptionalRelop=OptionalRelop;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(OptionalRelop!=null) OptionalRelop.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(OptionalRelop!=null) OptionalRelop.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(OptionalRelop!=null) OptionalRelop.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFactor(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalRelop!=null)
            buffer.append(OptionalRelop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFactor]");
        return buffer.toString();
    }
}
