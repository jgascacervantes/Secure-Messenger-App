package seproject2.secure_messenger;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.push.PushManager;
import com.amazonaws.mobile.push.SnsTopic;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.Map;

public class Messenger extends AppCompatActivity {
    private static final String TAG = "B_MESSAGE";
    //View.OnClickListener sendClickListener;

    private Button button_X;
    private Button button_Y;
    private Button button_Z;
    private boolean encrypted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        pattern();
        SEND();
        encrypt();
    }
    public class Publish extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... message) {
            //print MessageId of message published to SNS topic
            PushManager pushManager = AWSMobileClient.defaultMobileClient().getPushManager();
            AmazonSNS snsClient = pushManager.getsnsclient();
            Map<String, SnsTopic> topics = pushManager.getTopics();
            String topicArn = topics.keySet().toArray()[0].toString();
            //publish to an SNS topic

            String msg = message[0];
            PublishRequest publishRequest = new PublishRequest(topicArn, msg);
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
                        String Message = destiny + "<*>" + msg;
                        new Publish().execute(Message);
                    }
                }
        );
        //Intent intent = new Intent(this,activity_contacts_table.class)
    }
    public void pattern(){
        button_Y = (Button)findViewById(R.id.Pattern);
        button_Y.setOnClickListener(
                new View.OnClickListener(){


                    public void onClick(View s) {
                       // Intent gotopattern = new Intent(s.getContext(),PatternToShow.class);
                      //  startActivity(gotopattern);
                    }
                }
        );
    }
    public void encrypt(){
        button_Z = (Button)findViewById(R.id.Encrypt);
        button_Z.setOnClickListener(
                new View.OnClickListener(){


                    public void onClick(View b) {
                       if(encrypted=false) {
                           encrypted = true;
                           button_Z.setBackgroundColor(Color.GREEN);
                       }
                       else{
                           encrypted=false;
                       button_Z.setBackgroundColor(Color.WHITE);
                       }
                    }
                }
        );
    }


}

