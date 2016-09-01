package Function;

import java.util.ArrayList;

/**
 * Created by 전용범 on 2016-08-23.
 */
public class Client_Info {


    private static Client_Info client_info;

    private String name;
    private String id;
    private byte[] aeskey;

    private ArrayList<String> gpcode = new ArrayList<String>();
    private ArrayList<String> gpname = new ArrayList<String>();



    public static Client_Info getInstance(){
        if (client_info == null)
        {
            client_info = new Client_Info();
            return client_info;
        }
        else
        {
            return client_info;
        }
    }

    public void init(String id, String name, byte[] aeskey, ArrayList<String> gpcode, ArrayList<String> gpname)
    {
        this.id = id;
        this.name = name;
        this.aeskey = aeskey;
        this.gpcode = gpcode;
        this.gpname = gpname;
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public byte[] getAeskey() {
        return aeskey;
    }

    public ArrayList<String> getGpcode() {
        return gpcode;
    }

    public ArrayList<String> getGpname() {
        return gpname;
    }
}
