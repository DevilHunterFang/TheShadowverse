package shadowverseCharbosses.bosses.Maisha;

import shadowverseCharbosses.bosses.AbstractBossDeckArchetype;

public class ArchetypeBaseMaisha extends AbstractBossDeckArchetype {
    public ArchetypeBaseMaisha(String id, String loggerName) {
        super(id, "Nemesis", loggerName);
    }

    public void initialize() {}

    public void addedPreBattle() {
        super.addedPreBattle();
    }

    public void initializeBonusRelic() {}
}
