package com.nabdulla.chatdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView messageList;
    private EditText messageBox;
    private Button sendButton;
    private MessageListAdapter listAdapter;
    private MessageFactory messageFactory;
    private ConstraintLayout editorSection;//todo we will use this to show or hide messageBox and sendButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageFactory = new MessageFactory();

        messageList = findViewById(R.id.message_list);
        editorSection = findViewById(R.id.editor_section);
        messageBox = findViewById(R.id.message_box);
        sendButton = findViewById(R.id.send_button);

        listAdapter = new MessageListAdapter(this);
        messageList.setAdapter(listAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageBox.getText().length() != 0) {
                    sendMessage(messageBox.getText().toString());
                    messageBox.getText().clear();
                }

                if (messageBox.hasOnClickListeners()) {
                    messageBox.setOnClickListener(null);
                }
            }
        });

        /*todo we will use these at runtime, depending on the command from server
        messageBox.setOnClickListener(new DatePicker());
        messageBox.setOnClickListener(new TimePicker());*/
    }

    private void sendMessage(String str) {
        listAdapter.addMessage(new Message(str, false, false));//like send a message to server
        listAdapter.addMessage(messageFactory.getNextMessage());//like a message from server
        messageList.setSelection(listAdapter.getCount() - 1);
    }

    class MessageListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Message> messages = new ArrayList<>();

        public MessageListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Message getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            Message message = getItem(position);
            if (message.isBotMessage()) {
                if (message.hasOptions()) {
                    view = getLayoutInflater().inflate(R.layout.bot_message_options, parent, false);
                    LinearLayout linearLayout = view.findViewById(R.id.options_holder);
                    ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    boolean isFirst = true;
                    for (Option option : message.getOptions()) {
                        if (!isFirst)
                            linearLayout.addView(getLayoutInflater().inflate(R.layout.options_divider, parent, false));
                        isFirst = false;
                        TextView textView = new TextView(context);
                        textView.setText(option.getName());
                        textView.setTag(option.getId());//will use on OnClickListener
                        textView.setPadding(5, 5, 5, 5);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                optionClickListener(v);
                            }
                        });
                        linearLayout.addView(textView, layoutParams);
                    }
                } else {
                    view = getLayoutInflater().inflate(R.layout.bot_message_normal, parent, false);
                    TextView msg = view.findViewById(R.id.b_msg);
                    msg.setText(message.getMessage());
                }
            } else {
                view = getLayoutInflater().inflate(R.layout.user_message, parent, false);
                TextView msg = view.findViewById(R.id.u_msg);
                msg.setText(message.getMessage());
            }
            return view;
        }

        private void optionClickListener(View v) {
            String option_id = (String) v.getTag();//todo use 'option_id' to send update to server
            sendMessage(((TextView) v).getText().toString());
            Toast.makeText(context, "Selected " + option_id, Toast.LENGTH_SHORT).show();
        }

        public void addMessage(Message msg) {
            messages.add(msg);
            notifyDataSetChanged();
        }
    }
}