package net.ishchenko.idea.navigatefromliteral;

import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 08.05.13
 * Time: 21:35
 */
public abstract class OneWayPsiFileReferenceBase<T extends PsiElement> extends PsiPolyVariantReferenceBase<T>  {

    public OneWayPsiFileReferenceBase(T psiElement) {
        super(psiElement, true);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        String cleanFileName = computeStringValue();
        int slashPosition = cleanFileName.lastIndexOf('/');
        if (slashPosition >= 0) {
            cleanFileName = cleanFileName.substring(slashPosition + 1);
        } else {
            int backSlashPosition = cleanFileName.lastIndexOf('\\');
            if (backSlashPosition >= 0) {
                cleanFileName = cleanFileName.substring(backSlashPosition + 1);
            }
        }
        PsiFile[] files = FilenameIndex.getFilesByName(getElement().getProject(), cleanFileName, GlobalSearchScope.projectScope(getElement().getProject()));
        ResolveResult[] result = new ResolveResult[files.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new PsiElementResolveResult(files[i]);
        }
        return result;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return EMPTY_ARRAY;
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        return false;
    }

    @NotNull
    protected abstract String computeStringValue();

}
