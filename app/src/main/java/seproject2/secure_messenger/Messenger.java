package seproject2.secure_messenger;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.push.PushManager;
import com.amazonaws.mobile.push.SnsTopic;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.AccountsDO;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static seproject2.secure_messenger.AESEncryption.IV;

public class Messenger extends AppCompatActivity {
    private static final String TAG = "B_MESSAGE";
    //View.OnClickListener sendClickListener;

    private Button button_X;
    private Button button_Y;
    private Button button_Z;
    private Button btOk;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private boolean encrypted=false;
    private String encryptionKey = "0123456789abcedf";
    private EditText edPop;
    private RelativeLayout relativeLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger3);
        


        pattern();
        encryptbutton();
        SEND();
    }
    private class Publish extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... message) {
            // prepare to get endpoint ARN
            AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());
            DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
            AccountsDO recipient = null;

            //print MessageId of message published to SNS topic
            PushManager pushManager = AWSMobileClient.defaultMobileClient().getPushManager();
            AmazonSNS snsClient = pushManager.getsnsclient();
            Map<String, SnsTopic> topics = pushManager.getTopics();
            String topicArn = topics.keySet().toArray()[0].toString();

            // separate target from the message itself

            String msg = message[0];
            String tokens[] = msg.split("<.>");
            Log.d("tag", tokens[0]);
            Log.d("tag", tokens[1]);

            // check if the recipient exists
            try {
                recipient = mapper.load(AccountsDO.class, tokens[0]);
            } catch (AmazonServiceException ase) {
                System.err.print(ase.getErrorMessage());
            } catch (AmazonClientException ace) {
                System.err.println(ace.getMessage());
            }

            // set up message
            PublishRequest publishRequest = new PublishRequest();
            publishRequest.setMessage(tokens[1]);
            if (recipient != null) {
                publishRequest.setTargetArn(recipient.getDeviceID());
            }

            // send the message
            PublishResult publishResult = snsClient.publish(publishRequest);

            //Log.i(TAG, "**********" + msg);


             //Log.i(TAG, "**********" + "Message - " + publishResult.getMessageId());
            return null;
        }
    }

    public void SEND (){
        button_X = (Button)findViewById(R.id.send);
        button_X.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {
                        EditText e = (EditText) findViewById(R.id.message);
                        String msg = e.getText().toString();
                        EditText f = (EditText) findViewById(R.id.destiny);
                        String destiny = f.getText().toString();
                        //Log.i(TAG, "$$$$$$$" + msg);
                        //Log.i(TAG, "$$$$$$$" + destiny);
                        boolean ispatterned = false;
                        String Message= "/0";
                        if(encrypted){
                            try {
                                msg = msg +"/0/0/0";
                                byte [] cipher = encrypt(msg, encryptionKey);
                                String s = new String(cipher);
                                Message = destiny + "<*>" + s;

                            }
                            catch(Exception y){
                                y.printStackTrace();
                            }
                        }
                        else{
                            Message = String.valueOf(encrypted)+ "<*>"+ String.valueOf(ispatterned) + "<*>"+ destiny + "<*>" + msg;
                        }

                        new Publish().execute(Message);
                    }
                }
        );
        //Intent intent = new Intent(this,activity_contacts_table.class)
    }
public void pattern(){
    button_Y = (Button)findViewById(R.id.Pattern);
    button_Y.setOnClickListener(
            new View.OnClickListener() {

                public void onClick(View v) {
                    //Intent patterntoshow = new Intent(v.getContext(), PatternToShow.class);
                   // startActivity(patterntoshow);
                }
            }
    );
    //Intent intent = new Intent(this,activity_contacts_table.class)

}
    public void encryptbutton(){
        button_Z = (Button)findViewById(R.id.Encrypt);
        button_Z.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if(!encrypted){
                            encrypted=true;
                            button_Z.setBackgroundColor(Color.GREEN);
                            //EditText e = (EditText) findViewById(R.id.message);
                           // String msg = e.getText().toString();

                            layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            ViewGroup popupView = (ViewGroup)layoutInflater.inflate(R.layout.encryption_key_pop_up,null);

                            popupWindow = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,false);
                            edPop = (EditText)popupView.findViewById(R.id.edit_pop);
                            btOk  = (Button)popupView.findViewById(R.id.btok);
                            edPop.requestFocus();
                            relativeLayout = (RelativeLayout)findViewById(R.id.messenger3);
                            edPop.setText(encryptionKey);
                           popupWindow.showAtLocation(relativeLayout, Gravity.CENTER,500,500);
                            popupWindow.setOutsideTouchable(false);

                            popupWindow.setFocusable(true);
                            popupWindow.update();
                            btOk.setOnClickListener(new View.OnClickListener()
                            {
                            public void onClick(View v)
                            {
                                encryptionKey= edPop.getText().toString();
                                popupWindow.dismiss();
                            }
                        });


                        }
                        else{
                            encrypted= false;
                            button_Z.setBackgroundColor(Color.WHITE);
                        }

                    }
                });
        //Intent intent = new Intent(this,activity_contacts_table.class)
    }


    public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");

        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));

        return cipher.doFinal(plainText.getBytes("UTF-8"));

    }

}

