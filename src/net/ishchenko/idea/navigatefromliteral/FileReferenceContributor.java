package net.ishchenko.idea.navigatefromliteral;

import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 05.05.13
 * Time: 0:19
 */
public class FileReferenceContributor extends PsiReferenceContributor{

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PsiJavaPatterns.psiLiteral(), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            return new PsiReference[]{new OneWayPsiFileReference((PsiLiteral) element)};
            }
        });
    }
}
