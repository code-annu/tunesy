package developer.anurag.tunesy.main.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.List;

public class ConverterUtil {
    public static int convertDpToPixel(Context context,float dp){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    public static int convertPixelToDp(Context context,float pixel){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pixel / density);
    }

    public static String converListToString(List<String> list){
        String str=list.toString().replace("[","");
        str=str.replace("]","");
        return str.trim();
    }

    @SuppressLint("DefaultLocale")
    public static String convertSecDurationToStr(int seconds){
        int minutes=seconds/60;
        int secs=seconds%60;
        return String.format("%02d:%02d",minutes,secs);
    }


}
