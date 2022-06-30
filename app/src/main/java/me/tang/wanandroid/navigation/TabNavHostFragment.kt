package me.tang.wanandroid.navigation

import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment

class TabNavHostFragment: NavHostFragment() {
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return TabFragmentNavigator(requireContext(), childFragmentManager, id)
    }
}