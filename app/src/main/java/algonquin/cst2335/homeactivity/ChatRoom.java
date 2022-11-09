package algonquin.cst2335.homeactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.homeactivity.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.homeactivity.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;

    ArrayList<ChatMessage> messages;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;

    ChatRoomViewModel chatModel;

    ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        messages = chatModel.messages.getValue();

        //load from the database:
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MessageDatabase").build();
        mDAO = db.cmDAO();


        if(messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<>());

            //load everything:
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute( () -> {
                //whatever is in here runs on another processor.

                messages.addAll( mDAO.getAllMessages() );
                //now you can load the RecyclerVIew:

                runOnUiThread(() -> binding.recycleView.setAdapter( myAdapter ) );

            }  );
        }

        binding.sendButton.setOnClickListener(click -> {
            // get message typed
            String input = binding.textInput.getText().toString();

            // get current date
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            ChatMessage cm = new ChatMessage(input, currentDateandTime, true);
            messages.add(cm);

            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute( () -> {
                mDAO.insertMessage(cm);
            });
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to deletet the message: " + messageText.getText())
                        .setTitle("Question:")
                        .setNegativeButton("No", (dialog, cl) -> {})
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            ChatMessage m = messages.get(position);
                            mDAO.deleteMessage(m);


                            Snackbar.make(messageText, "You deleted message #"+position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk1 -> {

                                        Executor thread = Executors.newSingleThreadExecutor();
                                        thread.execute( () -> {
                                            mDAO.insertMessage(m);
                                        });

                                        messages.add(position, m);
                                        myAdapter.notifyItemInserted(position);
                                    }).show();

                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute( () -> {
                                mDAO.deleteMessage(m);
                            });

                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);

                }).create().show();

            });

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}