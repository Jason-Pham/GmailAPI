package helpers;

import java.io.File;

import static _base.Constant.IMAGE_FOLDER;

public class Utils {

    public static File[] getFileList(){
        return  new File(IMAGE_FOLDER).listFiles();
    }
}
