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
public class MemberAdapter extends ArrayAdapter<Member> {
    private List<Member> memberList;
    private Context context;

    public MemberAdapter(Context ctx, List<Member> memberList) {
        super(ctx, R.layout.member_row_layout, memberList);
        this.memberList = memberList;
        this.context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        MemberHolder holder = new MemberHolder();

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

            holder.editMemberNameView = editMemberName;
            holder.memberNameView = memberName;
            holder.saveBtn = save;
            holder.removeBtn = remove;
            v.setTag(holder);

        } else
            holder = (MemberHolder) v.getTag();

        Member member = memberList.get(position);

        holder.editMemberNameView.setText(member.getMemberName());
//        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.memberNameView.setText(holder.editMemberNameView.getText());
//            }
//        });

//        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                memberList.remove(member);
//            }
//        });

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