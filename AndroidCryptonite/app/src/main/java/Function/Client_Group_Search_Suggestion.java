package Function;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by olleh on 2016-08-26.
 */
public class Client_Group_Search_Suggestion implements SearchSuggestion {

    private String id;

    public Client_Group_Search_Suggestion(String suggestion) {
        this.id = suggestion;
    }

    public Client_Group_Search_Suggestion(Parcel source) {
        this.id = source.readString();
    }

    public static final Creator<Client_Group_Search_Suggestion> CREATOR = new Creator<Client_Group_Search_Suggestion>() {
        @Override
        public Client_Group_Search_Suggestion createFromParcel(Parcel source) {
            return new Client_Group_Search_Suggestion(source);
        }

        @Override
        public Client_Group_Search_Suggestion[] newArray(int size) {
            return new Client_Group_Search_Suggestion[size];
        }
    };

    @Override
    public String getBody() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }
}
