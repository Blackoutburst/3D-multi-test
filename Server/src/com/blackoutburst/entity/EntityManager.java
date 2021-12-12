package com.blackoutburst.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityManager implements EntityNetworkController {
	
	public static int entityId = 0;
	public static List<Entity> entities = new ArrayList<Entity>();
}
