package seproject2.secure_messenger;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.push.PushManager;

import java.lang.ref.WeakReference;

import seproject2.secure_messenger.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements InboxFragment.OnListFragmentInteractionListener {

    private static FloatingActionButton button_X;
    private PushManager pushManager;
    private Fragment inboxFragment;
    View.OnClickListener mOnClickListener;
    LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private String encryptionKey = "Type key here";
    private EditText edPop;
    private RelativeLayout relativeLayout;
    private Button btOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PushListenerService.setUIListener(new UIListener(){
            @Override
            public void onUIUpdate(String from, String message) {
               UPDATEUI();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "start a new message", Snackbar.LENGTH_LONG)
                        .setAction("SEND", mOnClickListener).show();
            }
        });
        //ShowContactsListener();

        pushManager = AWSMobileClient.defaultMobileClient().getPushManager();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(final Void... params) {
                // register device first to ensure we have a push endpoint.
                pushManager.registerDevice();

                // if registration succeeded.
                if (pushManager.isRegistered()) {
                    try {
                        pushManager.setPushEnabled(true);
                        // Automatically subscribe to the default SNS topic
                        pushManager.subscribeToTopic(pushManager.getDefaultTopic());
                        return null;
                    } catch (final AmazonClientException ace) {
                        return ace.getMessage();
                    }
                }
                return "Failed to register for push notifications.";
            }
        }.execute();


        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("seproject2.secure_messenger.Messenger");
                startActivity(intent);
            }
        };
    }
    public void onupdate() {
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup popupView = (ViewGroup) layoutInflater.inflate(R.layout.encryption_key_pop_up, null);
        popupWindow = new PopupWindow(popupView, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, false);
        edPop = (EditText) popupView.findViewById(R.id.edit_pop);
        btOk = (Button) popupView.findViewById(R.id.btok);
        edPop.requestFocus();
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        edPop.setText(encryptionKey);
        popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 500, 500);
        popupWindow.setOutsideTouchable(false);

        popupWindow.setFocusable(true);
        popupWindow.update();
        btOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                encryptionKey = edPop.getText().toString();
                popupWindow.dismiss();
            }
        });
    }

    public void UPDATEUI(){
        runOnUiThread(new Runnable() {
            public void run(){
                Log.d("UIUPDATE", "in UIUPDATE");
                RecyclerView view = (RecyclerView) findViewById(R.id.fragment);
                view.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
