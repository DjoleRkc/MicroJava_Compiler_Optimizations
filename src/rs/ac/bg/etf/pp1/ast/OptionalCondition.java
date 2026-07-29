// generated with ast extension for cup
// version 0.8
// 29/6/2026 19:40:28


package rs.ac.bg.etf.pp1.ast;

public class OptionalCondition extends OptionalConditionList {

    private Condition Condition;
    private AdditionalStatement AdditionalStatement;

    public OptionalCondition (Condition Condition, AdditionalStatement AdditionalStatement) {
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.AdditionalStatement=AdditionalStatement;
        if(AdditionalStatement!=null) AdditionalStatement.setParent(this);
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public AdditionalStatement getAdditionalStatement() {
        return AdditionalStatement;
    }

    public void setAdditionalStatement(AdditionalStatement AdditionalStatement) {
        this.AdditionalStatement=AdditionalStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Condition!=null) Condition.accept(visitor);
        if(AdditionalStatement!=null) AdditionalStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(AdditionalStatement!=null) AdditionalStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(AdditionalStatement!=null) AdditionalStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptionalCondition(\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AdditionalStatement!=null)
            buffer.append(AdditionalStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptionalCondition]");
        return buffer.toString();
    }
}
