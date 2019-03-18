/*
 *     This file is part of Lawnchair Launcher.
 *
 *     Lawnchair Launcher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Lawnchair Launcher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Lawnchair Launcher.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.deletescape.lawnchair.allapps

import android.view.View
import android.view.ViewGroup
import ch.deletescape.lawnchair.forEachChildIndexed
import com.android.launcher3.allapps.AllAppsContainerView
import com.android.launcher3.allapps.AllAppsPagedView
import com.android.launcher3.allapps.AllAppsStore

typealias AdapterHolders = Array<AllAppsContainerView.AdapterHolder>

class AllAppsTabsController(val tabs: AllAppsTabs, private val container: AllAppsContainerView) {

    val tabsCount get() = tabs.count
    val shouldShowTabs get() = tabsCount > 1

    fun createHolders(oldHolders: AdapterHolders?): AdapterHolders {
        if (oldHolders != null && oldHolders.size >= tabsCount) {
            return oldHolders
        }
        return AdapterHolders(tabsCount) { container.createHolder(false) }
    }

    fun reloadTabs() {
        tabs.reloadTabs()
    }

    fun registerIconContainers(allAppsStore: AllAppsStore, holders: AdapterHolders) {
        holders.forEach { allAppsStore.registerIconContainer(it.recyclerView) }
    }

    fun unregisterIconContainers(allAppsStore: AllAppsStore, holders: AdapterHolders) {
        holders.forEach { allAppsStore.unregisterIconContainer(it.recyclerView) }
    }

    fun setup(pagedView: AllAppsPagedView, holders: AdapterHolders) {
        tabs.forEachIndexed { index, tab ->
            holders[index].setIsWork(tab.isWork)
            holders[index].setup(pagedView.getChildAt(index), tab.matcher)
        }
    }

    fun setup(view: View, holders: AdapterHolders) {
        holders.forEach { it.recyclerView = null }
        holders[0].setup(view, null)
    }

    fun bindButtons(buttonsContainer: ViewGroup, pagedView: AllAppsPagedView) {
        buttonsContainer.forEachChildIndexed { view, i ->
            view.setOnClickListener { pagedView.snapToPage(i) }
        }
    }
}