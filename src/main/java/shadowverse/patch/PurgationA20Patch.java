package shadowverse.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

public class PurgationA20Patch {
    @SpirePatch(clz = MonsterRoomBoss.class, method = "onPlayerEntry")
    public static class AfterRivayle {
        @SpirePostfixPatch
        public static void Post(MonsterRoomBoss room) {
            if (AbstractDungeon.actNum == 3){
                if (AbstractDungeon.bossList.size() > 2)
                    AbstractDungeon.bossList.remove(2);
            }
        }
    }
}
