// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class ActualParameter extends ActualParams {

    private ActPar ActPar;
    private ActualParams ActualParams;

    public ActualParameter (ActPar ActPar, ActualParams ActualParams) {
        this.ActPar=ActPar;
        if(ActPar!=null) ActPar.setParent(this);
        this.ActualParams=ActualParams;
        if(ActualParams!=null) ActualParams.setParent(this);
    }

    public ActPar getActPar() {
        return ActPar;
    }

    public void setActPar(ActPar ActPar) {
        this.ActPar=ActPar;
    }

    public ActualParams getActualParams() {
        return ActualParams;
    }

    public void setActualParams(ActualParams ActualParams) {
        this.ActualParams=ActualParams;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActPar!=null) ActPar.accept(visitor);
        if(ActualParams!=null) ActualParams.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActPar!=null) ActPar.traverseTopDown(visitor);
        if(ActualParams!=null) ActualParams.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActPar!=null) ActPar.traverseBottomUp(visitor);
        if(ActualParams!=null) ActualParams.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActualParameter(\n");

        if(ActPar!=null)
            buffer.append(ActPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualParams!=null)
            buffer.append(ActualParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActualParameter]");
        return buffer.toString();
    }
}
