package com.thieumao.youtubeapp.image;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
	
//	public static String linkTuQC = "http://tivi.9appstore.com/gcmthailandmovies/film/linkTuQC.php";
	
	public static String TITLE = "title";
	public static String DESC = "desc";
	public static String PACKAGE = "package";
	public static String LINKAPP = "linkapp";
	public static String IMAGE = "image";
	
//	public static String linkJSON = "http://tivi.9appstore.com/TuQC/hello.php";
	
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}