package seproject2.secure_messenger;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import seproject2.secure_messenger.dummy.DummyContent;

import static seproject2.secure_messenger.AESEncryption.IV;

/** A service that listens to GCM notifications. */
public class PushListenerService extends GcmListenerService {

    private static final String LOG_TAG = PushListenerService.class.getSimpleName();

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

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs. For Set of keys use
     * data.keySet().
     */
    @Override
    public void onMessageReceived(final String from, final Bundle data) {
        String message = getMessage(data);
        DummyContent.DummyItem item = new DummyContent.DummyItem(from, message, message);
        DummyContent.addItem(item);
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
