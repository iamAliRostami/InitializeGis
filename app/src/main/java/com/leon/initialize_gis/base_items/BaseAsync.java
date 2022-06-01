package com.leon.initialize_gis.base_items;

import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.leon.initialize_gis.di.view_model.ProgressModel;

public abstract class BaseAsync extends AsyncTask<Activity, Void, Void> {
    private final ProgressModel progress;
    private final Object view;
    private final boolean showProgress;

    public BaseAsync(Context context, boolean showProgress, Object... view) {
        super();
        if (view != null && view.length > 0)
            this.view = view[0];
        else this.view = null;
        this.progress = getApplicationComponent().ProgressModel();
        this.showProgress = showProgress;
        this.progress.show(context);
    }

    public BaseAsync(Context context, Object... view) {
        super();
        if (view != null && view.length > 0)
            this.view = view[0];
        else this.view = null;
        this.progress = getApplicationComponent().ProgressModel();
        this.showProgress = true;
        this.progress.show(context);
    }

    public BaseAsync(boolean showProgress) {
        super();
        this.view = null;
        this.progress = getApplicationComponent().ProgressModel();
        this.showProgress = showProgress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        preTask(view);
    }

    @Override
    protected Void doInBackground(Activity... activities) {
        activities[0].runOnUiThread(() -> backgroundTask(activities[0]));
        backgroundTask((Context) activities[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        postTask(view);
        if (showProgress)
            progress.cancelDialog();
    }

    public ProgressModel getProgress() {
        return progress;
    }

    public abstract void postTask(Object o);

    public abstract void preTask(Object o);

    public abstract void backgroundTask(Activity activity);

    public abstract void backgroundTask(Context context);
}
