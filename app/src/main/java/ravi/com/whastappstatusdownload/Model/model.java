package ravi.com.whastappstatusdownload.Model;

/**
 * Created by nikpatel on 24/04/18.
 */

public class model {
    String name,path;
    long size;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
