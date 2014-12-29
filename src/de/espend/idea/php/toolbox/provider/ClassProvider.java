package de.espend.idea.php.toolbox.provider;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import de.espend.idea.php.toolbox.completion.dict.PhpToolboxCompletionContributorParameter;
import de.espend.idea.php.toolbox.extension.PhpToolboxProviderInterface;
import de.espend.idea.php.toolbox.navigation.dict.PhpToolboxDeclarationHandlerParameter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class ClassProvider implements PhpToolboxProviderInterface {

    protected boolean withInterface = false;

    @NotNull
    public Collection<LookupElement> getLookupElements(@NotNull PhpToolboxCompletionContributorParameter parameter) {

        Collection<LookupElement> lookupElements = new ArrayList<LookupElement>();

        PhpIndex phpIndex = PhpIndex.getInstance(parameter.getProject());
        for (String className : phpIndex.getAllClassNames(parameter.getCompletionResultSet().getPrefixMatcher())) {

            for(PhpClass phpClass: phpIndex.getClassesByName(className)) {
                String presentableFQN = phpClass.getPresentableFQN();
                if(presentableFQN != null) {
                    lookupElements.add(LookupElementBuilder.create(presentableFQN).withIcon(phpClass.getIcon()));
                }
            }

            if(this.withInterface) {
                for(PhpClass phpClass: phpIndex.getInterfacesByName(className)) {
                    String presentableFQN = phpClass.getPresentableFQN();
                    if(presentableFQN != null) {
                        lookupElements.add(LookupElementBuilder.create(presentableFQN).withIcon(phpClass.getIcon()));
                    }
                }
            }
        }

        return lookupElements;

    }

    @NotNull
    @Override
    public Collection<PsiElement> getPsiTargets(@NotNull PhpToolboxDeclarationHandlerParameter parameter) {

        Collection<PsiElement> targets = new ArrayList<PsiElement>();
        targets.addAll(PhpIndex.getInstance(parameter.getProject()).getClassesByFQN(parameter.getContents()));

        if(this.withInterface) {
            targets.addAll(PhpIndex.getInstance(parameter.getProject()).getInterfacesByFQN(parameter.getContents()));
        }

        return targets;
    }

    @NotNull
    @Override
    public String getName() {
        return "Class";
    }

}

