package hcay.pui.com.umlapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * ArrayAdapter to be used for the classifiers' listviews.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
 */
public class FeatureAdapter extends ArrayAdapter<Feature> {
    public List<Feature> featureList;

    private Context context;
    private boolean isMember;
    private boolean editable = true;

    public FeatureAdapter(Context ctx, List<Feature> featureList, boolean isMember) {
        super(ctx, R.layout.feature_row_layout, featureList);
        this.featureList = featureList;
        this.context = ctx;
        this.isMember = isMember;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.feature_row_layout, parent, false);
            FeatureHolder featureHolder = new FeatureHolder();
            featureHolder.editFeatureNameView = (EditText) v.findViewById(R.id.editFeatureName);
            featureHolder.removeButton = (Button) v.findViewById(R.id.removeButton);
            v.setTag(featureHolder);
        }

        final FeatureHolder featureHolder = (FeatureHolder) v.getTag();

        if (featureHolder.textWatcher != null) {
            featureHolder.editFeatureNameView.removeTextChangedListener(featureHolder.textWatcher);
        }

        final Feature feature = getItem(position);

        featureHolder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                feature.featureName = s.toString();
                featureHolder.editFeatureNameView.requestFocus();
                featureHolder.editFeatureNameView.setSelection(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        featureHolder.editFeatureNameView.addTextChangedListener(featureHolder.textWatcher);
        featureHolder.editFeatureNameView.setText(feature.featureName);
        featureHolder.editFeatureNameView.setHint("<" + (isMember ? "member" : "method") + ">");
        featureHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(featureList.get(position));
                notifyDataSetChanged();
            }
        });

        featureHolder.editFeatureNameView.clearFocus();
        featureHolder.editFeatureNameView.setClickable(editable);
        featureHolder.editFeatureNameView.setFocusableInTouchMode(editable);
        featureHolder.editFeatureNameView.setFocusable(editable);
        featureHolder.removeButton.setVisibility(editable ? View.VISIBLE : View.GONE);

        return v;
    }

    public void addItem(String featureName) {
        featureList.add(new Feature(featureName));
    }

    public void setEnabled(boolean enabled) {
        editable = enabled;
        notifyDataSetChanged();
    }

    private static class FeatureHolder {
        public EditText editFeatureNameView;
        public Button removeButton;
        public TextWatcher textWatcher;
    }
}

/**
 * Feature object for the FeatureAdapter list.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
 */
class Feature {
    public String featureName;
    public Feature(String value) {
        featureName = value;
    }
}
