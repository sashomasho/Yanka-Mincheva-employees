package com.sirma.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sirma.R;
import com.sirma.model.Team;

import java.util.ArrayList;

public class EmployeesAdapter extends BaseAdapter {

    public ArrayList<Team> mTeamList;
    Activity activity;

    public EmployeesAdapter(Activity activity, ArrayList<Team> teamList) {
        super();
        this.activity = activity;
        this.mTeamList = teamList;
    }

    @Override
    public int getCount() {
        return mTeamList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTeamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView emplID_1_Txt;
        TextView emplID_2_Txt;
        TextView projectID_Txt;
        TextView daysWorked_Txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_listview, null);
            holder = new ViewHolder();
            holder.emplID_1_Txt = (TextView) convertView.findViewById(R.id.emplID_1_Txt);
            holder.emplID_2_Txt = (TextView) convertView.findViewById(R.id.emplID_2_Txt);
            holder.projectID_Txt = (TextView) convertView
                    .findViewById(R.id.projectID_Txt);
            holder.daysWorked_Txt = (TextView) convertView.findViewById(R.id.daysWorked_Txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Team item = mTeamList.get(position);
        holder.emplID_1_Txt.setText(""+ item.getFirstEmployeeId());
        holder.emplID_2_Txt.setText(""+item.getSecondEmployeeId());
        holder.projectID_Txt.setText(""+item.getProjectId());
        holder.daysWorked_Txt.setText(""+item.getTotalDuration());

        return convertView;
    }
}
