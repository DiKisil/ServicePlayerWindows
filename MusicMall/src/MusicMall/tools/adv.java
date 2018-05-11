/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicMall.tools;

/**
 *
 * @author Diana
 */
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class adv {
    protected String name;
    protected Date begin;
    protected Date end;
    protected String localPath;
    protected String remotePath;
    protected List<Boolean> count = new ArrayList<Boolean>();

    public adv() {
        int b = 0;
        while (b < 12) {
            this.count.add(false);
            ++b;
        }
    }

    public List<Boolean> getCount() {
        return this.count;
    }

    public void setCount(List<Boolean> count) {
        this.count = count;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return this.localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getRemotePath() {
        return this.remotePath;
    }

    public Date getBegin() {
        return this.begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return this.end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setRemotePath(String remotePath) {
        System.out.println(remotePath);
        this.remotePath = remotePath;
    }
}