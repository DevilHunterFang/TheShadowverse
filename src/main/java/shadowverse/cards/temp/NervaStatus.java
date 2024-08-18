package shadowverse.cards.temp;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import shadowverse.monsters.*;
import shadowverse.powers.NervaPower1;
import shadowverse.powers.NervaPower2;
import shadowverse.powers.NervaPower3;
import shadowverse.powers.NervaPower4;

public class NervaStatus extends CustomCard {
    public static final String ID = "shadowverse:NervaStatus";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:NervaStatus");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/NervaStatus.png";


    public NervaStatus() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.NONE);
        this.isEthereal = true;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void triggerWhenDrawn() {
        Nerva nerva = null;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo instanceof Nerva) {
                nerva = (Nerva) mo;
            }
        }
        if (nerva != null) {
            for (int i = 1; i < 4; i++) {
                if (nerva.enemySlots.get(i) != null && !nerva.enemySlots.get(i).isDying) {
                    if (i < 3)
                        continue;
                    else {
                        addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, 4, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                            if (m != null && !m.isDying && !m.isEscaping) {
                                addToBot(new ApplyPowerAction(m, nerva, new StrengthPower(m, 2), 2));
                                addToBot(new ApplyPowerAction(m,nerva,new RegenerateMonsterPower(m,10),10));
                                addToBot(new ApplyPowerAction(m,nerva,new BufferPower(m,1),1));
                            }
                        }
                    }
                } else {
                    AbstractMonster m = null;
                    if (nerva.hasPower(NervaPower1.POWER_ID)) {
                        int rnd = AbstractDungeon.cardRandomRng.random(0, 1);
                        if (rnd == 0) {
                            m = new chushou1(nerva.spawnX - 155.0F * i, -20.0F);
                        } else {
                            m = new chushou2(nerva.spawnX - 155.0F * i, -20.0F);
                        }
                        if (nerva.enemySlots.get(i) == null) {
                            addToBot(new SpawnMonsterAction(m, true));
                            if (AbstractDungeon.ascensionLevel >= 19) {
                                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, nerva, new ArtifactPower(m, 3)));
                            } else {
                                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, nerva, new ArtifactPower(m, 2)));
                            }
                            addToBot(new ApplyPowerAction(m, nerva, new BarricadePower(m)));
                            addToBot(new GainBlockAction(m, 40));
                            nerva.enemySlots.put(i, m);
                        } else {
                            if (nerva.enemySlots.get(i).isDying) {
                                nerva.enemySlots.remove(i);
                                addToBot(new SpawnMonsterAction(m, true));
                                if (AbstractDungeon.ascensionLevel >= 19) {
                                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, nerva, new ArtifactPower(m, 3)));
                                } else {
                                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, nerva, new ArtifactPower(m, 2)));
                                }
                                addToBot(new ApplyPowerAction(m, nerva, new BarricadePower(m)));
                                addToBot(new GainBlockAction(m, 40));
                                
                                nerva.enemySlots.put(i, m);
                            }
                        }
                    } else if (nerva.hasPower(NervaPower2.POWER_ID)) {
                        m = new Assault(nerva.spawnX - 155.0F * i, -20.0F);
                        if (nerva.enemySlots.get(i) == null) {
                            addToBot(new SpawnMonsterAction(m, true));
                            m.usePreBattleAction();
                            
                            nerva.enemySlots.put(i, m);
                        } else {
                            if (nerva.enemySlots.get(i).isDying) {
                                nerva.enemySlots.remove(i);
                                addToBot(new SpawnMonsterAction(m, true));
                                m.usePreBattleAction();
                                
                                nerva.enemySlots.put(i, m);
                            }
                        }
                    }else if (nerva.hasPower(NervaPower3.POWER_ID)) {
                        m = new Spider3(nerva.spawnX - 155.0F * i, -20.0F);
                        if (nerva.enemySlots.get(i) == null) {
                            addToBot(new SpawnMonsterAction(m, true));
                            m.usePreBattleAction();
                            
                            nerva.enemySlots.put(i, m);
                        } else {
                            if (nerva.enemySlots.get(i).isDying) {
                                nerva.enemySlots.remove(i);
                                addToBot(new SpawnMonsterAction(m, true));
                                m.usePreBattleAction();
                                
                                nerva.enemySlots.put(i, m);
                            }
                        }
                    }else {
                        m = new General(nerva.spawnX - 155.0F * i, -20.0F);
                        if (nerva.enemySlots.get(i) == null) {
                            addToBot(new SpawnMonsterAction(m, true));
                            m.usePreBattleAction();
                            
                            nerva.enemySlots.put(i, m);
                        } else {
                            if (nerva.enemySlots.get(i).isDying) {
                                nerva.enemySlots.remove(i);
                                addToBot(new SpawnMonsterAction(m, true));
                                m.usePreBattleAction();
                                
                                nerva.enemySlots.put(i, m);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }


    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public AbstractCard makeCopy() {
        return new NervaStatus();
    }
}
