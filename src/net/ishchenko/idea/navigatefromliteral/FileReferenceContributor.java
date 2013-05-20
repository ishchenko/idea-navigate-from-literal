package net.ishchenko.idea.navigatefromliteral;

import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 05.05.13
 * Time: 0:19
 */
public class FileReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PsiJavaPatterns.psiLiteral(), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                return new PsiReference[]{new OneWayPsiFileFromPsiLiteralReference((PsiLiteral) element)};
            }
        });
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue(), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                return new PsiReference[]{new OneWayPsiFileFromXmlAttributeValueReference((XmlAttributeValue) element)};
            }
        });
        registrar.registerReferenceProvider(XmlPatterns.xmlTag(), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                return new PsiReference[]{new OneWayPsiFileFromXmlTagReference((XmlTag) element)};
            }
        });
    }
}
