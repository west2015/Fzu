package fzu.mcginn.utils;

import android.view.View;
import android.graphics.Point;

public class ViewUtils {

	public static Point getRelativePoint(View view1, View view2) {
	    int[] arrPoint1 = new int[2];
	    int[] arrPoint2 = new int[2];
	    view1.getLocationOnScreen(arrPoint1);
	    view2.getLocationOnScreen(arrPoint2);
	    arrPoint2[0] = (arrPoint2[0] - arrPoint1[0] + view2.getWidth() / 2);
	    arrPoint2[1] = (arrPoint2[1] - arrPoint1[1] + view2.getHeight() / 2);
	    return new Point(arrPoint2[0], arrPoint2[1]);
	}

}
