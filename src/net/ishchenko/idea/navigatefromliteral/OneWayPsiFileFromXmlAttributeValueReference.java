package net.ishchenko.idea.navigatefromliteral;

import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 08.05.13
 * Time: 21:39
 */
public class OneWayPsiFileFromXmlAttributeValueReference extends OneWayPsiFileReferenceBase<XmlAttributeValue> {

    public OneWayPsiFileFromXmlAttributeValueReference(XmlAttributeValue psiElement) {
        super(psiElement);
    }

    @NotNull
    @Override
    protected String computeStringValue() {
        return getElement().getValue();
    }

}
