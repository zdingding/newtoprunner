package com.toprunner.ubii.toprunner.mvp.model;

import android.os.AsyncTask;

/**
 * Created by ${赵鼎} on 2016/10/27 0027.
 */

public class CircleModel {

    public CircleModel(){
        //
    }
    public void loadData(final IDataRequestListener listener){
        requestServer(listener);
    }

    private void requestServer(final IDataRequestListener listener) {

        new AsyncTask<Object, Integer, Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                //和后台交互
                return null;
            }

            protected void onPostExecute(Object result) {
                listener.loadSuccess(result);
            };
        }.execute();
    }


}
