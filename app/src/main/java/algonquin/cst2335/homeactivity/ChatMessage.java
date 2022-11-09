package algonquin.cst2335.homeactivity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @ColumnInfo(name="message")
    protected String message;

    @ColumnInfo(name = "SendOrReceive")
    protected boolean isSentBtn;

    @ColumnInfo(name = "TimeSent")
    protected String timeSent;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    public ChatMessage(){    }

    public ChatMessage(String msg, String ts, boolean isSent) {
        message = msg;
        timeSent = ts;
        isSentBtn = isSent;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

//    public boolean getisSentBtn(){
//        return isSentBtn;
//    }
}
