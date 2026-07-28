// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class FuncCall extends Factor {

    private Designator Designator;
    private DesignatorParamsList DesignatorParamsList;

    public FuncCall (Designator Designator, DesignatorParamsList DesignatorParamsList) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorParamsList=DesignatorParamsList;
        if(DesignatorParamsList!=null) DesignatorParamsList.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorParamsList getDesignatorParamsList() {
        return DesignatorParamsList;
    }

    public void setDesignatorParamsList(DesignatorParamsList DesignatorParamsList) {
        this.DesignatorParamsList=DesignatorParamsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorParamsList!=null) DesignatorParamsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorParamsList!=null) DesignatorParamsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorParamsList!=null) DesignatorParamsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FuncCall(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorParamsList!=null)
            buffer.append(DesignatorParamsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FuncCall]");
        return buffer.toString();
    }
}
