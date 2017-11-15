package com.dandy.glengine;

import android.content.Context;

import com.dandy.helper.android.LogHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class Group extends Actor {
    private static final String TAG = "Group";

    public Group(Context context) {
        super(context);
    }

    protected List<Actor> mChildren = new ArrayList<Actor>();

    /**
     * <pre>
     * Add the actors into this group.
     * 添加actors到组里来
     * </pre>
     *
     * @param actors actors to add as children
     */
    public void addChild(Actor... actors) {
        synchronized (this) {
            for (Actor child : actors) {
                if (child == null) {
                    continue;
                }
                // Avoid adding duplicated child
                if (mChildren.contains(child)) {
                    LogHelper.d(TAG, "The actor is already in the group");
                    continue;
                }
                mChildren.add(child);
                onChildAdded(child);
                child.setParent(this);
                LogHelper.d(TAG, LogHelper.getThreadName() + "mContext=" + mContext);
            }
        }
    }

    /**
     * @param child
     */
    protected abstract void onChildAdded(Actor child);

}
