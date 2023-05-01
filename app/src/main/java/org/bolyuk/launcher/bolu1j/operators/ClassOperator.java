package org.bolyuk.launcher.bolu1j.operators;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.Kovalski;
import org.bolyuk.launcher.bolu1j.classes.fromJava.BJavaFunction;
import org.bolyuk.launcher.bolu1j.classes.BJException;
import org.bolyuk.launcher.bolu1j.classes.BJclass;
import org.bolyuk.launcher.bolu1j.classes.BJobject;

import java.lang.reflect.Method;

public class ClassOperator implements AbstractOperator{
    @Override
    public boolean canExec(BJobject p, BJobject n) {
        return p != null;
    }

    @Override
    public String getOperator() {
        return "~";
    }

    @Override
    public BJobject exec(BJobject p, BJobject n, Interpreter context) {
        p = p.isStatement()?p.getStatement(context):p;
        n = (n == null)?n:n.getStatement(context);
        if(n == null) {
            return context.setVariable(p.value, new BJclass(context));
        } else {
            String v = n.isStatement() ? n.getStatement(context).value : n.value;

            if(v.equals("var")){
              return  context.setVariable(p.value, new BJobject());
            } else if(v.equals("remove"))
                return context.removeVariable(p.value);
            BJclass result = new BJclass(context);
            Class c;
            try {
                c = Class.forName(v);
                for(Method m : c.getMethods())
                    try {
                        result.in_context.putVariable(m.getName(), new BJavaFunction(result.in_context).parseJava(m).setJContext(c));
                    }catch (Exception e){
                        Kovalski.l("method "+m.getName()+" can not be parsed");
                    }

                return context.setVariable(p.value, result);
            } catch (Exception e) {
                return new BJException("class can not be created [ "+e.getMessage()+" ]");
            }
        }
    }
}
