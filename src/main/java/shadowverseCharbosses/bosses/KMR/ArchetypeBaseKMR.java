package shadowverseCharbosses.bosses.KMR;

import shadowverseCharbosses.bosses.AbstractBossDeckArchetype;

public class ArchetypeBaseKMR extends AbstractBossDeckArchetype {
    public ArchetypeBaseKMR(String id, String loggerName) {
        super(id, "KMR", loggerName);
    }

    public void initialize() {}

    public void addedPreBattle() {
        super.addedPreBattle();
    }

    public void initializeBonusRelic() {}
}
