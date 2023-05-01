package org.bolyuk.launcher.bolu1j.classes;

import org.bolyuk.launcher.bolu1j.Interpreter;

public class BJclass extends BJobject{
   public  Interpreter in_context;
   public BJclass(Interpreter context){
        super(",","class");
        in_context = new Interpreter(context);
    }


    public BJobject exec(BJStatement args){
        return in_context.exec(args, new BJobject(), 0);
    }

    @Override
    public BJobject copy() {
        BJclass r = new BJclass(in_context);
        r.setFinal(isFinal);
        r.value=value;
        r.type=type;
        return r;
    }
}
