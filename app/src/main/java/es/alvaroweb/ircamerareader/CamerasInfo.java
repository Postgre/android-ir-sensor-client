package es.alvaroweb.ircamerareader;

import java.util.List;

/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */

public class CamerasInfo {
    int count;
    List<Camera> cams;

    public class Camera{
        String name;
        String ip;

        public String getName() {
            return name;
        }

        public String getIp() {
            return ip;
        }
    }

    public int getCount() {
        return count;
    }

    public List<Camera> getCams() {
        return cams;
    }
}
