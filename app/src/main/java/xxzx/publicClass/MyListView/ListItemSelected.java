package xxzx.publicClass.MyListView;

import android.graphics.drawable.Drawable;

/**
 * Created by ch on 2016/4/7.
 */
public class ListItemSelected {
    private String title;
    private int objectid = -1;
    private boolean isChecked;
    private Drawable icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getObjectid() {
        return objectid;
    }

    public void setObjectid(int objectid) {
        this.objectid = objectid;
    }
}
