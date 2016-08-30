package Function;

import java.util.ArrayList;

/**
 * Created by 전용범 on 2016-08-23.
 */
public class Client_Info {


    private static  boolean init=false;
    private static Client_Info client_info;

    private String name;
    private String uscode;
    private byte[] aeskey;

    private ArrayList<String> gpcode = new ArrayList<String>();
    private ArrayList<String> gpname = new ArrayList<String>();



    public static Client_Info getInstance(){
        if (init == false)
        {
            init = true;
            client_info = new Client_Info();
            return client_info;
        }
        else
        {
            return client_info;
        }
    }

    public void init(String name, String uscode, byte[] aeskey, ArrayList<String> gpcode, ArrayList<String> gpname)
    {
        this.name = name;
        this.uscode = uscode;
        this.aeskey = aeskey;
        this.gpcode = gpcode;
        this.gpname = gpname;
    }





}
