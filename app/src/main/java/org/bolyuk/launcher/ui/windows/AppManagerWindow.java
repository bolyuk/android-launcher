package org.bolyuk.launcher.ui.windows;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.bolyuk.launcher.DesktopActivity;
import org.bolyuk.launcher.R;
import org.bolyuk.launcher.ui.AppIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class AppManagerWindow extends Window{
    public AppManagerWindow(Context context) {
        super(context, 250, 200);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.appmanager_window,null);

        RecyclerView recycler = layout.findViewById(R.id.recycler);

        recycler.setAdapter(new ListAdapter(context, getAppsList()));
        recycler.setLayoutManager(new LinearLayoutManager(context));

        EditText edit_text = layout.findViewById(R.id.search_by_app_name);

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edit_text.getText() != null){
                    List<AppIcon> result = new ArrayList<>();
                    List<AppIcon> list = getAppsList();

                    list.forEach(appIcon -> {
                        if(appIcon.name.contains(edit_text.getText()))
                            result.add(appIcon);
                    });

                    recycler.setAdapter(new ListAdapter(context, result));
                    recycler.requestLayout();
                }
            }
        });

        setBody(layout);
        setTitle("APP Browser");
    }

    public List<AppIcon> getAppsList(){
        PackageManager pManager = _context.getPackageManager();

        ArrayList<AppIcon> appsList = new ArrayList<AppIcon>();
        List<ApplicationInfo> packages = pManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo ri : packages) {
            AppIcon app = new AppIcon();

            app.name = (String) ri.loadLabel(pManager);
            app.packageName = ri.packageName;
            app.icon = ri.loadIcon(pManager);

            appsList.add(app);

        }

        return appsList;

    }

    public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

        private Context context;
        private List<AppIcon> appsList;
        public ListAdapter(Context c, List<AppIcon> list ) {
            context = c;
            appsList = list;
        }


        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(R.layout.app_viewholder, parent, false);

            return new ListViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            AppIcon i = appsList.get(position);

            holder.icon.setImageDrawable(i.icon);
            holder.name.setText(i.name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i.add(((DesktopActivity)context).desktop);
                    i.id = UUID.randomUUID().toString();
                    DesktopActivity.apps.write(i.id, i);
                    //Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(i.packageName);
                    //context.startActivity(launchIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return appsList.size();
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView name;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.app_icon);
            name = itemView.findViewById(R.id.app_name);
        }
    }
}
