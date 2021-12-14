package com.blackoutburst.game;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Cube> {

    public int compare(Cube b, Cube a) {
        int dist = Double.compare(a.distance, b.distance);
        return dist == 0 ? Double.compare(a.distance, b.distance) : dist;
    }
}