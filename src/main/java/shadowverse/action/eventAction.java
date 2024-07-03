package shadowverse.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import shadowverse.monsters.Iceschillendrig;
import shadowverse.patch.RenderHandPatch;

public class eventAction extends AbstractGameAction {
    private boolean hasBuilt = false;

    public static Iceschillendrig iceschillendrig;

    public void update() {
        if (!this.hasBuilt) {
            GenericEventDialog.show();
            this.hasBuilt = true;
            RenderHandPatch.plsDontRenderHand = true;
        }
        iceschillendrig.imageEventText.update();
        if (!GenericEventDialog.waitForInput && !this.isDone) {
            iceschillendrig.buttonEffect(GenericEventDialog.getSelectedOption());
            RenderHandPatch.plsDontRenderHand = false;
            this.isDone = true;
        }
    }
}
