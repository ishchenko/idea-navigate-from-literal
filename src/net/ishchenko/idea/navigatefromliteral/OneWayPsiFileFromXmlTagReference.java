package net.ishchenko.idea.navigatefromliteral;

import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 10.05.13
 * Time: 22:38
 */
public class OneWayPsiFileFromXmlTagReference extends OneWayPsiFileReferenceBase<XmlTag> {

    public OneWayPsiFileFromXmlTagReference(XmlTag psiElement) {
        super(psiElement);
    }

    @NotNull
    @Override
    protected String computeStringValue() {
        return getElement().getValue().getTrimmedText();
    }

}
