

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import android.view.LayoutInflater;

import android.widget.BaseAdapter;





/*
 display messages utilizing the Container_MsgView class
 */




 // *** Currently a work-in progress ***

 // current issues: ensuring that my own list members are not included in the adapter, as 
 // list of items are already held by the respective adapters


public class Chat_View_Adapter extends BaseAdapter {


    private int sendBckgrnd, recvBckgrnd; 
    private int send_bubbleBckgrnd, recv_bubbleBckgrnd;

    private float bubble_RiseAction;

    // java class to add on later
    // ViewBuilder class supplies API for developing views up iteratively
    private Create_Custom_Views devViews = new ViewBuilder();

    public final int SENT_STATE = 0;
    public final int RECVD_STATE = 1;


    ArrayList<ChatMessage> msgConversation;

    Context cntxt;

    LayoutInflater lyt_Inf;

    public Chat_View_Adapter(Context cntxt, Create_Custom_Views devViews, int recvBckgrnd, int sendBckgrnd, int recv_bubbleBckgrnd, int send_bubbleBckgrnd, float bubble_RiseAction) {
        this.msgConversation = new ArrayList<>();

        this.cntxt = cntxt;

        this.lyt_Inf = LayoutInflater.from(cntxt);

        this.recvBckgrnd = recvBckgrnd;
        this.sendBckgrnd = sendBckgrnd;

        this.recv_bubbleBckgrnd = recv_bubbleBckgrnd;
        this.send_bubbleBckgrnd = send_bubbleBckgrnd;

        this.bubble_RiseAction = bubble_RiseAction;

        this.devViews = devViews;
    }



    @Override
    public int getNumOfMsgs() {
        return msgConversation.size();
    }



// getItem() is a built-in feature of the ArrayAdapter
    @Override
    public Object getItem(int position) {
        return msgConversation.get(position);
    }


    @Override
    public long getIdOfItem(int position) {
        return position;
    }


// getItemViewType() is a built-in feature of BaseAdapter
    @Override
    public int getItemViewType(int position) {
        return msgConversation.get(position).getType().ordinal();
    }


// getViewTypeCount() is a built-in feature of BaseAdapter

    @Override
    public int getViewTypeCount() {
        return 2;
    }



// position, convertView, and parent are built-in parameters from the BaseAdapter

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // class to add later
        Container_MsgView container;

        int kind = getDetailsOfElemView(position);

        if (convertView == null) {

            switch (kind) {
                case SENT_STATE:
                    convertView = devViews.create_SentView(cntxt);
                    break;

                case RECVD_STATE:
                    convertView = devViews.create_RcvView(cntxt);
                    break;
            }


            container = new Container_MsgView(convertView, recvBckgrnd, sendBckgrnd, recv_bubbleBckgrnd, send_bubbleBckgrnd);
            convertView.setTag(container);

        } else {
            container = (Container_MsgView) convertView.getTag();
        }


        container.setMsg(msgConversation.get(position).getMsg());
     
     // setTimestamp() part of the Interface PreparedStatement --> Javase Docs Oracle
        container.setTimestamp(msgConversation.get(position).getFormattedTime());

        // setElevation() part of the View Class
        container.setElevation(bubble_RiseAction);

        container.setBackground(kind);


        String sendingEnd = msgConversation.get(position).getSender();
     
// setSender() public method is part of Android's IEmail.Message Class
        if (sendingEnd != null) {
            container.setSender(sendingEnd);
        }

        return convertView;
    }



// add() is a built-in feature of the ArrayAdapter
// notifyDataSetChanged() is a built-in public method of the BaseAdapter
    public void include_Msg(ChatMessage msg) {
        msgConversation.add(msg);

        notifyDataSetChanged();
    }


// addAll() is a built-in feature of the ArrayAdapter
// notifyDataSetChanged() is a built-in public method of the BaseAdapter

    public void include_Msgs(ArrayList<ChatMessage> msgConversation) {
        this.msgConversation.addAll(msgConversation);
        notifyDataSetChanged();
    }


    public void delete_Msg(int position) {
        if (this.msgConversation.size() > position) {
            this.msgConversation.remove(position);
        }
    }


    // notifyDataSetChanged() is a built-in public method of the BaseAdapter
    public void resetMsgs() {
        this.msgConversation.clear();
        notifyDataSetChanged();
    }
}
