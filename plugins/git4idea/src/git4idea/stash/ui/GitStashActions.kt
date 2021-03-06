// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package git4idea.stash.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.components.serviceIfCreated
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import git4idea.stash.GitStashOperations
import git4idea.stash.GitStashTracker
import git4idea.ui.StashInfo

val STASH_INFO = DataKey.create<List<StashInfo>>("GitStashInfoList")

abstract class GitSingleStashAction : DumbAwareAction() {
  abstract fun perform(project: Project, stashInfo: StashInfo): Boolean

  override fun update(e: AnActionEvent) {
    e.presentation.isEnabled = e.project != null && e.getData(STASH_INFO)?.size == 1
    e.presentation.isVisible = e.isFromActionToolbar || (e.project != null && e.getData(STASH_INFO) != null)
  }

  override fun actionPerformed(e: AnActionEvent) {
    if (perform(e.project!!, e.getRequiredData(STASH_INFO).single())) {
      e.project!!.serviceIfCreated<GitStashTracker>()?.scheduleRefresh()
    }
  }
}

class GitDropStashAction : GitSingleStashAction() {
  override fun perform(project: Project, stashInfo: StashInfo): Boolean {
    return GitStashOperations.dropStashWithConfirmation(project, null, stashInfo)
  }
}

class GitPopStashAction : GitSingleStashAction() {
  override fun perform(project: Project, stashInfo: StashInfo): Boolean {
    return GitStashOperations.unstash(project, stashInfo, null, true, false)
  }
}

class GitApplyStashAction : GitSingleStashAction() {
  override fun perform(project: Project, stashInfo: StashInfo): Boolean {
    return GitStashOperations.unstash(project, stashInfo, null, false, false)
  }
}