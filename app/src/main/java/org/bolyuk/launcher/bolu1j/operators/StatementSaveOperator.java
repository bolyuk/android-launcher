package org.bolyuk.launcher.bolu1j.operators;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.classes.BJException;
import org.bolyuk.launcher.bolu1j.classes.BJobject;

// TODO не работает
public class StatementSaveOperator implements AbstractOperator{
    @Override
    public boolean canExec(BJobject p, BJobject n) {
        return n != null;
    }

    @Override
    public String getOperator() {
        return "@";
    }

    @Override
    public BJobject exec(BJobject p, BJobject n, Interpreter context) {
        if(!n.isStatement())
            return new BJException("can not execute [ @ ] n is not statement ");
        return n;
    }
}
