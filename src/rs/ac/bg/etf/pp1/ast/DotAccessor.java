// generated with ast extension for cup
// version 0.8
// 29/6/2026 19:40:28


package rs.ac.bg.etf.pp1.ast;

public class DotAccessor extends AccessorList {

    private String I1;
    private AccessorList AccessorList;

    public DotAccessor (String I1, AccessorList AccessorList) {
        this.I1=I1;
        this.AccessorList=AccessorList;
        if(AccessorList!=null) AccessorList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public AccessorList getAccessorList() {
        return AccessorList;
    }

    public void setAccessorList(AccessorList AccessorList) {
        this.AccessorList=AccessorList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AccessorList!=null) AccessorList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AccessorList!=null) AccessorList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AccessorList!=null) AccessorList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DotAccessor(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(AccessorList!=null)
            buffer.append(AccessorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DotAccessor]");
        return buffer.toString();
    }
}
