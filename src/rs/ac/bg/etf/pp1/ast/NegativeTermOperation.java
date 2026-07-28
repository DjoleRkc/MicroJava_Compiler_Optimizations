// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class NegativeTermOperation extends Expr {

    private Term Term;
    private OperationList OperationList;

    public NegativeTermOperation (Term Term, OperationList OperationList) {
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
        this.OperationList=OperationList;
        if(OperationList!=null) OperationList.setParent(this);
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public OperationList getOperationList() {
        return OperationList;
    }

    public void setOperationList(OperationList OperationList) {
        this.OperationList=OperationList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Term!=null) Term.accept(visitor);
        if(OperationList!=null) OperationList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
        if(OperationList!=null) OperationList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Term!=null) Term.traverseBottomUp(visitor);
        if(OperationList!=null) OperationList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NegativeTermOperation(\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OperationList!=null)
            buffer.append(OperationList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NegativeTermOperation]");
        return buffer.toString();
    }
}
