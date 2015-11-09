package hcay.pui.com.umlapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by andrasta on 11/8/15.
 */
public class MemberAdapter extends ArrayAdapter<Member> {
    private List<Member> memberList;
    private Context context;

    public MemberAdapter(List<Member> planetList, Context ctx) {
        super(ctx, R.layout.member_row_layout, planetList);
        this.memberList = planetList;
        this.context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.member_row_layout, parent, false);
        }

        // Now we can fill the layout with the right values
        TextView tv = (TextView) convertView.findViewById(R.id.memberName);
        Member member = memberList.get(position);

        tv.setText(member.getMemberName());
//        distView.setText("" + p.getDistance());

        return convertView;
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