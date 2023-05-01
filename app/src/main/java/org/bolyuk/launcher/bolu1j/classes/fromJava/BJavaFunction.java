package org.bolyuk.launcher.bolu1j.classes.fromJava;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.classes.BJException;
import org.bolyuk.launcher.bolu1j.classes.BJStatement;
import org.bolyuk.launcher.bolu1j.classes.BJfunction;
import org.bolyuk.launcher.bolu1j.classes.BJobject;

import java.lang.reflect.Method;

public class BJavaFunction extends BJfunction {
    Method method;
    Object jContext;
    public BJavaFunction(Interpreter context) {
        super(context);
    }

    public BJavaFunction parseJava(Method f){
        method=f;

        return this;
    }

    public BJavaFunction setJContext(Object j){
        this.jContext=j;
        return this;
    }

    @Override
    public BJobject exec(BJStatement args) {
        try {
            Object[] obj =  BJavaUtil.argsToObj(args, _context, method);
            for (Object o: obj) {
                System.out.println(o.getClass().getTypeName()+", ");
            }
            Object r =method.invoke(jContext,obj);
            if(r != null)
                if(r.getClass().getTypeName().contains("org.boluj"))
                return (BJobject) r;
            else
                return new BJobject(r.toString(),"string");
            else
               return this;
        }catch (Exception e){
            e.printStackTrace();

            return new BJException(e.toString());
        }
    }

    @Override
    public BJobject copy() {
        BJavaFunction r = new BJavaFunction(_context);
        r.setFinal(isFinal);
        r.value=value;
        r.type=type;
        r.in_context=in_context;
        r.args=args;
        r.method=method;
        r.jContext=jContext;
        return r;
    }
}
