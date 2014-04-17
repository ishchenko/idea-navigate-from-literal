package net.ishchenko.idea.navigatefromliteral;

import com.google.common.collect.ComparisonChain;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 08.05.13
 * Time: 21:35
 */
public abstract class OneWayPsiFileReferenceBase<T extends PsiElement> extends PsiPolyVariantReferenceBase<T> {

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
        int dotPosition = cleanFileName.lastIndexOf('.');
        String cleanFileNameWithoutExtension;
        if (dotPosition > 0) { //strict equality to treat filenames that start with dot as filenames without extension
            cleanFileNameWithoutExtension = cleanFileName.substring(0, dotPosition);
        } else {
            cleanFileNameWithoutExtension = cleanFileName;
        }
        Project project = getElement().getProject();
        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        final String finalCleanFileName = cleanFileName;
        final String finalCleanFileNameWithoutExtension = cleanFileNameWithoutExtension;
        final Set<Pair<Integer, VirtualFile>> sortedResults = new TreeSet<Pair<Integer, VirtualFile>>(new Comparator<Pair<Integer, VirtualFile>>() {
            @Override
            public int compare(Pair<Integer, VirtualFile> o1, Pair<Integer, VirtualFile> o2) {
                return ComparisonChain.start().
                        compare(o1.getFirst(), o2.getFirst()).
                        compare(o1.getSecond(), o2.getSecond(), new Comparator<VirtualFile>() {
                            @Override
                            public int compare(VirtualFile o1, VirtualFile o2) {
                                String o1CanonicalPath = o1.getCanonicalPath();
                                String o2CanonicalPath = o2.getCanonicalPath();
                                if (o1CanonicalPath != null && o2CanonicalPath != null) {
                                    return o1CanonicalPath.compareTo(o2CanonicalPath);
                                } else {
                                    return 0;
                                }
                            }
                        }).
                        compare(o1.getSecond().getName(), o2.getSecond().getName()).
                        result();
            }
        });
        fileIndex.iterateContent(new ContentIterator() {
            @Override
            public boolean processFile(VirtualFile fileOrDir) {
                if (!fileOrDir.isDirectory()) {
                    if (fileOrDir.getName().equalsIgnoreCase(finalCleanFileName)) {
                        sortedResults.add(new Pair<Integer, VirtualFile>(10, fileOrDir));
                    } else if (fileOrDir.getNameWithoutExtension().equalsIgnoreCase(finalCleanFileName)
                            || fileOrDir.getNameWithoutExtension().equalsIgnoreCase(finalCleanFileNameWithoutExtension)) {
                        if (fileOrDir.getFileType().equals(getElement().getContainingFile().getFileType())) {
                            sortedResults.add(new Pair<Integer, VirtualFile>(20, fileOrDir));
                        } else {
                            sortedResults.add(new Pair<Integer, VirtualFile>(30, fileOrDir));
                        }
                    }
                }
                return true;
            }
        });
        PsiManager psiManager = PsiManager.getInstance(project);
        ResolveResult[] result = new ResolveResult[sortedResults.size()];
        int i = 0;
        for (Pair<Integer, VirtualFile> pair : sortedResults) {
            PsiFile psiFile = psiManager.findFile(pair.getSecond());
            if (psiFile != null) {
                result[i++] = new PsiElementResolveResult(psiFile);
            }
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
