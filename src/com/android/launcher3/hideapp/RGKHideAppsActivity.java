package com.android.launcher3.hideapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.android.launcher3.AppInfo;
import com.android.launcher3.R;
import com.android.launcher3.LauncherModel;

public class RGKHideAppsActivity extends BaseActivity {
    String [] aStrings;
    private ArrayList<String> noNeedHideapp ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        aStrings = this.getResources().getStringArray(R.array.no_need_hideapp);
        noNeedHideapp = new ArrayList<String>(Arrays.asList(aStrings) );
    Log.i(TAG,"onCreate:"+LauncherModel.allAddAppItems.size()+",====:"+aStrings.length);
    for (AppInfo appi: LauncherModel.allAddAppItems) {
        if(!noNeedHideapp.contains(appi.getPackageName())){
            nothideappList.add(appi);
        }
    }
    super.onCreate(savedInstanceState);
    contactsDelBtn.setText(R.string.hide_apps);
    }
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.select_all:
            selectAll();
            break;
        case R.id.contacts_delete_btn:
            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.dialog_hide_title)
                    ).setMessage(getResources().getString(R.string.dialog_hide_app)
                    ).setNegativeButton(
                    android.R.string.cancel, null).setPositiveButton(
                    android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(int i = contactsDeleteAdapter.selectedMap.size()-1 ; i>-1 ; i--){
                                if(contactsDeleteAdapter.selectedMap.get(i)){
                                Log.i(TAG, contactsDeleteAdapter.selectedMap.get(i)+
                                        ",nothideappList.get(i).getPackageName():"+nothideappList.get(i).getPackageName()+
                                        ",nothideappList.get(i).getTitle():"+nothideappList.get(i).getTitle());
                                OTDB.addAppInfoData(nothideappList.get(i).getTitle(),nothideappList.get(i).getPackageName());
                                nothideappList.remove(i);
                                }
                            }
                            Intent intent =new Intent(HideAppConfig.HIDE_APP_ACTION);
                            sendBroadcast(intent);
                            refreshData();
                        }
                    }).setCancelable(true).create().show();
            break;
        case R.id.contacts_cancel_btn:
            finish();
            break;
        }
    }
}
