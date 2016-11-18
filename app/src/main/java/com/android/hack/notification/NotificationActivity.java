package com.android.hack.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.hack.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Android通知
 * 1.删除整个分组通知时，每个通知都会发送删除的广播
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
    // Key for the string that's delivered in the action's intent.
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private PendingIntent replyPendingIntent;
    protected static final String ACTION_NOTIFICATION_REPLY
            = "com.android.hack.notification.activenotifications.reply";
    private List<CharSequence> mReplyHistorys;

    private static final String KEY_TEXT_CHAT = "key_text_chat";
    private PendingIntent chatPendingIntent;
    protected static final String ACTION_NOTIFICATION_CHAT
            = "com.android.hack.notification.activenotifications.chat";
    private List<CharSequence> mChatHistorys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        findViewById(R.id.btn_notification_normal).setOnClickListener(this);
        findViewById(R.id.btn_notification_group).setOnClickListener(this);
        findViewById(R.id.btn_notification_reply).setOnClickListener(this);
        findViewById(R.id.btn_notification_chat).setOnClickListener(this);

        mNotificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        Intent deleteIntent = new Intent(NotificationActivity.ACTION_NOTIFICATION_DELETE);
        mDeletePendingIntent = PendingIntent.getBroadcast(this,
                REQUEST_CODE, deleteIntent, 0);
        registerReceiver(mDeleteReceiver, new IntentFilter(NotificationActivity.ACTION_NOTIFICATION_DELETE));

        Intent replyIntent = new Intent(NotificationActivity.ACTION_NOTIFICATION_REPLY);
        replyPendingIntent = PendingIntent.getBroadcast(this,
                REQUEST_CODE, replyIntent, 0);
        registerReceiver(mReplyReceiver, new IntentFilter(NotificationActivity.ACTION_NOTIFICATION_REPLY));
        mReplyHistorys = new ArrayList<>();

        Intent chatIntent = new Intent(NotificationActivity.ACTION_NOTIFICATION_CHAT);
        chatPendingIntent = PendingIntent.getBroadcast(this,
                REQUEST_CODE, chatIntent, 0);
        registerReceiver(mChatReceiver, new IntentFilter(NotificationActivity.ACTION_NOTIFICATION_CHAT));

        mChatHistorys = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDeleteReceiver);
    }

    private BroadcastReceiver mDeleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            Log.e(TAG, "intent.getAction=" + intent.getAction());
            if (ACTION_NOTIFICATION_DELETE.equals(intent.getAction())) {
                updateNumberOfNotifications();
            }
        }
    };

    private BroadcastReceiver mReplyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
                if (remoteInput != null) {
                    Log.e(TAG, "回复的内容:" + remoteInput.getCharSequence(KEY_TEXT_REPLY));

                    sendReplyNotification(remoteInput.getCharSequence(KEY_TEXT_REPLY).toString(), mNormalNotificationId, mReplyHistorys);
                }
            }
        }
    };
    private BroadcastReceiver mChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
                if (remoteInput != null) {
                    Log.e(TAG, "回复的内容:" + remoteInput.getCharSequence(KEY_TEXT_CHAT));

                    mChatHistorys.add(remoteInput.getCharSequence(KEY_TEXT_CHAT));
                    sendChatNotification(mNormalNotificationId, mChatHistorys);
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_notification_normal:
                sendNormalNotification();
                break;
            case R.id.btn_notification_group:
                sendGroupNotification();
                break;
            case R.id.btn_notification_reply:
                mNormalNotificationId++;
                mReplyHistorys.clear();
                sendReplyNotification("收到请回复+" + mNormalNotificationId, mNormalNotificationId, mReplyHistorys);
                break;
            case R.id.btn_notification_chat:
                mNormalNotificationId++;
                mChatHistorys.clear();
                mChatHistorys.add("收到请回复");
                sendChatNotification(mNormalNotificationId, mChatHistorys);
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
     * 组通知实际上是一条单独的通知
     * Adds/updates/removes the notification summary as necessary.
     */
    protected void updateNotificationSummary() {
        final StatusBarNotification[] activeNotifications = mNotificationManager
                .getActiveNotifications();

        int numberOfNotifications = activeNotifications.length;
        // Since the notifications might include a summary notification remove it from the count if
        // it is present.
        for (StatusBarNotification notification : activeNotifications) {
            if (NOTIFICATION_GROUP.equals(notification.getNotification().getGroup()) == false) {
                numberOfNotifications--;
            }
            if (NOTIFICATION_GROUP.equals(notification.getNotification().getGroup()) && notification.getId() == NOTIFICATION_GROUP_SUMMARY_ID) {
                numberOfNotifications--;
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
        }
    }

    /**
     * Requests the current number of notifications from the {@link NotificationManager} and
     * display them to the user.
     */
    protected void updateNumberOfNotifications() {
        final StatusBarNotification[] activeNotifications = mNotificationManager
                .getActiveNotifications();

        int numberOfNotifications = activeNotifications.length;
        // Since the notifications might include a summary notification remove it from the count if
        // it is present.
        for (StatusBarNotification notification : activeNotifications) {
            if (NOTIFICATION_GROUP.equals(notification.getNotification().getGroup()) == false) {
                numberOfNotifications--;
            }
            if (NOTIFICATION_GROUP.equals(notification.getNotification().getGroup()) && notification.getId() == NOTIFICATION_GROUP_SUMMARY_ID) {
                numberOfNotifications--;
            }
        }
        if (numberOfNotifications > 0) {
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

    private void sendReplyNotification(String text, int id, List<CharSequence> history) {
        // 系统最多显示3条历史消息
        if (history.size() > 3) {
            history = history.subList(history.size() - 3, history.size());
        }
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel("回复：")
                .build();

        // Create the reply action and add the remote input.
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.ic_launcher, "Type message", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        // Build the notification and add the action.
        Notification newMessageNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(text)
                .addAction(action)
                .setColor(Color.GREEN)
                .setRemoteInputHistory(history.toArray(new CharSequence[]{}))
                .build();
        // Issue the notification.
        mNotificationManager.notify(id, newMessageNotification);

        mReplyHistorys.add(text);
    }

    private void sendChatNotification(int id, List<CharSequence> history) {
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_CHAT)
                .setLabel("回复：")
                .build();

        // Create the reply action and add the remote input.
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.ic_launcher, "Type message", chatPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        Collections.reverse(history);
        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("Me");
        for (CharSequence msg : history) {
            if (msg.toString().equals("收到请回复")) {
                messagingStyle.addMessage(msg, System.currentTimeMillis(), "小明");
            } else {
                messagingStyle.addMessage(msg, System.currentTimeMillis(), null);
            }
        }
        Collections.reverse(history);
        // Build the notification and add the action.
        Notification newMessageNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .addAction(action)
                .setColor(Color.GREEN)
                .setStyle(messagingStyle)
                .build();
        // Issue the notification.
        mNotificationManager.notify(id, newMessageNotification);
    }
}
