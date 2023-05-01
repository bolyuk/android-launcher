package org.bolyuk.launcher.ui.windows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bolyuk.launcher.bolu1j.Interpreter;
import org.bolyuk.launcher.bolu1j.classes.BJobject;
import org.bolyuk.launcher.R;

public class CodeWindow extends Window{
    public CodeWindow(Context context) {
        super(context, 300, 400);
        LinearLayout l = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.code_window,null);

        TextView output = l.findViewById(R.id.output);
        EditText input = l.findViewById(R.id.input);
        Button push = l.findViewById(R.id.push);

        Interpreter e = new Interpreter(null);

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        e.parse(input.getText().toString());

                        BJobject _response = e.exec();
                        if(_response.value != "")
                            output.append("\n"+_response.value);

                    }catch (Exception err) {
                        output.append("\n"+err.toString());
                    }
                    e.clearParsedCode();
                }
        });
        setBody(l);
    }
}
