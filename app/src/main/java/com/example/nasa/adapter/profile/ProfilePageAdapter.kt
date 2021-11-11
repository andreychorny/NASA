package com.example.nasa.adapter.profile

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.nasa.ui.LikedPostsFragment
import com.example.nasa.ui.OthersProfileFragment
import com.example.nasa.ui.PersonalProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.lang.IllegalArgumentException

class ProfilePageAdapter(val username: String, fm: FragmentManager) : FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private const val NUMBER_OF_FRAGMENTS = 2
        private const val MAIN_PROFILE_PAGE_NAME = "Profile"
        private const val LIST_OF_LIKED_POST = "Liked Posts"
    }

    private val mFragments = SparseArray<Fragment>()

    override fun getCount(): Int {
        return NUMBER_OF_FRAGMENTS
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 ->  {
                if(FirebaseAuth.getInstance().currentUser?.displayName == username){
                    PersonalProfileFragment.newInstance()
                }else{
                    OthersProfileFragment.newInstance(username)
                }
            }
            1 ->  LikedPostsFragment.newInstance()
            else -> throw IllegalArgumentException("No such fragment")
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        mFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        mFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 ->  MAIN_PROFILE_PAGE_NAME
            1 ->  LIST_OF_LIKED_POST
            else -> throw IllegalArgumentException("No such fragment")
        }
    }
}
