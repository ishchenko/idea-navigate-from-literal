package net.ishchenko.idea.navigatefromliteral;

import com.intellij.psi.*;
import com.intellij.psi.impl.JavaConstantExpressionEvaluator;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 06.05.13
 * Time: 20:22
 */
public class OneWayPsiFileReference extends PsiPolyVariantReferenceBase<PsiLiteral> {

    public OneWayPsiFileReference(@NotNull PsiLiteral element) {
        super(element);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {

        String computedStringValue = null;

        PsiPolyadicExpression parentExpression = PsiTreeUtil.getParentOfType(getElement(), PsiPolyadicExpression.class);

        if (parentExpression != null) {

            StringBuilder computedValue = new StringBuilder();
            for (PsiExpression operand : parentExpression.getOperands()) {
                if (operand instanceof PsiReference) {
                    PsiElement probableDefinition = ((PsiReference) operand).resolve();
                    if (probableDefinition instanceof PsiVariable) {
                        PsiExpression initializer = ((PsiVariable) probableDefinition).getInitializer();
                        if (initializer != null) {
                            Object value = JavaConstantExpressionEvaluator.computeConstantExpression(initializer, true);
                            if (value instanceof String) {
                                computedValue.append(value);
                            }
                        }
                    }
                } else {
                    Object value = JavaConstantExpressionEvaluator.computeConstantExpression(operand, true);
                    if (value instanceof String) {
                        computedValue.append(value);
                    }
                }
            }

            computedStringValue = computedValue.toString();

        } else {

            Object value = getElement().getValue();
            if (value instanceof String) {
                computedStringValue = (String) value;
            }

        }

        if (computedStringValue != null) {
            return getResolveResults(computedStringValue);
        } else {
            return ResolveResult.EMPTY_ARRAY;
        }

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

    private ResolveResult[] getResolveResults(String value) {
        String cleanFileName = value;
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

}
