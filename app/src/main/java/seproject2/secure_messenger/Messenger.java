package seproject2.secure_messenger;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.mobile.push.SnsTopic;
import android.util.Log;
import com.amazonaws.mobile.push.PushManager;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;


import java.util.Map;

public class Messenger extends AppCompatActivity {
    private static final String TAG = "B_MESSAGE";
    //View.OnClickListener sendClickListener;

    private static Button button_X;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);


        SEND();
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


}

