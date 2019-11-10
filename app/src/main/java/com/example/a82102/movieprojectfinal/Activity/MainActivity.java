package com.example.a82102.movieprojectfinal.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;


import android.view.MenuItem;
import android.widget.Toast;

import com.example.a82102.movieprojectfinal.Fragment.Fragment1;
import com.example.a82102.movieprojectfinal.Helper.NetworkHelper;
import com.example.a82102.movieprojectfinal.R;


import java.util.ArrayList;
//volley 라이브러리를 이용하여
//웹서버에서 http프로토콜을 통해 데이터를 요청하고 데이터를 응답을 받아
//GSON라이브러리를 이용해서 자바 객체로 바꾼 후 화면에 표시.
public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        networkStatusCheck();
        setToolbar();
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2); //전체 프래그먼트 중 몇개를 캐싱해놓을지
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        Fragment1 fragment1 = Fragment1.newInstance(0);
        Fragment1 fragment2 = Fragment1.newInstance(1);
        Fragment1 fragment3 = Fragment1.newInstance(2);
        Fragment1 fragment4 = Fragment1.newInstance(3);
        Fragment1 fragment5 = Fragment1.newInstance(4);

        pagerAdapter.addFragment(fragment1);
        pagerAdapter.addFragment(fragment2);
        pagerAdapter.addFragment(fragment3);
        pagerAdapter.addFragment(fragment4);
        pagerAdapter.addFragment(fragment5);
        viewPager.setAdapter(pagerAdapter);
        int dpValue = 54;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);


    }//end onCreate
    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setTitle("영화 목록");
        actionBar.setDisplayHomeAsUpEnabled(true);// 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);//뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
    }

    public void networkStatusCheck()
    {
        NetworkHelper.networkStatus = NetworkHelper.getConnectivityStatus(getApplicationContext());
        if(NetworkHelper.networkStatus == NetworkHelper.TYPE_MOBILE)
        {
            Toast.makeText(getApplicationContext(),"데이터 연결됨. CODE:"+Integer.toString(NetworkHelper.networkStatus),Toast.LENGTH_SHORT).show();
        }else if(NetworkHelper.networkStatus == NetworkHelper.TYPE_WIFI)
        {
            Toast.makeText(getApplicationContext(),"와이파이 연결됨. CODE:"+Integer.toString(NetworkHelper.networkStatus),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"인터넷 연결 안 됨. CODE:"+Integer.toString(NetworkHelper.networkStatus),Toast.LENGTH_SHORT).show();
            //연결이 안 되어있을 경우 단말에 저장된 데이터를 보여준다
        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter
    {
        ArrayList<Fragment> fragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        public void addFragment(Fragment fragment)
        {
            fragments.add(fragment);
        }
        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }
        @Override
        public int getCount()
        {
            return fragments.size();
        }
        @Override
        public float getPageWidth(int position) {
            //주어진 페이지의 비례 폭을 (0.f-1.f]에서 ViewPager의 측정 된 폭의 백분율로 반환합니다.
            return (0.8f);
    }
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "clicked menu", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
