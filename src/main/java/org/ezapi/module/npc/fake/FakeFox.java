package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.Ref;

public final class FakeFox extends FakeLiving {

    public FakeFox() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityFox", "EntityFox"), "FOX", "E", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof boolean[]) {
            boolean[] bools = (boolean[]) data;
            if (bools.length >= 1) {
                EzClass EntityFox = new EzClass(Ref.getNmsOrOld("world.entity.animal.EntityFox", "EntityFox"));
                EntityFox.setInstance(entity);
                EzEnum Type = new EzEnum(Ref.getInnerClass(EntityFox.getInstanceClass(), "Type"));
                if (bools[0]) {
                    Type.newInstance(Ref.getVersion() >= 16 ? "b" : "SNOW");
                } else {
                    Type.newInstance(Ref.getVersion() >= 16 ? "a" : "RED");
                }
                EntityFox.invokeMethod("setFoxType", new Class[] {Type.getInstanceEnum()}, new Object[] {Type.getInstance()});
                if (bools.length >= 2) {
                    EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
                    EntityAgeable.setInstance(entity);
                    EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {bools[1]});
                }
            }
        }
    }

}
