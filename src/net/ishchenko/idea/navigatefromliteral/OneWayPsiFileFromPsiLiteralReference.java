package net.ishchenko.idea.navigatefromliteral;

import com.intellij.psi.*;
import com.intellij.psi.impl.JavaConstantExpressionEvaluator;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 06.05.13
 * Time: 20:22
 */
public class OneWayPsiFileFromPsiLiteralReference extends OneWayPsiFileReferenceBase<PsiLiteral> {

    public OneWayPsiFileFromPsiLiteralReference(@NotNull PsiLiteral element) {
        super(element);
    }

    @NotNull
    @Override
    protected String computeStringValue() {

        String computedStringValue = "";

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

        return computedStringValue;

    }


}
