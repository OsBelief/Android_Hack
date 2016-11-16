package com.android.hack.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.hack.R;

/**
 * Android通知
 */
public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "NotificationActivity";

    private static final int REQUEST_CODE = 2323;
    private static final String NOTIFICATION_GROUP =
            "com.android.hack.notification.activenotifications.notification_type";
    protected static final String ACTION_NOTIFICATION_DELETE
            = "com.android.hack.notification.activenotifications.delete";
    private static final int NOTIFICATION_GROUP_SUMMARY_ID = 1;
    private static int mNormalNotificationId = 1000;
    private static int sNotificationId = NOTIFICATION_GROUP_SUMMARY_ID + 1;
    private NotificationManager mNotificationManager;
    private PendingIntent mDeletePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        findViewById(R.id.btn_notification_normal).setOnClickListener(this);
        findViewById(R.id.btn_notification_group).setOnClickListener(this);

        mNotificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        Intent deleteIntent = new Intent(NotificationActivity.ACTION_NOTIFICATION_DELETE);
        mDeletePendingIntent = PendingIntent.getBroadcast(this,
                REQUEST_CODE, deleteIntent, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_notification_normal:
                sendNormalNotification();
                break;
            case R.id.btn_notification_group:
                sendGroupNotification();
                break;
        }
    }

    /**
     * 普通通知
     */
    private void sendNormalNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("这是普通通知")
                .setAutoCancel(true)
                .setDeleteIntent(mDeletePendingIntent);
        final Notification notification = builder.build();
        mNotificationManager.notify(mNormalNotificationId++, notification);
    }

    /**
     * 通知分组
     */
    private void sendGroupNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("这是分组通知")
                .setAutoCancel(true)
                .setDeleteIntent(mDeletePendingIntent)
                .setGroup(NOTIFICATION_GROUP);
        final Notification notification = builder.build();
        mNotificationManager.notify(getNewNotificationId(), notification);

        updateNotificationSummary();
    }

    /**
     * Adds/updates/removes the notification summary as necessary.
     */
    protected void updateNotificationSummary() {
        final StatusBarNotification[] activeNotifications = mNotificationManager
                .getActiveNotifications();

        int numberOfNotifications = activeNotifications.length;
        // Since the notifications might include a summary notification remove it from the count if
        // it is present.
        for (StatusBarNotification notification : activeNotifications) {
            if (notification.getId() == NOTIFICATION_GROUP_SUMMARY_ID) {
                numberOfNotifications--;
                break;
            }
        }

        if (numberOfNotifications > 1) {
            // Add/update the notification summary.
            String notificationContent = getString(R.string.sample_notification_summary_content,
                    "" + numberOfNotifications);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .setSummaryText(notificationContent))
                    .setGroup(NOTIFICATION_GROUP)
                    .setGroupSummary(true);
            final Notification notification = builder.build();
            mNotificationManager.notify(NOTIFICATION_GROUP_SUMMARY_ID, notification);
        } else {
            // Remove the notification summary.
            mNotificationManager.cancel(NOTIFICATION_GROUP_SUMMARY_ID);
        }
    }

    /**
     * Retrieves a unique notification ID.
     */
    public int getNewNotificationId() {
        int notificationId = sNotificationId++;

        // Unlikely in the sample, but the int will overflow if used enough so we skip the summary
        // ID. Most apps will prefer a more deterministic way of identifying an ID such as hashing
        // the content of the notification.
        if (notificationId == NOTIFICATION_GROUP_SUMMARY_ID) {
            notificationId = sNotificationId++;
        }
        return notificationId;
    }
}
