// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class Print extends Statement {

    private Expr Expr;
    private OptionalWidth OptionalWidth;

    public Print (Expr Expr, OptionalWidth OptionalWidth) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.OptionalWidth=OptionalWidth;
        if(OptionalWidth!=null) OptionalWidth.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public OptionalWidth getOptionalWidth() {
        return OptionalWidth;
    }

    public void setOptionalWidth(OptionalWidth OptionalWidth) {
        this.OptionalWidth=OptionalWidth;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(OptionalWidth!=null) OptionalWidth.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(OptionalWidth!=null) OptionalWidth.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(OptionalWidth!=null) OptionalWidth.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Print(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalWidth!=null)
            buffer.append(OptionalWidth.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Print]");
        return buffer.toString();
    }
}
