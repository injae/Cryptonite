package Function;


import android.content.Context;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;

/**
 * Created by 전용범 on 2016-08-14.
 */
public class PathPicker {

    public static final int Select_File = 0;
    public static final int Select_Files = 1;
    public static final int Select_Dir = 2;

    private FilePickerDialog dialog;
    private DialogProperties properties;

    public PathPicker(Context context, int mode)
    {
        properties = new DialogProperties();

        if(mode ==1)
            properties.selection_mode= DialogConfigs.MULTI_MODE;
        else
            properties.selection_mode= DialogConfigs.SINGLE_MODE;

        if(mode==2)
            properties.selection_type=DialogConfigs.DIR_SELECT;
        else
            properties.selection_type=DialogConfigs.FILE_SELECT;

        properties.root= new File(DialogConfigs.DEFAULT_DIR);

        properties.extensions=null;

        dialog = new FilePickerDialog(context,properties);

    }

    public void show()
    {
        if (dialog != null)
            dialog.show();
    }
    public void setDialogSelectionListener(DialogSelectionListener callback) {
        dialog.setDialogSelectionListener(callback);
    }
}
