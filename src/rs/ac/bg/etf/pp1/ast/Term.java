// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class Term implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private Factor Factor;
    private MultiplicativeSequenceList MultiplicativeSequenceList;

    public Term (Factor Factor, MultiplicativeSequenceList MultiplicativeSequenceList) {
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
        this.MultiplicativeSequenceList=MultiplicativeSequenceList;
        if(MultiplicativeSequenceList!=null) MultiplicativeSequenceList.setParent(this);
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public MultiplicativeSequenceList getMultiplicativeSequenceList() {
        return MultiplicativeSequenceList;
    }

    public void setMultiplicativeSequenceList(MultiplicativeSequenceList MultiplicativeSequenceList) {
        this.MultiplicativeSequenceList=MultiplicativeSequenceList;
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
        if(Factor!=null) Factor.accept(visitor);
        if(MultiplicativeSequenceList!=null) MultiplicativeSequenceList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
        if(MultiplicativeSequenceList!=null) MultiplicativeSequenceList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        if(MultiplicativeSequenceList!=null) MultiplicativeSequenceList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Term(\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MultiplicativeSequenceList!=null)
            buffer.append(MultiplicativeSequenceList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Term]");
        return buffer.toString();
    }
}
