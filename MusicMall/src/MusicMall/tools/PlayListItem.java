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
import java.util.Date;

public class PlayListItem {
    protected String name;
    protected String type;
    protected String path;
    protected Date begin_play;
    protected Date end_play;
    protected double lentgh = 0.0;
    protected double volume;

    public PlayListItem(String name, String type, String path, Date begin_play, Date end_play, double lentgh, double volume) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.begin_play = begin_play;
        this.end_play = end_play;
        this.lentgh = lentgh;
        this.volume = volume;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getBegin_play() {
        return this.begin_play;
    }

    public void setBegin_play(Date begin_play) {
        this.begin_play = begin_play;
    }

    public Date getEnd_play() {
        return this.end_play;
    }

    public void setEnd_play(Date end_play) {
        this.end_play = end_play;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getLentgh() {
        return this.lentgh;
    }

    public void setLentgh(double lentgh) {
        this.lentgh = lentgh;
    }

    public double getVolume() {
        return this.volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
