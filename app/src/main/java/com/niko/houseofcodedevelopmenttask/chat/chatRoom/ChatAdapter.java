package com.niko.houseofcodedevelopmenttask.chat.chatRoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.niko.houseofcodedevelopmenttask.R;

import java.util.ArrayList;

/**
 * This class will help inject chat elements into the chat view.
 */
public class ChatAdapter extends BaseAdapter {

    Context c;

    ArrayList<ChatElement> chatElements;

    public ChatAdapter(Context c, ArrayList<ChatElement> elements) {
        this.c = c;
        this.chatElements = elements;
    }

    @Override
    public int getCount() {
        return chatElements.size();
    }

    @Override
    public Object getItem(int position) {
        return chatElements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.content_chat_room, parent, false);
        }

        // Get info for view.
        TextView tvUser = (TextView) view.findViewById(R.id.text_view_message_user);
        TextView tvTime = (TextView) view.findViewById(R.id.text_view_message_time);
        TextView tvText = (TextView) view.findViewById(R.id.text_view_message_text);

        final ChatElement e = (ChatElement) this.getItem(position);

        // If element is a message.
        if (e instanceof ChatMessage) {
            tvUser.setText(e.getMessageUser());
            tvTime.setText(e.displayTime());
            tvText.setText(((ChatMessage) e).getMessageText());

            // On click
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(c, ((ChatMessage) e).getMessageText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

}
