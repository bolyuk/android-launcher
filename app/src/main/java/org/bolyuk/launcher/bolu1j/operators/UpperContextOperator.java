package org.bolyuk.launcher.bolu1j.operators;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.classes.BJException;
import org.bolyuk.launcher.bolu1j.classes.BJStatement;
import org.bolyuk.launcher.bolu1j.classes.BJobject;

public class UpperContextOperator implements AbstractOperator{
    @Override
    public boolean canExec(BJobject p, BJobject n) {
        return n != null;
    }

    @Override
    public String getOperator() {
        return "`";
    }

    @Override
    public BJobject exec(BJobject p, BJobject n, Interpreter context) {
        if(context.getInner() == null)
            return new BJException("upper context is not exist");
        if(!n.isStatement())
            return new BJException("expected `(statement)");
        return context.getInner().exec((BJStatement)n,new BJobject(),0 );
    }
}
