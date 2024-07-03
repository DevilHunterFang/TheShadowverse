package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import shadowverse.powers.chushouExplosionPower;

public class WorldEliminator extends CustomMonster {
    public static final String ID = "shadowverse:WorldEliminator";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:WorldEliminator");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstTurn = true;

    private int strikeDmg;

    public WorldEliminator(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(50, 50), -5.0F, -20.0F, 145.0F, 280.0F, null, x, y);
        this.animation = new SpriterAnimation("img/monsters/Monster/WorldEliminator.scml");
        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(70, 70);
        } else {
            setHp(50, 50);
        }
        if (AbstractDungeon.ascensionLevel >= 19) {

            this.strikeDmg = 7;
        } else if (AbstractDungeon.ascensionLevel >= 4) {

            this.strikeDmg = 5;
        } else {
            this.strikeDmg = 3;
        }
        this.damage.add(new DamageInfo((AbstractCreature)this, this.strikeDmg));
    }


    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 19) {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new ArtifactPower((AbstractCreature)this, 3)));
        } else {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new ArtifactPower((AbstractCreature)this, 2)));
        }
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new chushouExplosionPower((AbstractCreature)this, 12)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)AbstractDungeon.player, this.damage
                            .get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
                break;
        }
    }

    @Override
    protected void getMove(int num) {
        if (this.firstTurn) {
            this.firstTurn = false;
            setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            return;
        }
    }

}
