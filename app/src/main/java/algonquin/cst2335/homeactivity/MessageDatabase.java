package algonquin.cst2335.homeactivity;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//declares a database, table is ChatMessage
@Database(entities = {ChatMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    //Must return the DAO class in your code
    public abstract ChatMessageDAO cmDAO();
}
