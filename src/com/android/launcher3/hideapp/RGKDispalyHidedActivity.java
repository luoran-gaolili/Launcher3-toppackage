package com.android.launcher3.hideapp;

import java.util.ArrayList;

import com.android.launcher3.LauncherModel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.launcher3.R;

public class RGKDispalyHidedActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        OTDB=new OptDatabase(this);
        nothideappList=LauncherModel.hidedAddAppItems;
        Log.i(TAG,"nothideappList:"+nothideappList.size());
        super.onCreate(savedInstanceState);
        contactsDelBtn.setText(R.string.display_apps);
    }
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.select_all:
            selectAll();
            break;
        case R.id.contacts_delete_btn:
            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.dialog_display_title)
                    ).setMessage(getResources().getString(R.string.dialog_display_hidedapp)
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
                                OTDB.deleteDataByPackageName(nothideappList.get(i).getPackageName());
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
