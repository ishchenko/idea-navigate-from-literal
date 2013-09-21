package net.ishchenko.idea.navigatefromliteral;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 21.09.13
 * Time: 20:55
 */
public class OneWayPsiFileReference extends OneWayPsiFileReferenceBase<PsiElement> {

    public OneWayPsiFileReference(PsiElement psiElement) {
        super(psiElement);
    }

    @NotNull
    @Override
    protected String computeStringValue() {
        String text = getElement().getText();
        if (text.startsWith("\"") && text.endsWith("\"") || text.startsWith("'") && text.endsWith("'")) {
            return text.substring(1, text.length() - 1);
        } else {
            //Some strange literal, has no quotes. Try anyway
            return text;
        }
    }

}
