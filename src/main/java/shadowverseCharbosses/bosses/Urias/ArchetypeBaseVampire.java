package shadowverseCharbosses.bosses.Urias;

import shadowverseCharbosses.bosses.AbstractBossDeckArchetype;

public class ArchetypeBaseVampire extends AbstractBossDeckArchetype {
    public ArchetypeBaseVampire(String id, String loggerName) {
        super(id, "Vampire", loggerName);
    }

    public void initialize() {}

    public void addedPreBattle() {
        super.addedPreBattle();
    }

    public void initializeBonusRelic() {}
}
