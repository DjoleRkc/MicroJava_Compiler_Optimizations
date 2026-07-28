// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class DoWhile extends Statement {

    private DoStart DoStart;
    private Statement Statement;
    private While While;
    private OptionalConditionList OptionalConditionList;

    public DoWhile (DoStart DoStart, Statement Statement, While While, OptionalConditionList OptionalConditionList) {
        this.DoStart=DoStart;
        if(DoStart!=null) DoStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.While=While;
        if(While!=null) While.setParent(this);
        this.OptionalConditionList=OptionalConditionList;
        if(OptionalConditionList!=null) OptionalConditionList.setParent(this);
    }

    public DoStart getDoStart() {
        return DoStart;
    }

    public void setDoStart(DoStart DoStart) {
        this.DoStart=DoStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public While getWhile() {
        return While;
    }

    public void setWhile(While While) {
        this.While=While;
    }

    public OptionalConditionList getOptionalConditionList() {
        return OptionalConditionList;
    }

    public void setOptionalConditionList(OptionalConditionList OptionalConditionList) {
        this.OptionalConditionList=OptionalConditionList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoStart!=null) DoStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(While!=null) While.accept(visitor);
        if(OptionalConditionList!=null) OptionalConditionList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoStart!=null) DoStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(While!=null) While.traverseTopDown(visitor);
        if(OptionalConditionList!=null) OptionalConditionList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoStart!=null) DoStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(While!=null) While.traverseBottomUp(visitor);
        if(OptionalConditionList!=null) OptionalConditionList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoWhile(\n");

        if(DoStart!=null)
            buffer.append(DoStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(While!=null)
            buffer.append(While.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalConditionList!=null)
            buffer.append(OptionalConditionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoWhile]");
        return buffer.toString();
    }
}
