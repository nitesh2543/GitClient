package com.lbrand.githubclient.common;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;


public class FragmentHelper {
    /**
     * @param activity
     * @param containerId
     * @param fragment
     */
    public static void replaceFragment(final FragmentActivity activity, final int containerId,
                                       final Fragment fragment) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment, fragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public static void replaceAndAddFragment(final FragmentActivity activity, final int containerId,
                                             final Fragment fragment) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(final FragmentActivity activity, final int containerId,
                                   final Fragment fragment) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.add(containerId, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Pop the top state off the back stack immediately .
     *
     * @param activity
     */

    public static void popBackStackImmediate(final FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStackImmediate();
    }

    public static void clearBackStack(final FragmentActivity activity) {
        try {
            activity.getSupportFragmentManager().popBackStackImmediate(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    public static int getStackCount(final FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        return fragmentManager.getBackStackEntryCount();
    }

    public static Fragment getFragment(final FragmentActivity activity, final int containerId) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        return fragmentManager.findFragmentById(containerId);
    }
}
