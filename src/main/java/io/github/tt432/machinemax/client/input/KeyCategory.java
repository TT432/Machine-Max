package io.github.tt432.machinemax.client.input;

import net.neoforged.neoforge.client.settings.IKeyConflictContext;

import static net.neoforged.neoforge.client.settings.KeyConflictContext.GUI;

public enum KeyCategory implements IKeyConflictContext, IKeyCategory {

    GENERAL {
        @Override
        public String getCategory() {
            return "key.category.machine_max.general";
        }

        @Override
        public boolean isActive() {
            return !GUI.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            if (other == this) return true;
            else return (other instanceof KeyCategory);
        }
    },
    GROUND {
        @Override
        public String getCategory() {
            return "key.category.machine_max.ground";
        }

        @Override
        public boolean isActive() {
            return !GUI.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return other == this || other == GENERAL; //二者为同一类时，或另一类为通用按键时，冲突
        }
    },
    SHIP {
        @Override
        public String getCategory() {
            return "key.category.machine_max.ship";
        }

        @Override
        public boolean isActive() {
            return !GUI.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return other == this || other == GENERAL; //二者为同一类时，或另一类为通用按键时，冲突
        }
    },
    PLANE {
        @Override
        public String getCategory() {
            return "key.category.machine_max.plane";
        }

        @Override
        public boolean isActive() {
            return !GUI.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return other == this || other == GENERAL; //二者为同一类时，或另一类为通用按键时，冲突
        }
    },
    MECH {
        @Override
        public String getCategory() {
            return "key.category.machine_max.mech";
        }

        @Override
        public boolean isActive() {
            return !GUI.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return other == this || other == GENERAL; //二者为同一类时，或另一类为通用按键时，冲突
        }
    }
}
