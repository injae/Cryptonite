package Function;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Parcel;
import android.util.Log;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by olleh on 2016-08-26.
 */
public class Client_Group_Search_Make implements PacketRule{

    private Client_Server_Connector css;
    private static Client_Group_Search_Make cgs;
    private FloatingSearchView searchView;
    public Queue<String> queue = new LinkedList<>();

    private Client_Group_Search_Make(FloatingSearchView searchView){
        css = Client_Server_Connector.getInstance();
        this.searchView = searchView;
    }


    public static Client_Group_Search_Make getInstance(FloatingSearchView searchView){
 //       if (cgs == null)
  //      {
            cgs = new Client_Group_Search_Make(searchView);
  //      }
        return cgs;
    }


    public void Search(String id){


        if (queue.isEmpty()) {
            SearchTask task = new SearchTask();
            task.execute(id);
        }
        else
        {
            queue.add(id);
        }
    }

    class SearchTask extends AsyncTask<String,ArrayList<Client_Group_Search_Suggestion>,Boolean>
    {
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                byte[] event = new byte[1024];
                event[0] = GROUP_SEARCH;
                event[1] = (byte) params[0].getBytes().length;
                Function.frontInsertByte(2, params[0].getBytes(), event);
                css.send.setPacket(event).write();

                Log.d("Search","Searching : " + String.valueOf(params[0]));
                String choice = new String(css.receive.setAllocate(1024).read().getByte()).trim();

                if (choice.equals("FALSE")) {
                    publishProgress(new ArrayList<Client_Group_Search_Suggestion>());
                    Log.d("Search","NULL");
                    return false;
                } else {
                    ArrayList<Client_Group_Search_Suggestion> arr = new ArrayList<>();
                    for (int i = 0; i < Integer.parseInt(choice); i++) {
                        arr.add(new Client_Group_Search_Suggestion(new String(css.receive.setAllocate(1024).read().getByte()).trim()));
                        Log.d("Search",arr.get(i).getBody());
                    }
                    publishProgress(arr);
                    return true;
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(ArrayList<Client_Group_Search_Suggestion>... values) {
            searchView.swapSuggestions(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!queue.isEmpty())
            {
                SearchTask task = new SearchTask();
                task.execute(queue.remove());
            }
        }
    }


}
