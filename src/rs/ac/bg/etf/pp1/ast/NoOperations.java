// generated with ast extension for cup
// version 0.8
// 28/6/2026 22:12:19


package rs.ac.bg.etf.pp1.ast;

public class NoOperations extends OperationList {

    public NoOperations () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoOperations(\n");

        buffer.append(tab);
        buffer.append(") [NoOperations]");
        return buffer.toString();
    }
}
