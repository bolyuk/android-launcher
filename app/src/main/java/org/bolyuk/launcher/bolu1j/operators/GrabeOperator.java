package org.bolyuk.launcher.bolu1j.operators;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.classes.BJException;
import org.bolyuk.launcher.bolu1j.classes.BJobject;

public class GrabeOperator implements AbstractOperator{
    @Override
    public boolean canExec(BJobject p, BJobject n) {
        return n != null;
    }

    @Override
    public String getOperator() {
        return "$";
    }

    @Override
    public BJobject exec(BJobject p, BJobject n, Interpreter context) {
        n = (n.isStatement())? n.getStatement(context):n;
        try {
            return context.getVariable(n.value);
        }catch (Exception err){}
        return new BJException("can not execute [ "+getOperator()+" "+n.type+" ]");
    }
}
