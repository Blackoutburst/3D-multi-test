package com.blackoutburst.game;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Cube> {

    public int compare(Cube b, Cube a) {
        int dist = Double.valueOf(a.distance).compareTo(b.distance);
        return dist == 0 ? Double.valueOf(a.distance).compareTo(b.distance) : dist;
    }
}