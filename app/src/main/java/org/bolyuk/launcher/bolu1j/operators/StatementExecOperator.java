package org.bolyuk.launcher.bolu1j.operators;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.Parser;
import org.bolyuk.launcher.bolu1j.classes.BJException;
import org.bolyuk.launcher.bolu1j.classes.BJStatement;
import org.bolyuk.launcher.bolu1j.classes.BJobject;

// TODO не работает
public class StatementExecOperator implements AbstractOperator{
    @Override
    public boolean canExec(BJobject p, BJobject n) {
        return n != null;
    }

    @Override
    public String getOperator() {
        return "#";
    }

    @Override
    public BJobject exec(BJobject p, BJobject n, Interpreter context) {
        if(!(n.isStatement() || n.isString()))
            return new BJException("can not execute [ # ] n is not statement or a string");

        if(n.isStatement())
             return context.exec(Parser.parseCode(context.exec((BJStatement) n, new BJobject(),0).value, context));
        else
            return context.exec(Parser.parseCode(n.value, context));

    }
}
