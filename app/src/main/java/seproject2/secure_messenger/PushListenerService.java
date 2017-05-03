package seproject2.secure_messenger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import seproject2.secure_messenger.dummy.DummyContent;

import static seproject2.secure_messenger.AESEncryption.IV;

/** A service that listens to GCM notifications. */
public class PushListenerService extends GcmListenerService {

    private static final String LOG_TAG = PushListenerService.class.getSimpleName();
    private Button button_Z;
    private Button btOk;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private boolean encrypted=false;
    private String encryptionKey;
    private EditText edPop;
    private RelativeLayout relativeLayout;
    private static UIListener mUIListener;
    private MainActivity mainActivity;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    /**
     * Helper method to extract SNS message from bundle.
     *
     * @param data bundle
     * @return message string from SNS push notification
     */
    public static String getMessage(final Bundle data) {
        // If a push notification is sent as plain text, then the message appears in "default".
        // Otherwise it's in the "message" for JSON format.
        return data.containsKey("default") ? data.getString("default") : data.getString(
                "message", "");
    }

    public static void setUIListener(UIListener listener){
        mUIListener = listener;
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs. For Set of keys use
     * data.keySet().
     */
    @Override
    public void onMessageReceived(final String from, final Bundle data) {
        //mainActivity.onupdate();
        String message = getMessage(data);
        final DummyContent.DummyItem item = new DummyContent.DummyItem(from, message, message);
        DummyContent.addItem(item);
        Log.d(LOG_TAG, DummyContent.ITEMS.toString());
        mUIListener.onUIUpdate(from, message);

        scheduler.schedule(new Runnable() { public void run() {
            DummyContent.remItem(item);
            mUIListener.onUIUpdate(null, null);
        }}, 60 * 5, TimeUnit.SECONDS);


        if(encrypted){

        }

        //Log.d(LOG_TAG,"Encrypted?"+)

        Log.d(LOG_TAG, "From: " + from);
        Log.d(LOG_TAG, "Message: " + message);

    }
    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");

        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));

        return new String(cipher.doFinal(cipherText),"UTF-8");

    }

}
