
package com.android.launcher3.hideapp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.android.launcher3.AllAppsList;
import com.android.launcher3.AppInfo;
import com.android.launcher3.LauncherModel;
import com.android.launcher3.LauncherAppState;
import com.android.launcher3.R;

import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BaseActivity extends Activity implements OnClickListener,
        OnItemClickListener {
    protected ArrayList<AppInfo> nothideappList = new ArrayList<AppInfo>();
    protected static final String TAG = "MultiSelectActivity";
    protected ListView contactsDelList;
    protected Button contactsDelBtn;
    protected Button contactsCancelBtn;
    protected CheckBox selectAll;
    protected ContactsDeleteAdapter contactsDeleteAdapter;
    protected ArrayList<String> packagenameArrayList;
    protected OptDatabase OTDB=new OptDatabase(this);
    protected ContactsDeleteListItemViews holderViews;
    private SQLiteDatabase db;

    private final class ContactsDeleteListItemViews {
        TextView nameView;
        CheckBox delCheckBox;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contacts_delete_activity);
        contactsDelList = (ListView) findViewById(R.id.contacts_delete_list);
        contactsDelBtn = (Button) findViewById(R.id.contacts_delete_btn);
        contactsCancelBtn = (Button) findViewById(R.id.contacts_cancel_btn);
        selectAll = (CheckBox) (findViewById(R.id.contacts_delete_list_header)
                .findViewById(R.id.select_all));
        contactsDelList.setOnItemClickListener(this);
        contactsDelBtn.setOnClickListener(this);
        contactsCancelBtn.setOnClickListener(this);
        selectAll.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
    Log.i(TAG,"onResume");
        super.onResume();
        refreshData();
    }

    protected void refreshData() {
        contactsDeleteAdapter = new ContactsDeleteAdapter(this);
        contactsDelList.setAdapter(contactsDeleteAdapter);
    }

    class ContactsDeleteAdapter extends BaseAdapter {
        Map<Integer, Boolean> selectedMap;
        HashSet<String> delContactsIdSet;

        public ContactsDeleteAdapter(Context context) {
            selectedMap = new HashMap<Integer, Boolean>();
            delContactsIdSet = new HashSet<String>();
            Log.i(TAG,"ContactsDeleteAdapter,nothideappList.size():"+nothideappList.size());
            for (int i = 0; i <nothideappList.size(); i++) {
                selectedMap.put(i, false);
                Log.i(TAG,"nothideappList.get(position):"+nothideappList.get(i).getIntent().getAction());
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(BaseActivity.this)
                        .inflate(R.layout.contacts_delete_list_item, null);
                
                holderViews = new ContactsDeleteListItemViews();
                holderViews.nameView = (TextView) convertView
                        .findViewById(R.id.name);
                holderViews.delCheckBox = (CheckBox) convertView
                        .findViewById(R.id.delete_list_item);
                convertView.setTag(holderViews);
            }
            ContactsDeleteListItemViews views = (ContactsDeleteListItemViews) convertView
                    .getTag();
            final String name = "  " + nothideappList.get(position).getTitle();
            Drawable imgDrawable = new BitmapDrawable(nothideappList.get(position).getIconBitmap());
            imgDrawable.setBounds(0, 0, imgDrawable.getMinimumWidth(), imgDrawable.getMinimumHeight());
            views.nameView.setCompoundDrawables(imgDrawable, null, null, null);
            
            views.nameView.setText(name);
            views.delCheckBox.setChecked(selectedMap.get(position));

            if (selectedMap.get(position)) {
                delContactsIdSet.add(String.valueOf(nothideappList.get(position).id));
            } else {
                delContactsIdSet.remove(String.valueOf(nothideappList.get(position).id));
            }
            return convertView;
        }

        @Override
        public int getCount() {
               return nothideappList.size();
        }

        @Override
        public Object getItem(int position) {
            Log.i(TAG,"nothideappList.get(position):"+nothideappList.get(position).getPackageName());
            if (position<nothideappList.size()) {
                return nothideappList.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    @Override
    public void onClick(View v) {
    }
    public void selectAll(){
        if (selectAll.isChecked()) {
            for (int i = 0; i < contactsDeleteAdapter.getCount(); i++) {
                contactsDeleteAdapter.selectedMap.put(i, true);
            }
            contactsDelBtn.setEnabled(true);
        } else {
            for (int i = 0; i < contactsDeleteAdapter.getCount(); i++) {
                contactsDeleteAdapter.selectedMap.put(i, false);
            }
            contactsDeleteAdapter.delContactsIdSet.clear();
            contactsDelBtn.setEnabled(false);
        }
        contactsDeleteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
            long id) {
        Log.i(TAG, "onItemClick,position:"+position+",id:"+id);
        ContactsDeleteListItemViews views = (ContactsDeleteListItemViews) view
                .getTag();
        views.delCheckBox.toggle();
        contactsDeleteAdapter.selectedMap.put(position, views.delCheckBox
                .isChecked());
        contactsDeleteAdapter.notifyDataSetChanged();
        if (contactsDeleteAdapter.selectedMap.containsValue(false)) {
            selectAll.setChecked(false);
        } else {
            selectAll.setChecked(true);
        }
        
        if (contactsDeleteAdapter.selectedMap.containsValue(true)) {
            contactsDelBtn.setEnabled(true);
        } else {
            contactsDelBtn.setEnabled(false);
        }
    }
}
