package org.bolyuk.launcher.bolu1j.operators;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.classes.BJException;
import org.bolyuk.launcher.bolu1j.classes.BJobject;

public class PlusOperator implements AbstractOperator {

    @Override
    public boolean canExec(BJobject p, BJobject n) {
        return p != null && n != null;
    }

    @Override
    public String getOperator() {
        return "+";
    }

    @Override
    public BJobject exec(BJobject p, BJobject n, Interpreter context) {
        p = (p.isStatement())? p.getStatement(context):p;
        n = (n.isStatement())? n.getStatement(context):n;


        if(p.isNumber() && n.isNumber())
            return new BJobject(String.valueOf(p.getNumber()+n.getNumber()),"number");
        if(p.isString() || n.isString())
            return new BJobject(p.value+n.value, "string");
        return new BJException("PlusOperator: can not execute [ "+p.type+" "+getOperator()+" "+n.type+" ]");
    }
}
