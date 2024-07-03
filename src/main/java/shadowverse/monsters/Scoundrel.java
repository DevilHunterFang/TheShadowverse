package shadowverse.monsters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.ApplyStasisAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StasisPower;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

public class Scoundrel extends CustomMonster {
    public static final String ID = "shadowverse:Scoundrel";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Scoundrel");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 48;

    public static final int HP_MAX = 52;

    public static final int A_2_HP_MIN = 50;

    public static final int A_2_HP_MAX = 54;

    private int shootDmg;
    private int lootDmg;
    private int slashCount = 0;
    private static final String START = MOVES[0];

    public Scoundrel(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(48, 52), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Scoundrel/Scoundrel.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 13;
            this.lootDmg = 8;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 12;
            this.lootDmg = 7;
        } else {
            this.shootDmg = 11;
            this.lootDmg = 6;
        }
        this.damage.add(new DamageInfo(this, this.lootDmg));
        this.damage.add(new DamageInfo(this, this.shootDmg));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0)));
                AbstractDungeon.actionManager.addToBottom(new ApplyStasisAction(this));
                this.slashCount++;
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)4, AbstractMonster.Intent.ATTACK, (this.damage
                        .get(1)).base));
                break;
            case 4:
                this.slashCount++;
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1)));
                if (this.slashCount == 3) {
                    setMove((byte)2, AbstractMonster.Intent.DEFEND);
                    break;
                }
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)4, AbstractMonster.Intent.ATTACK, (this.damage
                        .get(1)).base));
                break;
            case 2:
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 17));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 11));
                }
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)3, AbstractMonster.Intent.ESCAPE));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)3, AbstractMonster.Intent.ESCAPE));
                if (this.hasPower(StasisPower.POWER_ID)){
                    AbstractCard c = ReflectionHacks.getPrivate(this.getPower(StasisPower.POWER_ID),StasisPower.class,"card");
                    if (c != null && AbstractDungeon.player.masterDeck.group.stream().anyMatch(card -> card.cardID.equals(c.cardID))){
                        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (Settings.WIDTH / 2), (Settings.HEIGHT / 2)));
                        for (AbstractCard card : AbstractDungeon.player.masterDeck.group){
                            if (card.cardID.equals(c.cardID)){
                                AbstractDungeon.player.masterDeck.removeCard(card);
                                break;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void getMove(int num) {
        setMove(START, (byte)1, Intent.ATTACK_DEBUFF, (this.damage.get(0)).base);
    }

    @Override
    public void die() {
        super.die();
    }
}
