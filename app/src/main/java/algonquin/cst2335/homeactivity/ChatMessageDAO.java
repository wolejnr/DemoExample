package algonquin.cst2335.homeactivity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//object that performs CRUD operations
@Dao
public interface ChatMessageDAO {

    @Insert
    void insertMessage(ChatMessage m);

                        //This matches the @Entity class name
    @Query("Select * from ChatMessage")
    List<ChatMessage> getAllMessages();

    @Delete
    void deleteMessage(ChatMessage m);
}
