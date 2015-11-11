package hcay.pui.com.umlapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by andrasta on 11/8/15.
 */
public class FeatureAdapter extends ArrayAdapter<Feature> {
    private List<Feature> featureList;
    private Context context;
    private boolean isMember;

    public FeatureAdapter(Context ctx, List<Feature> featureList, boolean isMember) {
        super(ctx, R.layout.feature_row_layout, featureList);
        this.featureList = featureList;
        this.context = ctx;
        this.isMember = isMember;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

//        View v = convertView;
        FeatureHolder holder;

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.feature_row_layout, parent, false);

            // Now we can fill the layout with the right values
            EditText editFeatureName = (EditText) convertView.findViewById(R.id.editFeatureName);
            TextView featureName = (TextView) convertView.findViewById(R.id.featureName);
            Button save = (Button) convertView.findViewById(R.id.saveFeatureBtn);
            Button remove = (Button) convertView.findViewById(R.id.removeFeatureBtn);

            editFeatureName.setHint("<Enter " + (isMember ? "member" : "method") + " name here>");

            holder = new FeatureHolder(editFeatureName, featureName, save, remove);
            convertView.setTag(holder);
        } else
            holder = (FeatureHolder) convertView.getTag();

        holder.saveBtn.setTag(position);

        Feature feature = featureList.get(position);
        holder.editFeatureNameView.setText(feature.getFeatureName());

        return convertView;
    }

    public void addItem(String featureName){
        featureList.add(new Feature(featureName));
    }

    private static class FeatureHolder {
        public EditText editFeatureNameView;
        public TextView featureNameView;
        public Button saveBtn;
        public Button removeBtn;

        public FeatureHolder(EditText edit, TextView label, Button save, Button remove){
            editFeatureNameView = edit;
            featureNameView = label;
            saveBtn = save;
            save.setOnClickListener(mSaveClickListener);
            removeBtn = remove;
        }

        // Prevents us from having to create a listener every time get view is called
        // Cited from here:
        // http://scottweber.com/2013/04/30/adding-click-listeners-to-views-in-adapters/
        private View.OnClickListener mSaveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBtn.setVisibility(View.GONE);

                String text = editFeatureNameView.getText().toString();
                editFeatureNameView.setVisibility(View.GONE);

                featureNameView.setVisibility(View.VISIBLE);
                featureNameView.setText(text);
            }
        };

    }
}

class Feature {

    private String featureName;
    public Feature(String value){
        featureName = value;
    }

    public String getFeatureName(){
        return featureName;
    }
}