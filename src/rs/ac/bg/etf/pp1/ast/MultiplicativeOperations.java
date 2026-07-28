// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class MultiplicativeOperations extends MultiplicativeSequenceList {

    private MultiplicativeSequenceList MultiplicativeSequenceList;
    private Mulop Mulop;
    private Factor Factor;

    public MultiplicativeOperations (MultiplicativeSequenceList MultiplicativeSequenceList, Mulop Mulop, Factor Factor) {
        this.MultiplicativeSequenceList=MultiplicativeSequenceList;
        if(MultiplicativeSequenceList!=null) MultiplicativeSequenceList.setParent(this);
        this.Mulop=Mulop;
        if(Mulop!=null) Mulop.setParent(this);
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
    }

    public MultiplicativeSequenceList getMultiplicativeSequenceList() {
        return MultiplicativeSequenceList;
    }

    public void setMultiplicativeSequenceList(MultiplicativeSequenceList MultiplicativeSequenceList) {
        this.MultiplicativeSequenceList=MultiplicativeSequenceList;
    }

    public Mulop getMulop() {
        return Mulop;
    }

    public void setMulop(Mulop Mulop) {
        this.Mulop=Mulop;
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MultiplicativeSequenceList!=null) MultiplicativeSequenceList.accept(visitor);
        if(Mulop!=null) Mulop.accept(visitor);
        if(Factor!=null) Factor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MultiplicativeSequenceList!=null) MultiplicativeSequenceList.traverseTopDown(visitor);
        if(Mulop!=null) Mulop.traverseTopDown(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MultiplicativeSequenceList!=null) MultiplicativeSequenceList.traverseBottomUp(visitor);
        if(Mulop!=null) Mulop.traverseBottomUp(visitor);
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultiplicativeOperations(\n");

        if(MultiplicativeSequenceList!=null)
            buffer.append(MultiplicativeSequenceList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mulop!=null)
            buffer.append(Mulop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultiplicativeOperations]");
        return buffer.toString();
    }
}
