package hcay.pui.com.umlapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.util.List;

import hcay.pui.com.recognizer.Template;

/**
 * Created by andrasta on 11/8/15.
 */
public class MemberAdapter extends ArrayAdapter<Member> {
    private List<Member> memberList;
    private Context context;

    public MemberAdapter(Context ctx, List<Member> memberList) {
        super(ctx, R.layout.member_row_layout, memberList);
        this.memberList = memberList;
        this.context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

//        View v = convertView;
        MemberHolder holder;

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.member_row_layout, parent, false);

            // Now we can fill the layout with the right values
            EditText editMemberName = (EditText) convertView.findViewById(R.id.editMemberName);
            TextView memberName = (TextView) convertView.findViewById(R.id.memberName);
            Button save = (Button) convertView.findViewById(R.id.saveMemberBtn);
            Button remove = (Button) convertView.findViewById(R.id.removeMemberBtn);

            holder = new MemberHolder(editMemberName, memberName, save, remove);
            convertView.setTag(holder);

        } else
            holder = (MemberHolder) convertView.getTag();

        holder.saveBtn.setTag(position);

        Member member = memberList.get(position);
        holder.editMemberNameView.setText(member.getMemberName());

        return convertView;
    }

    public void addItem(String memberName){
        memberList.add(new Member(memberName));
    }

    private static class MemberHolder {
        public EditText editMemberNameView;
        public TextView memberNameView;
        public Button saveBtn;
        public Button removeBtn;

        public MemberHolder(EditText edit, TextView label, Button save, Button remove){
            editMemberNameView = edit;
            memberNameView = label;
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

                String text = editMemberNameView.getText().toString();
                editMemberNameView.setVisibility(View.GONE);

                memberNameView.setVisibility(View.VISIBLE);
                memberNameView.setText(text);
            }
        };



    }
}

class Member {

    private String memberName;
    public Member(String value){
        memberName = value;
    }

    public String getMemberName(){
        return memberName;
    }
}