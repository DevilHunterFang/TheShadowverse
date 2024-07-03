package shadowverse.patch;

import actlikeit.dungeons.CustomDungeon;
import actlikeit.events.GetForked;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import shadowverse.dungeons.WhispersOfPurgation;

public class AfterRivaylePatch {
    @SpirePatch(clz = GetForked.class, method = "<ctor>")
    public static class AfterRivayle {
        @SpirePostfixPatch
        public static void Post(GetForked forked, boolean afterdoor) {
            if (AbstractDungeon.actNum == 2){
                forked.imageEventText.loadImage("img/event/WhispersOfPurgation.png");
            }
        }
    }
}
