package com.nabdulla.chatdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    private ListView messageList;
    private EditText messageBox;
    private Button sendButton;
    private MessageListAdapter listAdapter;
    private MessageFactory messageFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageFactory = new MessageFactory();

        messageBox = findViewById(R.id.message_box);
        messageList = findViewById(R.id.message_list);
        sendButton = findViewById(R.id.send_button);

        listAdapter = new MessageListAdapter(this);
        messageList.setAdapter(listAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageBox.getText().length() != 0) {
                    sendMessage(messageBox.getText().toString());
                }
            }
        });
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

class OptionFactory {
    private List<Option> options = new ArrayList<>();
    private Random random = new Random();

    public OptionFactory() {
        for (int i = 0; i < 100; i++) {
            String name = "Option " + i;
            String id = "option-" + i;
            options.add(new Option(name, id));
        }
    }

    public List<Option> getOptions(int count) {
        int st = random.nextInt(options.size() - 2);
        return options.subList(st, Math.min(options.size() - 1, st + count));
    }
}

class MessageFactory {
    private List<Message> messages = new ArrayList<>();
    private Random random = new Random();
    private OptionFactory factory = new OptionFactory();
    private int next;

    public MessageFactory() {
        for (int i = 0; i < 20; i++) {
            boolean hasOptions = random.nextBoolean();
            List<Option> options;
            Message message = new Message("Sample Message " + i, true, hasOptions);
            if (hasOptions) {
                options = factory.getOptions(random.nextInt(10));
                message.getOptions().addAll(options);
            }

            messages.add(message);
        }
        next = 0;
    }

    public Message getNextMessage() {
        Message message = messages.get(next);
        next = (next + 1) % messages.size();
        return message;
    }
}