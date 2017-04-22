package seproject2.secure_messenger;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import seproject2.secure_messenger.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements InboxFragment.OnListFragmentInteractionListener {

    private static FloatingActionButton button_X;


    View.OnClickListener mOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "start a new message", Snackbar.LENGTH_LONG)
                        .setAction("SEND", mOnClickListener).show();
            }
        });
        //ShowContactsListener();


        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("seproject2.secure_messenger.Messenger");
                startActivity(intent);
            }
        };
    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
