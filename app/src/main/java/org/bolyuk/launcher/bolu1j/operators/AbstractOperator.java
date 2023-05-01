package org.bolyuk.launcher.bolu1j.operators;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.classes.BJobject;

public interface AbstractOperator {
     boolean canExec(BJobject p, BJobject n);
     String getOperator();
     BJobject exec(BJobject p, BJobject n, Interpreter context);
}
