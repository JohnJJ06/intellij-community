/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.codeInsight.template.impl;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author peter
 */
public class LiveTemplateCompletionContributor extends CompletionContributor {
  public LiveTemplateCompletionContributor() {
    extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters parameters,
                                    ProcessingContext context,
                                    @NotNull CompletionResultSet result) {
        if (parameters.getInvocationCount() > 0) return; //only in autopopups for now

        final PsiFile file = parameters.getOriginalFile();
        final int offset = parameters.getOffset();
        final String prefix = result.getPrefixMatcher().getPrefix();
        for (final TemplateImpl template : TemplateSettings.getInstance().getTemplates()) {
          final String key = template.getKey();
          if (prefix.equals(key)) {
            if (!template.isDeactivated() && !template.isSelectionTemplate() && TemplateManagerImpl.isApplicable(file, offset, template)) {
              result.addElement(LookupElementBuilder.create(key).setTypeText(template.getDescription()).setInsertHandler(new InsertHandler<LookupElement>() {
                @Override
                public void handleInsert(InsertionContext context, LookupElement item) {
                  context.getDocument().deleteString(context.getStartOffset(), context.getTailOffset());
                  context.setAddCompletionChar(false);
                  TemplateManager.getInstance(context.getProject()).startTemplate(context.getEditor(), template);
                }
              }));
            }
          }
        }
      }
    });
  }
}
